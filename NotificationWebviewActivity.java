package com.rogerthat.rlvltd.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;



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
public class NotificationWebviewActivity extends AppCompatActivity {


    private WebView webciew;
    private ProgressDialog pd;

    private Context context;

    public Toolbar toolbar;
    TextView actionbarTitle;
    ImageView logoutToolbarIcon;
    private PreferenceHelper sharedpreference;



    @BindView(R.id.marqueeTv)
    TextView marqueeTv;

    @BindView(R.id.relativeTop)
    RelativeLayout toprelative;

    @BindView(R.id.marqueeLinear)
    RelativeLayout marqueeLinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewfrag);
        context = getApplicationContext();
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        actionbarTitle = (TextView) toolbar.findViewById(R.id.actionbarTitle);
        logoutToolbarIcon = (ImageView) toolbar.findViewById(R.id.logouticon);

        sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();




        webciew = (WebView) findViewById(R.id.webview);

        pd = new ProgressDialog(NotificationWebviewActivity.this);
        pd.setMessage("Loading...");


        webciew.setWebViewClient(new MyBrowser());
        webciew.getSettings().setLoadsImagesAutomatically(true);
        webciew.getSettings().setJavaScriptEnabled(true);
        webciew.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webciew.setVerticalScrollBarEnabled(true);
        webciew.setHorizontalScrollBarEnabled(true);


        /*if (webciew.getVisibility() == View.VISIBLE) {
            pd.show();
        }*/


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String webviewurl = "";
        actionbarTitle.setText("Notifications");
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);



        String userType = "";
        String userId = "";


        //NOTIFICATIONS >>

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







        logoutToolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogProcess();
            }
        });

        marqueeTv.setSelected(true);
        String marquee = sharedpreference.LoadStringPref(AppConfig.MARQUEE_TEXT, AppConfig.MARQUEE_TEXT);
        if (!marquee.equalsIgnoreCase(AppConfig.MARQUEE_TEXT)) {
            marqueeTv.setText(marquee);
        }


    }

    private static final String URL_STRING = "http://portal.rogerlaviale.com/api/v1/general/page_content.json";





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
        new AlertDialog.Builder(NotificationWebviewActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sharedpreference.initPref();
                        sharedpreference.SaveStringPref(AppConfig.FROM_LOGOUT, "True");
                        sharedpreference.ApplyPref();

                        Intent in = new Intent(NotificationWebviewActivity.this, LoginActivity.class);
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

                Intent in = new Intent(NotificationWebviewActivity.this, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }




}