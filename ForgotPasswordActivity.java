package com.rogerthat.rlvltd.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.apiresponsemodels.ForgotResponse;
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


/**
 * Created by Ratan on 7/9/2015.
 */
public class ForgotPasswordActivity extends AppCompatActivity {


    private Context context;
    private PreferenceHelper sharedpreference;

    @BindView(R.id.backtologin_Tv)
    TextView backtologin_Tv;

    @BindView(R.id.retrive_pasword_btn)
    Button retrive_pasword_btn;

    @BindView(R.id.userNameEt)
    EditText userNameEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        context = ForgotPasswordActivity.this;
        ButterKnife.bind(this);
        sharedpreference = new PreferenceHelper(context, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();

        //setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        retrive_pasword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboardmethod();
                if (!(userNameEt.getText().toString().trim().isEmpty())) {
                    if (new ConnectionDetector(context).isConnectingToInternet() == true) {
                        getForgotPasswordAPICall();
                    } else {
                        CommonUses.showMessageSnackbarForErrorDuration(context, backtologin_Tv, AppConfig.INTERNETFAILED_MESSAGE, 2500);
                    }
                } else {
                    CommonUses.showMessageSnackbarForErrorDuration(context, backtologin_Tv, "Please Enter UserName.", 2500);
                }
            }
        });
        backtologin_Tv.setPaintFlags(backtologin_Tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        backtologin_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();


    }


    //Network call  for Getting Login Credentials
    private void getForgotPasswordAPICall() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();
        HashMap<String, String> forgotpassword_params = new HashMap<String, String>();
        forgotpassword_params.put("UserName", userNameEt.getText().toString().trim());
        //Login API Call using Interface
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ForgotResponse> call = apiService.forgotPasswordAPICall(forgotpassword_params);
        call.enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //GEt full response in FULLRESPONSE object
                    ForgotResponse fullresponse = response.body();
                    if (fullresponse.getCode().equalsIgnoreCase("2012")) {
                        userNameEt.setText("");
                        CommonUses.showPositiveDialogButton(context, "Success", fullresponse.getMessage());
                    } else {
                        CommonUses.showToastwithDuration(2500, "Forgot Password failed. Something wrong. Please try again !!! ", context);
                    }
                } else {
                    CommonUses.printErrorMessage(response, userNameEt, context, backtologin_Tv);
                }
                progress.cancel();
            }

            @Override
            public void onFailure(Call<ForgotResponse> call, Throwable t) {
                // Log error here since request failed
                progress.cancel();
                CommonUses.showToastwithDuration(2500, "Forgot Password failed. Something wrong!!!", context);
                if (BuildConfig.DEBUG)
                    CommonUses.showToast(context, t.getMessage());
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

}