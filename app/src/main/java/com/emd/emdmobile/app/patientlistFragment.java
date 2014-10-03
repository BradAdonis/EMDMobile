package com.emd.emdmobile.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;


public class patientlistFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_PARAM1 = "patientList";
    private static final String ARG_PARAM2 = "patientListIDs";

    private ArrayList<patientDetails> Patients;
    private String[] patientsID;

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;

    public static patientlistFragment newInstance(ArrayList<patientDetails> Patients, String[] patientsID) {
        patientlistFragment fragment = new patientlistFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,Patients);
        args.putSerializable(ARG_PARAM2,patientsID);
        fragment.setArguments(args);
        return fragment;
    }


    public patientlistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Patients = (ArrayList<patientDetails>)getArguments().getSerializable(ARG_PARAM1);
            patientsID = (String[])getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patientlist, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        patientItemList adapter = new patientItemList(this.getActivity(),Patients,patientsID);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        if(Patients.size() == 0) {
            TextView tv = (TextView) view.findViewById(android.R.id.empty);
            tv.setText("Data will sync from cloud");
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            String Identifier = patientsID[position];
            mListener.onFragmentInteraction("patient",Identifier);
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String Function, String id);
    }

}
