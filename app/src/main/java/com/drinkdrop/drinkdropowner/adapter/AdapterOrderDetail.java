package com.drinkdrop.drinkdropowner.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 4/13/18.
 */

public class AdapterOrderDetail extends RecyclerView.Adapter<AdapterOrderDetail.ViewHolder> {

    Context context;

    private LayoutInflater mInflater;
    GlobalClass globalClass;
    ProgressDialog pd;
    String TAG = "reminder_adapter";
    ImageView img_no_item;

    ArrayList<HashMap<String,String>> arr_order_details_product;

    public AdapterOrderDetail(Context context,
                                      ArrayList<HashMap<String,String>> arr_order_details_product) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.arr_order_details_product = arr_order_details_product;

        globalClass = (GlobalClass) context.getApplicationContext();
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

    }

    @Override
    public AdapterOrderDetail.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_detail_single_row, parent, false);
        return new AdapterOrderDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterOrderDetail.ViewHolder holder, final int position) {



        holder.tv_product_name.setText(arr_order_details_product.get(position).get("product_name"));
        holder.tv_product_id.setText(arr_order_details_product.get(position).get("productId"));
        holder.tv_product_qty.setText(arr_order_details_product.get(position).get("product_quantity"));
        holder.tv_product_price.setText("JOD "+arr_order_details_product.get(position).get("product_price"));


    }

    @Override
    public int getItemCount() {
        return arr_order_details_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name,tv_product_id,tv_product_qty,tv_product_price;


        public ViewHolder(View itemView) {
            super(itemView);


            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_id = itemView.findViewById(R.id.tv_product_id);
            tv_product_qty = itemView.findViewById(R.id.tv_product_qty);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);

        }
    }

}