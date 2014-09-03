package com.emd.emdmobile.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link webView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link webView#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class webView extends Fragment  {

    private class eMDWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String URL){
            view.loadUrl(URL);
            return true;
        }
    }

    private static final String ARG_PARAM1 = "urlParameter";
    private String mUrlParam;
    private OnFragmentInteractionListener mListener;

    public static webView newInstance(String urlParameter) {
        webView fragment = new webView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, urlParameter);
        fragment.setArguments(args);
        return fragment;
    }
    public webView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrlParam = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);
        WebView wView = (WebView)v.findViewById(R.id.webPageViewer);
        wView.setWebViewClient(new eMDWebViewClient());
        wView.getSettings().setJavaScriptEnabled(true);
        wView.loadUrl(mUrlParam);
        wView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        wView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
