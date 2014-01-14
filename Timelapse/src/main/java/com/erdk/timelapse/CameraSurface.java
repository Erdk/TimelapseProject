package com.erdk.timelapse;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by erdk on 13.01.14.
 */
public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraSurface";
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CameraSurface (Context context, Camera camera) {
        super(context);

        mCamera = camera;

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG, "Cannot stop camera preview!");
        }

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // take care of it in activity
    }
}
