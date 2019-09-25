package com.cnam.discover.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.IBinder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnam.discover.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

@SuppressWarnings("deprecation")
public class DiscoverService extends Service {
    private static Camera mCamera;
    public static final String DISCOVER_PICTURE = "DISCOVER_PICTURE";
    public static final String LOG = "DiscoverService";
    private static final int HEIGHT = 480;
    private static final int WIDTH = 480;

    private Bitmap mBitmap;

    private FirebaseVisionFaceDetector mFaceDetector;

    @Override
    public void onCreate() {
        super.onCreate();

        mFaceDetector = initFacedetector();

        SurfaceView surface = new SurfaceView(this);
        try {
            mCamera = Camera.open(0);
            mCamera.getParameters().setPictureSize(WIDTH,HEIGHT);
            mCamera.getParameters().setRotation(90);
            mCamera.getParameters().setPictureFormat(ImageFormat.JPEG);
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
        public void onPictureTaken(final byte[] data, Camera camera) {

            mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            //Pour tester
            MainActivity.imageView.setImageBitmap(mBitmap);

            mFaceDetector.detectInImage(FirebaseVisionImage.fromBitmap(mBitmap)).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                @Override
                public void onSuccess(List<FirebaseVisionFace> faces) {
                    if(faces.size() > 0)
                        sendBroadcast(data);
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
            sendBroadcast(data);
        }
    };

    private void sendBroadcast(byte[] data){
        Intent intent = new Intent(DiscoverService.DISCOVER_PICTURE);
        intent.putExtra(DiscoverService.DISCOVER_PICTURE, data);
        sendBroadcast(intent);
    }

    private FirebaseVisionFaceDetector initFacedetector(){
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                        .build();

        return FirebaseVision.getInstance().getVisionFaceDetector(options);
    }
}