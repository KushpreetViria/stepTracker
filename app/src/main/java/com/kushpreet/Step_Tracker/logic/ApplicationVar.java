package com.kushpreet.Step_Tracker.logic;

import android.content.Context;

import com.kushpreet.Step_Tracker.Presistense.DatabaseHelper;

public class ApplicationVar {
    private static int currentUser = 0;

    public enum BroadcastVar {
        KEY_INT("Key_Int"),KEY_BOOL("Key_Bool"),KEY_STR("Key_Str"),
        ACTION_UPDATE_UI_STEP("Action_Step_UIUpdate"),
        ACTION_UPDATE_STEP_DB("Action_Update_Step_DB"),
        ACTION_REQUEST_STEP_COUNT("Action_Request_step_Count"),
        ACTION_SERVICE_ERROR("Action_Service_Error");

        String val;
        BroadcastVar(String strVal){val = strVal;}

        public String getVar() {return val;}
    }

    public static DatabaseHelper getDatabase(Context context){ return new DatabaseHelper(context); }
    public static void setCurrentUser(int userId){
        currentUser = userId;
    }
    public static int getCurrentUser(){
        return currentUser;
    }


}
