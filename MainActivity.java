package com.rogerthat.rlvltd.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.apiresponsemodels.GetMenusApiResponse;
import com.rogerthat.rlvltd.com.apiresponsemodels.LogoutAPIResposne;
import com.rogerthat.rlvltd.com.fragment.BuzzDashboardFragment;
import com.rogerthat.rlvltd.com.fragment.BuzzOrdersListFragment;
import com.rogerthat.rlvltd.com.fragment.BuzzSearchCarloTab;
import com.rogerthat.rlvltd.com.fragment.CarloBarberaSearchFragment;
import com.rogerthat.rlvltd.com.fragment.CustomerListFragment;
import com.rogerthat.rlvltd.com.fragment.FabricCollectionFragment;
import com.rogerthat.rlvltd.com.fragment.HomePageFragment;
import com.rogerthat.rlvltd.com.fragment.HotDealsSearchFragment;
import com.rogerthat.rlvltd.com.fragment.InventorySearchFragment;
import com.rogerthat.rlvltd.com.fragment.LatestHotdealFragment;
import com.rogerthat.rlvltd.com.fragment.OrderStatusSearchFragment;
import com.rogerthat.rlvltd.com.fragment.RLVOutListFragment;
import com.rogerthat.rlvltd.com.fragment.SalesReportSearchListFragment;
import com.rogerthat.rlvltd.com.fragment.SamplingStatusSearchFragment;
import com.rogerthat.rlvltd.com.util.ApiClient;
import com.rogerthat.rlvltd.com.util.ApiInterface;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.ConnectionDetector;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    private boolean onceclicked = false;

    public Toolbar toolbar;
    TextView actionbarTitle;
    ImageView logoutToolbarIcon;
    ImageView buzz_icon;
    private PreferenceHelper sharedpreference;
    private Context context;
    private String userId = "";
    private String userName = "";

    @BindView(R.id.txt_user_name)
    TextView txtUserName;

    @BindView(R.id.txt_user_typee)
    TextView txt_user_typee;

    @BindView(R.id.img_user_image)
    CircleImageView imgUserImage;

    @BindView(R.id.drawer_raw_container)
    LinearLayout drawer_raw_container;

    @BindView(R.id.profileLinear)
    LinearLayout profileLinear;


    @BindView(R.id.relativeLogout)
    RelativeLayout relativeLogout;


    private boolean customerLogin = false;
    private boolean salesLogin = false;
    List<GetMenusApiResponse.ResultBean> menusListarray;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        final FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        sharedpreference = new PreferenceHelper(MainActivity.this, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();

        redirectToBuzz(fragmentTransaction2);


        if (new ConnectionDetector(context).isConnectingToInternet() == true) {

            getMenusList();

        } else {
            CommonUses.showToast(context, AppConfig.INTERNETFAILED_MESSAGE);

        }


        relativeLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogProcess();
            }
        });

        logoutToolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogProcess();
            }
        });

        profileLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerLogin) {

                    try {

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", "Edit Profile");
                        in.putExtra("UserType", "3");
                        in.putExtra("UserId", userId);
                        startActivity(in);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (salesLogin) {

                    try {

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", "Edit Profile");
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);
                        startActivity(in);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        try {
            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {

                    String fromDrawer = getIntent().getExtras().getString(AppConfig.FROM_DRAWER);

                    if (fromDrawer != null) {
                        if (fromDrawer.equalsIgnoreCase(AppConfig.FROM_DRAWER)) {
                            replaceFragment(fragmentTransaction2, new HomePageFragment(), "Dashboard");
                        } else {
                            //sendtoNotificationActivity();
                            redirectToBuzz(fragmentTransaction2);
                        }

                    } else {
                        //sendtoNotificationActivity();
                        redirectToBuzz(fragmentTransaction2);
                    }


                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void redirectToBuzz(FragmentTransaction  fragmentTransaction2) {
        String isBuzzPermissionHave = sharedpreference.LoadStringPref(AppConfig.IS_BUZZ, AppConfig.IS_BUZZ);
        String isCarloPermissionHave = sharedpreference.LoadStringPref(AppConfig.IS_CARLOBARBERA, AppConfig.IS_CARLOBARBERA);
        if (isBuzzPermissionHave.equalsIgnoreCase("True")) {
            replaceFragment(fragmentTransaction2, new BuzzDashboardFragment(), "Welcome, " + userName);
        } else {
            replaceFragment(fragmentTransaction2, new HomePageFragment(), "Dashboard");

        }
    }

    private void sendtoNotificationActivity() {
        String fromNotification = getIntent().getExtras().getString(AppConfig.FROM_NOTIFICATIONS);
        if (fromNotification != null) {
            if (fromNotification.equalsIgnoreCase(AppConfig.FROM_NOTIFICATIONS)) {
                try {

                    Intent in = new Intent(context, WebviewActivity.class);
                    in.putExtra("Module", "Notifications");
                    in.putExtra("UserType", "");
                    in.putExtra("UserId", userId);
                    startActivity(in);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        actionbarTitle = (TextView) toolbar.findViewById(R.id.actionbarTitle);
        logoutToolbarIcon = (ImageView) toolbar.findViewById(R.id.logouticon);
        buzz_icon = (ImageView) toolbar.findViewById(R.id.buzz_icon);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        context = MainActivity.this;
        ButterKnife.bind(MainActivity.this);


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
            sharedpreference.initPref();
            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);

            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);

            JSONObject returnJobj = new JSONObject(userTokenobjStirng);
            userId = returnJobj.optString("UserId");
            userName = returnJobj.optString("Name");

            checkforCustomerorSalesLogin();

            setValuesToDrawerHeader(returnJobj);


            String isBuzzPermission = sharedpreference.LoadStringPref(AppConfig.IS_BUZZ, AppConfig.IS_BUZZ);
            if (!isBuzzPermission.equalsIgnoreCase("True")) {
                buzz_icon.setVisibility(View.GONE);
            }


            buzz_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //BUZZ / BUZZ DASHBOARD - SALES LOGIN
                    Intent in = getIntent();
                    in.putExtra(AppConfig.FROM_DRAWER, "");
                    startActivity(in);
                    finish();
                    //CommonUses.showToast(context, "Buzz Click");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesToDrawerHeader(JSONObject returnJobj) {
        txtUserName.setText(userName);


        if (returnJobj.optString("UserType").equalsIgnoreCase("3")) {
            //For Customer login print company name
            txt_user_typee.setText(returnJobj.optString("CompanyName"));
        } else {
            //For Sales login print UsertypeName
            txt_user_typee.setText(returnJobj.optString("UserTypeName"));
        }

        String st_pic_url = returnJobj.optString("UserPhoto");


        // please check take_photo url then after set take_photo profile
        if (!st_pic_url.equalsIgnoreCase("")) {
            Glide.with(getApplicationContext()).load(st_pic_url)
                    .asBitmap()
                    .thumbnail(
                            Glide.with(this)
                                    .load(st_pic_url)
                                    .asBitmap()
                                    .centerCrop()
                                    .thumbnail(0.5f))
                    .placeholder(R.drawable.icon256)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgUserImage);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
//            replaceFragment(fragmentTransaction, new HomePageFragment(), "Home");
            Intent in = getIntent();
            in.putExtra(AppConfig.FROM_DRAWER, AppConfig.FROM_DRAWER);
            startActivity(in);
            finish();

        } else if (id == R.id.nav_inventory) {


        } else if (id == R.id.nav_listcustomers) {


        } else if (id == R.id.nav_feedback) {


        } else if (id == R.id.nav_terms) {
//            replaceFragment(fragmentTransaction, new SurveyFragment(), getResources().getString(R.string.survey));
//            startActivity(new Intent(MainActivity.this,SurveyFormActivity.class));


        }/* else if (id == R.id.nav_logout) {
            logoutDialogProcess();

        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(FragmentTransaction fragmentTransaction, Fragment replaceFragmentName, String drawerTitle) {
        fragmentTransaction.replace(R.id.container, replaceFragmentName).addToBackStack("");
        fragmentTransaction.commit();
//        getSupportActionBar().setTitle(drawerTitle);
        actionbarTitle.setText(drawerTitle);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            SalesReportSearchListFragment.clearData();
        } else {
            SalesReportSearchListFragment.clearData();
//            super.onBackPressed();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            int num = fm.getBackStackEntryCount();
            if (num <= 1) {
                if (onceclicked) {
                    SalesReportSearchListFragment.clearData();
                } else {
                    if (doubleBackToExitPressedOnce) {
                        SalesReportSearchListFragment.clearData();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }

                    this.doubleBackToExitPressedOnce = true;
//                    CommonUses.showSnackbar(toolbar, "Please click back again to exit");
                    CommonUses.showMessageSnackbarForErrorDuration(context, toolbar, "Please click back again to exit", 2500);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                }
            } else {

               /* if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");


                    fm.popBackStack();

                } else {
                    Log.i("MainActivity", "nothing on backstack, calling super");
                    super.onBackPressed();
                }*/





               /* String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                //Log.d("tag-->>",tag);
                if (tag!=null)
                {
                    if (tag.equalsIgnoreCase("SSSSalesListFragmentWithoutDb"))
                    {
                        SalesReportSearchFragment samplingStatusSearch = new SalesReportSearchFragment();
                        //samplingStatusSearch.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, samplingStatusSearch).addToBackStack(null).commit();
                    }
                    else {
                        super.onBackPressed();
                    }
                }

                else {*/
                super.onBackPressed();
                // }

                //finishFromChild(getParent());
                //finish();

            }
        }
    }

    private void logoutDialogProcess() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (new ConnectionDetector(context).isConnectingToInternet() == true) {

                            getLogoutCredentials();

                        } else {

                            CommonUses.showToastwithDuration(2500, AppConfig.INTERNETFAILED_MESSAGE, context);
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
//            .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonUses.checkForExpiryDate(MainActivity.this);
        startService(new Intent("com.rogerthat.rlvltd.com.firebase.MyFirebaseMessagingService").setPackage("com.rogerthat.rlvltd.com.firebase"));
    }

    private void checkforCustomerorSalesLogin() {
        String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
        JSONObject returnJobj = null;
        try {
            returnJobj = new JSONObject(userTokenobjStirng);

            String usertype = returnJobj.optString("UserType");
            if (usertype.equalsIgnoreCase("3")) {
                customerLogin = true;
            } else {
                salesLogin = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Network call  for Getting Login Credentials
    private void getMenusList() {

        JSONObject returnJobj = null;

        String deviceToken = "", userType = "", deviceNo = "";

        sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();

        String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
        String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);

        try {
            returnJobj = new JSONObject(userTokenobjStirng);
            userId = returnJobj.optString("UserId");
            deviceToken = returnJobj.optString("DeviceToken");
            userType = returnJobj.optString("UserType");
            deviceNo = deviceidStr;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showProgress("Loading...");


        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("UserId", userId);
        loginParams.put("DeviceToken", deviceToken);
        loginParams.put("UserType", userType);
        loginParams.put("device_no", deviceNo);

        //Login API Call using Interface
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<GetMenusApiResponse> call = apiService.getmenusapiCall(loginParams);
        call.enqueue(new Callback<GetMenusApiResponse>() {
            @Override
            public void onResponse(Call<GetMenusApiResponse> call, Response<GetMenusApiResponse> response) {

                int statusCode = response.code();
                if (statusCode == 200) {
                    //GEt full response in FULLRESPONSE object
                    final GetMenusApiResponse fullresponse = response.body();

                    menusListarray = fullresponse.getResult();

                    if (menusListarray.size() > 0) {
                        /*
                        //removal of carlo barbera from dashboard menu
                        for (int i = 0; i < menusListarray.size(); i++) {
                            GetMenusApiResponse.ResultBean singleObj = menusListarray.get(i);

                            //Carlo barbera for Customer User : Usertype == 3
                            if (singleObj.getId().equalsIgnoreCase("67") ||
                                    //Carlo barbera for Sales User : Usertype == 2
                                    singleObj.getId().equalsIgnoreCase("66")) {
                                menusListarray.remove(i);
                            }

                        }*/

                        for (final GetMenusApiResponse.ResultBean singleObj : menusListarray) {
                            if (!singleObj.getMenuType().equalsIgnoreCase("2")) {

                                View child = getLayoutInflater().inflate(R.layout.drawer_item, null);
                                TextView moduleNameTv = (TextView) child.findViewById(R.id.moduleName);
                                ImageView moduleIcon = (ImageView) child.findViewById(R.id.homeIcon);
                                child.setTag(singleObj.getId());
                                moduleNameTv.setText(singleObj.getTitle().replaceAll("Module", ""));

                                try {
                                    Glide
                                            .with(context)
                                            .load(singleObj.getIcon())
                                            .placeholder(R.drawable.star)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .priority(Priority.HIGH)
                                            .dontAnimate() // This solved the problem
                                            .into(moduleIcon);
                                } catch (Exception e) {
                                    e.getMessage();
                                    e.printStackTrace();

                                }


                                drawer_raw_container.addView(child);
                                child.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        draweOnClickEvent(singleObj);
                                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                        drawer.closeDrawer(GravityCompat.START);
                                    }
                                });

                            }
                        }
                    }
                } else {
//                    CommonUses.printErrorMessage(response, uselessEdt);
//                    CommonUses.showToast(response, uselessEdt);

                }
                hideProgress();

            }

            @Override
            public void onFailure(Call<GetMenusApiResponse> call, Throwable t) {
                // Log error here since request failed
                hideProgress();
                if (BuildConfig.DEBUG)
                    CommonUses.showToast(context, t.getMessage());


            }
        });
    }

    private void draweOnClickEvent(GetMenusApiResponse.ResultBean singleObj) {
        if (salesLogin) {
            try {

                Gson gson = new Gson();
                String loginResponseString = gson.toJson(singleObj);
                JSONObject jOBJ = new JSONObject(loginResponseString);

                Log.i("OBJECT_TAG", String.valueOf(jOBJ));
                //HOME / Dashboard
                if (jOBJ.optString("Id").equalsIgnoreCase("9")) {

                    Intent in = getIntent();
                    in.putExtra(AppConfig.FROM_DRAWER, AppConfig.FROM_DRAWER);
                    startActivity(in);
                    finish();

                }

                //FEEDBACK
                if (jOBJ.optString("Id").equalsIgnoreCase("42")) {

                    Intent in = new Intent(context, WebviewActivity.class);
                    in.putExtra("Module", "FeedBack");
                    in.putExtra("UserType", "2");
                    in.putExtra("UserId", userId);
                    startActivity(in);

                }

                //TERMS & CONDITIONS
                if (jOBJ.optString("Id").equalsIgnoreCase("41")) {

                    Intent in = new Intent(context, WebviewActivity.class);
                    in.putExtra("Module", "Terms & Conditions");
                    in.putExtra("UserType", "2");
                    in.putExtra("UserId", userId);
                    startActivity(in);

                }

                //INVENTORY STATUS
                if (jOBJ.optString("Id").equalsIgnoreCase("27")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    InventorySearchFragment inventorySearchFragment = new InventorySearchFragment();
                    inventorySearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventorySearchFragment).addToBackStack("").commit();



                }
                //HOT DEALS

                if(jOBJ.optString("Id").equalsIgnoreCase("68")){
                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    /*HotDealsSearchFragment hotDealsSearchFragment = new HotDealsSearchFragment();
                    hotDealsSearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container,hotDealsSearchFragment).addToBackStack("").commit();*/

                    LatestHotdealFragment inventoryListFragment = new LatestHotdealFragment();
                    inventoryListFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventoryListFragment).addToBackStack(null).commit();
                  //  hidekeyboardmethod();
                }

                //CARLO BARBERA
                if (jOBJ.optString("Id").equalsIgnoreCase("66")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    CarloBarberaSearchFragment inventorySearchFragment = new CarloBarberaSearchFragment();
                    inventorySearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventorySearchFragment).addToBackStack("").commit();


                }


                //RLV OutList
                if (jOBJ.optString("Id").equalsIgnoreCase("60")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    RLVOutListFragment inventorySearchFragment = new RLVOutListFragment();
                    inventorySearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventorySearchFragment).addToBackStack("").commit();


                }

              /*  //Fabric Collections
                if (jOBJ.optString("Id").equalsIgnoreCase("62")) {


                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", jOBJ.toString());
                    FabricCollectionFragment rlvOutListFragment = new FabricCollectionFragment();
                    rlvOutListFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, rlvOutListFragment).addToBackStack("").commit();
                }*/
                //BUZZ / BUZZ DASHBOARD - SALES LOGIN
                if (jOBJ.optString("Id").equalsIgnoreCase("62")) {

                    Intent in = getIntent();
                    in.putExtra(AppConfig.FROM_DRAWER, "");
                    startActivity(in);
                    finish();

                }


                //ORDER STATUS
                if (jOBJ.optString("Id").equalsIgnoreCase("20")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    OrderStatusSearchFragment orderstatusSeach = new OrderStatusSearchFragment();
                    orderstatusSeach.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, orderstatusSeach).addToBackStack("").commit();


                }


                //NOTIFICATIONS >>
                if (jOBJ.optString("Id").equalsIgnoreCase("49")) {

                    try {

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", "Notifications");
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);
                        startActivity(in);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //CUSTOMERS >>
                if (jOBJ.optString("Id").equalsIgnoreCase("16")) {

                    try {
                        sharedpreference.removestring(AppConfig.CUSTOMER_LIST_KEYWORD);
                        //TODO
                        //if for the first time it comes to this screen
                        //then will replace fragment to inventory list page
                        Bundle bundle = new Bundle();
                        bundle.putString("CategoryObj", jOBJ.toString());
                        CustomerListFragment customerListFragment = new CustomerListFragment();
                        customerListFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, customerListFragment).addToBackStack("").commit();
                        hidekeyboardmethod();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //ORDERS >>
                if (jOBJ.optString("Id").equalsIgnoreCase("18")) {

                    try {
                        /*sharedpreference.removestring(AppConfig.ORDER_LIST_KEYWORD);
                        Bundle bundle = new Bundle();
                        bundle.putString("CategoryObj", jOBJ.toString());
                        bundle.putString("FromPage", "Home");
                        OrdersListFragment orderslistFragment = new OrdersListFragment();
                        orderslistFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, orderslistFragment).addToBackStack("").commit();
                        hidekeyboardmethod();*/

                        sharedpreference.removestring(AppConfig.ORDER_LIST_KEYWORD);
                        Bundle bundle = new Bundle();
                        bundle.putString("CategoryObj", jOBJ.toString());
                        bundle.putString("FromPage", "Home");
                        BuzzOrdersListFragment orderslistFragment = new BuzzOrdersListFragment();
                        orderslistFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, orderslistFragment).addToBackStack("").commit();
                        hidekeyboardmethod();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //SAMPLING STATUS
                if (jOBJ.optString("Id").equalsIgnoreCase("53")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    SamplingStatusSearchFragment samplingStatusSearch = new SamplingStatusSearchFragment();
                    samplingStatusSearch.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, samplingStatusSearch).addToBackStack("").commit();

                }


                //COMPLAINT SUMMARY >>
                if (jOBJ.optString("Id").equalsIgnoreCase("31")) {

                    try {


                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.COMPAINT_SUMMARY_MODULE);
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //GOODS RETURNS MODULE >>
                if (jOBJ.optString("Id").equalsIgnoreCase("32")) {

                    try {
                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.GOODS_RETURNS_MODULE);
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //Sampling Dispatch MODULE >>
                if (jOBJ.optString("Id").equalsIgnoreCase("30")) {

                    try {
                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.SAMPLING_DISPATCH_MODULE);
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //News Corner >> Sales User
                if (jOBJ.optString("Id").equalsIgnoreCase("40")) {

                    try {
                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.NEWS_CORNER);
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);


                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //customer login
        if (customerLogin) {
            try {

                Gson gson = new Gson();
                String loginResponseString = gson.toJson(singleObj);
                JSONObject jOBJ = new JSONObject(loginResponseString);

                Log.i("OBJECT_TAG", String.valueOf(jOBJ));
                //HOME / Dashboard >> ID ==1
                if (jOBJ.optString("Id").equalsIgnoreCase("1")) {

                    Intent in = getIntent();
                    in.putExtra(AppConfig.FROM_DRAWER, AppConfig.FROM_DRAWER);
                    startActivity(in);
                    finish();


                }

                //FEEDBACK >> ID ==45
                if (jOBJ.optString("Id").equalsIgnoreCase("45")) {

                    Intent in = new Intent(context, WebviewActivity.class);
                    in.putExtra("Module", "FeedBack");
                    in.putExtra("UserType", "3");
                    in.putExtra("UserId", userId);
                    startActivity(in);

                }



                //TERMS & CONDITIONS  >> ID ==44
                if (jOBJ.optString("Id").equalsIgnoreCase("44")) {

                    Intent in = new Intent(context, WebviewActivity.class);
                    in.putExtra("Module", "Terms & Conditions");
                    in.putExtra("UserType", "3");
                    in.putExtra("UserId", userId);
                    startActivity(in);

                }

                //Inventory status >> ID ==7
                if (jOBJ.optString("Id").equalsIgnoreCase("7")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", jOBJ.toString());

                    InventorySearchFragment inventorySearchFragment = new InventorySearchFragment();
                    inventorySearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventorySearchFragment).addToBackStack("").commit();

                }

                //RLV OutList
                if (jOBJ.optString("Id").equalsIgnoreCase("61")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    RLVOutListFragment inventorySearchFragment = new RLVOutListFragment();
                    inventorySearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventorySearchFragment).addToBackStack("").commit();


                }

                //Buzz == Fabric Collections
                if (jOBJ.optString("Id").equalsIgnoreCase("63")) {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", jOBJ.toString());
                    FabricCollectionFragment rlvOutListFragment = new FabricCollectionFragment();
                    rlvOutListFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, rlvOutListFragment).addToBackStack("").commit();*/

                    //BUZZ / BUZZ DASHBOARD - SALES LOGIN
                    Intent in = getIntent();
                    in.putExtra(AppConfig.FROM_DRAWER, "");
                    startActivity(in);
                    finish();
                }

                //CarloBarbera = ID = 67
                if (jOBJ.optString("Id").equalsIgnoreCase("67")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", jOBJ.toString());

                    CarloBarberaSearchFragment inventorySearchFragment = new CarloBarberaSearchFragment();
                    inventorySearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventorySearchFragment).addToBackStack("").commit();

                }

                //HOT DEALS

                if(jOBJ.optString("Id").equalsIgnoreCase("69")){
                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    /*HotDealsSearchFragment hotDealsSearchFragment = new HotDealsSearchFragment();
                    hotDealsSearchFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container,hotDealsSearchFragment).addToBackStack("").commit();*/

                    LatestHotdealFragment inventoryListFragment = new LatestHotdealFragment();
                    inventoryListFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, inventoryListFragment).addToBackStack(null).commit();
                }

                //Edit Profile >> ID ==46
                if (jOBJ.optString("Id").equalsIgnoreCase("46")) {

                    try {

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", "SalesService");
                        in.putExtra("UserType", "3");
                        in.putExtra("UserId", userId);
                        startActivity(in);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //NOTIFICATIONS >>
                if (jOBJ.optString("Id").equalsIgnoreCase("50")) {

                    try {

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", "Notifications");
                        in.putExtra("UserType", "");
                        in.putExtra("UserId", userId);
                        startActivity(in);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //ORDER STATUS
                if (jOBJ.optString("Id").equalsIgnoreCase("6")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryObj", "");

                    OrderStatusSearchFragment orderstatusSeach = new OrderStatusSearchFragment();
                    orderstatusSeach.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, orderstatusSeach).addToBackStack("").commit();


                }

                //ORDERS >>
                if (jOBJ.optString("Id").equalsIgnoreCase("2")) {

                    try {
                        /*sharedpreference.removestring(AppConfig.ORDER_LIST_KEYWORD);
                        Bundle bundle = new Bundle();
                        bundle.putString("CategoryObj", jOBJ.toString());
                        bundle.putString("FromPage", "Home");
                        OrdersListFragment orderslistFragment = new OrdersListFragment();
                        orderslistFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, orderslistFragment).addToBackStack("").commit();
                        hidekeyboardmethod();*/

                        sharedpreference.removestring(AppConfig.ORDER_LIST_KEYWORD);
                        Bundle bundle = new Bundle();
                        bundle.putString("CategoryObj", jOBJ.toString());
                        bundle.putString("FromPage", "Home");
                        BuzzOrdersListFragment orderslistFragment = new BuzzOrdersListFragment();
                        orderslistFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, orderslistFragment).addToBackStack("").commit();
                        hidekeyboardmethod();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //News Corner >> Customer User
                if (jOBJ.optString("Id").equalsIgnoreCase("43")) {

                    try {
                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.NEWS_CORNER);
                        in.putExtra("UserType", "2");
                        in.putExtra("UserId", userId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgress(String setMessage) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(setMessage);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


    private void hidekeyboardmethod() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //Network call  for Getting Login Credentials
    private void getLogoutCredentials() {


        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //IMEI Number
        final String deviceid = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);/*mTelephonyManager.getDeviceId();*/
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        String loginId = "", UserType = "", DeviceToken = "";
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
        try {
            JSONObject returnJobj = new JSONObject(userTokenobjStirng);
            userId = returnJobj.optString("UserId");
            loginId = returnJobj.optString("LoginId");
            UserType = returnJobj.optString("UserType");
            DeviceToken = returnJobj.optString("DeviceToken");
            String customerDetailsObj = getIntent().getExtras().getString("SingleObj");


        } catch (Exception e) {
            e.printStackTrace();
        }


        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();

       /* HashMap<String, String> counterParams = new HashMap<String, String>();
        counterParams.put("RegionId", selectedRegionId);
        counterParams.put("ProjectId", selectedProjectId);*/
        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("UserType", UserType);
        loginParams.put("DeviceToken", DeviceToken);
        loginParams.put("UserId", userId);
        loginParams.put("LoginId", loginId);
        loginParams.put("device_no", deviceid);


        //Login API Call using Interface
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LogoutAPIResposne> call = apiService.logoutAPICall(loginParams);
        call.enqueue(new Callback<LogoutAPIResposne>() {
            @Override
            public void onResponse(Call<LogoutAPIResposne> call, Response<LogoutAPIResposne> response) {

                int statusCode = response.code();
                if (statusCode == 200) {

                    //GEt full response in FULLRESPONSE object
                    LogoutAPIResposne fullresponse = response.body();


                    sharedpreference.initPref();
                    sharedpreference.SaveStringPref(AppConfig.FROM_LOGOUT, "True");
                    sharedpreference.ApplyPref();

                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    finish();

                } else {

                    CommonUses.showToastwithDuration(2500, "Something worng! Please try again.", context);


                }
                progress.cancel();

            }

            @Override
            public void onFailure(Call<LogoutAPIResposne> call, Throwable t) {
                // Log error here since request failed
                progress.cancel();
                if (BuildConfig.DEBUG)
                    CommonUses.showToast(context, t.getMessage());
                CommonUses.showToastwithDuration(2500, "Something worng! Please try again.", context);


            }
        });
    }


}