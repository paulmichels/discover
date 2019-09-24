package com.cnam.discover.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Base64;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.cnam.discover.MainActivity;
import com.cnam.discover.api.ApiRequest;
import com.cnam.discover.api.ApiResponseParser;
import com.cnam.discover.api.ApiSingleton;
import com.cnam.discover.dto.IdentificationDto;
import com.cnam.discover.dto.TestDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class DiscoverService extends Service {
    private static Camera mCamera;
    public static final String FACE_INFORMATIONS = "FACE_INFORMATIONS";

    private ApiRequest apiRequest;
    private RequestQueue requestQueue;
    private FirebaseVisionFaceDetector faceDetector;

    @Override
    public void onCreate() {
        super.onCreate();

        requestQueue = ApiSingleton.getInstance(this).getRequestQueue();
        apiRequest = new ApiRequest(requestQueue, this);
        faceDetector = initFacedetector();

        SurfaceView surface = new SurfaceView(this);
        try {
            mCamera = Camera.open(0);
            mCamera.getParameters().setPictureSize(480,480);
            mCamera.setPreviewDisplay(surface.getHolder());
            takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void takePicture() {
        try {
            mCamera.startPreview();
            mCamera.takePicture(null, null, pictureCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
            final Bitmap finalPicture = rotateBitmap(picture);

            MainActivity.imageView.setImageBitmap(finalPicture);

            faceDetector.detectInImage(FirebaseVisionImage.fromBitmap(finalPicture)).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                @Override
                public void onSuccess(List<FirebaseVisionFace> faces) {
                    if(faces.size() > 0)
                        sendPictureToServer(finalPicture);
                }
            }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                }
            );
            takePicture();
        }
    };

    private Bitmap rotateBitmap(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    private Bitmap formatBitmap(Bitmap bmpOriginal) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        bmpGrayscale = Bitmap.createScaledBitmap(bmpGrayscale, bmpOriginal.getWidth(), bmpOriginal.getHeight(), false);
        return bmpGrayscale;
    }

    private String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void sendBroadcast(){
        Intent intent = new Intent(DiscoverService.FACE_INFORMATIONS);
        //intent.putExtra(DiscoverService.BASE_64_BITMAP, base64);
        sendBroadcast(intent);
    }

    private FirebaseVisionFaceDetector initFacedetector(){
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                        .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        return detector;
    }

    private void sendPictureToServer(Bitmap bitmap){
        bitmap = formatBitmap(bitmap);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("image", bitmapToBase64(bitmap));

        apiRequest.apiPostRequest(ApiRequest.GET_IDENTIFICATION, parameters, new ApiRequest.apiCallback() {
            @Override
            public void onSuccess(Context context, JSONObject jsonObject) {
                TestDto testDto = ApiResponseParser.parseTest(jsonObject);
                sendBroadcast();
            }

            @Override
            public void onError(Context context, String message) {
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}