package com.kushpreet.Step_Tracker.presentation.steptracking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kushpreet.Step_Tracker.Presistense.StepDataPersistence;
import com.kushpreet.Step_Tracker.R;
import com.kushpreet.Step_Tracker.logic.ApplicationVar;
import com.kushpreet.Step_Tracker.objects.steps.StepData;

import java.util.List;

/*
todo: get stepData from database and send it to adapter
 */
public class StepTracker extends AppCompatActivity {
    StepDataPersistence database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_tracker);

        RecyclerView trackedStepsList = (RecyclerView)findViewById(R.id.stepTrackerRecycleView);
        trackedStepsList.setLayoutManager(new LinearLayoutManager(this));

        database = ApplicationVar.getDatabase(this);
        List<StepData> stepList = database.getUserAllStepData(ApplicationVar.getCurrentUser());
        trackedStepsList.setAdapter(new StepTrackerAdapter(stepList));

        database.close();
    }
}
