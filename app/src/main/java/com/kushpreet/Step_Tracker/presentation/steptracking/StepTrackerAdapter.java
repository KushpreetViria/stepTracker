package com.kushpreet.Step_Tracker.presentation.steptracking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kushpreet.Step_Tracker.R;
import com.kushpreet.Step_Tracker.objects.steps.StepData;

import java.util.List;

public class StepTrackerAdapter extends RecyclerView.Adapter<StepTrackerViewHolder>{
    List<StepData> stepList;

    public StepTrackerAdapter(List<StepData> data){
        stepList = data;
    }

    @NonNull
    @Override
    public StepTrackerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.step_tracker_recycle_view_list_item_layout,parent,false);
        return new StepTrackerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepTrackerViewHolder holder, int position) {
        StepData current = stepList.get(position);
        holder.dateTextView.setText(current.getFormattedDate());
        holder.stepsTextView.setText(String.valueOf(current.getSteps()));
    }

    @Override
    public int getItemCount() { return (stepList != null) ? stepList.size() : 0; }
}
