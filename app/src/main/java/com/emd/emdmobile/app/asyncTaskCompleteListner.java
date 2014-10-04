package com.emd.emdmobile.app;

public interface asyncTaskCompleteListner{
    public void onWebTaskComplete(asyncTask a);
    public void onDominoTaskComplete(boolean result);
    public void onSqlSelectTaskComplete(asyncTask a);
    public void onSqlNonSelectTaskComplete(asyncTask a);
}