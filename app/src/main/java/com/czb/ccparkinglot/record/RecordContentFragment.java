package com.czb.ccparkinglot.record;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czb.ccparkinglot.R;

public class RecordContentFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.record_fragment, container, false);
        return mView;
    }

    public void refresh(RecordItem recordItem) {
        TextView carName = mView.findViewById(R.id.record_fragment_textview_car_name);
        carName.setText(recordItem.getCarName());
        TextView parkinglotName = mView.findViewById(R.id.record_fragment_textview_parkinglot_name);
        parkinglotName.setText(recordItem.getParkinglotName());
        TextView startTime = mView.findViewById(R.id.record_fragment_textview_start_time);
        startTime.setText(recordItem.getStartTime().toString());
        TextView endTime = mView.findViewById(R.id.record_fragment_textview_end_time);
        if (recordItem.getEndTime() == null) {
            endTime.setText("--");
        } else {
            endTime.setText(recordItem.getEndTime().toString());
        }
    }
}
