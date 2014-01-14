package com.erdk.timelapse;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private String mSavePath;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCamera = GetCameraInstance();
        CameraSurface cameraSurface = new CameraSurface(this, mCamera);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraSurface);

        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
//                        mCamera.startPreview();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCamera != null) {
            mCamera.release();
        }

        File savePath = new File(mSavePath);
        if (savePath.exists()) {
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{mSavePath}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Toast.makeText(MainActivity.this, "Timelapse: media library refresh completed!", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private static void RescanMedia(Context context, String savePath) {
        MediaScannerConnection.scanFile(context, new String[]{savePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d(TAG, "Media refresh completed!");
                    }
                });
    }

    private static Camera GetCameraInstance() {
        Camera camera = null;

        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "Cannot open camera: " + e.getMessage());
        }

        return camera;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Date time = Calendar.getInstance().getTime();

            File rootDir = Environment.getExternalStorageDirectory();
            File appDir = new File(rootDir + "/Timelapses");
            if (!appDir.exists()) {
                if (!appDir.mkdirs()) {
                    return;
                }
            } else {
                mSavePath = appDir.getAbsolutePath();
            }

            File pictureFile = new File(appDir + "/" + time.toString() + ".jpg");
            if (pictureFile == null) {
                try {
                    pictureFile.createNewFile();
                } catch (IOException e) {
                    Log.d(TAG, "Error creating media file, check storage permissions: " + e.getMessage());
                    return;
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }

            RescanMedia(MainActivity.this, appDir.getAbsolutePath());
            camera.startPreview();
        }
    };
}
