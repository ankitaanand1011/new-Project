package com.drinkdrop.drinkdropowner.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.activity.ScheduledDetails;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 3/9/18.
 */

public class AdapterSchedule extends BaseAdapter {

    Context mContext;

    GlobalClass globalClass;

    LayoutInflater inflater;
    ProgressDialog pd;

    TextView tv_sl_no,tv_created_date,tv_type,tv_day_date,tv_name;
    ImageView img_details;
    String TAG = "capacity";

    ArrayList<HashMap<String,String>> arr_schedule_list;



    public AdapterSchedule(Context c, ArrayList<HashMap<String,String>> arr_schedule_list, ProgressDialog pd) {
        mContext = c;

        this.arr_schedule_list = arr_schedule_list;
        this.pd = pd;

        globalClass = (GlobalClass) mContext.getApplicationContext();

    }

    @Override
    public int getCount() {
        return arr_schedule_list.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_schedule_list.size();

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


        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Log.d("TAG", "getItem: "+position);
        View view1 = inflater.inflate(R.layout.schedule_single_row, parent, false);


        if (position % 2 == 0){

            view1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {

            view1.setBackgroundColor(mContext.getResources().getColor(R.color.lightblue));
        }


        tv_sl_no = view1.findViewById(R.id.tv_sl);
        tv_created_date = view1.findViewById(R.id.tv_created_date);
        tv_type = view1.findViewById(R.id.tv_type);
        tv_day_date = view1.findViewById(R.id.tv_day_date);
        tv_name = view1.findViewById(R.id.tv_name);
        img_details = view1.findViewById(R.id.img_details);


        tv_created_date.setText(arr_schedule_list.get(position).get("schedule_creation_date"));
        tv_type.setText(arr_schedule_list.get(position).get("schedule_type"));
        tv_day_date.setText(arr_schedule_list.get(position).get("schedule_day_date"));
        tv_name.setText(arr_schedule_list.get(position).get("fname")+" "+arr_schedule_list.get(position).get("lname"));
        int po = position+1;
        tv_sl_no.setText(String.valueOf(po));

        img_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+arr_schedule_list.get(position).get("schedule_id"));
                Intent intent = new Intent(mContext,ScheduledDetails.class);
                intent.putExtra("scheduled_id",arr_schedule_list.get(position).get("schedule_id"));
                mContext.startActivity(intent);
            }
        });









        return view1;
    }

}
