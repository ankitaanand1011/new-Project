package com.drinkdrop.drinkdropowner.adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.activity.LoginScreen;
import com.drinkdrop.drinkdropowner.modal.OrderDetailORM;
import com.drinkdrop.drinkdropowner.modal.OrderItemORM;
import com.drinkdrop.drinkdropowner.utils.Constants;
import com.drinkdrop.drinkdropowner.utils.DocumentHelper;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 3/14/18.
 */

public class AdapterReceived  extends BaseAdapter  {

    Activity mContext;

    //Global_Class global_class;

    LayoutInflater inflater;
    AlertDialog alertDialog1;
    private LayoutInflater mInflater;
    ArrayList<HashMap<String,String>> arr_order;
    ArrayList<HashMap<String,String>> arr_order_products;
    ArrayList<HashMap<String,String>> arr_order_details = new ArrayList<>();
    ArrayList<HashMap<String,String>> arr_order_details_product = new ArrayList<>();
    ListView list_received ;
    int pos= 0;

    ImageView img_share_location,img_accept,img_deliver;
    TextView tv_order_id,tv_sl;
    LinearLayout ll_product;
    GlobalClass global;
    ProgressDialog pd;
    String TAG = "a_received";
    String date_to_send_from;
    String date_to_send_to;
    RecyclerView rv_reminders;
    OrderDetailORM _orderDetails;
    ArrayList<OrderDetailORM> orderDetailORMS;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    public AdapterReceived(Activity c, ArrayList<HashMap<String,String>> arr_order,
                           ArrayList<HashMap<String,String>> arr_order_products, ProgressDialog pd,
                           String date_to_send_from, String date_to_send_to,
                           ArrayList<OrderDetailORM> orderDetailsORM) {

        mContext = c;
        this.arr_order = arr_order;
        this.arr_order_products = arr_order_products;
        this.pd = pd;
        this.date_to_send_from = date_to_send_from;
        this.date_to_send_to = date_to_send_to;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        global = (GlobalClass)mContext.getApplicationContext();
        orderDetailORMS=orderDetailsORM;


    }

    @Override
    public int getCount() {
        return arr_order.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_order.get(i);
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View view1=null;

     //   inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       if (view1 == null) {



        //   View view1 = inflater.inflate(R.layout.received_single_row, null, true);
            view1 = inflater.inflate(R.layout.received_single_row, null, true);

           ll_product = view1.findViewById(R.id.ll_product);
           tv_sl = view1.findViewById(R.id.tv_sl);
           tv_order_id = view1.findViewById(R.id.tv_order_id);

           img_accept = view1.findViewById(R.id.img_accept);
           img_share_location = view1.findViewById(R.id.img_share_location);
           img_deliver = view1.findViewById(R.id.img_deliver);

           if (arr_order.get(position).get("order_status").equals("Ordered")) {

               img_accept.setVisibility(View.VISIBLE);
               img_deliver.setVisibility(View.GONE);
           } else {
               // img_accept.setImageResource(R.mipmap.deliver);
               img_accept.setVisibility(View.GONE);
               img_deliver.setVisibility(View.VISIBLE);
           }

           int i = position+1;
           tv_sl.setText(String.valueOf(i));
           // holder.tv_sl.setText(String.valueOf(position+1));



           tv_order_id.setText(arr_order.get(position).get("order_id"));

           if (position % 2 == 0) {

               view1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
           } else {

               view1.setBackgroundColor(mContext.getResources().getColor(R.color.lightblue));
           }


           // Log.d("SSS","getView: "+ arr_order.get(position).get("order_id"));

           for (int j = 0; j < arr_order_products.size(); j++) {
               if (arr_order.get(position).get("order_id").matches
                       (arr_order_products.get(j).get("order_id"))) {


                   ll_product.addView(getViewProduct(j));

               }

           }

           img_accept.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String order_id = arr_order.get(position).get("order_id");
                   owner_order_accepted(order_id, date_to_send_from, date_to_send_to);
               }
           });

           img_deliver.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String order_id = arr_order.get(position).get("order_id");
                   owner_order_delivered(order_id, date_to_send_from, date_to_send_to);
               }
           });

           tv_order_id.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   /*String order_id = arr_order.get(position).get("order_id");
                   String fname    = arr_order.get(position).get("fname");
                   String lname    = arr_order.get(position).get("lname");
                   String emailid  = arr_order.get(position).get("emailid");
                   String phoneno  = arr_order.get(position).get("phoneno");*/

                   String order_id = arr_order.get(position).get("order_id");
                   String fname ="";
                   String lname ="";
                   String emailid ="";
                   String phoneno ="";
                   owner_order_details(order_id,fname,lname,emailid,phoneno);
               }
           });


           img_share_location.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   String order_id = arr_order.get(position).get("order_id");

                   double latitude = Double.valueOf(arr_order.get(position).get("lat"));
                   double longitude = Double.valueOf(arr_order.get(position).get("lng"));
                   Log.d(TAG, "latitude: " + latitude);
                   Log.d(TAG, "longitude: " + longitude);


                   openDialog(order_id, latitude, longitude, position);
               }
           });


       }else{
           Log.d("TAG", "getView: else");
       }

       return view1;
    }


    private class ViewHolder1 {

        TextView tv_product_name,tv_product_id,tv_qty;

        public ViewHolder1(View view) {

            tv_product_name = view.findViewById(R.id.tv_product_name);
            tv_product_id = view.findViewById(R.id.tv_product_id);
            tv_qty = view.findViewById(R.id.tv_qty);
        }
    }

    public View getViewProduct(int pos) {
      //  inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_item, null, false);
        ViewHolder1 viewHolder1 = new ViewHolder1(view);


        viewHolder1.tv_product_name.setText(arr_order_products.get(pos).get("product_name"));
        viewHolder1.tv_product_id.setText(arr_order_products.get(pos).get("productId"));
        viewHolder1.tv_qty.setText(arr_order_products.get(pos).get("product_quantity"));


        return view;
    }



    public void openDialog(final String order_id, final double latitude, final double longitude, final int pos) {
      /*  final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_paricipants);*/
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        View dialogView = inflater.inflate(R.layout.dialog_assign_delivery_boy, null);
        builderSingle.setView(dialogView);

        alertDialog1 = builderSingle.create();
        alertDialog1.show();

        TextView tv_share_delivery = dialogView.findViewById(R.id.tv_share_delivery);
        TextView tv_order_details = dialogView.findViewById(R.id.tv_order_details);

        tv_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = checkPermission();
                if (result) {

                    String fileName = DocumentHelper.generatePDFNotice(mContext, orderDetailORMS.get(pos));

                    Uri fileURI = Uri.fromFile(new File(Constants.APP_TEMP_FILE_PATH + fileName));



                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    //  mContext.startActivity(share);
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
                    whatsappIntent.setType("application/pdf");
                    whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    whatsappIntent.setPackage("com.whatsapp");

                    try {
                        mContext.startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {

                        Toasty.error(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT, true).show();
                    }

                }
            }
        });

        tv_share_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, location);


                try {
                    mContext.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toasty.error(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });


    }

    public  void  orderdetail_dialog(String order_id, final String fname_ , final String lname_ ,
                                     final String email_, final String phone_ ){

        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.order_detail_dialog, null);
        dialogBuilder.setView(dialogView);


        final android.support.v7.app.AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(300, 900);
        dialog.show();
       // arr_reminder_list = new ArrayList<>();


        final TextView tv_1 = dialogView.findViewById(R.id.tv_1);
        final TextView tv_name  = dialogView.findViewById(R.id.tv_name);
        final TextView tv_email = dialogView.findViewById(R.id.tv_email);
        final TextView tv_phone = dialogView.findViewById(R.id.tv_phone);

        rv_reminders = dialogView.findViewById(R.id.rv_reminders);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rv_reminders.setLayoutManager(mLayoutManager);

       /* tv_1.setText("#"+order_id);
        if(!lname_.isEmpty()) {
            tv_name.setText("Name:" + fname_ + " " + lname_);
        }else{
            tv_name.setText("Name:" + fname_);
        }
        if (!email_.isEmpty()) {
            tv_email.setText("Email id: " + email_);
        }else{
            tv_email.setText("Email id: Not specified");
        }

        if (!phone_.isEmpty()) {
            tv_phone.setText("Mobile: "+phone_);

        }else{
            tv_phone.setText("Mobile : Not specified");
        }


*/

    }


    public void owner_order_accepted(String order_id,String date_to_send_from, String date_to_send_to){

        pd.show();

        String url = WebserviceUrl.owner_order_accepted;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("order_id", order_id);
        params.put("from_date", date_to_send_from);
        params.put("to_date", date_to_send_to);

        Log.d(TAG , "owner_order_accepted- " + url);
        Log.d(TAG , "owner_order_accepted- " + params.toString());

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

                            arr_order.clear();
                            arr_order_products.clear();

                            JSONArray data = response.getJSONArray("data");
                            Log.d(TAG, "data: "+data.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                String order_id = obj.getString("order_id");
                                String sl = obj.getString("sl");
                                String order_status = obj.getString("order_status");
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



                                    arr_order_products.add(map_p);
                                }


                                JSONObject delivery_address = obj.getJSONObject("delivery_address");

                                String address_id = delivery_address.getString("address_id");
                                String first_name = delivery_address.getString("first_name");
                                String last_name = delivery_address.getString("last_name");
                                String mobile = delivery_address.getString("mobile");
                                String address = delivery_address.getString("address");
                                String country = delivery_address.getString("country");
                                String state = delivery_address.getString("state");
                                String city = delivery_address.getString("city");
                                String zip = delivery_address.getString("zip");
                                double lat = delivery_address.getDouble("lat");
                                double lng = delivery_address.getDouble("lng");


                                String f_address = first_name+" "+last_name+"\n"+address+", "+
                                        city+", "+state+", "+country+", "+zip+"\n"+"Phn - "+mobile;

                                HashMap<String, String> map = new HashMap<>();
                                map.put("order_id", order_id);
                                map.put("sl", sl);
                                map.put("order_status", order_status);
                                map.put("f_address", f_address);

                                Log.d(TAG, "f_address: "+f_address);

                                arr_order.add(map);



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

    public void owner_order_delivered(String order_id,String date_to_send_from, String date_to_send_to){

        pd.show();

        String url = WebserviceUrl.owner_order_delivered;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("order_id", order_id);
        params.put("type", "receive");
        params.put("from_date", date_to_send_from);
        params.put("to_date", date_to_send_to);


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

                            arr_order.clear();
                            arr_order_products.clear();

                            JSONArray data = response.getJSONArray("data");
                            Log.d(TAG, "data: "+data.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                String order_id = obj.getString("order_id");
                                String sl = obj.getString("sl");
                                String order_status = obj.getString("order_status");
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



                                    arr_order_products.add(map_p);
                                }


                                JSONObject delivery_address = obj.getJSONObject("delivery_address");

                                String address_id = delivery_address.getString("address_id");
                                String first_name = delivery_address.getString("first_name");
                                String last_name = delivery_address.getString("last_name");
                                String mobile = delivery_address.getString("mobile");
                                String address = delivery_address.getString("address");
                                String country = delivery_address.getString("country");
                                String state = delivery_address.getString("state");
                                String city = delivery_address.getString("city");
                                String zip = delivery_address.getString("zip");
                                double lat = delivery_address.getDouble("lat");
                                double lng = delivery_address.getDouble("lng");


                                String f_address = first_name+" "+last_name+"\n"+address+", "+
                                        city+", "+state+", "+country+", "+zip+"\n"+"Phn - "+mobile;

                                HashMap<String, String> map = new HashMap<>();
                                map.put("order_id", order_id);
                                map.put("sl", sl);
                                map.put("order_status", order_status);
                                map.put("f_address", f_address);

                                Log.d(TAG, "f_address: "+f_address);

                                arr_order.add(map);



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


    public void owner_order_details(final String order_id, final String fname_ ,
                                    final String lname_ ,
                                    final String email_, final String phone_ ){

        pd.show();

        String url = WebserviceUrl.owner_order_details;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("order_id", order_id);



        Log.d(TAG , "owner_order_details- " + url);
        Log.d(TAG , "owner_order_details- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "owner_order_details- " + response.toString());

                if (response != null) {
                    try {


                        // JSONObject result = response.getJSONObject("result");
                        int status = response.optInt("status");
                        String message = response.getString("message");


                        if(status == 1) {


                            arr_order_details_product.clear();

                            JSONArray data = response.getJSONArray("data");
                            Log.d(TAG, "data: "+data.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                String order_id = obj.getString("order_id");

                                Log.d(TAG, "order_id: "+order_id);

                                JSONArray products = obj.getJSONArray("products");
                                Log.d(TAG, "products: "+products.length());
                                for (int j = 0; j < products.length(); j++) {
                                    JSONObject products_obj = products.getJSONObject(j);


                                    String product_id = products_obj.getString("product_id");
                                    String product_quantity = products_obj.getString("product_quantity");
                                    String product_price = products_obj.getString("product_price");
                                    String product_name = products_obj.getString("product_name");
                                    String productId = products_obj.getString("productId");


                                    HashMap<String, String> map_p = new HashMap<>();
                                    map_p.put("product_id", product_id);
                                    map_p.put("product_quantity", product_quantity);
                                    map_p.put("product_price", product_price);
                                    map_p.put("product_name", product_name);
                                    map_p.put("productId", productId);

                                    Log.d(TAG, "order_id_p: "+order_id);
                                    Log.d(TAG, "product_id: "+product_id);



                                    arr_order_details_product.add(map_p);
                                    Log.d(TAG, "arr_order_details_product: "+arr_order_details_product.size());
                                }


                            }
                            AdapterOrderDetail orderDetail = new AdapterOrderDetail(mContext, arr_order_details_product);
                            orderdetail_dialog(order_id,fname_,lname_,email_,phone_);
                            rv_reminders.setAdapter(orderDetail);

                          //  notifyDataSetChanged();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Storage permission is necessary to share order details!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}