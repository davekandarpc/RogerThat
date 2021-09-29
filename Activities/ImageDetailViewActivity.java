package com.rogerthat.rlvltd.com.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.database.DatabaseHelper;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageDetailViewActivity extends AppCompatActivity {

    private Context context;
    private PreferenceHelper sharedpreference;
    private DatabaseHelper databaseHelper = null;

    ProgressDialog progress;

    @BindView(R.id.img_detail)
    BigImageView img_detail;

    @BindView(R.id.closeIcon)
    ImageView closeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detailsviewpage);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        context = ImageDetailViewActivity.this;
        sharedpreference = new PreferenceHelper(ImageDetailViewActivity.this, AppConfig.SHAREDPREFERENCE_STRING);

        createdbhelper();

        String url = getIntent().getExtras().getString("URL");

        try {
            BigImageViewer.prefetch(Uri.parse(url));



            img_detail.showImage(Uri.parse(url));

            ProgressPieIndicator pie = new ProgressPieIndicator();
            img_detail.setProgressIndicator(pie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    private void createdbhelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
    }

}