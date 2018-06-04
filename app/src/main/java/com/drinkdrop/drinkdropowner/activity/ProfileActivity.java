package com.drinkdrop.drinkdropowner.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.Shared_Prefrence;
import com.drinkdrop.drinkdropowner.utils.WebserviceUrl;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

/**
 * Created by Developer on 3/14/18.
 */

public class ProfileActivity extends AppCompatActivity{
    ImageView imageView2;
    TextView tv_edit,tv_phone,tv_email,tv_name;
    RelativeLayout rl_change_pic,rl_edit_details,rl_update_profile,rl_cancel,rl_customer_detail,
            rl_change_password;
    EditText input_mobile,input_fname,input_lname,input_email;
    Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    String TAG = "my_profile";
    ImageView  toolbar_back,toolbar_logo;
    TextView toolbar_title;
    File p_image;
    GlobalClass globalClass;
    ProgressDialog pd;
    Shared_Prefrence prefrence;
    ImageLoader loader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);


        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Prefrence(ProfileActivity.this);
        prefrence.loadPrefrence();
        pd=new ProgressDialog(ProfileActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ProfileActivity.this.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();





        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            }
            else{
                if(checkForPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 124)){

                }

            }
        }


        toolbar_back =  findViewById(R.id.toolbar_back);

        toolbar_logo =  findViewById(R.id.toolbar_logo);

        toolbar_title = findViewById(R.id.toolbar_title);


        toolbar_logo.setVisibility(View.GONE);

        toolbar_title.setVisibility(View.VISIBLE);

        toolbar_title.setText("My Profile");


        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        imageView2 =findViewById(R.id.imageView2);
        tv_edit = findViewById(R.id.tv_edit);
        ////edit
        rl_change_pic = findViewById(R.id.rl_change_pic);
        rl_edit_details = findViewById(R.id.rl_edit_details);
        rl_update_profile = findViewById(R.id.rl_update_profile);
        rl_cancel = findViewById(R.id.rl_cancel);
        input_mobile = findViewById(R.id.input_mobile);
        input_fname = findViewById(R.id.input_fname);
        input_lname = findViewById(R.id.input_lname);
        input_email =findViewById(R.id.input_email);

        ////not editable
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        rl_customer_detail = findViewById(R.id.rl_customer_detail);
        rl_change_password = findViewById(R.id.rl_change_password);

        rl_change_pic.setVisibility(View.GONE);
        rl_edit_details.setVisibility(View.GONE);
        rl_customer_detail.setVisibility(View.VISIBLE);


        tv_name.setText(globalClass.getFname()+" "+globalClass.getLname());
        tv_phone.setText(globalClass.getPhone_number());
        tv_email.setText(globalClass.getEmail());

        input_fname.setText(globalClass.getFname());
        input_lname.setText(globalClass.getLname());
        input_email.setText(globalClass.getEmail());
        input_mobile.setText(globalClass.getPhone_number());

        if(globalClass.getProfil_pic().isEmpty()){
            imageView2.setImageResource(R.mipmap.no_image);
        }else{
           /* Picasso.with(ProfileActivity.this).load(globalClass.getProfil_pic()).fit().centerCrop()
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.error64)
                    .into(imageView2);*/
            loader.displayImage(globalClass.getProfil_pic(), imageView2 , defaultOptions);
        }



        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_change_pic.setVisibility(View.VISIBLE);
                rl_edit_details.setVisibility(View.VISIBLE);
                rl_customer_detail.setVisibility(View.GONE);

            }
        });
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_change_pic.setVisibility(View.GONE);
                rl_edit_details.setVisibility(View.GONE);
                rl_customer_detail.setVisibility(View.VISIBLE);

            }
        });

        rl_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordScreen.class);
                startActivity(intent);

            }
        });


        rl_change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        rl_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globalClass.isNetworkAvailable()){
                    user_profile_update_url();
                }else{
                    Toasty.info(ProfileActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT, true).show();
                }


            }
        });


    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ProfileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            p_image = new File(getRealPathFromURI(uri));


            Log.d(TAG, "image = "+p_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                imageView2.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {


            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }


            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

/*
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);*/

                Log.d(TAG, "bitmap: "+bitmap);

                imageView2.setImageBitmap(bitmap);

                String path = Environment.getExternalStorageDirectory()+File.separator;
                // + File.separator
                //   + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {

                    p_image = file;

                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // iv_product_image.setImageBitmap(photo);
        }
        if(globalClass.isNetworkAvailable()){
            user_profile_pic_update_url();
        }else{
            Toasty.info(ProfileActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT, true).show();
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion","not granted");


                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if","if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else","else");
                        permissionsNeeded.add(perm);
                    }

                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // go ahead and request permissions
                requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            }
            return false;
        } else {
            // no permission need to be asked so all good...we have them all.
            return true;
        }

    }

    public void user_profile_pic_update_url(){

        // pd.show();

        String url = WebserviceUrl.owner_profile_pic_update;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("owner_id",globalClass.getId());

        try{

            params.put("profile_pic", p_image);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }



        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_pic_update- " + response.toString());
                    try {

                        //JSONObject result = response.getJSONObject("result");

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            // Log.d(TAG, "name: "+name)

                            JSONObject data = response.getJSONObject("data");

                            String profile_pic = data.optString("profile_pic");

                            globalClass.setProfil_pic(profile_pic);
                            prefrence.savePrefrence();




                            Toasty.success(ProfileActivity.this, "Your profile picture has been updated.", Toast.LENGTH_SHORT, true).show();
                            rl_change_pic.setVisibility(View.GONE);
                            rl_edit_details.setVisibility(View.GONE);
                            rl_customer_detail.setVisibility(View.VISIBLE);

                        }else{


                            Toasty.warning(ProfileActivity.this, message, Toast.LENGTH_SHORT, true).show();
                        }

                        // pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                // pd.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }

    public void user_profile_update_url(){

        pd.show();

        String url = WebserviceUrl.owner_profile_update;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("owner_id",globalClass.getId());
        params.put("first_name",input_fname.getText().toString());
        params.put("last_name",input_lname.getText().toString());
        params.put("mobile",input_mobile.getText().toString());


        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        cl.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        cl.post(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "user_profile_update_url- " + response.toString());
                    try {

                        ///   JSONObject result = response.getJSONObject("result");

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            // Log.d(TAG, "name: "+name)

                            JSONObject data = response.getJSONObject("data");


                            String first_name = data.optString("first_name");
                            String last_name = data.optString("last_name");
                            String mobile = data.optString("mobile");


                            globalClass.setFname(first_name);
                            globalClass.setLname(last_name);
                            globalClass.setPhone_number(mobile);
                            prefrence.savePrefrence();



                            tv_name.setText(first_name+" "+last_name);
                            tv_phone.setText(mobile);
                            //  tv_email.setText(globalClass.getEmail());


                            Toasty.success(ProfileActivity.this, "Your profile has been updated.", Toast.LENGTH_SHORT, true).show();
                            rl_change_pic.setVisibility(View.GONE);
                            rl_edit_details.setVisibility(View.GONE);
                            rl_customer_detail.setVisibility(View.VISIBLE);

                        }else{


                            Toasty.warning(ProfileActivity.this, message, Toast.LENGTH_SHORT, true).show();
                        }

                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });


    }


}
