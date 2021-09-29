package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.OrderStatusListApiResponse;
import com.rogerthat.rlvltd.com.fragment.InventoryListFragment;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import java.util.List;


public class OrderStatusListAdapter extends RecyclerView.Adapter<OrderStatusListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<OrderStatusListApiResponse.ResultBean> inventoryobjectsListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;
    private PreferenceHelper sharedpreference;
    String imageHostpathGolob;
    private String selectedArticleColorId;
    private String selectedArticlePatternId;
    private String selectedRangeId;
    private String selectedFolderNameId;
    private String selectedFolderNoId;
    String CatObj;
    private String selectedArticleColorName;
    private String selectedArticlePatternName;
    private String selectedRangeName;
    private String selectedFolderNameName;
    private String selectedFolderNoName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNoTV;
        public TextView ordertypeTv;
        public TextView orderDateTv;

        public TextView customerNameTV;
        public TextView invoiceAddressTv;
        public TextView deliveryAddressTv;

        /*public TextView articleNoTv3;
        public TextView orderedmtr3;
        public TextView dispatchedMtr3;

        public TextView invoiceNoTv;
        public TextView invoiceDtTv;
        public TextView courierNameTv;

        public TextView awbNoTv;
        public TextView dtOfDeliveryTV;*/



        public LinearLayout signleArticlerNumber;
//        public ImageView barcodeImage;

        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);

            orderNoTV = (TextView) view.findViewById(R.id.orderNoTV);
            ordertypeTv = (TextView) view.findViewById(R.id.ordertypeTv);
            orderDateTv = (TextView) view.findViewById(R.id.orderDateTv);

            customerNameTV = (TextView) view.findViewById(R.id.customerNameTV);
            invoiceAddressTv = (TextView) view.findViewById(R.id.invoiceAddressTv);
            deliveryAddressTv = (TextView) view.findViewById(R.id.deliveryAddressTv);


            signleArticlerNumber = (LinearLayout) view.findViewById(R.id.signleArticlerNumber);

        }
    }


    public OrderStatusListAdapter(Context mContext,
                                  List<OrderStatusListApiResponse.ResultBean> invenotyrListMain,
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
                    .inflate(R.layout.order_status_list_item_saleslogin, parent, false);
           /* itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_status_main_layout, parent, false);*/
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_status_list_item_saleslogin, parent, false);
            /*itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_status_main_layout, parent, false);*/
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.category_name.setText(inventoryobjectsListMain.get(position).getTitle().replaceAll("Module",""));

        holder.orderNoTV.setText(inventoryobjectsListMain.get(position).getOrderNo());
        holder.customerNameTV.setText(inventoryobjectsListMain.get(position).getCustomerName());
        holder.orderDateTv.setText(inventoryobjectsListMain.get(position).getOrderDate());
        holder.ordertypeTv.setText(inventoryobjectsListMain.get(position).getOrderType());
        holder.invoiceAddressTv.setText(inventoryobjectsListMain.get(position).getInvoiceAddress());
        holder.deliveryAddressTv.setText(inventoryobjectsListMain.get(position).getDeliveryAddress());

        sharedpreference = new PreferenceHelper(mContext, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();


        if (inventoryobjectsListMain.get(position).getArticleOrderded().size() > 0) {

            holder.signleArticlerNumber.setVisibility(View.VISIBLE);
            holder.signleArticlerNumber.removeAllViews();
            holder.signleArticlerNumber.invalidate();

            for (int i = 0; i < inventoryobjectsListMain.get(position).getArticleOrderded().size(); i++) {

                final OrderStatusListApiResponse.ResultBean.ArticleOrderdedBean singleObj = inventoryobjectsListMain.get(position).getArticleOrderded().get(i);
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = vi.inflate(R.layout.article_ordered_single_ln, null);

                TextView articleNoTv3 = (TextView) view.findViewById(R.id.articleNoTv3);
                TextView orderedmtr3 = (TextView) view.findViewById(R.id.orderedmtr3);
                TextView dispatchedMtr3 = (TextView) view.findViewById(R.id.dispatchedMtr3);

                TextView invoiceNoTv = (TextView) view.findViewById(R.id.invoiceNoTv);
                TextView invoiceDtTv = (TextView) view.findViewById(R.id.invoiceDtTv);
                TextView courierNameTv = (TextView) view.findViewById(R.id.courierNameTv);

                TextView awbNoTv = (TextView) view.findViewById(R.id.awbNoTv);
                TextView dtOfDeliveryTV = (TextView) view.findViewById(R.id.dtOfDeliveryTV);
                TextView remarksTv = (TextView) view.findViewById(R.id.remarksTv);
                TextView dueDateTv = (TextView) view.findViewById(R.id.dueDateTv);

                SpannableString content = new SpannableString(singleObj.getArticleNo());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                articleNoTv3.setText(content);
                orderedmtr3.setText(singleObj.getOrderedQuantity() + " " + singleObj.getOrderedMtrUnit());
                dispatchedMtr3.setText(singleObj.getDispatchedQuantity() + " " + singleObj.getDispatchedMtrUnit());
                invoiceNoTv.setText(singleObj.getInvoiceNo());
                invoiceDtTv.setText(singleObj.getInvoiceDate());
                courierNameTv.setText(singleObj.getCourierName());
                awbNoTv.setText(singleObj.getAWBNo());
                dtOfDeliveryTV.setText(singleObj.getDateofDelivery());
                remarksTv.setText(singleObj.getRemark());
                dueDateTv.setText(singleObj.getOrderDueDate());
                holder.signleArticlerNumber.addView(view);



                articleNoTv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(mContext, singleObj.getArticleNo(), Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("Article_No",singleObj.getArticleNo());
                        InventoryListFragment inventoryListFragment = new InventoryListFragment();
                        inventoryListFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, inventoryListFragment).addToBackStack(null).commit();
                    }
                });
            }

        }


        if (!isCustomerLogin) {
//            holder.biggestPieceAvail.setText(inventoryobjectsListMain.get(position).getBiggestPieceAvailable());
        }


        /*if (!inventoryobjectsListMain.get(position).getImage().equalsIgnoreCase("no_image.png")) {
            Glide
                    .with(mContext)
                    .load(imageHostpathGolob + inventoryobjectsListMain.get(position).getImage())
                    .placeholder(R.drawable.star)
                    .into(holder.productImage);

        }*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(inventoryobjectsListMain.get(position));
//                onItemSelectedListener.onSelected();


            }
        });


        /*holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inventoryobjectsListMain.get(position).getBigImage().equalsIgnoreCase("no_image.png")) {

                    if (!inventoryobjectsListMain.get(position).getBigImage().isEmpty()) {

                        try {
                            Dialog settingsDialog = new Dialog(mContext);
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

                            settingsDialog.show();
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
        });*/


        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }


    }

    private void saveDatainSP() {
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_ARTICLE_COLOR_ID, selectedArticleColorId);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_ARTICLE_PATTERN_ID, selectedArticlePatternId);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_RANGE_ID, selectedRangeId);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_FOLDERNAME_ID, selectedFolderNameId);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_FOLDERNO_ID, selectedFolderNoId);


        //CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_ARTICLE_NO_NAME, articleNumber.getText().toString().trim());
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_ARTICLE_COLOR_NAME, selectedArticleColorName);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_ARTICLE_PATTERN_NAME, selectedArticlePatternName);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_RANGE_NAME, selectedRangeName);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_FOLDERNAME_NAME, selectedFolderNameName);
        CommonUses.insertsharedpreference(sharedpreference, AppConfig.INVENTORY_FOLDERNO_NAME, selectedFolderNoName);
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