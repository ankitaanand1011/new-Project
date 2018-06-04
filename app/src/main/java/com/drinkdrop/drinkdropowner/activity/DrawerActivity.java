package com.drinkdrop.drinkdropowner.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import com.drinkdrop.drinkdropowner.fragment.FragmentAccepted;
import com.drinkdrop.drinkdropowner.fragment.FragmentHome;
import com.drinkdrop.drinkdropowner.fragment.FragmentReceived;
import com.drinkdrop.drinkdropowner.R;
import com.drinkdrop.drinkdropowner.fragment.FragmentScheduled;
import com.drinkdrop.drinkdropowner.utils.GlobalClass;
import com.drinkdrop.drinkdropowner.utils.Shared_Prefrence;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


/**
 * Created by ANDRIOD on 11/14/2016.
 */
public class DrawerActivity extends AppCompatActivity {


    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    Toolbar toolbar;
    ImageView toolbar_back,toolbar_profile,toolbar_logo;

    Fragment fragment = null;
    TextView tooltext;
    String video;
    ImageView toolbar_image;


    ProgressDialog progressBar;
    MenuItem target;
    Boolean install_first_time = false;
    String s,mTime;
    boolean doubleBackToExitPressedOnce = false;
    AlertDialog alertDialog1;
    String m_name;
    String video_type;
    ArrayList<String> participants = new ArrayList<>();
    String VIDEO_ID;
    int frag ;
    TextView header_text;
    String k_stat;
    String fcm_reg_token="not found";
    String fcm_reg_token_temp;
    String device_id;
    GlobalClass globalClass;
    Shared_Prefrence prefrence;
    ProgressDialog pd;
    ImageLoader loader;
    DisplayImageOptions defaultOptions;
    ImageView imageView2;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.drawer_activity);

        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Prefrence(DrawerActivity.this);
        prefrence.loadPrefrence();
        pd=new ProgressDialog(DrawerActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DrawerActivity.this.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();




        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Set a Toolbar to replace the ActionBar.

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar_back =  toolbar.findViewById(R.id.toolbar_back);
        toolbar_profile =  toolbar.findViewById(R.id.toolbar_profile);
        toolbar_logo =  toolbar.findViewById(R.id.toolbar_logo);
        toolbar_back.setVisibility(View.GONE);

        toolbar_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawerActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });



        // Find our drawer view
        mDrawer =  findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nvView);
        navigationView.setItemIconTintList(null);
        View head=navigationView.getHeaderView(0);
        header_text = head.findViewById(R.id.header_text);
        imageView2 = head.findViewById(R.id.imageView2);
        header_text.setText("Hi, "+globalClass.getFname());


        if(globalClass.getProfil_pic().isEmpty()){
            imageView2.setImageResource(R.mipmap.no_image);
        }else{

            loader.displayImage(globalClass.getProfil_pic(), imageView2 , defaultOptions);
        }


        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //the method we have create in activity
        }

        fragment = new FragmentHome();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


            // Setup drawer view
            setupDrawerContent(navigationView);

            drawerToggle = setupDrawerToggle();
            // Tie DrawerLayout events to the ActionBarToggle




        DrawerLayout drawer =  findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {



                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    //getActionBar().setTitle(R.string.app_name);
                    InputMethodManager inputMethodManager = (InputMethodManager) DrawerActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(DrawerActivity.this.getCurrentFocus().getWindowToken(), 0);
                    invalidateOptionsMenu();

                }


                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // getActionBar().setTitle("Home");
                    InputMethodManager inputMethodManager = (InputMethodManager) DrawerActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(DrawerActivity.this.getCurrentFocus().getWindowToken(), 0);
                    invalidateOptionsMenu();

                 /*   if(globalClass.getProfil_pic().isEmpty()){
                        imageView2.setImageResource(R.mipmap.no_image);
                    }else{

                        loader.displayImage(globalClass.getProfil_pic(), imageView2 , defaultOptions);
                    }
                    header_text.setText("Hi, "+globalClass.getFname());*/

                }
            };
        drawer.addDrawerListener(toggle);
        toggle.syncState();




    }







    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("frag", mTime);
      //  Log.d("savedInstanceState", "from_frag: "+outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        s =savedInstanceState.getString("frag");
       // Log.d("savedInstanceState", "onRestoreInstanceState: "+s);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        // Class fragmentClass = null;


        switch (menuItem.getItemId()) {
            case R.id.nav_home:

                frag = 1;


                fragment = new FragmentHome();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


                break;
            case R.id.nav_order_received:

                frag = 2;


                fragment = new FragmentReceived();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.flContent, fragment).commit();
                break;
            case R.id.nav_order_accepted:

                frag = 3;


                fragment = new FragmentAccepted();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.flContent, fragment).commit();

                break;

            case R.id.nav_order_schedule:

                frag = 4;


                fragment = new FragmentScheduled();
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                fragmentManager3.beginTransaction().replace(R.id.flContent, fragment).commit();

                break;


            case R.id.nav_Logout:


                prefrence.clearPrefrence();
                globalClass.setLogin_status(false);
                Toasty.success(DrawerActivity.this,"You are now logged out.", Toast.LENGTH_SHORT,true).show();
                Intent intent = new Intent(DrawerActivity.this, LoginScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();



                break;


            default:



        }

        if (fragment != null) {

            try {
                // fragment = (Fragment) fragmentCla0ss.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title

            // Close the navigation drawer
            mDrawer.closeDrawers();

        }

    }
    @Override
    protected void onResume() {

      //  show_chat();
        super.onResume();
      //  startService(new  Intent(this, Service_class.class));

        //Log.d("kite", "onResume: ");



       // Log.d("ORRRR", "Call Onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();

    }




   /* public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public void checkLocationPermission(){


        // Asking user if explanation is needed
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            //Prompt the user once explanation has been shown
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {



                    }

                } else {

                    checkLocationPermission();

                    // Permission denied, Disable the functionality that depends on this permission.
                    // Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




















   // boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toasty.info(DrawerActivity.this,"Tap back button in order to exit",Toast.LENGTH_SHORT,true).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
