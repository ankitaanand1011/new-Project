package com.drinkdrop.drinkdropowner.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.utils.AppController;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.GraphAccepted;
import com.drinkdrop.drinkdropowner.utils.GraphData;
import com.drinkdrop.drinkdropowner.utils.Shared_Prefrence;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineRadarDataSet;

import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Developer on 3/14/18.
 */

public class FragmentHome extends Fragment {
    private LineChart mChart, chart2;

    String TAG = "Home";

    GlobalClass globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    SimpleDateFormat simpleDateFormat;
    RelativeLayout rl_from, rl_to,rl_done_received,rl_done_accepted,rl_acc_from,rl_acc_to;
    DatePickerDialog.OnDateSetListener datepicker1, datepicker2, datepicker3,datepicker4;
    Calendar myCalendar_to = Calendar.getInstance();
    Calendar myCalendar_from = Calendar.getInstance();
    Calendar myCalendar_to_acc = Calendar.getInstance();
    Calendar myCalendar_from_acc = Calendar.getInstance();
    TextView tv_fromdate, tv_todate,tv_fromdate_acc,tv_todate_acc;
    String start_date_received = "",start_month_received = "",start_year_received = "";
    String end_date_received = "",end_month_received = "",end_year_received = "";
    String start_date_accepted = "",start_month_accepted = "",start_year_accepted = "";
    String end_date_accepted = "",end_month_accepted = "",end_year_accepted = "";
    BarChart chart_received,  chartAccepted;
    ArrayList<GraphData> graphDatas= null;
    ArrayList<GraphAccepted> graphAccepted_new=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        globalClass = (GlobalClass) getActivity().getApplicationContext();
        prefrence = new Shared_Prefrence(getActivity());
        prefrence.loadPrefrence();
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        graphDatas=new ArrayList<>();
        graphAccepted_new=new ArrayList<>();


        chart_received = (BarChart) view.findViewById(R.id.chart_received);
        chartAccepted = (BarChart) view.findViewById(R.id.chart_accepted);
        rl_from = view.findViewById(R.id.rl_from);
        rl_to = view.findViewById(R.id.rl_to);
        tv_fromdate = view.findViewById(R.id.tv_fromdate);
        tv_todate = view.findViewById(R.id.tv_todate);
        rl_done_received = view.findViewById(R.id.rl_done_received);
        rl_done_accepted = view.findViewById(R.id.rl_done_accepted);
        tv_fromdate_acc = view.findViewById(R.id.tv_fromdate_acc);
        tv_todate_acc = view.findViewById(R.id.tv_todate_acc);

        rl_acc_from = view.findViewById(R.id.rl_acc_from);
        rl_acc_to = view.findViewById(R.id.rl_acc_to);


        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        rl_to.setOnClickListener(new View.OnClickListener() {
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

        rl_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), datepicker2, myCalendar_from.get(Calendar.YEAR),
                        myCalendar_from.get(Calendar.MONTH), myCalendar_from.get(Calendar.DAY_OF_MONTH));
                String today_date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                Date d;

                try {
                  d = simpleDateFormat.parse(today_date);
                    Log.d("qwerty", "date: " + d);

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                //  dpd.getDatePicker().setMaxDate(d.getTime());
                dpd.show();

            }
        });

        rl_acc_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), datepicker4, myCalendar_to_acc.get(Calendar.YEAR),
                        myCalendar_to_acc.get(Calendar.MONTH), myCalendar_to_acc.get(Calendar.DAY_OF_MONTH));
                String today_date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
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

        rl_acc_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), datepicker3, myCalendar_from_acc.get(Calendar.YEAR),
                        myCalendar_from_acc.get(Calendar.MONTH), myCalendar_from_acc.get(Calendar.DAY_OF_MONTH));
                String today_date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
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

                String date_to_send_to = simpleDateFormat.format(myCalendar_to.getTime());
                String date=String.valueOf(date_to_send_to);
                String[] items1 = date.split("-");
                end_date_received=items1[0];
                end_month_received=items1[1];
                end_year_received=items1[2];


                Log.d(TAG, "end_date_received: "+end_date_received);
                Log.d(TAG, "end_month_received: "+end_month_received);
                Log.d(TAG, "end_year_received: "+end_year_received);



                tv_todate.setText(date_to_send_to);


            }
        };
        datepicker2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar_from.set(Calendar.YEAR, year);
                myCalendar_from.set(Calendar.MONTH, monthOfYear);
                myCalendar_from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
             //   date_to_send_from
                String date_to_send_from = simpleDateFormat.format(myCalendar_from.getTime());

                String date=String.valueOf(date_to_send_from);
                String[] items1 = date.split("-");
                start_date_received=items1[0];
                start_month_received=items1[1];
                start_year_received=items1[2];


                Log.d(TAG, "start_date_received: "+start_date_received);
                Log.d(TAG, "start_month_received: "+start_month_received);
                Log.d(TAG, "start_year_received: "+start_year_received);



                tv_fromdate.setText(date_to_send_from);


            }
        };


        datepicker3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar_from_acc.set(Calendar.YEAR, year);
                myCalendar_from_acc.set(Calendar.MONTH, monthOfYear);
                myCalendar_from_acc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date_to_send_from = simpleDateFormat.format(myCalendar_from_acc.getTime());
                String[] items1 = date_to_send_from.split("-");
                start_date_accepted=items1[0];
                start_month_accepted=items1[1];
                start_year_accepted=items1[2];

                Log.d(TAG, "start_date_accepted: "+start_date_accepted);
                Log.d(TAG, "start_month_accepted: "+start_month_accepted);
                Log.d(TAG, "start_year_accepted: "+start_year_accepted);


                tv_fromdate_acc.setText(date_to_send_from);


            }
        };

        datepicker4 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar_to_acc.set(Calendar.YEAR, year);
                myCalendar_to_acc.set(Calendar.MONTH, monthOfYear);
                myCalendar_to_acc.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String date_to_send_to = simpleDateFormat.format(myCalendar_to_acc.getTime());
                String[] items1 = date_to_send_to.split("-");
                end_date_accepted=items1[0];
                end_month_accepted=items1[1];
                end_year_accepted=items1[2];

                Log.d(TAG, "end_date_received: "+end_date_received);
                Log.d(TAG, "end_month_received: "+end_month_received);
                Log.d(TAG, "end_year_received: "+end_year_received);


                tv_todate_acc.setText(date_to_send_to);


            }
        };
      //  checkLogin(start_date_received,end_date_received,start_month_received,start_year_received);
   //     checkReceived(start_month_accepted,end_month_accepted,start_year_accepted);


        rl_done_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tv_fromdate.getText().toString().isEmpty() && !tv_todate.getText().toString().isEmpty()) {



                    checkReceived(start_date_received,end_date_received,start_month_received,start_year_received);

                } else {
                    Toast.makeText(getActivity(), "Enter dates", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rl_done_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (!tv_fromdate.getText().toString().isEmpty() && !tv_todate.getText().toString().isEmpty()) {

                   checkAccepted(start_month_accepted,end_month_accepted,start_year_accepted);

                } else {
                    Toast.makeText(getActivity(), "Enter dates", Toast.LENGTH_SHORT).show();
                }
            }
        });


        checkReceived(start_date_received,end_date_received,start_month_received,start_year_received);
        checkAccepted(start_month_accepted,end_month_accepted,start_year_accepted);


        return view;
    }

    private void checkReceived(final String start_date_new,final String end_date_new,final  String month_new,final String year_new) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.received_graph_data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //  hideDialog();


                Gson gson = new Gson();

                try {

                    graphDatas.clear();
                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");


                    Log.d("TAG", "status :\t" + status);
                    Log.d("TAG", "message :\t" + message);

                    JsonArray jarray1 = jobj.getAsJsonArray("received_graph_data");
                    // String name[] = new String[jarray1.size()];
                    for (int i = 0; i < jarray1.size(); i++) {

                        JsonObject jobj3 = jarray1.get(i).getAsJsonObject();

                        GraphData graphData = new GraphData();
                        graphData.setReceivedDate(jobj3.get("received_date").toString().replaceAll("\"", ""));
                        graphData.setTotalReceived(jobj3.get("total_received").toString().replaceAll("\"", ""));
                        String received_date = jobj3.get("received_date").toString().replaceAll("\"", "");
                        String total_received = jobj3.get("total_received").toString().replaceAll("\"", "");
                        graphDatas.add(graphData);
                        Log.d("TAG", "received_date :\t" + received_date);
                        Log.d("TAG", "total_received :\t" + total_received);



                        BarData data = new BarData(getXAxisValues(),getDataSet());
                        chart_received.setData(data);

                        chart_received.setDescription("");
                        chart_received.animateXY(2000, 2000);
                        chart_received.invalidate();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "c", Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("start_date", start_date_new);
                params.put("end_date",end_date_new);
                params.put("month", month_new);
                params.put("year", year_new);

                Log.d(TAG, "getParams: "+params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
    private ArrayList<IBarDataSet> getDataSetAccepted() {
        ArrayList<IBarDataSet> dataSetsAccepted = null;
        BarEntry dataAccepted;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        for(int i=0;i<graphAccepted_new.size();i++){
            dataAccepted = new BarEntry(Float.valueOf(graphAccepted_new.get(i).getTotalAccepted()), i); // Jan
            valueSet1.add(dataAccepted);
        }


        BarDataSet barDataSet2 = new BarDataSet(valueSet1, "Accepted Order");
      //  barDataSet2.setColor(Color.rgb(0, 0, 155));
        barDataSet2.setColor(Color.rgb(165, 105, 189));
        //BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        // barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSetsAccepted = new ArrayList<>();
        dataSetsAccepted.add(barDataSet2);
        //dataSets.add(barDataSet2);
        return dataSetsAccepted;
    }

    private ArrayList<String> getXAxisValuesAccepted() {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int j=0;j<graphAccepted_new.size();j++){
            xAxis.add(graphAccepted_new.get(j).getAcceptedDate());

        }

        return xAxis;
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int j=0;j<graphDatas.size();j++){
            xAxis.add(graphDatas.get(j).getReceivedDate());

        }

        return xAxis;
    }

    private void checkAccepted(final String start_month,final String end_month,final  String year) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.accepted_graph_data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //  hideDialog();

                Gson gson = new Gson();
                // Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>(){}.getType());

                // map.forEach((x,y)-> System.out.println("key : " + x + " , value : " + y));
                try {

                    graphAccepted_new.clear();
                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");


                    Log.d("TAG", "status :\t" + status);
                    Log.d("TAG", "message :\t" + message);

                    JsonArray jarray1 = jobj.getAsJsonArray("accepted_graph_data");
                    // String name[] = new String[jarray1.size()];
                    for (int i = 0; i < jarray1.size(); i++) {

                        JsonObject jobj3 = jarray1.get(i).getAsJsonObject();

                        GraphAccepted graphAccepted = new GraphAccepted();
                        graphAccepted.setAcceptedDate(jobj3.get("accepted_date").toString().replaceAll("\"", ""));
                        graphAccepted.setTotalAccepted(jobj3.get("total_accepted").toString().replaceAll("\"", ""));
                        String accepted_date = jobj3.get("accepted_date").toString().replaceAll("\"", "");
                        String total_accepted = jobj3.get("total_accepted").toString().replaceAll("\"", "");
                        graphAccepted_new.add(graphAccepted);
                        Log.d("TAG", "received_date :\t" + accepted_date);
                        Log.d("TAG", "total_received :\t" + total_accepted);

                        BarData data = new BarData(getXAxisValuesAccepted(),getDataSetAccepted());
                        chartAccepted.setData(data);
                        chartAccepted.setDescription("");
                        chartAccepted.animateXY(2000, 2000);
                        chartAccepted.invalidate();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "c", Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("start_month", start_month);
                params.put("end_month", end_month);
                params.put("year",year);

                Log.d(TAG, "getParams: "+params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }


    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;
        BarEntry v1e1;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        for(int i=0;i<graphDatas.size();i++){
            v1e1 = new BarEntry(Float.valueOf(graphDatas.get(i).getTotalReceived()), i); // Jan
            valueSet1.add(v1e1);
        }



        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Received Order");
       // barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
       /* BarDataSet barDataSet2 = new BarDataSet(valueSet1, "");
         barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);*/

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }
}
