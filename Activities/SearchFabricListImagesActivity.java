package com.rogerthat.rlvltd.com.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.adapters.SearchFabricListImagesAdapter;
import com.rogerthat.rlvltd.com.callback.NetworkCallbackM;
import com.rogerthat.rlvltd.com.model.ArticleGallery;
import com.rogerthat.rlvltd.com.model.Results;
import com.rogerthat.rlvltd.com.util.ApiClient;
import com.rogerthat.rlvltd.com.util.ApiInterface;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFabricListImagesActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    ViewPager viewPager;
    // int images[] = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4};
    SearchFabricListImagesAdapter searchFabricListImagesAdapter;
    ProgressDialog progressDialog;
    String aricalno;
    ImageView closeIcon;
    TextView txt_no_images;
    TextView txt_images_count;
    List<Results> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_fabric_list_images);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        closeIcon = (ImageView) findViewById(R.id.closeIcon);
        txt_no_images = (TextView) findViewById(R.id.txt_no_images);
        txt_images_count = (TextView) findViewById(R.id.txt_images_count);
        Intent intent = getIntent();
        aricalno = intent.getStringExtra("article_no");
        txt_images_count.setVisibility(View.GONE);
        if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {

            apiCalls();
        } else {
            CommonUses.showToastwithDuration(5000, AppConfig.INTERNETFAILED_MESSAGE, getApplicationContext());

        }

       /* searchFabricListImagesAdapter = new SearchFabricListImagesAdapter(SearchFabricListImagesActivity.this, images);
        viewPager.setAdapter(searchFabricListImagesAdapter);*/
    }


    private void apiCalls() {

        // showProgress("Loading...");

        boolean flagnew = getgallaryApi(new NetworkCallbackM() {
            @Override
            public void success() {
                // hideProgress();
            }
        });


    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int imagePos = position + 1;
            txt_images_count.setText(String.valueOf(imagePos) + "/" + String.valueOf(imagesList.size()));
        }
    };


    private boolean getgallaryApi(final NetworkCallbackM networkCallbackM) {

        final boolean[] returnFlag = {false};
        HashMap<String, String> details = new HashMap<>();
        details.put("article_no", aricalno);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArticleGallery> call = apiService.getgallry(details);

        call.enqueue(new Callback<ArticleGallery>() {
            @Override
            public void onResponse(Call<ArticleGallery> call, Response<ArticleGallery> response) {

                try {
                    ArticleGallery fullresponse = response.body();

                    if (fullresponse.getResult() != null && fullresponse.getResult().size() >= 0) {
                        viewPager.setVisibility(View.VISIBLE);
                        txt_no_images.setVisibility(View.GONE);
                        txt_images_count.setVisibility(View.VISIBLE);
                        imagesList = fullresponse.getResult();
                        searchFabricListImagesAdapter = new SearchFabricListImagesAdapter(SearchFabricListImagesActivity.this, imagesList);
                        viewPager.setAdapter(searchFabricListImagesAdapter);
                        viewPager.setCurrentItem(0);
                        viewPager.addOnPageChangeListener(pageChangeListener);
                        pageChangeListener.onPageSelected(viewPager.getCurrentItem());
                        returnFlag[0] = true;
                        networkCallbackM.success();

                    } else {
                        viewPager.setVisibility(View.GONE);
                        txt_no_images.setVisibility(View.VISIBLE);
                        txt_images_count.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

               /* if (fullresponse.getResult().size() > 0) {
                    folderNameSearchableList.clear();
                    folderIdSearchableList.clear();

                    //Add values to list from Response JSON ARray
                    for (FoldersApiResponse.ResultBean singleObj : fullresponse.getResult()) {
                        folderNameSearchableList.add(new SampleSearchModel(singleObj.getFolderName(), singleObj.getFolderCode()));
                        folderIdSearchableList.add(new SampleSearchModel(singleObj.getFolderCode(), singleObj.getFolderCode()));
                    }*/


            }

            @Override
            public void onFailure(Call<ArticleGallery> call, Throwable t) {
                CommonUses.showToast(getApplicationContext(), AppConfig.ERROR_MESSAGE);
                returnFlag[0] = false;
                networkCallbackM.success();
            }
        });

        return returnFlag[0];


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
}


