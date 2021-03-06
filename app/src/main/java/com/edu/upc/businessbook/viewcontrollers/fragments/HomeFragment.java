package com.edu.upc.businessbook.viewcontrollers.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.edu.upc.businessbook.R;
import com.edu.upc.businessbook.models.Local;
import com.edu.upc.businessbook.models.LocalsRepository;
import com.edu.upc.businessbook.network.BusinessBookApi;
import com.edu.upc.businessbook.viewcontrollers.adapters.LocalsAdapter;
import com.edu.upc.businessbook.viewcontrollers.dialogs.DialogPersonalized;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView localsRecyclerView;
    private LocalsAdapter localsAdapter;
    //LinearLayoutManager layoutManager;
    private RecyclerView.LayoutManager localsLayoutManager;
    private List<Local> locals;
    private SharedPreferences result;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        result = this.getActivity().getSharedPreferences("Session",Context.MODE_PRIVATE);
        localsRecyclerView = view.findViewById(R.id.recycler_locals);
        locals = new ArrayList<>();
        //localsAdapter = new LocalsAdapter(LocalsRepository.getInstance().getLocals());
        localsAdapter = new LocalsAdapter(locals);
        localsLayoutManager = new LinearLayoutManager(view.getContext());
        localsRecyclerView.setAdapter(localsAdapter);
        localsRecyclerView.setLayoutManager(localsLayoutManager);

        getListLocals(1);
        FloatingActionButton localFab = (FloatingActionButton) view.findViewById(R.id.localFab);
        localFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DialogPersonalized();
                dialogFragment.show(getFragmentManager(), "Dialog");
            }
        });



        return view;
    }
    private void getListLocals(int companyId){

        String token = result.getString("userToken","Token Expirado");

        String url = BusinessBookApi.getLocalsUrl(companyId);
        AndroidNetworking.get(url)
                .addHeaders("Authorization",token)
                .setPriority(Priority.LOW)
                .setTag("businessbook")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response != null){
                                locals = Local.Builder.from(response.getJSONArray("Result")).buildAll();
                                localsAdapter.setLocals(locals);
                                localsAdapter.notifyDataSetChanged();
                                Log.d("businessbook", String.format("Locals Count: %d", locals.size()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }


}
