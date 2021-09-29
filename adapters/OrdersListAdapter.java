package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.OrderListApiResponse;

import java.util.List;


public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<OrderListApiResponse.ResultBean> orderListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        public TextView contactPersonName;
        public TextView customerName;
        public TextView orderNo;
        public TextView orderDate;
        public TextView totalArticles;
        public TextView totalMtrs;
        public TextView orderStatus;
//        public TextView tallyorderno;

//        public ImageView customerImage;

        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);
//            contactPersonName = (TextView) view.findViewById(R.id.contactPersonName);
            customerName = (TextView) view.findViewById(R.id.customerName);
            orderNo = (TextView) view.findViewById(R.id.orderNo);
            orderDate = (TextView) view.findViewById(R.id.orderDate);
            totalArticles = (TextView) view.findViewById(R.id.totalArticles);
            totalMtrs = (TextView) view.findViewById(R.id.totalMtrs);
            orderStatus=(TextView)view.findViewById(R.id.orderStatus);
//            tallyorderno = (TextView) view.findViewById(R.id.tallyorderno);

//            customerImage = (ImageView) view.findViewById(R.id.customerImage);

        }
    }


    public OrdersListAdapter(Context mContext,
                             List<OrderListApiResponse.ResultBean> customerListMain,
                             OnItemSelectedListener selectedListener, String imageHostPath, OnLoadMoreListener onLoadMoreListenerTemp, boolean isCustomerLogin) {

        this.mContext = mContext;
        this.orderListMain = customerListMain;
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
                .inflate(R.layout.order_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.category_name.setText(orderListMain.get(position).getTitle().replaceAll("Module",""));

//        holder.contactPersonName.setText(orderListMain.get(position).getContactPersonName());
        holder.customerName.setText(orderListMain.get(position).getCustomerName());
        holder.orderNo.setText(orderListMain.get(position).getOrderNo());
        holder.orderDate.setText(orderListMain.get(position).getOrderDate());
        holder.totalArticles.setText("Total Articles : " + orderListMain.get(position).getTotalArticles());
        holder.totalMtrs.setText("Total Meters : " + orderListMain.get(position).getTotalMtrs());

        if (!orderListMain.get(position).getOrderStatus().equalsIgnoreCase(""))
        {
            holder.orderStatus.setVisibility(View.VISIBLE);
            holder.orderStatus.setText(orderListMain.get(position).getOrderStatus());
        }
        else {
            holder.orderStatus.setVisibility(View.GONE);
        }

        /*if (!orderListMain.get(position).getTallyOrderNo().isEmpty()) {
            holder.tallyorderno.setText("(" + orderListMain.get(position).getTallyOrderNo() + ")");
        } else {
            holder.tallyorderno.setVisibility(View.GONE);
        }*/
        holder.orderNo.setText(orderListMain.get(position).getOrderNo());

        /*Glide
                .with(mContext)
                .load("")
                .placeholder(R.drawable.star)
                .into(holder.customerImage);*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(orderListMain.get(position));
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
        return orderListMain.size();
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