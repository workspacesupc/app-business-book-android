package com.edu.upc.businessbook.viewcontrollers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.upc.businessbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProvidersFragment extends Fragment {


    public ProvidersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_providers, container, false);
    }

}
