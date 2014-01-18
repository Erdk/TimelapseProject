package com.erdk.timelapse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends Activity {

    private static final String TAG = "ActivityMain";

    private FragmentTimelapseOptions mFragmentTimelapseOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentTimelapseOptions = new FragmentTimelapseOptions();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_content, mFragmentTimelapseOptions)
                .commit();

        // Add a listener to the Capture button
        Button lNextButton = (Button) findViewById(R.id.button_capture);
        lNextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get options from fragment
                        TimelapseOptions lTimelapseOptions = mFragmentTimelapseOptions.getTimelapseOptions();
                        Log.d(TAG, "Timelapse duration: " + lTimelapseOptions.getDuration());
                        Log.d(TAG, "Timelapse interval: " + lTimelapseOptions.getInterval());
                        Log.d(TAG, "Timelapse FPS: " + lTimelapseOptions.getFPS());

                        // remove fragment options
                        FragmentPreview lFragmentPreview = new FragmentPreview();
                        Bundle lBundle = new Bundle();
                        lBundle.putParcelable("options", lTimelapseOptions);
                        lFragmentPreview.setArguments(lBundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.activity_main_content, lFragmentPreview)
                                .commit();

                        v.setVisibility(View.GONE);
                    }
                }
        );
    }
}
