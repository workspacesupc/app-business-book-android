package com.edu.upc.businessbook.viewcontrollers.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.edu.upc.businessbook.R;
import com.edu.upc.businessbook.viewcontrollers.dialogs.DialogPersonalized;
import com.edu.upc.businessbook.viewcontrollers.fragments.HomeFragment;
import com.edu.upc.businessbook.viewcontrollers.fragments.ProfileFragment;
import com.edu.upc.businessbook.viewcontrollers.fragments.ReportsFragment;
import com.edu.upc.businessbook.viewcontrollers.network.NewApi;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String STRING_PREFERENCE = "Session";
    private static final String ACCOUNT_TOKEN = "userToken";
    private static final String COMPANY_ID = "CompanyId";
    private static final String EMPLOYEE_ID = "EmployeeId";
    private String number;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            return navigateTo(item);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            number = getIntent().getExtras().getString("number");
            login();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateTo(navigation.getMenu().findItem(R.id.navigation_home));


    }

    private Fragment getFragmentFor(MenuItem item){
        switch (item.getItemId()) {
            case R.id.navigation_home: return new HomeFragment();
            case R.id.navigation_reports: return new ReportsFragment();
            case R.id.navigation_profile: return new ProfileFragment();
            default: return  new HomeFragment();
        }
    }

    private boolean navigateTo(MenuItem item){
        item.setChecked(true);
        return getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, getFragmentFor(item))
                .commit()>0;
    }

    public void login(){

        AndroidNetworking.post(NewApi.postUserLoginUrl())
                .addBodyParameter("users", number)
                .addBodyParameter("password", "123")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
                            preferences.edit().putString(ACCOUNT_TOKEN,"Bearer "+response.getJSONObject("Result").getString("accessToken")).apply();
                            preferences.edit().putString(COMPANY_ID,response.getJSONObject("Result").getString("CompanyId")).apply();
                            preferences.edit().putString(EMPLOYEE_ID,response.getJSONObject("Result").getString("EmployeeId")).apply();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("test", String.format("onError: %s",
                                error.getErrorDetail()));
                    }
                });
    }


    private void launchOnBoardActivity() {
        Intent intent = new Intent(this, OnBoardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_exit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_exit:
                AccessToken accessToken = AccountKit.getCurrentAccessToken();
                if (accessToken != null) {
                    AccountKit.logOut();
                }else{
                    SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
                    preferences.edit().putString(ACCOUNT_TOKEN,null).apply();
                }
                launchOnBoardActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
