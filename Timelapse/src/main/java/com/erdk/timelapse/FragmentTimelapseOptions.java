package com.erdk.timelapse;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by erdk on 18.01.14.
 */
public class FragmentTimelapseOptions extends Fragment {

    private TextView mDurationTextView;
    private TextView mIntervalTextView;
    private TextView mFPSTextView;
    private TimelapseOptions mTimelapseOptions;

    public FragmentTimelapseOptions() {
        mTimelapseOptions = new TimelapseOptions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timelapse_options, container, false);

        if (savedInstanceState == null) {
            mDurationTextView = (TextView) rootView.findViewById(R.id.fragment_timelapse_options_duration_textView);
            mDurationTextView.setText("1 min");
            SeekBar durationSeekBar = (SeekBar) rootView.findViewById(R.id.fragment_timelapse_options_duration_seekBar);
            durationSeekBar.setProgress(mTimelapseOptions.getDuration());
            durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress == 0) {
                        seekBar.setProgress(1);
                    }

                    if (progress > 0) {
                        mTimelapseOptions.setDuration(progress / 10);
                        mDurationTextView.setText(String.valueOf(mTimelapseOptions.getDuration()) + " min");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            mIntervalTextView = (TextView) rootView.findViewById(R.id.fragment_timelapse_options_interval_textView);
            mIntervalTextView.setText("1 sec");
            SeekBar intervalSeekBar = (SeekBar) rootView.findViewById(R.id.fragment_timelapse_options_interval_seekBar);
            intervalSeekBar.setProgress(mTimelapseOptions.getInterval());
            intervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress == 0) {
                        seekBar.setProgress(1);
                    } else {
                        mTimelapseOptions.setInterval(progress);
                        mIntervalTextView.setText(String.valueOf(mTimelapseOptions.getInterval()) + " sec");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            mFPSTextView = (TextView) rootView.findViewById(R.id.fragment_timelapse_options_fps_textView);
            mFPSTextView.setText("30 fps");
            SeekBar fpsSeekBar = (SeekBar) rootView.findViewById(R.id.fragment_timelapse_options_fps_seekBar);
            fpsSeekBar.setProgress(mTimelapseOptions.getFPS());
            fpsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress == 0) {
                        seekBar.setProgress(1);
                    }

                    if (progress > 0) {
                        mTimelapseOptions.setFPS(progress);
                        mFPSTextView.setText(String.valueOf(mTimelapseOptions.getFPS()) + " fps");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }
        return rootView;
    }

    public TimelapseOptions getTimelapseOptions() {
        return mTimelapseOptions;
    }
}
