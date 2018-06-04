package com.drinkdrop.drinkdropowner.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.adapter.AdapterAccepted;
import com.drinkdrop.drinkdropowner.adapter.AdapterSchedule;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.Shared_Prefrence;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 4/19/18.
 */

public class FragmentScheduled extends Fragment {

    String TAG = "schedule";
    ArrayList<HashMap<String,String>> arr_schedule_list;
    GlobalClass globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    ListView list_scheduled;

    AdapterSchedule adapterSchedule;
    SimpleDateFormat simpleDateFormat;
    Date d;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        globalClass = (GlobalClass)getActivity().getApplicationContext();
        prefrence = new Shared_Prefrence(getActivity());
        prefrence.loadPrefrence();
        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        arr_schedule_list= new ArrayList<>();

        list_scheduled = view.findViewById(R.id.list_scheduled);
        user_schedule_list();



        return view;

    }

    public void user_schedule_list(){

        pd.show();

        String url = WebserviceUrl.owner_schedule_list;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

       // params.put("user_id", globalClass.getId());


        Log.d(TAG , "user_schedule_list- " + url);
        Log.d(TAG , "user_schedule_list- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "user_schedule_list- " + response.toString());

                if (response != null) {
                    try {


                        // JSONObject result = response.getJSONObject("result");
                        int status = response.optInt("status");
                        String message = response.getString("message");

                        if(status == 1) {
                            arr_schedule_list.clear();

                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                String schedule_id = obj.getString("schedule_id");
                                String schedule_creation_date = obj.getString("schedule_creation_date");
                                String schedule_type = obj.getString("schedule_type");
                                String schedule_day_date = obj.getString("schedule_day_date");
                                String fname = obj.getString("fname");
                                String lname = obj.getString("lname");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("schedule_id", schedule_id);
                                map.put("schedule_creation_date", schedule_creation_date);
                                map.put("schedule_type", schedule_type);
                                map.put("schedule_day_date", schedule_day_date);
                                map.put("fname", fname);
                                map.put("lname", lname);

                                arr_schedule_list.add(map);


                            }
                            adapterSchedule = new AdapterSchedule(getActivity(),arr_schedule_list,pd);
                            list_scheduled.setAdapter(adapterSchedule);
                            pd.dismiss();

                        }else{

                            Toasty.error(getActivity(),message, Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "user_schedule_list- " + res);

                pd.dismiss();


            }


        });


    }
}
