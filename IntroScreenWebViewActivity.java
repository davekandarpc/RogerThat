package com.rogerthat.rlvltd.com;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

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
public class IntroScreenWebViewActivity extends AppCompatActivity {


    private WebView webciew;
//    private ProgressDialog pd;

    private Context context;

    //public Toolbar toolbar;
    private PreferenceHelper sharedpreference;


    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;

    @BindView(R.id.signinLinear)
    LinearLayout signinLinear;

    @BindView(R.id.signinText)
    TextView signinText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introscreen_webviewfrag);
        context = IntroScreenWebViewActivity.this;
        ButterKnife.bind(this);
        appbarlayout.setVisibility(View.GONE);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);

        sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();

        webciew = (WebView) findViewById(R.id.webview);

//        pd = new ProgressDialog(IntroScreenWebViewActivity.this);
//        pd.setMessage("Loading...");


        webciew.setWebViewClient(new PQClient());
        webciew.getSettings().setLoadsImagesAutomatically(true);
        webciew.getSettings().setJavaScriptEnabled(true);
        webciew.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webciew.setVerticalScrollBarEnabled(true);
        webciew.setHorizontalScrollBarEnabled(true);

        signinText.setPaintFlags(signinText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //setSupportActionBar(toolbar);
        String webviewurl = "http://rogerlaviale.com/#About_Us";
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webciew.loadUrl(webviewurl);
//        pd.show();

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


        signinLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(IntroScreenWebViewActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });

    }

    private static final String URL_STRING = "http://portal.rogerlaviale.com/api/v1/general/page_content.json";




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class PQClient extends WebViewClient {
//        ProgressDialog progressDialog;

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
            /*if (progressDialog == null) {
                progressDialog = new ProgressDialog(IntroScreenWebViewActivity.this);
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
            }*/
        }

        // Called when all page resources loaded
        public void onPageFinished(WebView view, String url) {
            /*try {
                // Close progressDialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (sharedpreference.LoadStringPref(AppConfig.FROM_LOGOUT, AppConfig.FROM_LOGOUT).equalsIgnoreCase("true")) {
            /*try {
                databaseHelper.deleteDataTables(context, databaseHelper);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            sharedpreference.clearAllPrefs();
            CommonUses.showMessageSnackbarForErrorDuration(context, signinLinear, "Logout Success.",2000);

        }


        if (sharedpreference.LoadStringPref(AppConfig.LOGGEDIN, AppConfig.LOGGEDIN).equalsIgnoreCase("True")) {
            Intent in = new Intent(IntroScreenWebViewActivity.this, LoginActivity.class);
            startActivity(in);

        }

    }




}