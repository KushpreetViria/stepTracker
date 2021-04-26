package com.kushpreet.Step_Tracker.Services.StepCounter;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kushpreet.Step_Tracker.Presistense.StepDataPersistence;
import com.kushpreet.Step_Tracker.logic.ApplicationVar;
import com.kushpreet.Step_Tracker.logic.Utils.DateManipulate;
import com.kushpreet.Step_Tracker.objects.MyDate;
import com.kushpreet.Step_Tracker.objects.steps.StepData;

import java.util.Timer;
import java.util.TimerTask;

/* Service class that runs in background, uses StepDetector to constantly
 * count steps, and sends this information to database, or to the UI.
 * */
public class StepCountService extends Service {
    private static final String TAG = "Step counter Service"; //for debug
    private static final int HALF_SEC = 500;
    private static final int FIVE_MIN = 1000 * 60 * 5;

    private BroadcastReceiver serviceReceiver;
    private StepDetector stepDetector;
    private Timer timer;
    private TimerTask timerTaskUI;
    private TimerTask timerTaskDB;
    private boolean sendSteps;
    private int currentUser;

    StepDataPersistence database;

    @Override
    public void onCreate() {
        super.onCreate();
        sendSteps = true;
        database = ApplicationVar.getDatabase(this);
        currentUser = ApplicationVar.getCurrentUser();
        
        StepData todayData = database.getUserStepData(currentUser, DateManipulate.getCurrentDeviceDate());
        int startingSteps = todayData != null ? todayData.getSteps() : 0;
        this.stepDetector = new StepDetector(this, startingSteps);
        startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setupReceiver();
        try {
            stepDetector.startSensor();
        } catch (SensorFailureException e) {
            Log.e(TAG, e.getMessage());
            Intent NewIntent = new Intent(ApplicationVar.BroadcastVar.ACTION_SERVICE_ERROR.getVar());
            NewIntent.putExtra(ApplicationVar.BroadcastVar.KEY_STR.getVar(), stepDetector.getSteps());
            sendBroadcast(intent);
            e.printStackTrace();
            stopSelf();
        }
        startTimer();
        return START_STICKY;
    }

    //sends the updated step number to UI every half sec (if requested)
    //updates database every 5 minutes
    private void startTimer() {
        timer = new Timer();
        timerTaskUI = new TimerTask() {
            public void run() {
                if (sendSteps)
                    sendStepsToUI(stepDetector.getSteps());
            }
        };
        timerTaskDB = new TimerTask() {
            public void run() {
                sendStepCountToDb(stepDetector.getSteps());
            }
        };
        timer.schedule(timerTaskUI, 0, HALF_SEC); //update ui twice every sec
        timer.schedule(timerTaskDB, 0, FIVE_MIN); // update database automatically every 5 minutes
    }

    private void setupReceiver(){
        serviceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == ApplicationVar.BroadcastVar.ACTION_REQUEST_STEP_COUNT.getVar()) { // UI ask for steps
                    sendSteps = intent.getBooleanExtra(ApplicationVar.BroadcastVar.KEY_BOOL.getVar(), true);
                } else if (action == ApplicationVar.BroadcastVar.ACTION_UPDATE_STEP_DB.getVar()) {  //if UI asks to update db
                    sendStepCountToDb(stepDetector.getSteps());
                } else {
                    throw new IllegalStateException("Unexpected value in StepCounterService: " + action);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ApplicationVar.BroadcastVar.ACTION_UPDATE_STEP_DB.getVar());
        intentFilter.addAction(ApplicationVar.BroadcastVar.ACTION_REQUEST_STEP_COUNT.getVar());
        intentFilter.addAction(ApplicationVar.BroadcastVar.ACTION_SERVICE_ERROR.getVar());
        registerReceiver(serviceReceiver, intentFilter);
    }

    private void sendStepCountToDb(int steps) {
        int currentUserId = ApplicationVar.getCurrentUser();
        String date = DateManipulate.getCurrentDeviceDate();
        MyDate myDate = DateManipulate.createFromString(date);
        StepData stepData = new StepData(steps, DateManipulate.createFromString(date));

        if(database.updateUserStepData(currentUserId,stepData) == null) //update it (based on current date)
            database.insertUserStepData(currentUserId, stepData);   //insert new if update never happened
    }

    private void sendStepsToUI(int steps) {
        Intent NewIntent = new Intent();
        NewIntent.setAction(ApplicationVar.BroadcastVar.ACTION_UPDATE_UI_STEP.getVar());
        NewIntent.putExtra(ApplicationVar.BroadcastVar.KEY_INT.getVar(), steps);
        sendBroadcast(NewIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        int steps = stepDetector.getSteps();
        sendStepsToUI(steps);
        sendStepCountToDb(steps);
        stepDetector.removeSensor();
        unregisterReceiver(serviceReceiver);
        timer.cancel();
        database.close();
        super.onDestroy();
    }
}

