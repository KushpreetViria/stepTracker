package com.kushpreet.Step_Tracker.presentation.steptracking;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kushpreet.Step_Tracker.R;

public class StepTrackerViewHolder extends RecyclerView.ViewHolder{
    TextView dateTextView;
    TextView stepsTextView;
    public StepTrackerViewHolder(View itemView) {
        super(itemView);
        dateTextView = (TextView) itemView.findViewById(R.id.step_tracker_list_item_date);
        stepsTextView = (TextView) itemView.findViewById(R.id.step_tracker_list_item_steps);
    }
}