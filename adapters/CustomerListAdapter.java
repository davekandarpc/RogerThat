package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.CustomerListApiResponse;

import java.util.List;


public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<CustomerListApiResponse.ResultBean> customerListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contactPersonName;
        public TextView companyName;
        public TextView mobileEmail;
        public TextView cityStateCountry;
        public ImageView customerImage;

        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);
            contactPersonName = (TextView) view.findViewById(R.id.contactPersonName);
            companyName = (TextView) view.findViewById(R.id.companyName);
            mobileEmail = (TextView) view.findViewById(R.id.mobileEmail);
            cityStateCountry = (TextView) view.findViewById(R.id.cityStateCountry);
            customerImage = (ImageView) view.findViewById(R.id.customerImage);

        }
    }


    public CustomerListAdapter(Context mContext,
                               List<CustomerListApiResponse.ResultBean> customerListMain,
                               OnItemSelectedListener selectedListener, String imageHostPath, OnLoadMoreListener onLoadMoreListenerTemp, boolean isCustomerLogin) {

        this.mContext = mContext;
        this.customerListMain = customerListMain;
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

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.category_name.setText(customerListMain.get(position).getTitle().replaceAll("Module",""));

        String mobileEmailStr = "", cityStateStr = "";

        if (CheckForNullOrBlankString(customerListMain.get(position).getMobileNo())) {
            mobileEmailStr = customerListMain.get(position).getMobileNo();
        }
        if (!mobileEmailStr.isEmpty()) {
            if (CheckForNullOrBlankString(customerListMain.get(position).getEmailPrimary())) {
                mobileEmailStr = mobileEmailStr + " | " + customerListMain.get(position).getEmailPrimary();
            }
        } else {
            mobileEmailStr = customerListMain.get(position).getEmailPrimary();
        }


        //if mobile or email is available
        if (!mobileEmailStr.isEmpty()) {

            if (CheckForNullOrBlankString(customerListMain.get(position).getCity())) {
                cityStateStr = customerListMain.get(position).getCity();
            }
            if (!cityStateStr.isEmpty()) {
                if (CheckForNullOrBlankString(customerListMain.get(position).getState())) {
                    cityStateStr = cityStateStr + ", " + customerListMain.get(position).getState();
                }
                if (CheckForNullOrBlankString(customerListMain.get(position).getCountry())) {
                    cityStateStr = cityStateStr + ", " + customerListMain.get(position).getCountry();
                }
            } else {
                if (CheckForNullOrBlankString(customerListMain.get(position).getState())) {
                    cityStateStr = customerListMain.get(position).getState();
                }
                if (CheckForNullOrBlankString(cityStateStr)) {
                    if (CheckForNullOrBlankString(customerListMain.get(position).getCountry()))
                        cityStateStr = cityStateStr + ", " + customerListMain.get(position).getCountry();
                }else {
                    if (CheckForNullOrBlankString(customerListMain.get(position).getCountry()))
                        cityStateStr = cityStateStr + ", " + customerListMain.get(position).getCountry();
                }
            }


        } else {
            //if mobile or email is not available
            if (CheckForNullOrBlankString(customerListMain.get(position).getCity())) {
                mobileEmailStr = customerListMain.get(position).getCity();
            }
            if (!cityStateStr.isEmpty()) {
                if (CheckForNullOrBlankString(customerListMain.get(position).getState())) {
                    mobileEmailStr = cityStateStr + ", " + customerListMain.get(position).getState();
                }
                if (CheckForNullOrBlankString(customerListMain.get(position).getCountry())) {
                    mobileEmailStr = cityStateStr + ", " + customerListMain.get(position).getCountry();
                }
            } else {
                if (CheckForNullOrBlankString(customerListMain.get(position).getState())) {
                    mobileEmailStr = customerListMain.get(position).getState();
                }
                if (CheckForNullOrBlankString(cityStateStr)) {
                    if (CheckForNullOrBlankString(customerListMain.get(position).getCountry()))
                        mobileEmailStr = mobileEmailStr + ", " + customerListMain.get(position).getCountry();
                }else {
                    if (CheckForNullOrBlankString(customerListMain.get(position).getCountry()))
                        mobileEmailStr = mobileEmailStr + ", " + customerListMain.get(position).getCountry();
                }
            }

        }

        holder.contactPersonName.setText(customerListMain.get(position).getContactPersonName());
        holder.companyName.setText(customerListMain.get(position).getCompanyName());
        holder.mobileEmail.setText(mobileEmailStr);
        if (!cityStateStr.isEmpty())
            holder.cityStateCountry.setText(cityStateStr);
        else
            holder.cityStateCountry.setVisibility(View.GONE);


        Glide
                .with(mContext)
                .load(imageHostpathGolob + customerListMain.get(position).getVLargeImage())
                .placeholder(R.drawable.star)
                .into(holder.customerImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(customerListMain.get(position));
                listner.onSelected();


            }
        });


        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }


    }

    public String getSingleObj() {
        return singleObj;
    }


    @Override
    public int getItemCount() {
//        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
        return customerListMain.size();
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


    public boolean CheckForNullOrBlankString(String validateStr) {
        boolean returnStr = false;

        if (validateStr != null) {
            validateStr = validateStr.replaceAll("null", "");
            if (validateStr != null) {
                if (!validateStr.isEmpty()) {
                    //if this string is not empty then will return true else false for null,blank or empty string
                    returnStr = true;
                } else {
                    returnStr = false;
                }
            } else {
                returnStr = false;
            }

        }

        return returnStr;
    }

}