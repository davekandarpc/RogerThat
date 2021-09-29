package com.rogerthat.rlvltd.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rogerthat.rlvltd.com.apiresponsemodels.TermConditionAPiResponse;
import com.rogerthat.rlvltd.com.util.ApiClient;
import com.rogerthat.rlvltd.com.util.ApiInterface;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/*
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;*/


/**
 * Created by Ratan on 7/9/2015.
 */
public class WebviewActivity extends AppCompatActivity implements View.OnLongClickListener {


    private WebView webciew;
    private ProgressDialog pd;

    private Context context;

    public Toolbar toolbar;
    TextView actionbarTitle;
    ImageView logoutToolbarIcon;
    private PreferenceHelper sharedpreference;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @BindView(R.id.marqueeTv)
    TextView marqueeTv;

    @BindView(R.id.relativeTop)
    RelativeLayout toprelative;

    @BindView(R.id.marqueeLinear)
    RelativeLayout marqueeLinear;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewfrag);
        context = WebviewActivity.this;
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        actionbarTitle = (TextView) toolbar.findViewById(R.id.actionbarTitle);
        logoutToolbarIcon = (ImageView) toolbar.findViewById(R.id.logouticon);

        sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();


        final String module = getIntent().getExtras().getString("Module");
        String userType = getIntent().getExtras().getString("UserType");
        String userId = getIntent().getExtras().getString("UserId");


        webciew = (WebView) findViewById(R.id.webview);

       /* if (module.equalsIgnoreCase(AppConfig.GOODS_RETURNS_MODULE) || module.equalsIgnoreCase(AppConfig.ADD_GOODS_RETURNS)
                || module.equalsIgnoreCase(AppConfig.SAMPLING_DISPATCH_MODULE) || module.equalsIgnoreCase(AppConfig.ADD_SAMPLING_DISPATCH)
                   || module.equalsIgnoreCase(AppConfig.COMPAINT_SUMMARY_MODULE) || module.equalsIgnoreCase(AppConfig.ADD_COMPLAINT_MODULE)){

        } else {
            pd = new ProgressDialog(WebviewActivity.this);
            pd.setMessage("Loading...");
        }*/
        pd = new ProgressDialog(WebviewActivity.this);
        pd.setMessage("Loading...");

        /*2/8/18 akash matlani  comment start */
        /* webciew.setWebViewClient(new MyBrowser());*/
        /*2/8/18 akash matlani  comment ended */
        webciew.getSettings().setLoadsImagesAutomatically(true);
        webciew.getSettings().setJavaScriptEnabled(true);
        webciew.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webciew.setVerticalScrollBarEnabled(true);
        webciew.setHorizontalScrollBarEnabled(true);

        webciew.setLongClickable(true);

        /*added by akash matlani 2/8/18 started*/
      /*  webciew.getSettings().setBuiltInZoomControls(true);
        webciew.getSettings().setSupportZoom(true);
        webciew.getSettings().setLoadWithOverviewMode(true);
        webciew.setWebViewClient(new WebViewClient());*/
        /*added by akash matlani 2/8/18 ended*/

        /*1/8/29 akash matlani*/
        webciew.setInitialScale(1);
        //webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webciew.getSettings().setLoadWithOverviewMode(true);
        webciew.getSettings().setUseWideViewPort(true);
        webciew.getSettings().setJavaScriptEnabled(true);

        webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        /*if (webciew.getVisibility() == View.VISIBLE) {
            pd.show();
        }*/


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String webviewurl = "";
        actionbarTitle.setText(module);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);


        if (!checkPermission()) {
            requestPermission();
        }

        //FEEDBACK >>
        if (module.equalsIgnoreCase("Feedback")) {
            pd.show();
            marqueeLinear.setVisibility(View.VISIBLE);


            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/feedback.php?" + "UserType=" + userType +
                    "&UserId=" + userId;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });

        }

        //Terms & Conditions >>
        if (module.equalsIgnoreCase("Terms & Conditions")) {
            marqueeLinear.setVisibility(View.VISIBLE);

            pd.show();

            webviewurl = "http://portal.rogerlaviale.com/api/v1/general/page_content.json?PageNo=1";
            getTermsConditionContent();
//            postData();

        }


        //EDIT PROFILE >>
        if (module.equalsIgnoreCase("Edit Profile")) {

            marqueeLinear.setVisibility(View.GONE);
            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);

            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/edit_profile.php?" + "UserType=" + userType +
                    "&iRefUserId=" + userId;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }


        //SALES & SERVICE >>
        if (module.equalsIgnoreCase("SalesService")) {
            pd.show();
            marqueeLinear.setVisibility(View.VISIBLE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Sales & Service Contacts");

            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/ss_contact_us.php?iRefUserId=" + userId;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


        }


        //NOTIFICATIONS >>
        if (module.equalsIgnoreCase("Notifications")) {
            pd.show();
            marqueeLinear.setVisibility(View.VISIBLE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Notifications");

            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("UserId");
                userType = returnJobj.optString("UserType");
            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/notification.php?UserId=" + userId + "&UserType=" + userType;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


        }

        //CUSTOMER_ADD_MODULE >>
        if (module.equalsIgnoreCase(AppConfig.CUSTOMER_ADD_MODULE)) {
            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Customer Add");

            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");
            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_add.php?iRefUserId=" + userId;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


        }

        //CUSTOMER_EDIT_MODULE >>
        if (module.equalsIgnoreCase(AppConfig.CUSTOMER_EDIT_MODULE)) {
            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Customer Edit");

            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

                String customerDetailsObj = getIntent().getExtras().getString("SingleObj");
                JSONObject custmoerSignleObj = new JSONObject(customerDetailsObj);
                userId = custmoerSignleObj.optString("CustomerId");


            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_add.php?iCustomerId=" + userId + "&mode_type=edit";

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


        }


        //ORDER_ADD_MODULE >>
        if (module.equalsIgnoreCase(AppConfig.ORDER_ADD_MODULE)) {
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Order Add");

            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            webviewurl = "https://horizoncore.in/roger_that/mobile_apps/core_files/order_add.php?iRefUserId=" + userId;
//            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_complain.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/order_add.php?iRefUserId=" + userId + "&iUserType=" + userType;
            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);

        }

        //ORDER_EDIT_MODULE >>
        if (module.equalsIgnoreCase(AppConfig.ORDER_EDIT_MODULE)) {
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Order Edit");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

                String customerDetailsObj = getIntent().getExtras().getString("SingleObj");
                JSONObject custmoerSignleObj = new JSONObject(customerDetailsObj);
                orderId = custmoerSignleObj.optString("iOrderId");

//                https://horizoncore.in/roger_that/mobile_apps/core_files/order_add.php?iRefUserId=11&iOrderId=7&mode_type=edit

            } catch (Exception e) {
                e.printStackTrace();
            }

//            webviewurl = "https://horizoncore.in/roger_that/mobile_apps/core_files/order_add.php?iRefUserId=" + userId + "&iOrderId=" + orderId + "&mode_type=edit"; LoginUserType=2&LoginUserId=1555
            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/order_add.php?iRefUserId=" + userId + "&iOrderId=" + orderId + "&mode_type=edit&LoginUserType="+userType+"&LoginUserId="+userId;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);
        }

        //ORDER_EDIT_MODULE2 >> WHEN CLICK OF SUBMIT BUTTON FRMO BUZZ ORDER ADD
        if (module.equalsIgnoreCase(AppConfig.ORDER_EDIT_MODULE_BUZZ)) {
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Order Edit");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

                String redirectLink = getIntent().getExtras().getString("SingleObj");
                webviewurl = redirectLink;

            } catch (Exception e) {
                e.printStackTrace();
            }
            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);
        }


        //COMPLAINT SUMMARY WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.COMPAINT_SUMMARY_MODULE)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            logoutToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Complaints");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_complain.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


        }


        //ADD COMPLAINT WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.ADD_COMPLAINT_MODULE)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Add Complaint");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_complain_add.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }


        //GOODS_RETURNS_MODULE WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.GOODS_RETURNS_MODULE)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Goods Returns");

            logoutToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));


            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/goods_return.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }


        //ADD GOODS RETURNS  WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.ADD_GOODS_RETURNS)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Add Goods Returns");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/goods_return_add.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);

        }


        //SAMPLING_DISPATCH_MODULE  WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.SAMPLING_DISPATCH_MODULE)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Sampling Request");
            logoutToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/sampling_request.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }


        // ADD_SAMPLING_DISPATCHMODULE  WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.ADD_SAMPLING_DISPATCH)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("New Sampling Request");
            logoutToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout));

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/sampling_request_add.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }


        // CUSTOMERWISE_OUTSTANDING WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.CUSTOMERWISE_OUTSTANDING)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Customer wise outstanding");
//            logoutToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_outstanding_report.php?iRefUserId=" + userId + "&iUserType=" + userType + "&iCustomerId=0";

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }

        // CUSTOMERWISE_LEDGER WEB UI >>
        if (module.equalsIgnoreCase(AppConfig.CUSTOMERWISE_LEDGER)) {
//            pd.show();
            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Customer wise ledger");
//            logoutToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/customer_ledger_report.php?iRefUserId=" + userId + "&iUserType=" + userType + "&iCustomerId=0";

            webciew.loadUrl(webviewurl);
            loadImageUploadWebview(webciew);


        }

        //NEWS CORNER>>
        if (module.equalsIgnoreCase(AppConfig.NEWS_CORNER)) {

            marqueeLinear.setVisibility(View.GONE);


            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);

            actionbarTitle.setText("News Corner");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                Log.d("exception-->>", e.getMessage());
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/news_corner.php?iUserType=" + userType;
            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });

            /*akash matlani 18-09-18 started comment
            for copy and paste function not working thats why this code in comment */

            /*changes from here */

            /*webciew.getSettings().setDomStorageEnabled(true);
             *//* webciew.getSettings().setJavaScriptEnabled(true);*//*

            webciew.getSettings().setJavaScriptEnabled(true);

            *//*comment by akash matlani 30/07/18*//*
           // webciew.getSettings().setLoadWithOverviewMode(true);
            //webciew.getSettings().setUseWideViewPort(true);
            webciew.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webciew.setScrollbarFadingEnabled(false);
            //webciew.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

            *//*akash matlani 1/8/18*//*

            webciew.setInitialScale(1);
            //webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            webciew.getSettings().setLoadWithOverviewMode(true);
            webciew.getSettings().setUseWideViewPort(true);
            webciew.getSettings().setJavaScriptEnabled(true);

            webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);



           // webciew.loadUrl(webviewurl);


            *//*akash matlani 1/8/18*//*
            webciew.setInitialScale(1);
            //webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            webciew.getSettings().setLoadWithOverviewMode(true);
            webciew.getSettings().setUseWideViewPort(true);
            webciew.getSettings().setJavaScriptEnabled(true);

            webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);






            webciew.setLongClickable(true);
            webciew.setOnLongClickListener(this);
           *//* webciew.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {


                    return true;
                }



            });
*/

            /*akash matlani 18-09-18 ended  comment */
            webciew.setWebViewClient(new DownloadImageClient());
            /*complete chnages here*/

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


/*
            webciew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webciew.loadUrl("http://www.google.com");
                }
            });*/


            /*akash matlani 1/8/18 started*/
            webciew.addJavascriptInterface(this, "AndroidInterface");
            /*akash matlani 1/8/18 ended*/
            webciew.setDownloadListener(new DownloadListener() {

                @SuppressLint("JavascriptInterface")
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();

                    request.setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,    //Download folder
                            "RogerThat_NewsCorner_" + CommonUses.getCurrentTimestamp("dd_MMM_yyyy_hh_mm_ss"));  //Name of file


                    DownloadManager dm = (DownloadManager) getSystemService(
                            DOWNLOAD_SERVICE);

                    dm.enqueue(request);
                  /* webciew.loadUrl("http://www.google.com");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));*/
                    Toast.makeText(WebviewActivity.this, "Image Downloaded Successfully.", Toast.LENGTH_LONG).show();

                }
            });


        }


        //SALES REPORT>
        if (module.equalsIgnoreCase(AppConfig.SALES_REPORT)) {

            marqueeLinear.setVisibility(View.GONE);


            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Sales Report");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }

            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/sales_report.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });


        }


        //RANGEWISE_SALES_REPORT
        if (module.equalsIgnoreCase(AppConfig.RANGEWISE_SALES_REPORT)) {

            marqueeLinear.setVisibility(View.GONE);


            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Rangewise Sales Report");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }
            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/rangewise_sales_report.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }


        //COLLECTION_REPORT
        if (module.equalsIgnoreCase(AppConfig.COLLECTION_REPORT)) {

            marqueeLinear.setVisibility(View.GONE);

            String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);
            String deviceidStr = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);
            actionbarTitle.setText("Collection Report");

            String orderId = "";
            try {
                JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                userId = returnJobj.optString("iRefUserId");
                userType = returnJobj.optString("UserType");

            } catch (Exception e) {
                e.printStackTrace();
            }
            webviewurl = "http://portal.rogerlaviale.com/mobile_apps/core_files/collection_report.php?iRefUserId=" + userId + "&iUserType=" + userType;

            webciew.loadUrl(webviewurl);

            webciew.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        if (webciew.canGoBack()) {
                            webciew.goBack();
                        } else {
                            finish();
                        }
                        return true;
                    }
                    return false;
                }
            });

        }


        logoutToolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userTokenobjStirng = sharedpreference.LoadStringPref(AppConfig.LOGGEDIN_USERDETAILS, AppConfig.LOGGEDIN_USERDETAILS);


                if (module.equalsIgnoreCase(AppConfig.COMPAINT_SUMMARY_MODULE)) {

                    //Add Complaint Summary
                    try {

                        JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                        String refUserId = returnJobj.optString("iRefUserId");
                        String userTypeTemop = returnJobj.optString("UserType");

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.ADD_COMPLAINT_MODULE);
                        in.putExtra("UserType", userTypeTemop);
                        in.putExtra("UserId", refUserId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (module.equalsIgnoreCase(AppConfig.GOODS_RETURNS_MODULE)) {

                    //Add Goods Returns

                    try {

                        JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                        String refUserId = returnJobj.optString("iRefUserId");
                        String userTypeTemop = returnJobj.optString("UserType");

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.ADD_GOODS_RETURNS);
                        in.putExtra("UserType", userTypeTemop);
                        in.putExtra("UserId", refUserId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (module.equalsIgnoreCase(AppConfig.SAMPLING_DISPATCH_MODULE)) {

                    //Add SAMPLING DISPATCH

                    try {

                        JSONObject returnJobj = new JSONObject(userTokenobjStirng);
                        String refUserId = returnJobj.optString("iRefUserId");
                        String userTypeTemop = returnJobj.optString("UserType");

                        Intent in = new Intent(context, WebviewActivity.class);
                        in.putExtra("Module", AppConfig.ADD_SAMPLING_DISPATCH);
                        in.putExtra("UserType", userTypeTemop);
                        in.putExtra("UserId", refUserId);
                        startActivity(in);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    logoutDialogProcess();
                }

            }
        });

        marqueeTv.setSelected(true);
        String marquee = sharedpreference.LoadStringPref(AppConfig.MARQUEE_TEXT, AppConfig.MARQUEE_TEXT);
        if (!marquee.equalsIgnoreCase(AppConfig.MARQUEE_TEXT)) {
            marqueeTv.setText(marquee);
        }


    }

    private void loadImageUploadWebview(final WebView webciewTemp) {
        WebSettings webSettings;

        webSettings = webciewTemp.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);

        webciewTemp.setWebViewClient(new PQClient());
        webciewTemp.setWebChromeClient(new PQChromeClient());
        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration
        if (Build.VERSION.SDK_INT >= 19) {
            webciewTemp.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            webciewTemp.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webciewTemp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    if (webciewTemp.canGoBack()) {
                        webciewTemp.goBack();
                    } else {
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private static final String URL_STRING = "http://portal.rogerlaviale.com/api/v1/general/page_content.json";

    public void postData() {


        String postData = "PageNo=1";

       /* webciew.postUrl(
                "http://portal.rogerlaviale.com/api/v1/general/page_content.json",
                EncodingUtils.getBytes(postData, "BASE64"));*/

        /*
        try {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        //Your code goes here


                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("PageNo", "1"));

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(URL_STRING);
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httppost.setHeader("Content-Type", "application/json");
                        HttpResponse response = httpclient.execute(httppost);

                        String data = new BasicResponseHandler().handleResponse(response);
                        webciew.loadData(data, "text/html", "utf-8");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    }

    @Override
    public boolean onLongClick(View v) {
       /* v.performLongClick();
        webciew.getSettings().setLoadsImagesAutomatically(true);
        webciew.getSettings().setJavaScriptEnabled(true);
        webciew.setWebViewClient(new DownloadImageClient());*/
        return true;
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            pd.dismiss();

            String webUrl = webciew.getUrl();

        }


    }

    private void logoutDialogProcess() {
        new AlertDialog.Builder(WebviewActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sharedpreference.initPref();
                        sharedpreference.SaveStringPref(AppConfig.FROM_LOGOUT, "True");
                        sharedpreference.ApplyPref();

                        Intent in = new Intent(WebviewActivity.this, LoginActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        finish();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Network call  for Getting Login Credentials
    private void getTermsConditionContent() {

        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();

       /* HashMap<String, String> counterParams = new HashMap<String, String>();
        counterParams.put("RegionId", selectedRegionId);
        counterParams.put("ProjectId", selectedProjectId);*/
        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("PageNo", "1");

        //Login API Call using Interface
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TermConditionAPiResponse> call = apiService.termscondition(loginParams);
        call.enqueue(new Callback<TermConditionAPiResponse>() {
            @Override
            public void onResponse(Call<TermConditionAPiResponse> call, Response<TermConditionAPiResponse> response) {

                int statusCode = response.code();
                if (statusCode == 200) {

                    //GEt full response in FULLRESPONSE object
                    TermConditionAPiResponse fullresponse = response.body();

                    if (fullresponse.getCode().equalsIgnoreCase("2000")) {

                        String content = fullresponse.getResult().get(0).getContent();

                        webciew.loadData(content, "text/html", "UTF-8");

                    } else {

                    }


                } else {

                }
                progress.cancel();

            }

            @Override
            public void onFailure(Call<TermConditionAPiResponse> call, Throwable t) {
                // Log error here since request failed
                progress.cancel();
                if (BuildConfig.DEBUG)
                    CommonUses.showToast(context, t.getMessage());


            }
        });
    }

    public class PQClient extends WebViewClient {
        ProgressDialog progressDialog;

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // If url contains mailto link then open Mail Intent

            // Stay within this webview and load url
            view.loadUrl(url);


            return true;
        }

        //Show loader on url load
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // Then show progress  Dialog
            // in standard case YourActivity.this
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(WebviewActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }

        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            webciew.loadUrl("javascript:(function(){ " +
                    "document.getElementById('android-app').style.display='none';})()");
            //javascript:document.getElementById('imPage').style.display='none';"


            try {
                // Close progressDialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PQChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;

        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard

            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");
            Log.d("File", "File: " + file);
            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[]{captureIntent});

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);


        }

        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }


        public void onCloseWindow(WebView w) {
            super.onCloseWindow(w);
            toprelative.removeViewAt(toprelative.getChildCount() - 1);
            Log.d("", "Window close");
        }

        // Add new webview in same window
        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            WebView childView = new WebView(context);
            childView.getSettings().setJavaScriptEnabled(true);
            childView.setWebChromeClient(this);
            childView.setWebViewClient(new myWebClient());

            toprelative.addView(childView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(childView);
            resultMsg.sendToTarget();
            return true;
        }


    }


    public class DownloadImageClient extends WebViewClient {
        ProgressDialog progressDialog;

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // If url contains mailto link then open Mail Intent

            // Stay within this webview and load url

            view.loadUrl(url);

            /*akash matlani 1/8/18 started*/
            webciew.setInitialScale(1);

            //webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webciew.getSettings().setLoadWithOverviewMode(true);
            webciew.getSettings().setUseWideViewPort(true);
            webciew.getSettings().setJavaScriptEnabled(true);

            webciew.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

            /*akash matlani 1/8/18 ended*/
            if (url.contains(".jpg")) {

                DownloadManager mdDownloadManager = (DownloadManager) context
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                File destinationFile = new File(
                        Environment.getExternalStorageDirectory() + "/RogerThat_NewsCorner",
                        getFileName(url));
                request.setDescription("Downloading ...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationUri(Uri.fromFile(destinationFile));
                mdDownloadManager.enqueue(request);

                return true;
            }


            return true;
        }

        //Show loader on url load
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // Then show progress  Dialog
            // in standard case YourActivity.this
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(WebviewActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }

        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            webciew.loadUrl("javascript:(function(){ " +
                    "document.getElementById('android-app').style.display='none';})()");

            /*1/8/18*/
           /* webciew.loadUrl( "javascript:(function() { " +
                    "document.getElementById(android-app).style.display='none';" +
                    "});" +

                    "})()");
*/
            try {
                // Close progressDialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }


    /*@Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);

        final WebView.HitTestResult webViewHitTestResult = webciew.getHitTestResult();

        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

            contextMenu.setHeaderTitle("Download Image From Below");

            contextMenu.add(0, 1, 0, "Save - Download Image")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            String DownloadImageURL = webViewHitTestResult.getExtra();

                            if (URLUtil.isValidUrl(DownloadImageURL)) {

                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageURL));
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                request.setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS,    //Download folder
                                        "RogerThat_NewsCorner_" + CommonUses.getCurrentTimestamp("dd_MMM_yyyy_hh_mm_ss"));  //Name of file

                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);

                                Toast.makeText(WebviewActivity.this, "Image Downloaded Successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(WebviewActivity.this, "Sorry.. Something Went Wrong.", Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });
        }
    }*/


    public String getFileName(String url) {
        String filenameWithoutExtension = "";
        filenameWithoutExtension = String.valueOf(System.currentTimeMillis()
                + ".jpg");
        return filenameWithoutExtension;
    }


    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(WebviewActivity.this, READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(WebviewActivity.this, WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(WebviewActivity.this, CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(WebviewActivity.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean locationAccepted2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean locationAccepted3 = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && locationAccepted2 && locationAccepted3)

                        Toast.makeText(WebviewActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(WebviewActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WebviewActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}