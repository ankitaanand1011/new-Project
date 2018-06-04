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

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.adapter.AdapterAccepted;
import com.drinkdrop.drinkdropowner.adapter.AdapterReceived;
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

/**
 * Created by Developer on 3/14/18.
 */

public class FragmentAccepted extends Fragment {
    String TAG = "accepted";
    GlobalClass globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    ListView list_accepted;
    ArrayList<HashMap<String,String>> arr_accepted;
    ArrayList<HashMap<String,String>> arr_accepted_products;
    AdapterAccepted adapterAccepted;
    SimpleDateFormat simpleDateFormat;
    Date d;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accepted, container, false);

        globalClass = (GlobalClass)getActivity().getApplicationContext();
        prefrence = new Shared_Prefrence(getActivity());
        prefrence.loadPrefrence();
        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        list_accepted = view.findViewById(R.id.list_accepted);

        arr_accepted = new ArrayList<>();
        arr_accepted_products = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


        owner_order_accepted();

        return view;

    }

    public void owner_order_accepted(){

        pd.show();

        String url = WebserviceUrl.owner_accepted_order_list;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("limit", "");
        params.put("offset", "");

        Log.d(TAG , "owner_order_accepted- " + url);
        Log.d(TAG , "owner_order_accepted- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "owner_order_accepted- " + response.toString());

                try {


                    // JSONObject result = response.getJSONObject("result");
                    int status = response.optInt("status");
                    String message = response.getString("message");


                    if(status == 1) {

                        arr_accepted.clear();
                        arr_accepted_products.clear();

                        JSONArray data = response.getJSONArray("data");
                        Log.d(TAG, "data: "+data.toString());
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);

                            String order_id = obj.getString("order_id");
                            String sl = obj.getString("sl");
                            String order_accepted_date = obj.getString("order_accepted_date");


                            String inputPattern = "yyyy-MM-dd HH:mm:ss";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = null;
                            String str = null;

                            try {
                                date = inputFormat.parse(order_accepted_date);
                                str = outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            Log.d(TAG, "order_id: "+order_id);

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



                                arr_accepted_products.add(map_p);
                            }





                            HashMap<String, String> map = new HashMap<>();
                            map.put("order_id", order_id);
                            map.put("formatted_date",str);
                            map.put("sl", sl);



                            arr_accepted.add(map);



                        }
                        adapterAccepted = new AdapterAccepted(getActivity(), arr_accepted,arr_accepted_products,pd);
                        list_accepted.setAdapter(adapterAccepted);
                        adapterAccepted.notifyDataSetChanged();


                        adapterAccepted.notifyDataSetChanged();

                        pd.dismiss();

                    }

                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "owner_receive_orderlist- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(getActivity()).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

}
