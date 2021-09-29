package com.rogerthat.rlvltd.com.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.rogerthat.rlvltd.com.MainActivity;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.database.DatabaseHelper;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import butterknife.ButterKnife;


public class InventoryActivity extends AppCompatActivity {

    private Context context;
    private PreferenceHelper sharedpreference;
    private DatabaseHelper databaseHelper = null;

    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        context = InventoryActivity.this;
        sharedpreference = new PreferenceHelper(InventoryActivity.this, AppConfig.SHAREDPREFERENCE_STRING);

        createdbhelper();

    }

    private void settypeface(RadioButton radio_customer, RadioButton radio_marketing) {
        Typeface font = Typeface.createFromAsset(getAssets(), "LatoRegular.ttf");

        radio_customer.setTypeface(font);
        radio_marketing.setTypeface(font);
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


        }


        if (sharedpreference.LoadStringPref(AppConfig.LOGGEDIN, AppConfig.LOGGEDIN).equalsIgnoreCase("True")) {
            Intent in = new Intent(InventoryActivity.this, MainActivity.class);
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


    private void createdbhelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
    }



}
