package com.kushpreet.Step_Tracker.objects.steps;

import com.kushpreet.Step_Tracker.objects.MyDate;

public class StepData{// extends Data{
    MyDate date;
    int steps;
    public StepData(int steps, MyDate date){
        this.steps = steps;
        this.date = date;
    }
    public StepData(int steps,int year, int month, int day){
        date = new MyDate(year,month,day);
        this.steps = steps;
    }

    public String getFormattedDate(){
        return date.toString();
    }

    public MyDate getDate(){ return this.date; }
    public int getSteps(){ return this.steps; }

    public void setDate(MyDate newDate){ this.date = newDate; }
    public void setDate(int year, int month, int day){
        MyDate newDate = new MyDate(year,month,day);
        this.date = newDate;
    }
    public void setSteps(int newSteps){
        this.steps = newSteps;
    }
}
