package com.rogerthat.rlvltd.com;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.rogerthat.rlvltd.com.apiresponsemodels.LoginAPIResposne;
import com.rogerthat.rlvltd.com.database.DatabaseHelper;
import com.rogerthat.rlvltd.com.util.ApiClient;
import com.rogerthat.rlvltd.com.util.ApiInterface;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.ConnectionDetector;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;


public class LoginActivity extends AppCompatActivity {

    private Context context;
    private PreferenceHelper sharedpreference;
    private PreferenceHelper sharedpreference2;

    private DatabaseHelper databaseHelper = null;
    private boolean password_visible = false;
    ProgressDialog progress;

    @BindView(R.id.userNameEt)
    EditText userNameEt;

    @BindView(R.id.passwordEt)
    EditText passwordEt;

    @BindView(R.id.signinButton)
    Button signinButton;

    @BindView(R.id.singupButton)
    Button singupButton;

    @BindView(R.id.knowmore_layout)
    LinearLayout knowmore_layout;

    @BindView(R.id.know_more)
    TextView know_more;

    @BindView(R.id.forgot_passwordTV)
    TextView forgot_passwordTV;

    @BindView(R.id.instagram_icon)
    ImageView instagram_icon;

    @BindView(R.id.instagram_text)
    TextView instagram_text;


    @BindView(R.id.website_icon)
    ImageView website_icon;

    @BindView(R.id.website_Text)
    TextView website_Text;


    /*  @Bind(R.id.introLinear)
    LinearLayout introLinear;
    @Bind(R.id.introText)
    TextView introText;*/
    private static final int PERMISSION_REQUEST_CODE = 200;
   // private String android_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        context = LoginActivity.this;

        sharedpreference = new PreferenceHelper(LoginActivity.this, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference2 = new PreferenceHelper(LoginActivity.this, AppConfig.SHAREDPREFERENCE_STRING2);
        sharedpreference.initPref();
        sharedpreference2.initPref();
        createdbhelper();
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboardmethod();
                if (!(userNameEt.getText().toString().trim().isEmpty()) &&
                        !(passwordEt.getText().toString().trim().isEmpty())) {
                    if (new ConnectionDetector(context).isConnectingToInternet() == true) {
                        getLoginCredentials();

                    } else {
//                        CommonUses.showMessageSnackbarForError(context, signinButton, AppConfig.INTERNETFAILED_MESSAGE);
                        CommonUses.showMessageSnackbarForErrorDuration(context, signinButton, AppConfig.INTERNETFAILED_MESSAGE, 2500);

                    }
                } else {
//                    CommonUses.showSnackbar(passwordEt, "Please Enter UserId & Password.");
                    CommonUses.showMessageSnackbarForErrorDuration(context, signinButton, "Please Enter UserId & Password.", 2500);

                }
            }

        });
        singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(in);
            }
        });
        knowmore_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent in = new Intent(LoginActivity.this, IntroScreenWebViewActivity.class);
                    startActivity(in);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, IntroScreenWebViewActivity.class);
                startActivity(in);
            }
        });
        forgot_passwordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboardmethod();
                try {
                    Intent in = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        instagram_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/rogerlaviale/"));
                startActivity(i);
            }
        });

        instagram_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/rogerlaviale/"));
                startActivity(i);
            }
        });

        website_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://rogerlaviale.com"));
                startActivity(i);
            }
        });

        website_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://rogerlaviale.com"));
                startActivity(i);
            }
        });
       /* introLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, IntroScreenWebViewActivity.class);
                startActivity(in);
                finish();
            }
        });*/
        if (!checkPermission()) {
            requestPermission();
        }
        userNameEt.setText(checkforSameSPString(sharedpreference2.LoadStringPref(AppConfig.LASTLOGIN_USERID, AppConfig.LASTLOGIN_USERID),
                AppConfig.LASTLOGIN_USERID));
        passwordEt.setText(checkforSameSPString(sharedpreference2.LoadStringPref(AppConfig.LASTLOGIN_PASSWORD, AppConfig.LASTLOGIN_PASSWORD),
                AppConfig.LASTLOGIN_PASSWORD));

//        userNameEt.setText("Mayur1");
//        passwordEt.setText("Mayur@1");
//
//        userNameEt.setText("Orders");
//        passwordEt.setText("Orders@1");
    }


    private String checkforSameSPString(String spValueStr, String compareStr) {
        if (spValueStr.equalsIgnoreCase(compareStr)) {
            spValueStr = "";
        }
        return spValueStr;
    }


    private void settypeface(RadioButton radio_customer, RadioButton radio_marketing) {
        Typeface font = Typeface.createFromAsset(getAssets(), "LatoRegular.ttf");
        radio_customer.setTypeface(font);
        radio_marketing.setTypeface(font);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUses.checkForExpiryDate(LoginActivity.this);
        if (sharedpreference.LoadStringPref(AppConfig.FROM_LOGOUT, AppConfig.FROM_LOGOUT).equalsIgnoreCase("true")) {
            /*try {
                databaseHelper.deleteDataTables(context, databaseHelper);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            databaseHelper.deleteDataTables(databaseHelper, "Customers");
            sharedpreference.clearAllPrefs();
            CommonUses.showMessageSnackbarForErrorDuration(context, signinButton, "Logout Success.", 2000);

        }
        if (sharedpreference.LoadStringPref(AppConfig.LOGGEDIN, AppConfig.LOGGEDIN).equalsIgnoreCase("True")) {
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(in);

        }

    }

    private void hidekeyboardmethod() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    //Network call  for Getting Login Credentials
    private void getLoginCredentials() {
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //IMEI Number
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //final String deviceid = mTelephonyManager.getDeviceId();

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null && !(refreshedToken.isEmpty())) {
            refreshedToken = refreshedToken;
        } else {
            refreshedToken = "";

        }
        final ProgressDialog progress = new ProgressDialog(context);
        final String android_device_id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();



       /* HashMap<String, String> counterParams = new HashMap<String, String>();
        counterParams.put("RegionId", selectedRegionId);an
        counterParams.put("ProjectId", selectedProjectId);*/
        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("UserName", userNameEt.getText().toString().trim());
        loginParams.put("Password", passwordEt.getText().toString().trim());
        /*loginParams.put("device_no", deviceid);*/
        loginParams.put("device_no",android_device_id);
        loginParams.put("device", "2");
        loginParams.put("device_platform", "Android");
        loginParams.put("device_uuid", refreshedToken);
        loginParams.put("device_model", manufacturer + " " + model);
        loginParams.put("device_version", "API_Level_" + version + "_VERSION_" + versionRelease);
        //Login API Call using Interface

        Log.i("ANDROID ID=======>",android_device_id);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginAPIResposne> call = apiService.loginAPICall(loginParams);
        call.enqueue(new Callback<LoginAPIResposne>() {
            @Override
            public void onResponse(Call<LoginAPIResposne> call, Response<LoginAPIResposne> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //GEt full response in FULLRESPONSE object
                    LoginAPIResposne fullresponse = response.body();
                    if (fullresponse.getCode().equalsIgnoreCase("2004")) {
                        Gson gson = new Gson();
                        String loginResponseString = gson.toJson(fullresponse.getResult());
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.USERTOKEN, fullresponse.getResult().getDeviceToken());
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.LOGGEDIN_USERDETAILS, loginResponseString);
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.LOGGEDIN, "True");
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.DEVICEID, android_device_id);
                        CommonUses.insertsharedpreference(sharedpreference,AppConfig.USER_ID,fullresponse.getResult().getUserId());
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.USER_TYPE, fullresponse.getResult().getUserType());
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.IREF_USER_ID, fullresponse.getResult().getIRefUserId());
                        CommonUses.insertsharedpreference(sharedpreference,AppConfig.LOGIN_ID, fullresponse.getResult().getLoginId());
                        //CommonUses.insertsharedpreference(sharedpreference, AppConfig.LOGINEMAIL_ID,fullresponse.getResult().getLoginId());
                        CommonUses.insertsharedpreference(sharedpreference, AppConfig.LOGINEMAIL_ID,fullresponse.getResult().getLoginEmailId());
                        CommonUses.insertsharedpreference(sharedpreference2, AppConfig.LASTLOGIN_USERID, userNameEt.getText().toString().trim());
                        CommonUses.insertsharedpreference(sharedpreference2, AppConfig.LASTLOGIN_PASSWORD, passwordEt.getText().toString().trim());
                        databaseHelper.deleteDataTables(databaseHelper, "All");

                        //FOR BUZZ & CARLOBARBERA MODULE
                        //If its a Sales User : UserType = 2
                        if (fullresponse.getResult().getUserType().equalsIgnoreCase("2")) {
                            boolean isBuzz = false, isCarloBarbera = false, isOrderStatus = false;
                            List<LoginAPIResposne.UserMenuAccessArrBean> menuAccessList = fullresponse.getUserMenuAccessArr();
                            for (LoginAPIResposne.UserMenuAccessArrBean singleObj : menuAccessList) {
                                //Buzz = Id = 62
                                if (singleObj.getId().equalsIgnoreCase("62")) {
                                    isBuzz = true;
                                }
                                //CarloBarbera = Id = 66
                                if (singleObj.getId().equalsIgnoreCase("66")) {
                                    isCarloBarbera = true;
                                }
                                //OrderStatus = Id = 20
                                if (singleObj.getId().equalsIgnoreCase("20")) {
                                    isOrderStatus = true;
                                }
                            }
                            CommonUses.insertsharedpreference(sharedpreference, AppConfig.IS_BUZZ, String.valueOf(isBuzz));
                            CommonUses.insertsharedpreference(sharedpreference, AppConfig.IS_CARLOBARBERA, String.valueOf(isCarloBarbera));
                            CommonUses.insertsharedpreference(sharedpreference, AppConfig.IS_ORDER_STATUS, String.valueOf(isOrderStatus));
                        }

                        //FOR BUZZ & CARLOBARBERA MODULE
                        //If its a Customer User : UserType = 3
                        if (fullresponse.getResult().getUserType().equalsIgnoreCase("3")) {
                            boolean isBuzz = false, isCarloBarbera = false, isOrderStatus = false;
                            List<LoginAPIResposne.UserMenuAccessArrBean> menuAccessList = fullresponse.getUserMenuAccessArr();
                            for (LoginAPIResposne.UserMenuAccessArrBean singleObj : menuAccessList) {
                                //Buzz = Id = 63
                                if (singleObj.getId().equalsIgnoreCase("63")) {
                                    isBuzz = true;
                                }
                                //CarloBarbera = Id = 67
                                if (singleObj.getId().equalsIgnoreCase("67")) {
                                    isCarloBarbera = true;
                                }
                                //OrderStatus = Id = 6
                                if (singleObj.getId().equalsIgnoreCase("6")) {
                                    isOrderStatus = true;
                                }
                            }
                            CommonUses.insertsharedpreference(sharedpreference, AppConfig.IS_BUZZ, String.valueOf(isBuzz));
                            CommonUses.insertsharedpreference(sharedpreference, AppConfig.IS_CARLOBARBERA, String.valueOf(isCarloBarbera));
                            CommonUses.insertsharedpreference(sharedpreference, AppConfig.IS_ORDER_STATUS, String.valueOf(isOrderStatus));
                        }

                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(in);
                        startService(new Intent("com.rogerthat.rlvltd.com.firebase.MyFirebaseMessagingService").setPackage("com.rogerthat.rlvltd.com.firebase"));
//                        CommonUses.showToastwithDuration(5000,fullresponse.getMessage(),context);
                    }
                } else {
                    CommonUses.printErrorMessage(response, passwordEt, context, signinButton);
                }
                progress.cancel();
            }

            @Override
            public void onFailure(Call<LoginAPIResposne> call, Throwable t) {
                // Log error here since request failed
                progress.cancel();
                if (BuildConfig.DEBUG)
                    CommonUses.showToast(context, t.getMessage());


            }
        });
    }

    private void createdbhelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LoginActivity.this, READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted)
                        Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(LoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_PHONE_STATE},
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
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}