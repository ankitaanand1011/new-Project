package com.drinkdrop.drinkdropowner.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.adapter.AdapterScheduleDetails;
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

public class ScheduledDetails extends AppCompatActivity {

    ImageView toolbar_back,toolbar_logo;
    TextView toolbar_title;
    GlobalClass globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    ListView list_scheduled_details;
    String TAG = "schedule_detail";
    ArrayList<HashMap<String,String>> arr_schedule_detail_list;
    ArrayList<HashMap<String,String>> arr_schedule_product_list;
    AdapterScheduleDetails adapterScheduleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_details);

        globalClass = (GlobalClass) getApplicationContext();
        prefrence = new Shared_Prefrence(ScheduledDetails.this);
        prefrence.loadPrefrence();
        pd = new ProgressDialog(ScheduledDetails.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        arr_schedule_detail_list = new ArrayList<>();
        arr_schedule_product_list = new ArrayList<>();


        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_logo = findViewById(R.id.toolbar_logo);

        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_back.setVisibility(View.VISIBLE);

        toolbar_logo.setVisibility(View.GONE);

        toolbar_title.setText("Schedule Details");
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list_scheduled_details = findViewById(R.id.list_scheduled_details);
        user_schedule_view();

    }

    public void user_schedule_view(){

        pd.show();

        String url = WebserviceUrl.user_schedule_view;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("schedule_id", getIntent().getStringExtra("scheduled_id"));


        Log.d(TAG , "user_schedule_view- " + url);
        Log.d(TAG , "user_schedule_view- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "user_schedule_view- " + response.toString());

                if (response != null) {
                    try {


                        // JSONObject result = response.getJSONObject("result");
                        int status = response.optInt("status");
                        String message = response.getString("message");

                        if(status == 1) {
                            arr_schedule_detail_list.clear();

                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                String order_id = obj.getString("order_id");
                                String order_date = obj.getString("order_date");
                                String inputPattern = "yyyy-MM-dd HH:mm:ss";
                                String outputPattern = "dd-MM-yyyy";
                                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                Date date = null;
                                String str = null;

                                try {
                                    date = inputFormat.parse(order_date);
                                    str = outputFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }



                                JSONArray products = obj.getJSONArray("products");

                                for (int j = 0; j < products.length(); j++) {
                                    JSONObject products_obj = products.getJSONObject(j);


                                    String product_id = products_obj.getString("product_id");
                                    String product_quantity = products_obj.getString("product_quantity");
                                    String product_price = products_obj.getString("product_price");
                                    String product_name = products_obj.getString("product_name");
                                    String productId = products_obj.getString("productId");


                                    HashMap<String, String> map_p = new HashMap<>();
                                    map_p.put("order_id", order_id);
                                    map_p.put("product_id", product_id);
                                    map_p.put("product_quantity", product_quantity);
                                    map_p.put("product_price", product_price);
                                    map_p.put("product_name", product_name);
                                    map_p.put("productId", productId);

                                    Log.d(TAG, "order_id_p: "+order_id);
                                    Log.d(TAG, "product_id: "+product_id);



                                    arr_schedule_product_list.add(map_p);
                                }


                                HashMap<String, String> map = new HashMap<>();
                                map.put("order_id", order_id);
                                map.put("formatted_date",str);

                                arr_schedule_detail_list.add(map);


                            }
                            adapterScheduleDetails = new AdapterScheduleDetails(ScheduledDetails.this,arr_schedule_detail_list,
                                    arr_schedule_product_list,pd);
                            list_scheduled_details.setAdapter(adapterScheduleDetails);
                            pd.dismiss();

                        }else{

                            Toasty.error(ScheduledDetails.this,message, Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "user_schedule_view- " + res);

                pd.dismiss();


            }


        });


    }
}
