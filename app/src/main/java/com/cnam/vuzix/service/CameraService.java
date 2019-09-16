package com.cnam.vuzix.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

import com.cnam.vuzix.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

//TODO : release la camera!

@SuppressWarnings("deprecation")
public class CameraService extends Service {
    private static Camera mCamera;

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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
                    MainActivity.imageView.setImageBitmap(picture);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void takePicture(final String fileName) {
        Camera.PictureCallback callback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
                MainActivity.imageView.setImageBitmap(picture);
                    try {
                        FileOutputStream out = new FileOutputStream(fileName);
                        picture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        picture.recycle();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        };
    }
 */
}