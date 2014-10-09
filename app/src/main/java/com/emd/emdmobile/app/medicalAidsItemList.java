package com.emd.emdmobile.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Bradl_000 on 2014-10-09.
 */
public class medicalAidsItemList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<medicalAidDetails> MedicalAids;

    public medicalAidsItemList(Activity context, ArrayList<medicalAidDetails> medicalAids, String[] medicalAidNames){
        super(context, R.layout.medicalaids_item_list,medicalAidNames);
        this.context = context;
        MedicalAids = medicalAids;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflator = context.getLayoutInflater();
        View rowView = inflator.inflate(R.layout.medicalaids_item_list,null,true);

        TextView txtMedicalAidName = (TextView)rowView.findViewById(R.id.txtMedicalAidName);
        TextView txtConsultationText = (TextView)rowView.findViewById(R.id.txtConsultationText);
        TextView txtProcedureText = (TextView)rowView.findViewById(R.id.txtProceduresText);
        TextView txtAcuteText = (TextView)rowView.findViewById(R.id.txtAcuteText);
        TextView txtChronicText = (TextView)rowView.findViewById(R.id.txtChronicText);
        TextView txtGrandTotal = (TextView)rowView.findViewById(R.id.txtGrandTotal);

        medicalAidDetails d = MedicalAids.get(position);

        txtMedicalAidName.setText(d.MedicalAid);
        txtConsultationText.setText(d.ConsultationTotal);
        txtProcedureText.setText(d.ProceduralTotal);
        txtAcuteText.setText(d.AcuteTotal);
        txtChronicText.setText(d.ChronicTotal);
        txtGrandTotal.setText(d.GrandTotal);

        return rowView;
    }
}
