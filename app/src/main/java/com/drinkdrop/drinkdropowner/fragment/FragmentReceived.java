package com.drinkdrop.drinkdropowner.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.adapter.AdapterReceived;
import com.drinkdrop.drinkdropowner.modal.AddressORM;
import com.drinkdrop.drinkdropowner.modal.OrderDetailORM;
import com.drinkdrop.drinkdropowner.modal.OrderItemORM;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.Shared_Prefrence;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Developer on 3/14/18.
 */


public class FragmentReceived extends Fragment {
    ListView list_received;
    ArrayList<String> order_id;
    AdapterReceived adapterReceived;
    String TAG = "received";
    GlobalClass globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;

    SimpleDateFormat simpleDateFormat;
    RelativeLayout rl_from, rl_to;
    DatePickerDialog.OnDateSetListener datepicker1, datepicker2;
    Calendar myCalendar_to = Calendar.getInstance();
    Calendar myCalendar_from = Calendar.getInstance();
    TextView tv_fromdate, tv_todate;
    String date_to_send_to = "", date_to_send_from = "";
    private boolean loading = true;


    ArrayList<HashMap<String, String>> arr_order;
    ArrayList<HashMap<String, String>> arr_order2;
    ArrayList<HashMap<String, String>> arr_order_products;
    ArrayList<HashMap<String, String>> arr_order_products2;

    OrderDetailORM orderDetails;
    ArrayList<OrderDetailORM> detailORMS =new ArrayList<>();
    int offseT , data_recieveD , limiT = 5;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String refresh_status, called;

    private int previousTotal = 0;
    int currentPage = 0;
    int j =0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_received, container, false);

        globalClass = (GlobalClass) getActivity().getApplicationContext();
        prefrence = new Shared_Prefrence(getActivity());
        prefrence.loadPrefrence();
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

    //    mSwipeRefreshLayout = view.findViewById(R.id.swipyrefreshlayout);
        list_received = view.findViewById(R.id.list_received);
        rl_from = view.findViewById(R.id.rl_from);
        rl_to = view.findViewById(R.id.rl_to);
        tv_fromdate = view.findViewById(R.id.tv_fromdate);
        tv_todate = view.findViewById(R.id.tv_todate);




        arr_order = new ArrayList<>();
        arr_order2 = new ArrayList<>();
        arr_order_products = new ArrayList<>();
        arr_order_products2 = new ArrayList<>();


        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        rl_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), datepicker1, myCalendar_to.get(Calendar.YEAR),
                        myCalendar_to.get(Calendar.MONTH), myCalendar_to.get(Calendar.DAY_OF_MONTH));
                String today_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Date d;

                try {
                    d = simpleDateFormat.parse(today_date);
                    Log.d("qwerty", "onClick: " + d);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                //dpd.getDatePicker().setMaxDate(d.getTime());
                dpd.show();
            }
        });

        rl_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), datepicker2, myCalendar_from.get(Calendar.YEAR),
                        myCalendar_from.get(Calendar.MONTH), myCalendar_from.get(Calendar.DAY_OF_MONTH));
                String today_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Date d;

                try {
                    d = simpleDateFormat.parse(today_date);
                    Log.d("qwerty", "onClick: " + d);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                //  dpd.getDatePicker().setMaxDate(d.getTime());
                dpd.show();

            }
        });


        datepicker1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar_to.set(Calendar.YEAR, year);
                myCalendar_to.set(Calendar.MONTH, monthOfYear);
                myCalendar_to.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date_to_send_from = simpleDateFormat.format(myCalendar_to.getTime());
                tv_fromdate.setText(date_to_send_from);

                if (!tv_fromdate.getText().toString().isEmpty() && !tv_todate.getText().toString().isEmpty()) {
                    owner_receive_orderlist(tv_fromdate.getText().toString(), tv_todate.getText().toString(),currentPage);
                } else {
                    Toast.makeText(getActivity(), "Enter dates", Toast.LENGTH_SHORT).show();
                }


            }
        };



        datepicker2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar_from.set(Calendar.YEAR, year);
                myCalendar_from.set(Calendar.MONTH, monthOfYear);
                myCalendar_from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date_to_send_to = simpleDateFormat.format(myCalendar_from.getTime());
                tv_todate.setText(date_to_send_to);

                if (!tv_fromdate.getText().toString().isEmpty() && !tv_todate.getText().toString().isEmpty()) {
                    owner_receive_orderlist(tv_fromdate.getText().toString(), tv_todate.getText().toString(),currentPage);
                } else {
                    Toast.makeText(getActivity(), "Enter dates", Toast.LENGTH_SHORT).show();
                }


            }
        };
    /*    TextView btnLoadMore = new TextView(getActivity());
        btnLoadMore.setText("Load More");


        list_received.addFooterView(btnLoadMore);
*/


   /*     list_received.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                      //  currentPage++;
                        j++;

                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + limiT)) {
                    owner_receive_orderlist(date_to_send_from, date_to_send_to);
                    loading = true;
                }
            }
        });
*/
    /*    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(globalClass.isNetworkAvailable()){
                    if(refresh_status.equals("released")) {
                        owner_receive_orderlist_refresh(date_to_send_from, date_to_send_to);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    }
            }
        });

*/

      /*  btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                currentPage = currentPage+1;
                owner_receive_orderlist_refresh(date_to_send_from, date_to_send_to,currentPage);
            }
        });
*/
        owner_receive_orderlist(date_to_send_from, date_to_send_to,currentPage);



        return view;

    }


    public void owner_receive_orderlist(final String date_to_send_from,
                                        final String date_to_send_to,int currentPage) {

        pd.show();
/*
        called="1";
        refresh_status = "released";
*/

        String url = WebserviceUrl.owner_receive_orderlist;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

      //  int j =0;
        try {

            params.put("from_date", date_to_send_from);
            params.put("to_date", date_to_send_to);
            params.put("limit", "");
            params.put("offset", "");

            Log.d(TAG, "owner_receive_orderlist- " + url);
            Log.d(TAG, "owner_receive_orderlist- " + params.toString());

            int DEFAULT_TIMEOUT = 30 * 1000;
            client.setMaxRetriesAndTimeout(5, DEFAULT_TIMEOUT);
            client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.d(TAG, "owner_receive_orderlist- " + response.toString());

                    try {


                        // JSONObject result = response.getJSONObject("result");
                        int status = response.optInt("status");
                        String message = response.getString("message");


                        if (status == 1) {

                            arr_order.clear();
                            arr_order_products.clear();

                            JSONArray data = response.getJSONArray("data");
                            Log.d(TAG, "data: " + data.toString());
                            data_recieveD = data.length();

                            for (int i = 0; i < data.length(); i++) {
                                ArrayList<OrderItemORM> orderItems = new ArrayList<>();
                                JSONObject obj = data.getJSONObject(i);

                                String order_id = obj.getString("order_id");
                                String sl = obj.getString("sl");
                                String order_status = obj.getString("order_status");

                                orderDetails = new OrderDetailORM();
                                orderDetails.setOrderID(obj.getString("order_id"));
                                orderDetails.setOrderStatus(obj.getString("order_status"));

                            /*    if (!obj.toString().equals("null")) {
                                    JSONObject  user = obj.getJSONObject("user");
                                    //Make parse here
                                    String fname   = user.optString("fname");
                                    String lname   = user.optString("lname");
                                    String emailid = user.optString("emailid");
                                    String phoneno = user.optString("phoneno");

                                    Log.d("fname", "fname: " + fname);
                                }*/







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


                                orderDetails.setOrderDate(str);
                                orderDetails.setPaymentMode(obj.getString("payment_type"));
                                orderDetails.setGrandTotal(obj.getString("grant_total"));
                                Log.d(TAG, "order_id: " + order_id);




                                JSONArray products = obj.getJSONArray("products");

                                for (int j = 0; j < products.length(); j++) {
                                    JSONObject products_obj = products.getJSONObject(j);
                                    OrderItemORM orderItem = new OrderItemORM();
                                    orderItem.setProductID(products_obj.getString("productId"));
                                    orderItem.setProductName(products_obj.getString("product_name"));
                                    orderItem.setProductPrice(products_obj.getString("product_price"));
                                    orderItem.setProductQuantity(products_obj.getString("product_price"));

                                    String product_id = products_obj.getString("product_id");
                                    String product_quantity = products_obj.getString("product_quantity");
                                    String product_price = products_obj.getString("product_price");
                                    String product_name = products_obj.getString("product_name");
                                    String productId = products_obj.getString("productId");

                                    orderItems.add(orderItem);


                                    HashMap<String, String> map_p = new HashMap<>();
                                    map_p.put("order_id", order_id);
                                    map_p.put("product_id", product_id);
                                    map_p.put("product_quantity", product_quantity);
                                    map_p.put("product_price", product_price);
                                    map_p.put("product_name", product_name);
                                    map_p.put("productId", productId);

                                 /*   Log.d(TAG, "order_id_p: " + order_id);
                                    Log.d(TAG, "product_id: " + product_id);*/


                                    arr_order_products.add(map_p);
                                }

                                orderDetails.setOrderItems(orderItems);


                                JSONObject delivery_address = obj.getJSONObject("delivery_address");

                                AddressORM addressItem = new AddressORM();
                                addressItem.setAddressID(delivery_address.getString("address_id"));
                                addressItem.setFirstName(delivery_address.getString("first_name"));
                                addressItem.setLastName(delivery_address.getString("last_name"));
                                addressItem.setMobile(delivery_address.getString("mobile"));
                                addressItem.setAddress(delivery_address.getString("address"));
                                addressItem.setCountry(delivery_address.getString("country"));
                                addressItem.setState(delivery_address.getString("state"));
                                addressItem.setCity(delivery_address.getString("city"));
                                addressItem.setZip(delivery_address.getString("zip"));
                                addressItem.setLatitude(delivery_address.getDouble("lat"));
                                addressItem.setLongitude(delivery_address.getDouble("lng"));

                                orderDetails.setAddressORM(addressItem);


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


                                String f_address = first_name + " " + last_name + "\n" + address + ", " +
                                        city + ", " + state + ", " + country + ", " + zip + "\n" + "Phn - " + mobile;

                                HashMap<String, String> map = new HashMap<>();
                                map.put("order_id", order_id);
                                map.put("sl", sl);
                                map.put("order_status", order_status);
                                map.put("f_address", f_address);
                                map.put("lat", String.valueOf(lat));
                                map.put("lng", String.valueOf(lng));
                             /*   map.put("fname", fname);
                                map.put("lname", lname);
                                map.put("emailid", emailid);
                                map.put("phoneno", phoneno);
*/

                                Log.d(TAG, "f_address: " + f_address);
                                Log.d(TAG, "lat: " + lat);
                                Log.d(TAG, "lng: " + lng);

                                arr_order.add(map);

                                detailORMS.add(orderDetails);


                            }

                            adapterReceived = new AdapterReceived(getActivity(), arr_order, arr_order_products,
                                    pd, date_to_send_from, date_to_send_to, detailORMS);
                            list_received.setAdapter(adapterReceived);
                            adapterReceived.notifyDataSetChanged();


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
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void owner_receive_orderlist_refresh(final String date_to_send_from,
                                                final String date_to_send_to,int currentPage) {

        pd.show();


        String url = WebserviceUrl.owner_receive_orderlist;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        try {

            params.put("from_date", date_to_send_from);
            params.put("to_date", date_to_send_to);
            params.put("limit", limiT);
            params.put("offset", currentPage);

        Log.d(TAG, "owner_receive_orderlist- " + url);
        Log.d(TAG, "owner_receive_orderlist- " + params.toString());

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5, DEFAULT_TIMEOUT);
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "owner_receive_orderlist- " + response.toString());

                try {


                    // JSONObject result = response.getJSONObject("result");
                    int status = response.optInt("status");
                    String message = response.getString("message");


                    if (status == 1) {

                        arr_order2.clear();
                        arr_order_products2.clear();

                        JSONArray data = response.getJSONArray("data");
                        Log.d(TAG, "data: " + data.toString());

                        for (int i = 0; i < data.length(); i++) {
                            ArrayList<OrderItemORM> orderItems=new ArrayList<>();
                            JSONObject obj = data.getJSONObject(i);
                            orderDetails=new OrderDetailORM();
                            String order_id = obj.getString("order_id");
                            orderDetails.setOrderID(obj.getString("order_id"));
                            String sl = obj.getString("sl");
                            String order_status = obj.getString("order_status");
                            orderDetails.setOrderStatus(obj.getString("order_status"));

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


                            orderDetails.setOrderDate(str);
                            orderDetails.setPaymentMode(obj.getString("payment_type"));
                            orderDetails.setGrandTotal(obj.getString("grant_total"));
                            Log.d(TAG, "order_id: " + order_id);

                            JSONArray products = obj.getJSONArray("products");


                            for (int j = 0; j < products.length(); j++) {
                                JSONObject products_obj = products.getJSONObject(j);
                                OrderItemORM orderItem=new OrderItemORM();
                                orderItem.setProductID(products_obj.getString("productId"));
                                orderItem.setProductName(products_obj.getString("product_name"));
                                orderItem.setProductPrice(products_obj.getString("product_price"));
                                orderItem.setProductQuantity(products_obj.getString("product_price"));

                                String product_id = products_obj.getString("product_id");
                                String product_quantity = products_obj.getString("product_quantity");
                                String product_price = products_obj.getString("product_price");
                                String product_name = products_obj.getString("product_name");
                                String productId = products_obj.getString("productId");

                                orderItems.add(orderItem);


                                HashMap<String, String> map_p = new HashMap<>();
                                map_p.put("order_id", order_id);
                                map_p.put("product_id", product_id);
                                map_p.put("product_quantity", product_quantity);
                                map_p.put("product_price", product_price);
                                map_p.put("product_name", product_name);
                                map_p.put("productId", productId);

                                Log.d(TAG, "order_id_p: " + order_id);
                                Log.d(TAG, "product_id: " + product_id);


                                arr_order_products2.add(map_p);
                            }

                            orderDetails.setOrderItems(orderItems);


                            JSONObject delivery_address = obj.getJSONObject("delivery_address");

                            AddressORM addressItem= new AddressORM();
                            addressItem.setAddressID(delivery_address.getString("address_id"));
                            addressItem.setFirstName(delivery_address.getString("first_name"));
                            addressItem.setLastName(delivery_address.getString("last_name"));
                            addressItem.setMobile(delivery_address.getString("mobile"));
                            addressItem.setAddress(delivery_address.getString("address"));
                            addressItem.setCountry(delivery_address.getString("country"));
                            addressItem.setState(delivery_address.getString("state"));
                            addressItem.setCity(delivery_address.getString("city"));
                            addressItem.setZip(delivery_address.getString("zip"));
                            addressItem.setLatitude(delivery_address.getDouble("lat"));
                            addressItem.setLongitude(delivery_address.getDouble("lng"));

                            orderDetails.setAddressORM(addressItem);


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


                            String f_address = first_name + " " + last_name + "\n" + address + ", " +
                                    city + ", " + state + ", " + country + ", " + zip + "\n" + "Phn - " + mobile;

                            HashMap<String, String> map = new HashMap<>();
                            map.put("order_id", order_id);
                            map.put("sl", sl);
                            map.put("order_status", order_status);
                            map.put("f_address", f_address);
                            map.put("lat", String.valueOf(lat));
                            map.put("lng", String.valueOf(lng));

                            Log.d(TAG, "f_address: " + f_address);
                            Log.d(TAG, "lat: " + lat);
                            Log.d(TAG, "lng: " + lng);

                            arr_order2.add(map);

                            detailORMS.add(orderDetails);


                        }
                      //  arr_order_products2.addAll(arr_order_products);
                        arr_order.addAll(arr_order2);
                       // arr_order2.addAll(arr_order);
                        adapterReceived = new AdapterReceived(getActivity(), arr_order, arr_order_products2,
                                pd, date_to_send_from, date_to_send_to,detailORMS);
                        list_received.setAdapter(adapterReceived);



                        adapterReceived.notifyDataSetChanged();

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
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
