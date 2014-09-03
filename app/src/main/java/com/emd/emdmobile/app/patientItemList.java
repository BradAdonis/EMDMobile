package com.emd.emdmobile.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class patientItemList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<patientDetails> Patients;
    private final static String strTrue = "true";
    private final static String strUnknown = "unknown";

    public patientItemList(Activity context, ArrayList<patientDetails> patients, String[] patientsid){
        super(context,R.layout.patient_item_list,patientsid);
        this.context = context;
        Patients = patients;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.patient_item_list, null, true);

        ImageView imgPatient = (ImageView)rowView.findViewById(R.id.imgPatient);
        TextView txtAccountNumber = (TextView)rowView.findViewById(R.id.txtPatientAccountNo);
        TextView txtPatientName = (TextView)rowView.findViewById(R.id.txtPatientName);
        TextView txtPatientDOB = (TextView)rowView.findViewById(R.id.txtPatientDOB);
        TextView txtPatientMedicalAid = (TextView)rowView.findViewById(R.id.txtPatientMedicalAid);
        TextView txtPatientContact = (TextView)rowView.findViewById(R.id.txtPatientContact);
        TextView txtPatientGender = (TextView)rowView.findViewById(R.id.txtPatientGender);
        TextView txtPatientAddress = (TextView)rowView.findViewById(R.id.txtPatientAddress);

        patientDetails p = Patients.get(position);

        if(p.Main.contains(strTrue)){
            imgPatient.setImageResource(R.drawable.ic_action_patient);
        }
        else{
            imgPatient.setImageResource(R.drawable.ic_action_patientdep);
        }

        txtAccountNumber.setText(p.AccountNumber);
        String pName = p.LastName + ", " + p.FirstName;
        txtPatientName.setText(pName);
        if(p.DOB.contains(strUnknown)){
            txtPatientDOB.setText("No DOB");
        }
        else{
            txtPatientDOB.setText(p.DOB);

        }

        if(p.MedAidName.contains(strUnknown) || p.MedAidName.contains("-")){
            txtPatientMedicalAid.setText("No Medical Aid");
        }
        else{
            txtPatientMedicalAid.setText(p.MedAidName);
        }

        if(p.Cell.contains(strUnknown)){
            if(p.Tel.contains(strUnknown)){
                if(p.Work.contains(strUnknown)){
                    if(p.Email.contains(strUnknown)){
                        txtPatientContact.setText("No Contact");
                    }
                    else{
                        txtPatientContact.setText(p.Email);
                    }
                }
                else{
                    txtPatientContact.setText(p.Work);
                }
            }
            else{
                txtPatientContact.setText(p.Tel);
            }
        }
        else{
            txtPatientContact.setText(p.Cell);
        }

        if(p.Gender.startsWith("M") || p.Gender.startsWith("m") || p.Gender.startsWith("F") || p.Gender.startsWith("f")) {
            txtPatientGender.setText(p.Gender.substring(0, 1).toUpperCase());
        }
        else {
            txtPatientGender.setText("");
        }

        if(p.Address.contains(strUnknown)){
            txtPatientAddress.setText("Unknown Address");
        }
        else{
            txtPatientAddress.setText(p.Address.replace(";"," "));
        }
        return rowView;
    }
}
