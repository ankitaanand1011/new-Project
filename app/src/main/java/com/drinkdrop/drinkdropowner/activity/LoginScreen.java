package com.drinkdrop.drinkdropowner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.Shared_Prefrence;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 3/7/18.
 */

public class LoginScreen extends AppCompatActivity{
    String  TAG = "login";
    GlobalClass globalClass;
    ProgressDialog pd;
    Shared_Prefrence prefrence;
    TextView login_tv, tv_forgot_password;
    EditText email_edt,password_edt;
    String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);


        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Prefrence(LoginScreen.this);
        prefrence.loadPrefrence();
        pd=new ProgressDialog(LoginScreen.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "device_id: "+device_id);
        globalClass.setDeviceid(device_id);

        if(globalClass.isNetworkAvailable()){
            if (globalClass.getLogin_status()){
                Intent intent = new Intent(LoginScreen.this, DrawerActivity.class);
                startActivity(intent);
                finish();
            }
        }else{ Toasty.info(LoginScreen.this, "Please check your internet connection.", Toast.LENGTH_LONG, true).show();}


        login_tv = findViewById(R.id.login_tv);
        email_edt = findViewById(R.id.email_edt);
        password_edt = findViewById(R.id.password_edt);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);


        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalClass.isNetworkAvailable()) {
                    if (!email_edt.getText().toString().isEmpty()) {
                        if (isValidEmail(email_edt.getText().toString())) {
                            if (!password_edt.getText().toString().isEmpty()) {
                                user_login_url();
                            } else {
                                Toasty.warning(LoginScreen.this, "Please enter password.", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Toasty.warning(LoginScreen.this, "Please enter valid email.", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.warning(LoginScreen.this, "Please enter email.", Toast.LENGTH_SHORT, true).show();
                    }
                }else {
                    Toasty.warning(LoginScreen.this, "Please check your internet connection.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this, Forget_Password.class);
                startActivity(intent);
            }
        });


    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void user_login_url(){

        pd.show();

        String url = WebserviceUrl.login;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.put("email",email_edt.getText().toString());
        params.put("password",password_edt.getText().toString());
        params.put("device_type",globalClass.device_type);
        params.put("device_id",globalClass.getDeviceid());
        params.put("fcm_token",WebserviceUrl.fcm_token);




        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "registration_url- " + response.toString());
                    try {

                        // JSONObject result = response.getJSONObject("result");

                        int status = response.getInt("status");
                        String message = response.getString("message");


                        Log.d(TAG, "onSuccess:status>>>> "+status);
                        Log.d(TAG, "onSuccess:message>>>> "+message);



                        if (status == 1) {

                            JSONObject data= response.getJSONObject("data");
                            Log.d(TAG, "onSuccess:data1>>>> "+data);

                            String vendor_id = data.getString("vendor_id");
                            String first_name = data.getString("first_name");
                            String last_name = data.getString("last_name");
                            String mobile = data.getString("mobile");
                            String email = data.getString("email");
                            String profile_pic = data.getString("profile_pic");


                            //   String name = fname+" "+lname;

                            globalClass.setId(vendor_id);
                            globalClass.setFname(first_name);
                            globalClass.setLname(last_name);
                            globalClass.setPhone_number(mobile);
                            globalClass.setEmail(email);
                            globalClass.setLogin_status(true);
                            globalClass.setProfil_pic(profile_pic);

                            prefrence.savePrefrence();



                            Toasty.success(LoginScreen.this, "Login successful.", Toast.LENGTH_SHORT, true).show();
                            Intent intent = new Intent(LoginScreen.this, DrawerActivity.class);
                            startActivity(intent);
                            finish();
                            pd.dismiss();



                        }else{

                            Toasty.error(LoginScreen.this, message, Toast.LENGTH_SHORT, true).show();
                            pd.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(TAG, "onFailure: "+responseString);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
