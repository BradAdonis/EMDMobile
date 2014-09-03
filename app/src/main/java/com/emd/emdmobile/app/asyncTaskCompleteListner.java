package com.emd.emdmobile.app;

import java.util.List;

//Firstly create the interface for the completed task
public interface asyncTaskCompleteListner{
    public void onWebTaskComplete(String Method, String result);
    public void onDominoTaskComplete(boolean result);
    public void onSqlSelectTaskComplete(String Method, List<sqlObject> objs);
    public void onSqlNonSelectTaskComplete(String Method, long i);
}