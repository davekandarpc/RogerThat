package com.rogerthat.rlvltd.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.rogerthat.rlvltd.com.adapters.SpAdapter;
import com.rogerthat.rlvltd.com.database.DatabaseHelper;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class InstallationFormActivity extends AppCompatActivity {

    private Context context;
    private PreferenceHelper sharedpreference;
    ProgressDialog progress;

    private Uri imageUriglobal;
    private Uri selectedImageUri1;
    private Bitmap thumbnail1;
    private String selectedpicturePath1 = "";
    private File imgFile1;

    private Uri selectedImageUri2;
    private Bitmap thumbnail2;
    private String selectedpicturePath2 = "";
    private File imgFile2;

    private Uri selectedImageUri3;
    private Bitmap thumbnail3;
    private String selectedpicturePath3 = "";
    private File imgFile3;


    private static String DATE_TIME_TAG = "";
    private static final int PERMISSION_REQUEST_CODE = 200;
    protected static final int REQUEST_CAMERA = 1888;
    private static int RESULT_LOAD_IMAGE = 1;

    private boolean photo1clicked = false;
    private boolean photo2clicked = false;
    private boolean photo3clicked = false;
    ProgressDialog progressBar;


    private List<String> unionsList = new ArrayList<String>();
    private List<String> unionsIDList = new ArrayList<String>();
    private SpAdapter adapter;
    int selectedUnionsPos = -1;
    private String selectedCustomerId = "";

    private String currentTimeStr;

    private DatabaseHelper databaseHelper = null;
    private String thisInstallationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installationform_activity);

        ButterKnife.bind(this);
//        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Installation");
        getSupportActionBar().setElevation(0);




        createdbhelper();


        if (!checkPermission()) {
            requestPermission();
        }






    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    private void hidekeyboardmethod() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(InstallationFormActivity.this, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(InstallationFormActivity.this, READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(InstallationFormActivity.this, CAMERA);

        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(InstallationFormActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);

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






    private void createdbhelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
    }



}
