package com.kushpreet.Step_Tracker.Presistense;


import com.kushpreet.Step_Tracker.objects.steps.StepData;

import java.util.List;

public interface StepDataPersistence {
    public StepData insertUserStepData(int userId, StepData stepData);
    public List<StepData> getUserAllStepData(int userId);
    public StepData getUserStepData(int userId, String date);
    public StepData updateUserStepData(int userId, StepData stepData);
    public boolean removeUserStepData(int userId, String date);

    public void close();
}
