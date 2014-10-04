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

import java.util.ArrayList;

public class claimlistFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_PARAM1 = "claimList";
    private static final String ARG_PARAM2 = "claimListIDs";

    private ArrayList<claimDetails> Claims;
    private String[] claimsID;

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public static claimlistFragment newInstance(ArrayList<claimDetails> Claims, String[] claimIDs) {
        claimlistFragment fragment = new claimlistFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, Claims);
        args.putSerializable(ARG_PARAM2, claimIDs);

        fragment.setArguments(args);
        return fragment;
    }

    public claimlistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Claims = (ArrayList<claimDetails>)getArguments().getSerializable(ARG_PARAM1);
            claimsID = (String[])getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claimlist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        claimItemList adapter = new claimItemList(this.getActivity(), Claims, claimsID);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        if(Claims.size() == 0) {
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

            String identifier = claimsID[position];
            mListener.onFragmentInteraction("claim",identifier);
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
