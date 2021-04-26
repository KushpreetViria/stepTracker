package com.kushpreet.Step_Tracker.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kushpreet.Step_Tracker.R;
import com.kushpreet.Step_Tracker.Services.StepCounter.StepCountService;
import com.kushpreet.Step_Tracker.logic.ApplicationVar;
import com.kushpreet.Step_Tracker.presentation.steptracking.StepTracker;

// step counter activity UI class
public class StepCounter extends AppCompatActivity {
    public String TAG = "StepActivity"; //for debug
    private TextView tv_stepNumber;
    private Button serviceStartBtn;
    private Button serviceStopBtn;
    private Button stepTrackerLauncher;
    private Intent myIntent;
    private BroadcastReceiver activityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //keep screen on
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//keep portrait mode

        tv_stepNumber = (TextView) findViewById(R.id.tvStepNumber);
        serviceStartBtn = (Button) findViewById(R.id.startBtn);
        serviceStopBtn = (Button) findViewById(R.id.stopBtn);
        stepTrackerLauncher = (Button) findViewById(R.id.stepTrackerLaunchBtn);

        serviceStartBtn.setOnClickListener(view -> {
            startService(myIntent = new Intent(StepCounter.this, StepCountService.class));
            serviceStartBtn.setEnabled(false);
        });
        serviceStopBtn.setOnClickListener(view -> {
            if (myIntent != null) stopService(myIntent);
            else myIntent = null;
            serviceStartBtn.setEnabled(true);
        });
        stepTrackerLauncher.setOnClickListener(view -> {
            Intent intent = new Intent(StepCounter.this, StepTracker.class);
            StepCounter.this.startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupReceiver();
        sendIntent(ApplicationVar.BroadcastVar.ACTION_REQUEST_STEP_COUNT.getVar(),
                ApplicationVar.BroadcastVar.KEY_BOOL.getVar(), true);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Stopping UI");
        unregisterReceiver(activityReceiver);
        sendIntent(ApplicationVar.BroadcastVar.ACTION_UPDATE_STEP_DB.getVar());
        sendIntent(ApplicationVar.BroadcastVar.ACTION_REQUEST_STEP_COUNT.getVar(),
                ApplicationVar.BroadcastVar.KEY_BOOL.getVar(), false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        sendIntent(ApplicationVar.BroadcastVar.ACTION_UPDATE_STEP_DB.getVar());
        if (myIntent != null) stopService(myIntent);
        super.onDestroy();
    }

    public void sendIntent(String action) {
        Intent NewIntent = new Intent();
        NewIntent.setAction(action);
        sendBroadcast(NewIntent);
    }

    public void sendIntent(String action, String extra, boolean bool) {
        Intent NewIntent = new Intent();
        NewIntent.setAction(action);
        NewIntent.putExtra(extra, bool);
        sendBroadcast(NewIntent);
    }


    public void setupReceiver() {
        activityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceive(intent);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ApplicationVar.BroadcastVar.ACTION_UPDATE_UI_STEP.getVar());
        intentFilter.addAction(ApplicationVar.BroadcastVar.ACTION_SERVICE_ERROR.getVar());
        registerReceiver(activityReceiver, intentFilter);
    }

    private void onBroadcastReceive(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ApplicationVar.BroadcastVar.ACTION_UPDATE_UI_STEP.getVar())) {
            int stepCount = intent.getIntExtra(ApplicationVar.BroadcastVar.KEY_INT.getVar(), 0);
            tv_stepNumber.setText(String.valueOf(stepCount));
        } else if (action.equals(ApplicationVar.BroadcastVar.ACTION_SERVICE_ERROR.getVar())) {
            String msg = intent.getStringExtra(ApplicationVar.BroadcastVar.KEY_STR.getVar());
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            serviceStartBtn.setEnabled(true);
        }
    }
}
