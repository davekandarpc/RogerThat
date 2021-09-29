package com.rogerthat.rlvltd.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.apiresponsemodels.LoginAPIResposne;
import com.rogerthat.rlvltd.com.util.ApiClient;
import com.rogerthat.rlvltd.com.util.ApiInterface;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.ConnectionDetector;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rogerthat.rlvltd.com.fragment.RangeSalesReportListFragment.sharedpreference;


public class SignUpActivity extends AppCompatActivity {

    private Context context;
    private PreferenceHelper sharedpreference;
    @BindView(R.id.userNameEt)
    EditText userNameEt;

    @BindView(R.id.storenameEt)
    EditText storenameEt;

    @BindView(R.id.cityNameEt)
    EditText cityNameEt;

    @BindView(R.id.phoneEt)
    EditText phoneEt;

    @BindView(R.id.emailEt)
    EditText emailEt;

    @BindView(R.id.backtoLoginTv)
    TextView backtoLoginTv;

    @BindView(R.id.signupSubmitButton)
    Button signupSubmitButton;

    @BindView(R.id.resetButton)
    Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        context = SignUpActivity.this;

        backtoLoginTv.setPaintFlags(backtoLoginTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameEt.setText("");
                storenameEt.setText("");
                cityNameEt.setText("");
                phoneEt.setText("");
                emailEt.setText("");
            }
        });

        signupSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidekeyboardmethod();
                if (checkValidation()) {
                    if (new ConnectionDetector(context).isConnectingToInternet() == true) {

                        getSignUpAPICall();

                    } else {
//                        CommonUses.showMessageSnackbarForError(context, signinButton, AppConfig.INTERNETFAILED_MESSAGE);
                        CommonUses.showMessageSnackbarForErrorDuration(context, signupSubmitButton, AppConfig.INTERNETFAILED_MESSAGE, 2500);
                    }
                }
            }
        });

        backtoLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void hidekeyboardmethod() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private boolean checkValidation() {

        if (userNameEt.getText().toString().isEmpty()) {
            userNameEt.requestFocus();
            openKeyboard(userNameEt);
            Toast.makeText(SignUpActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (storenameEt.getText().toString().isEmpty()) {
            storenameEt.requestFocus();
            openKeyboard(storenameEt);
            Toast.makeText(SignUpActivity.this, "Please Enter Store Name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (cityNameEt.getText().toString().isEmpty()) {
            cityNameEt.requestFocus();
            openKeyboard(cityNameEt);
            Toast.makeText(SignUpActivity.this, "Please Enter City Name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (phoneEt.getText().toString().isEmpty()) {
            phoneEt.requestFocus();
            openKeyboard(phoneEt);
            Toast.makeText(SignUpActivity.this, "Please Enter Phone", Toast.LENGTH_LONG).show();
            return false;
        }

        if (emailEt.getText().toString().isEmpty()) {
            emailEt.requestFocus();
            openKeyboard(emailEt);
            Toast.makeText(SignUpActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void openKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    //Network call  for Getting Login Credentials
    private void getSignUpAPICall() {


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
        final String deviceid = sharedpreference.LoadStringPref(AppConfig.DEVICEID, AppConfig.DEVICEID);/*mTelephonyManager.getDeviceId();*/


        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();

        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("device_no", deviceid);
        loginParams.put("Name", userNameEt.getText().toString().trim());
        loginParams.put("StoreName", storenameEt.getText().toString().trim());
        loginParams.put("City", cityNameEt.getText().toString().trim());
        loginParams.put("Phone", phoneEt.getText().toString().trim());
        loginParams.put("Email", emailEt.getText().toString().trim());
        loginParams.put("device", "2");

        //Login API Call using Interface
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginAPIResposne> call = apiService.signupApiCall(loginParams);
        call.enqueue(new Callback<LoginAPIResposne>() {
            @Override
            public void onResponse(Call<LoginAPIResposne> call, Response<LoginAPIResposne> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //GEt full response in FULLRESPONSE object
                    LoginAPIResposne fullresponse = response.body();
                    if (fullresponse.getCode().equalsIgnoreCase("2017")) {
                        Gson gson = new Gson();
                        CommonUses.showToastwithDuration(2500, fullresponse.getMessage(), context);
                        String loginResponseString = gson.toJson(fullresponse.getResult());
                        Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    } else {
                        CommonUses.showToastwithDuration(2500, "Signup failed. Something wrong!!!", context);
                    }
                } else {
                    CommonUses.printErrorMessage(response, emailEt, context, signupSubmitButton);
                }
                progress.cancel();
            }

            @Override
            public void onFailure(Call<LoginAPIResposne> call, Throwable t) {
                // Log error here since request failed
                progress.cancel();
                CommonUses.showToastwithDuration(2500, "Signup failed. Please try after sometime !!!", context);
                if (BuildConfig.DEBUG)
                    CommonUses.showToast(context, t.getMessage());
            }
        });
    }


}
