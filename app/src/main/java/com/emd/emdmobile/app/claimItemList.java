package com.emd.emdmobile.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Bradl_000 on 2014-09-30.
 */
public class claimItemList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<claimDetails> Claims;
    private final static String strUnknown = "unknown";

    public claimItemList(Activity context, ArrayList<claimDetails> claims, String[] claimids){
        super(context, R.layout.claim_item_list,claimids);
        this.context = context;
        Claims = claims;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflator = context.getLayoutInflater();
        View rowView = inflator.inflate(R.layout.claim_item_list,null,true);

        ImageView imgStatus = (ImageView)rowView.findViewById(R.id.imgClaimStatus);
        TextView txtPatient = (TextView) rowView.findViewById(R.id.txtPatientName);
        TextView txtVisDate = (TextView) rowView.findViewById(R.id.txtVisitDate);
        TextView txtICDDescr = (TextView) rowView.findViewById(R.id.txtICD);
        TextView txtTotalCharged = (TextView) rowView.findViewById(R.id.txtTotalCharged);
        TextView txtOutstanding = (TextView) rowView.findViewById(R.id.txtOutstanding);
        //TextView txtPatientCharged = (TextView) rowView.findViewById(R.id.txtPatientCharged);
        //TextView txtMedicalCharged = (TextView) rowView.findViewById(R.id.txtMedicalAidCharged);
        TextView txtMedicalAid = (TextView) rowView.findViewById(R.id.txtClaimMedAid);
        TextView txtMedicalAidOption = (TextView) rowView.findViewById(R.id.txtClaimMedAidOpt);

        claimDetails c = Claims.get(position);

        if(c.TotalOutstanding.contentEquals("0")){
            imgStatus.setImageResource(R.drawable.ic_action_approve);
        }
        else{
            imgStatus.setImageResource(R.drawable.ic_action_reject);
        }

        txtPatient.setText(c.Patient);
        txtVisDate.setText(c.VisitDate);
        txtTotalCharged.setText(c.TotalCharged);
        txtOutstanding.setText(c.TotalOutstanding);
        //txtMedicalCharged.setText(c.TotalMedicalAidCharged);
        //txtPatientCharged.setText(c.TotalPatientCharged);
        txtICDDescr.setText(c.ICDDescr);
        txtMedicalAid.setText(c.MedicalAid);
        txtMedicalAidOption.setText(c.MedicalAidOption);

        return rowView;
    }

}
