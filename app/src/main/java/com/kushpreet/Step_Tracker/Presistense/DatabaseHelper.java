package com.kushpreet.Step_Tracker.Presistense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kushpreet.Step_Tracker.logic.Utils.DateManipulate;
import com.kushpreet.Step_Tracker.objects.steps.StepData;

import java.util.ArrayList;
import java.util.List;

//database for users and steps
public class DatabaseHelper extends SQLiteOpenHelper implements StepDataPersistence {
    public static final String DATABASE_NAME = "STEP_DATABASE";
    public static final int VERSION = 1;

    public static final String STEP_DATA_TABLE = "STEP_DATA_TABLE";

    public static final String COLUMN_STEP_USER_ID = "STEP_USER_ID";
    public static final String COLUMN_STEP_DATE = "STEP_DATE";
    public static final String COLUMN_STEP_COUNT = "STEP_COUNT";

    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME , null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStepTable = "CREATE TABLE " + STEP_DATA_TABLE + " (" + COLUMN_STEP_USER_ID + " INTEGER," +
                " " + COLUMN_STEP_DATE + " TEXT," +
                COLUMN_STEP_COUNT + " INTEGER)";
        db.execSQL(createStepTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+STEP_DATA_TABLE);
        this.onCreate(db);
    }
    //--------------------------------------------------------------------------------------------//

    @Override
    public StepData insertUserStepData(int userId, StepData data) {
        if(getUserStepData(userId,data.getFormattedDate()) != null);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEP_USER_ID,userId);
        values.put(COLUMN_STEP_DATE,data.getFormattedDate());
        values.put(COLUMN_STEP_COUNT,data.getSteps());
        long success = db.insert(STEP_DATA_TABLE,null,values);
        return (success > 0) ? data : null;
    }

    @Override
    public List<StepData> getUserAllStepData(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<StepData> stepList = new ArrayList<StepData>();
        String query = "SELECT * FROM " + STEP_DATA_TABLE;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){//loop and create the userList
            do{
                StepData steps = createStepDataFromCursor(cursor);
                stepList.add(steps);
            }while(cursor.moveToNext());
        }else{
            stepList = null;
        }
        cursor.close();
        db.close();
        return stepList;
    }

    @Override
    public StepData getUserStepData(int userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + STEP_DATA_TABLE+
                " WHERE "+ COLUMN_STEP_USER_ID+" = ? AND "
                + COLUMN_STEP_DATE+" = ?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(userId),date});

        StepData sData= null;
        if(cursor.moveToFirst())
            sData = createStepDataFromCursor(cursor);
        cursor.close();
        return sData;
    }

    @Override
    public StepData updateUserStepData(int userId, StepData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEP_USER_ID,userId);
        values.put(COLUMN_STEP_DATE,data.getFormattedDate());
        values.put(COLUMN_STEP_COUNT,data.getSteps());
        int updatedSteps = db.update(STEP_DATA_TABLE,values,COLUMN_STEP_USER_ID+" = ? AND "
                        +COLUMN_STEP_DATE+" = ?", new String[]{String.valueOf(userId),data.getFormattedDate()});
        Log.i("DATABASE",""+updatedSteps);
        return (updatedSteps > 0) ? data : null;
    }

    @Override
    public boolean removeUserStepData(int userId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        long deletedRows = db.delete(STEP_DATA_TABLE,COLUMN_STEP_USER_ID+" = ? AND "+
                        COLUMN_STEP_DATE +" = ?", new String[]{String.valueOf(userId),date});
        return (deletedRows > 0) ? true : false;
    }

    private StepData createStepDataFromCursor(Cursor cursor) {
        String date = cursor.getString(1);
        int steps = cursor.getInt(2);

        StepData newStepData = new StepData(steps, DateManipulate.createFromString(date));
        return newStepData;
    }
}
