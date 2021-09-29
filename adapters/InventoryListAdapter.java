package com.rogerthat.rlvltd.com.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.Activities.ImageDetailViewActivity;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.InventorySearchApiResponse;
import com.rogerthat.rlvltd.com.util.CommonUses;

import java.util.List;


public class InventoryListAdapter extends RecyclerView.Adapter<InventoryListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<InventorySearchApiResponse.ResultBean> inventoryobjectsListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView articleNoTv;
        public TextView biggestPieceAvail;
        public TextView indiaStatusTv;
        public TextView indiarefillStatus;
        public TextView globalStatusTv;
        public TextView globalRefillStatusTv;
        public TextView rangeTv;
        public TextView folderNameTv;
        public TextView folderNoTv;
        public TextView categoryTv;
        public TextView colorTv;
        public TextView patternTv;
        public TextView contentTv;
        public ImageView productImage;
        public ImageView questionMark;
        public LinearLayout maxPieceLinear;


//        public ImageView barcodeImage;

        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);
            articleNoTv = (TextView) view.findViewById(R.id.articleNoTv);
            biggestPieceAvail = (TextView) view.findViewById(R.id.biggestPieceAvail);
            indiaStatusTv = (TextView) view.findViewById(R.id.indiaStatusTv);
            indiarefillStatus = (TextView) view.findViewById(R.id.indiarefillStatus);
            globalStatusTv = (TextView) view.findViewById(R.id.globalStatusTv);
            globalRefillStatusTv = (TextView) view.findViewById(R.id.globalRefillStatusTv);
            rangeTv = (TextView) view.findViewById(R.id.rangeTv);
            folderNameTv = (TextView) view.findViewById(R.id.folderNameTv);
            folderNoTv = (TextView) view.findViewById(R.id.folderNoTv);
            categoryTv = (TextView) view.findViewById(R.id.categoryTv);
            colorTv = (TextView) view.findViewById(R.id.colorTv);
            patternTv = (TextView) view.findViewById(R.id.patternTv);
            contentTv = (TextView) view.findViewById(R.id.contentTv);
            productImage = (ImageView) view.findViewById(R.id.imageView);
            questionMark = (ImageView) view.findViewById(R.id.questionMark);
            maxPieceLinear = (LinearLayout) view.findViewById(R.id.maxPieceLinear);
        }
    }


    public InventoryListAdapter(Context mContext,
                                List<InventorySearchApiResponse.ResultBean> invenotyrListMain,
                                OnItemSelectedListener selectedListener, String imageHostPath, OnLoadMoreListener onLoadMoreListenerTemp, boolean isCustomerLogin) {

        this.mContext = mContext;
        this.inventoryobjectsListMain = invenotyrListMain;
        this.listner = selectedListener;
        this.imageHostpathGolob = imageHostPath;
        this.loadMoreListener = onLoadMoreListenerTemp;
        this.isCustomerLogin = isCustomerLogin;

    }


    public interface OnItemSelectedListener {
        void onSelected();

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        if (isCustomerLogin) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inventory_list_item_customerlogin, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inventory_list_item_saleslogin, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.category_name.setText(inventoryobjectsListMain.get(position).getTitle().replaceAll("Module",""));

        holder.articleNoTv.setText(inventoryobjectsListMain.get(position).getItemNo());
        holder.indiaStatusTv.setText(inventoryobjectsListMain.get(position).getIndiaStatus());
        holder.indiarefillStatus.setText(inventoryobjectsListMain.get(position).getIndiaBackByDate());
        holder.globalStatusTv.setText(inventoryobjectsListMain.get(position).getGlobalStatus());
        holder.globalRefillStatusTv.setText(inventoryobjectsListMain.get(position).getGlobalBackByDate());
        holder.rangeTv.setText(inventoryobjectsListMain.get(position).getRange());
        holder.folderNameTv.setText(inventoryobjectsListMain.get(position).getFolderName());
        holder.folderNoTv.setText(inventoryobjectsListMain.get(position).getFolderNo());
        holder.categoryTv.setText(inventoryobjectsListMain.get(position).getCategory());
        holder.colorTv.setText(inventoryobjectsListMain.get(position).getColor());
        holder.patternTv.setText(inventoryobjectsListMain.get(position).getPattern());
        holder.contentTv.setText(inventoryobjectsListMain.get(position).getContent());


        if (!isCustomerLogin) {
            holder.biggestPieceAvail.setText(inventoryobjectsListMain.get(position).getBiggestPieceAvailable());
        }


        if (!inventoryobjectsListMain.get(position).getImage().equalsIgnoreCase("no_image.png")) {
            Glide
                    .with(mContext)
                    .load(imageHostpathGolob + inventoryobjectsListMain.get(position).getImage())
                    .placeholder(R.drawable.default_new_img)
                    .into(holder.productImage);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(inventoryobjectsListMain.get(position));
//                onItemSelectedListener.onSelected();


            }
        });


        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inventoryobjectsListMain.get(position).getBigImage().equalsIgnoreCase("no_image.png")) {

                    if (!inventoryobjectsListMain.get(position).getBigImage().isEmpty()) {

                        try {
                            /*Dialog settingsDialog = new Dialog(mContext);
                            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            settingsDialog.setContentView(LayoutInflater.from(mContext).inflate(R.layout.image_layout, null));
                            ImageView dialogImage = (ImageView) settingsDialog.findViewById(R.id.dialogImage);
                            final TextView loadingText = (TextView) settingsDialog.findViewById(R.id.loadingText);


                            *//* Glide
                            .with(mContext)
                            .load(imageHostpathGolob + inventoryobjectsListMain.get(position).getBigImage())
                            .placeholder(R.drawable.star)
                            .into(dialogImage);*//*

                            Glide.with(mContext)
                                    .load(imageHostpathGolob + inventoryobjectsListMain.get(position).getBigImage())
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            loadingText.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            loadingText.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .placeholder(R.drawable.star)
                                    .into(dialogImage);

                            settingsDialog.show();*/


                            Intent in = new Intent(mContext, ImageDetailViewActivity.class);
                            in.putExtra("URL", imageHostpathGolob + inventoryobjectsListMain.get(position).getBigImage());
                            mContext.startActivity(in);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUses.showToastwithDuration(2500, "No Big Image is Available.", mContext);
                    }

                } else {
                    CommonUses.showToastwithDuration(2500, "No Big Image is Available.", mContext);

                }


            }
        });


        holder.questionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Dialog settingsDialog = new Dialog(mContext);
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    settingsDialog.setContentView(LayoutInflater.from(mContext).inflate(R.layout.image_layout, null));
                    ImageView dialogImage = (ImageView) settingsDialog.findViewById(R.id.dialogImage);
                    final TextView loadingText = (TextView) settingsDialog.findViewById(R.id.loadingText);


                    Glide.with(mContext)
                            .load("http://portal.rogerlaviale.com/storage/criteria_status.png")
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    loadingText.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    loadingText.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .placeholder(R.drawable.star)
                            .into(dialogImage);

                    settingsDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        String mainIMageURL = inventoryobjectsListMain.get(position).getImage();
        if (mainIMageURL.equalsIgnoreCase("no_image.png")) {
            holder.productImage.setVisibility(View.GONE);
        } else {
            holder.productImage.setVisibility(View.VISIBLE);
        }

    }

    public String getSingleObj() {
        return singleObj;
    }


    @Override
    public int getItemCount() {
//        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
        return inventoryobjectsListMain.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }


}