package com.kushpreet.Step_Tracker.logic.Utils;

import com.kushpreet.Step_Tracker.objects.MyDate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManipulate {
    private DateManipulate(){} //don't construct this

    public static String getCurrentDeviceDate(){
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(today);
    }

    public static MyDate createFromString(String date) {
        String[] splitDate = date.split("-");
        MyDate returnVal = null;
        if (splitDate.length == 3) {
            try {
                int day = Integer.parseInt(splitDate[0]);
                int month = Integer.parseInt(splitDate[1]);
                int year = Integer.parseInt(splitDate[2]);
                if(validateDate(day,month,year)){
                    returnVal = new MyDate(year,month,day);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return returnVal;
    }

    public static boolean validateDate(int day, int month, int year){
        boolean returnVal = true;
        if(month > 0 && month <= 12 && day > 0) {
            if(month == 2) {
                if (((year % 4 == 0) && !(year % 100 == 0)) && (year % 400 == 0))
                    returnVal = day <= 29;
                else
                    returnVal = day <= 28;
            }else if(month == 4 || month == 6 ||month==9||month==11){
                returnVal = day <= 30;
            }else{
                returnVal = day <= 31;
            }
        }
        return returnVal;
    }
}
