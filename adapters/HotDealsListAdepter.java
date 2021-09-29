package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.Activities.ImageDetailViewActivity;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.HotDealDetailsApiResponse;
import com.rogerthat.rlvltd.com.apiresponsemodels.OrderListApiResponse;
import com.rogerthat.rlvltd.com.fragment.InventoryListFragment;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import java.util.List;

/**
 * Created by kandarp3 on 18/03/20.
 */

public class HotDealsListAdepter extends RecyclerView.Adapter<HotDealsListAdepter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<HotDealDetailsApiResponse.ResultBean> hotdealDetailsApiResponse;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;
    private PreferenceHelper sharedpreference;
    String imageHostpathGolob;

    public HotDealsListAdepter(Context mContext,
                               List<HotDealDetailsApiResponse.ResultBean> hotdealDetailsApiResponse,
                               OnItemSelectedListener listner,
                               OnLoadMoreListener loadMoreListener,
                               boolean isCustomerLogin,
                               String imageHostpathGolob) {
        this.mContext = mContext;
        this.hotdealDetailsApiResponse = hotdealDetailsApiResponse;
        this.listner = listner;
        this.loadMoreListener = loadMoreListener;
        this.isCustomerLogin = isCustomerLogin;
        this.imageHostpathGolob = imageHostpathGolob;
    }


    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = null;
        if (isCustomerLogin) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hot_deals_items_layout, parent, false);
           /* itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_status_main_layout, parent, false);*/
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.hot_deals_items_layout, parent, false);
            /*itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_status_main_layout, parent, false);*/
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.dealName.setText(hotdealDetailsApiResponse.get(position).getHotDealsName());
        holder.categoryContent.setText(hotdealDetailsApiResponse.get(position).getContentCategoryName());
        holder.discount.setText(hotdealDetailsApiResponse.get(position).getDiscount());
        holder.remark.setText(hotdealDetailsApiResponse.get(position).getRemarks());


        sharedpreference = new PreferenceHelper(mContext, AppConfig.SHAREDPREFERENCE_STRING);
        sharedpreference.initPref();

       // Log.i("TAG_SIGNARTICE", String.valueOf(hotdealDetailsApiResponse.get(position).getHotdealDetails().size()));

        if(hotdealDetailsApiResponse.get(position).getHotdealDetails().size() > 0){

            holder.signleArticlerNumber.setVisibility(View.VISIBLE);
            holder.signleArticlerNumber.removeAllViews();
            holder.signleArticlerNumber.invalidate();


            for(int i = 0; i < hotdealDetailsApiResponse.get(position).getHotdealDetails().size() ; i++){

                final HotDealDetailsApiResponse.ResultBean.HotdealDetails singleObj = hotdealDetailsApiResponse.get(position).getHotdealDetails().get(i);
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = vi.inflate(R.layout.hot_deals_single_layout, null);

                TextView article_no = (TextView) view.findViewById(R.id.articleNoTv3);
                TextView orignal_price = (TextView) view.findViewById(R.id.orignal_price_mtr3);
                TextView discount_price = (TextView) view.findViewById(R.id.discount_price_Tv);
                TextView total_Mtr = (TextView) view.findViewById(R.id.totalMtr3);
                TextView range = (TextView) view.findViewById(R.id.range_Tv);
                TextView color = (TextView) view.findViewById(R.id.color_Tv);
                TextView pattern = (TextView) view.findViewById(R.id.pattern_Tv);
                TextView piece_length = (TextView) view.findViewById(R.id.piece_length_Tv);
                ImageView artic_Image = (ImageView) view.findViewById(R.id.imageView_hot);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.imgelinearlayoutid);
                SpannableString content = new SpannableString(singleObj.getArticleCode());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                article_no.setText(content);
                orignal_price.setText(singleObj.getOriginalPrice());
                discount_price.setText(singleObj.getDiscountedPrice());
                total_Mtr.setText(singleObj.getTotalMtr());
                range.setText(singleObj.getRange());
                color.setText(singleObj.getColor());
                pattern.setText(singleObj.getPattern());
                piece_length.setText(singleObj.getPieceLengths());



                holder.signleArticlerNumber.addView(view);
                //Log.i("SET_VISIBILITY", String.valueOf(content));
                article_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Article_No",singleObj.getArticleCode());
                        InventoryListFragment inventoryListFragment = new InventoryListFragment();
                        inventoryListFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, inventoryListFragment).addToBackStack(null).commit();
                    }
                });

                if(singleObj.getArticleImage() != null){
                    Glide
                            .with(mContext)
                            .load(imageHostpathGolob + singleObj.getArticleImage())
                            .placeholder(R.drawable.default_new_img)
                            .into(artic_Image);

                }else{
                    linearLayout.setVisibility(View.GONE);
                }

                artic_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(mContext, ImageDetailViewActivity.class);
                        in.putExtra("URL", imageHostpathGolob + singleObj.getArticleImageBigImage());
                        mContext.startActivity(in);
                    }
                });
            }


        }





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(hotdealDetailsApiResponse.get(position));
//                onItemSelectedListener.onSelected();


            }
        });


        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

    }

    @Override
    public int getItemCount() {

        return hotdealDetailsApiResponse.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView dealName;
        public TextView categoryContent;
        public TextView discount;

        public TextView remark;
        public LinearLayout signleArticlerNumber;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dealName = itemView.findViewById(R.id.dealNameTv);
            categoryContent = itemView.findViewById(R.id.contebtcategoryTv);
            discount = itemView.findViewById(R.id.discountTv);
            remark = itemView.findViewById(R.id.remarkTv);
            signleArticlerNumber = (LinearLayout) itemView.findViewById(R.id.signleArticlerNumber_hot);
        }
    }

    public String getSingleObj() {
        return singleObj;
    }

    public interface OnItemSelectedListener {
        void onSelected();

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


}
