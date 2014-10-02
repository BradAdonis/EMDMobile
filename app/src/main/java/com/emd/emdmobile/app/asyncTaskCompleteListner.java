package com.emd.emdmobile.app;

import java.util.List;

//Firstly create the interface for the completed task
public interface asyncTaskCompleteListner{
    public void onWebTaskComplete(asyncTask a);
    public void onDominoTaskComplete(boolean result);
    public void onSqlSelectTaskComplete(asyncTask a);
    public void onSqlNonSelectTaskComplete(asyncTask a);
}