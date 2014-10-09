package com.emd.emdmobile.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.emd.emdmobile.app.dummy.DummyContent;

import java.util.ArrayList;

public class medicalAidsListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_PARAM1 = "medicalAidsList";
    private static final String ARG_PARAM2 = "medicalAidsNames";

    private ArrayList<medicalAidDetails> medicalAidsList;
    private String[] medicalAidNames;
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;

    private ListAdapter mAdapter;

    public static medicalAidsListFragment newInstance(ArrayList<medicalAidDetails> medicalAidsList, String[] medicalAidNames) {
        medicalAidsListFragment fragment = new medicalAidsListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, medicalAidsList);
        args.putSerializable(ARG_PARAM2, medicalAidNames);
        fragment.setArguments(args);
        return fragment;
    }

    public medicalAidsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            medicalAidsList = (ArrayList<medicalAidDetails>)getArguments().getSerializable(ARG_PARAM1);
            medicalAidNames = (String[])getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicalaidslistfragment, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        medicalAidsItemList adapter = new medicalAidsItemList(this.getActivity(), medicalAidsList, medicalAidNames);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        if(medicalAidNames.length == 0){
            TextView tv = (TextView) view.findViewById(android.R.id.empty);
            tv.setText("Data will sync from cloud");
        }

        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
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
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

}
