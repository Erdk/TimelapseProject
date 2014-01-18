package com.erdk.timelapse;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by erdk on 18.01.14.
 */
public class FragmentPreview extends Fragment {

    private static final String TAG = "FragmentPreview";

    private String mSavePath;
    private Camera mCamera;
    private TimelapseOptions mTimelapseOptions;
    private boolean mIsRunning;
    private TextView mCurrentAndTotalFramesTextView;

    public FragmentPreview() {
        File rootDir = Environment.getExternalStorageDirectory();
        File appDir = new File(rootDir + "/Timelapses");
        if (appDir.exists()) {
            mSavePath = appDir.getAbsolutePath();
        } else {
            mSavePath = rootDir.getAbsolutePath();
        }

        mIsRunning = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle lArguments = getArguments();
        if (lArguments == null) {
            mTimelapseOptions = new TimelapseOptions();
        } else {
            mTimelapseOptions = lArguments.getParcelable("options");

            if (mTimelapseOptions == null) {
                mTimelapseOptions = new TimelapseOptions();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View lRootView = inflater.inflate(R.layout.fragment_preview, container, false);
        Button lStartButton = (Button) lRootView.findViewById(R.id.fragment_preview_start_button);
        lStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mIsRunning) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int lNumberOfFrames = mTimelapseOptions.getFPS() * mTimelapseOptions.getDuration();
                            for (int i = 0; i < lNumberOfFrames; i++) {
                                mCamera.takePicture(null, null, mPicture);
                                mCurrentAndTotalFramesTextView.post(new PostProgress(i, lNumberOfFrames));

                                try {
                                    Thread.sleep(mTimelapseOptions.getInterval() * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                    mIsRunning = true;
                }
            }
        });

        mCurrentAndTotalFramesTextView = (TextView) lRootView.findViewById(R.id.fragment_preview_numer_textView);
        mCurrentAndTotalFramesTextView.setText("0 / " + String.valueOf(mTimelapseOptions.getFPS() * mTimelapseOptions.getDuration()));

        // add camera preview
        mCamera = GetCameraInstance();
        SurfaceCamera lSurfaceCamera = new SurfaceCamera(getActivity(), mCamera);

        FrameLayout preview = (FrameLayout) lRootView.findViewById(R.id.fragment_preview_camera_surface);
        preview.addView(lSurfaceCamera);

        return lRootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCamera != null) {
            mCamera.release();
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Date time = Calendar.getInstance().getTime();

            File pictureFile = new File(mSavePath + "/" + time.toString() + ".jpg");
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

            RescanMedia(getActivity(), mSavePath);
            camera.startPreview();
        }
    };

    private static Camera GetCameraInstance() {
        Camera camera = null;

        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "Cannot open camera: " + e.getMessage());
        }

        return camera;
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

    class PostProgress implements Runnable {

        private int mCurrent;
        private int mTotal;

        PostProgress(int pCurrent, int pTotal) {
            mCurrent = pCurrent;
            mTotal = pTotal;
        }

        @Override
        public void run() {
            mCurrentAndTotalFramesTextView.setText(String.valueOf(mCurrent + 1) + " / "
                    + String.valueOf(mTotal));
        }
    }
}
