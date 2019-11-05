package com.cnam.discover.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnam.discover.api.ApiRequest;
import com.cnam.discover.api.ApiResponseParser;
import com.cnam.discover.api.ApiSingleton;
import com.cnam.discover.interfaces.IPerson;
import com.cnam.discover.observer.IdentifiedObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class DiscoverService extends Service {

    private static Camera mCamera;
    private static final int HEIGHT = 480;
    private static final int WIDTH = 480;

    private FirebaseVisionFaceDetector mFaceDetector;

    @Override
    public void onCreate() {
        super.onCreate();

        mFaceDetector = initFacedetector();

        try {
            mCamera = Camera.open(0);
            mCamera.getParameters().setPictureSize(WIDTH,HEIGHT);
            mCamera.getParameters().setRotation(90);
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
        public void onPictureTaken(final byte[] data, Camera camera) {

            /*
              A tester si Vuzix prend des photos encodées dans le format accépté par FirebaseVisionImageMetadata
              Soit FirebaseVisionImageMetadata.IMAGE_FORMAT_YV12
              Soit FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21
              Sinon tant pis, pas très important
             */

            /*
            FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                    .setWidth(480)
                    .setHeight(480)
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_YV12)
                    .build();
            mFaceDetector.detectInImage(FirebaseVisionImage.fromByteArray(data, metadata)).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            */

            mFaceDetector.detectInImage(FirebaseVisionImage.fromBitmap(BitmapFactory.decodeByteArray(data, 0, data.length))).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                @Override
                public void onSuccess(List<FirebaseVisionFace> faces) {
                    if(faces.size() > 0) {
                        sendToServer(data);
                    } else {
                        IdentifiedObserver.getInstance().updateValue(new ArrayList<IPerson>());
                        takePicture();
                    }
                }
            }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        takePicture();
                    }
                }
            );
        }
    };

    private void sendToServer(byte[] data){

        Map<String, String> parameters = new HashMap<>();
        parameters.put("data", Base64.encodeToString(data, Base64.DEFAULT));

        ApiRequest apiRequest = new ApiRequest(ApiSingleton.getInstance(getApplicationContext()).getRequestQueue(), getApplicationContext());
        apiRequest.apiPostRequest(ApiRequest.GET_IDENTIFICATION, parameters, new ApiRequest.apiCallback() {
            @Override
            public void onSuccess(Context context, JSONObject jsonObject) {
                List<IPerson> personList = ApiResponseParser.parseTest(jsonObject);
                if(personList.size() > 0) {
                    IdentifiedObserver.getInstance().updateValue(personList);
                }
                takePicture();
            }

            @Override
            public void onError(Context context, String message) {
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                takePicture();
            }
        });
    }

    private FirebaseVisionFaceDetector initFacedetector(){
        FirebaseVisionFaceDetectorOptions options =
            new FirebaseVisionFaceDetectorOptions.Builder()
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .build();

        return FirebaseVision.getInstance().getVisionFaceDetector(options);
    }
}