package com.emd.emdmobile.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

public class asyncTask extends AsyncTask<String, String, String> {

    public enum asyncSystem{
        WebMethod,
        DominoAuth,
        SqlMethod,
    }

    public enum asyncSQLMethod{
        Select,
        Insert,
        Delete,
        Update,
        Refresh,
    }

    public asyncTaskCompleteListner callBack;
    private emdSession clientSession;
    private asyncSystem aSystem;
    private webFunction func = new webFunction();
    private sqlObject obj;
    private long sqlID;
    private List<sqlObject> listObj;
    private ProgressDialog loadingDialog;
    private JSONObject jObject;
    private Activity a;
    private String Message;
    private String Method = "";
    private asyncSQLMethod sqlMethod;
    private String sqlWhere = "";

    public asyncTask(Activity activity){
        this.callBack = (asyncTaskCompleteListner) activity;
        this.a = activity;
    }

    public void setAsyncSystem(asyncSystem aSystem){
        this.aSystem = aSystem;
    }

    public void setJsonObject(JSONObject jObject){
        this.jObject = jObject;
    }

    public void setMessage(String Message){
        this.Message = Message;
    }

    public void setClientSession(emdSession clientSession){
        this.clientSession = clientSession;
    }

    public void setAsyncMethod(String Method){
        this.Method = Method;
    }

    public void setSqlObjects(List<sqlObject> objs){
        this.listObj = objs;
    }

    public void setSqlObject(sqlObject obj){
        this.obj = obj;
    }

    public void setSqlMethod(asyncSQLMethod m){
        sqlMethod = m;
    }


    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        if(Message != null) {
            loadingDialog = ProgressDialog.show(a, "Working", Message);
        }
    }

    @Override
    protected String doInBackground(String... webUrl){
        try {
            if(aSystem == asyncSystem.WebMethod) {
                func.sendJSONRequest(webUrl[0], jObject);
                return func.getJsonResponseString();
            }

            if(aSystem == asyncSystem.DominoAuth){
                func.sendDominoAuthRequest(webUrl[0],clientSession.getUsername(), clientSession.getPassword(), clientSession.getDB());
                return (func.getHtmlResponseString());
            }

            if(aSystem == asyncSystem.SqlMethod){
                String result = "";
                sqlDataSource s = new sqlDataSource(a.getApplicationContext());
                s.open();

                if (sqlMethod == asyncSQLMethod.Select){
                    listObj = s.getAllObjects(obj);
                    result = String.valueOf(listObj.size());

                }else if(sqlMethod == asyncSQLMethod.Insert){
                    if(listObj.size() > 0){
                        for(int i = 0; i < listObj.size(); i++){
                            sqlObject o = listObj.get(i);
                            long index = s.insertObject(o);
                            result = String.valueOf(index);
                            sqlID = index;
                        }
                    }
                    else{
                        long index = s.insertObject(obj);
                        result = String.valueOf(index);
                        sqlID = index;
                    }
                }else if(sqlMethod == asyncSQLMethod.Delete){
                    if(listObj.size() > 0){
                        for(int i = 0; i < listObj.size(); i++){
                            sqlObject o = listObj.get(i);
                            long index = s.deleteObject(o);
                            result = String.valueOf(index);
                            sqlID = index;
                        }
                    }
                    else{
                        long index = s.deleteObject(obj);
                        result = String.valueOf(index);
                        sqlID = index;
                    }
                }
                else if(sqlMethod == asyncSQLMethod.Refresh){
                    if(listObj.size() > 0){
                        for(int i = 0; i < listObj.size(); i++){
                            sqlObject o = listObj.get(i);
                            long idel = s.deleteObject(o);
                            long iins = s.insertObject(o);
                            result = String.valueOf(iins);
                            sqlID = iins;
                        }
                    }
                }

                s.close();

                return result;
            }
        }
        catch(Exception e){
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String Result){
        super.onPostExecute(Result);

        if(Message != null){
            loadingDialog.dismiss();
        }
        try{
            if(aSystem == asyncSystem.WebMethod){
                callBack.onWebTaskComplete(this.Method,Result);
            }

            if(aSystem == asyncSystem.DominoAuth){
                callBack.onDominoTaskComplete(func.getAuthValid());
            }

            if(aSystem == asyncSystem.SqlMethod){
                if(sqlMethod == asyncSQLMethod.Select){
                    callBack.onSqlSelectTaskComplete(Method,listObj);
                }
                else{
                    callBack.onSqlNonSelectTaskComplete(Method,sqlID);
                }
            }

        }
        catch(Exception e){
        }
    }
}
