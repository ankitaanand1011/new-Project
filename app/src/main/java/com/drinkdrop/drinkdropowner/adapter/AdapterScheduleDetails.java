package com.drinkdrop.drinkdropowner.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.drinkdrop.drinkdropowner.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 4/19/18.
 */

public class AdapterScheduleDetails extends BaseAdapter{
    Context mContext;
    ArrayList<HashMap<String,String>> arr_schedule_detail_list;
    ArrayList<HashMap<String,String>> arr_schedule_product_list;
    LayoutInflater inflater;


    ImageView img_product;
    TextView tv_order_date,tv_order_id;
    LinearLayout ll_product;
    ProgressDialog pd;
    String TAG = "a_received";

    public AdapterScheduleDetails(Context c, ArrayList<HashMap<String,String>> arr_schedule_detail_list,
                                  ArrayList<HashMap<String,String>> arr_schedule_product_list, ProgressDialog pd) {
        mContext = c;
        this.arr_schedule_detail_list = arr_schedule_detail_list;
        this.arr_schedule_product_list = arr_schedule_product_list;
        this.pd = pd;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        /*global_class = (Global_Class)mContext.getApplicationContext();
        global_class.grid_images = grid_images;*/



        // custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/open_sans_light.ttf");
    }

    @Override
    public int getCount() {
        return arr_schedule_detail_list.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_schedule_detail_list.get(position);

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


        View view1 = inflater.inflate(R.layout.scheduled_detail_single_row, null, true);



        tv_order_id = view1.findViewById(R.id.tv_order_id);
        tv_order_date = view1.findViewById(R.id.tv_order_date);
        ll_product = view1.findViewById(R.id.ll_product);



        tv_order_id.setText("#"+arr_schedule_detail_list.get(position).get("order_id"));
        tv_order_date.setText(arr_schedule_detail_list.get(position).get("formatted_date"));

        if (position % 2 == 0){

            view1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {

            view1.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        }


        for (int j = 0; j < arr_schedule_product_list.size(); j++){
            if (arr_schedule_detail_list.get(position).get("order_id").matches
                    (arr_schedule_product_list.get(j).get("order_id"))){


                ll_product.addView(getViewProduct(j));

            }

        }





        return view1;
    }


    private class ViewHolder1 {

        TextView tv_product_name,tv_product_id,tv_product_qty;

        public ViewHolder1(View view) {

            tv_product_name = view.findViewById(R.id.tv_product_name);
            tv_product_id = view.findViewById(R.id.tv_product_id);
            tv_product_qty = view.findViewById(R.id.tv_product_qty);

        }
    }

    public View getViewProduct(int pos) {
        //  inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_item_schedule, null, false);

        ViewHolder1 viewHolder1 = new ViewHolder1(view);
        viewHolder1.tv_product_name.setText(arr_schedule_product_list.get(pos).get("product_name"));
        viewHolder1.tv_product_id.setText(arr_schedule_product_list.get(pos).get("productId"));
        viewHolder1.tv_product_qty.setText(arr_schedule_product_list.get(pos).get("product_quantity"));


        return view;
    }




}
