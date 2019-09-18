package com.cnam.discover.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

import com.cnam.discover.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class CameraService extends Service {
    private static Camera mCamera;
    public static final String REFRESH_DATA_INTENT = "REFRESH_DATA_INTENT";
    public static final String BASE_64_BITMAP = "BASE_64_BITMAP";

    @Override
    public void onCreate() {
        super.onCreate();
        SurfaceView surface = new SurfaceView(this);
        try {
            mCamera = Camera.open(0);
            mCamera.setPreviewDisplay(surface.getHolder());
            takePictureThread();
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

    //Faire des test : https://stackoverflow.com/questions/6964011/handler-vs-asynctask-vs-thread
    public void takePictureThread() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            takePicture("tmp");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
    }

    public void takePicture(final String fileName) {
        try {
            mCamera.startPreview();
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
                    picture = rotateBitmap(picture);
                    picture = formatBitmap(picture, 256, 256);
                    sendBroadcast(bitmapToBase64(picture));
                    MainActivity.imageView.setImageBitmap(picture);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    private Bitmap formatBitmap(Bitmap bmpOriginal, int width, int height) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        bmpGrayscale = Bitmap.createScaledBitmap(bmpGrayscale, width, height, false);
        return bmpGrayscale;
    }

    private String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void sendBroadcast(String base64){
        Intent intent = new Intent(CameraService.REFRESH_DATA_INTENT);
        intent.putExtra(CameraService.BASE_64_BITMAP, base64);
        sendBroadcast(intent);
    }

}