package com.drinkdrop.drinkdropowner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Developer on 7/19/17.
 */

public class Shared_Prefrence {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;



    GlobalClass globalclass;
    private boolean pref_logInStatus;
    private String pref_name;
    private String pref_fname;
    private String pref_lname;
    private String pref_id;
    private String pref_email;
    private String pref_phone_number;
    private String pref_user_type;
    private String pref_profile_img;
    private String pref_shipping_address;
    private String pref_cart_no;
    private String pref_ship_address_id;
    private String pref_ship_full_address;

    private String fcm;


    private static final String PREFS_NAME = "preferences";
    private static final String PREFS_NAME2 = "preferences2";

    private static final String PREF_logInStatus = "logInStatus";
    private static final String PREF_name = "name";
    private static final String PREF_fname = "fname";
    private static final String PREF_lname = "lname";
    private static final String PREF_email = "email";
    private static final String PREF_phone_number = "phone_number";
    private static final String PREF_user_type = "user_type";
    private static final String PREF_id = "id";
    private static final String PREF_profile_img = "profile_img";
    private static final String PREF_cart_no = "cart_no";
    private static final String PREF_ship_address_id = "ship_address_id";
    private static final String PREF_ship_full_address = "ship_full_address";




    public Shared_Prefrence(Context context) {
        this.context = context;

        this.globalclass = (GlobalClass) context.getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        this.sharedPreferences2 = context.getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE);
        this.editor2 = sharedPreferences2.edit();



    }

    public void savePrefrence() {
        if (globalclass.getLogin_status()) {

            pref_logInStatus = globalclass.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);

            pref_name = globalclass.getName();
            editor.putString(PREF_name, pref_name);

            pref_fname = globalclass.getFname();
            editor.putString(PREF_fname, pref_fname);

            pref_lname = globalclass.getLname();
            editor.putString(PREF_lname, pref_lname);

            pref_id= globalclass.getId();
            editor.putString(PREF_id,pref_id);

            pref_email= globalclass.getEmail();
            editor.putString(PREF_email,pref_email);

            pref_phone_number= globalclass.getPhone_number();
            editor.putString(PREF_phone_number,pref_phone_number);



            pref_profile_img = globalclass.getProfil_pic();
            editor.putString(PREF_profile_img, pref_profile_img);

            pref_cart_no = globalclass.getCart_no();
            editor.putString(PREF_cart_no, pref_cart_no);

          /*  pref_ship_address_id = globalclass.getShipping_id();
            editor.putString(PREF_ship_address_id, pref_ship_address_id);

            pref_ship_full_address = globalclass.getShipping_full_address();
            editor.putString(PREF_ship_full_address, pref_ship_full_address);*/






            editor.commit();

        }else{
            // dont save anything, if user is logged out
            pref_logInStatus = globalclass.getLogin_status();
            editor.putBoolean(PREF_logInStatus, pref_logInStatus);
            editor.commit();
        }

    }

    public void loadPrefrence() {
        pref_logInStatus = sharedPreferences.getBoolean(PREF_logInStatus, false);
        globalclass.setLogin_status(pref_logInStatus);

        Log.d("TV", globalclass.getLogin_status() + "");
        if (globalclass.getLogin_status()) {

            pref_name = sharedPreferences.getString(PREF_name, "");
            globalclass.setName(pref_name);

            pref_fname = sharedPreferences.getString(PREF_fname, "");
            globalclass.setFname(pref_fname);

            pref_lname = sharedPreferences.getString(PREF_lname, "");
            globalclass.setLname(pref_lname);

            pref_id= sharedPreferences.getString(PREF_id,"");
            globalclass.setId(pref_id);

            pref_phone_number=sharedPreferences.getString(PREF_phone_number,"");
            globalclass.setPhone_number(pref_phone_number);

            pref_email=sharedPreferences.getString(PREF_email,"");
            globalclass.setEmail(pref_email);

            pref_cart_no=sharedPreferences.getString(PREF_cart_no,"");
            globalclass.setCart_no(pref_cart_no);

            /*pref_ship_address_id=sharedPreferences.getString(PREF_ship_address_id,"");
            globalclass.setShipping_id(pref_ship_address_id);

            pref_ship_full_address=sharedPreferences.getString(PREF_ship_full_address,"");
            globalclass.setShipping_full_address(pref_ship_full_address);
*/


            pref_profile_img=sharedPreferences.getString(PREF_profile_img,"");
            globalclass.setProfil_pic(pref_profile_img);




        }
    }

    public void clearPrefrence(){

        editor.clear();
        editor.commit();


    }













}
