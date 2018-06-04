package com.drinkdrop.drinkdropowner.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
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
 * Created by Developer on 3/14/18.
 */

public class AdapterAccepted extends BaseAdapter {

    Context mContext;
    ArrayList<HashMap<String,String>> arr_accepted;
    ArrayList<HashMap<String,String>> arr_accepted_products;
    LayoutInflater inflater;


    ImageView img_product;
    TextView tv_product_name,tv_delivery_date,tv_order_id,tv_sl,tv_status;
    LinearLayout ll_product;
    ProgressDialog pd;
    String TAG = "a_received";

    public AdapterAccepted(Context c, ArrayList<HashMap<String,String>> arr_accepted,
                           ArrayList<HashMap<String,String>> arr_accepted_products,  ProgressDialog pd) {
        mContext = c;
        this.arr_accepted = arr_accepted;
        this.arr_accepted_products = arr_accepted_products;
        this.pd = pd;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        /*global_class = (Global_Class)mContext.getApplicationContext();
        global_class.grid_images = grid_images;*/



        // custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/open_sans_light.ttf");
    }

    @Override
    public int getCount() {
        return arr_accepted.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_accepted.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
     //   View view= null;


        View view1 = inflater.inflate(R.layout.accepted_single_row, null, true);

        tv_sl = view1.findViewById(R.id.tv_sl);
        tv_product_name = view1.findViewById(R.id.tv_product_name);
        tv_order_id = view1.findViewById(R.id.tv_order_id);
        tv_delivery_date = view1.findViewById(R.id.tv_delivery_date);
        tv_status = view1.findViewById(R.id.tv_status);
        ll_product = view1.findViewById(R.id.ll_product);

        tv_status.setText("Accepted");



        tv_sl.setText(arr_accepted.get(position).get("sl"));
        tv_order_id.setText(arr_accepted.get(position).get("order_id"));
        tv_delivery_date.setText(arr_accepted.get(position).get("formatted_date"));

        if (position % 2 == 0){

            view1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {

            view1.setBackgroundColor(mContext.getResources().getColor(R.color.lightblue));
        }


        for (int j = 0; j < arr_accepted_products.size(); j++){
            if (arr_accepted.get(position).get("order_id").matches
                    (arr_accepted_products.get(j).get("order_id"))){


                ll_product.addView(getViewProduct(j));

            }

        }


        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String order_id = arr_accepted.get(position).get("order_id");
                owner_order_delivered(order_id);
            }
        });



        return view1;
    }


    private class ViewHolder1 {

        TextView tv_product_name,tv_product_id;

        public ViewHolder1(View view) {

            tv_product_name = view.findViewById(R.id.tv_product_name);
            tv_product_id = view.findViewById(R.id.tv_product_id);

        }
    }

    public View getViewProduct(int pos) {
        //  inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_item_accepted, null, false);

        ViewHolder1 viewHolder1 = new ViewHolder1(view);
        viewHolder1.tv_product_name.setText(arr_accepted_products.get(pos).get("product_name"));
        viewHolder1.tv_product_id.setText(arr_accepted_products.get(pos).get("productId"));


        return view;
    }

    public void owner_order_delivered(String order_id){

        pd.show();

        String url = WebserviceUrl.owner_order_delivered;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("order_id", order_id);
        params.put("type", "accepted");
        params.put("from_date", "");
        params.put("to_date", "");


        Log.d(TAG , "owner_order_delivered- " + url);
        Log.d(TAG , "owner_order_delivered- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "owner_order_accepted- " + response.toString());

                if (response != null) {
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
                            notifyDataSetChanged();
                            //   img_accept.setImageResource(R.mipmap.deliver);
                            Toasty.success(mContext, message, Toast.LENGTH_SHORT, true).show();
                            pd.dismiss();

                        }else{

                            Toasty.error(mContext, message, Toast.LENGTH_SHORT, true).show();
                            pd.dismiss();
                        }

                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "owner_order_accepted- " + res);

                pd.dismiss();

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(mContext).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

}
