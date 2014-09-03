package com.emd.emdmobile.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class alertDialog extends Dialog {
    private Activity a;
    private String Msg;
    private Dialog d;
    private Context context;

    public alertDialog(Activity a, String Msg){
        super(a);
        this.a = a;
        this.Msg = Msg;
    }

    public alertDialog(Context c, String Msg){
        super(c);
        this.context = c;
        this.Msg = Msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);
        createDialogView();
    }

    private void createDialogView(){
        TextView tView = (TextView)findViewById(R.id.msgText);
        tView.setText(Msg);
    }


}
