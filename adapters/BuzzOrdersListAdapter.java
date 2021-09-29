package com.rogerthat.rlvltd.com.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.DownloadOrderResponse;
import com.rogerthat.rlvltd.com.apiresponsemodels.OrderListApiResponse;
import com.rogerthat.rlvltd.com.callback.DownloadFileURL;
import com.rogerthat.rlvltd.com.util.ApiClient;
import com.rogerthat.rlvltd.com.util.ApiInterface;
import com.rogerthat.rlvltd.com.util.AppConfig;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.util.PreferenceHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class BuzzOrdersListAdapter extends RecyclerView.Adapter<BuzzOrdersListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<OrderListApiResponse.ResultBean> orderListMain;
    OnItemSelectedListener listner;
    OnShareSelectedListener shareListener;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;
    private PreferenceHelper sharedpreference;

    String imageHostpathGolob;
    ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        public TextView contactPersonName;
        public TextView customerName;
        public TextView orderNo;
        public TextView orderDate;
        public TextView totalArticles;
        public TextView totalMtrs;
        public TextView orderStatus;
        public ImageView share_icon;
        public ImageView edit_icon;
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
            orderStatus = (TextView) view.findViewById(R.id.orderStatus);
            share_icon = (ImageView) view.findViewById(R.id.share_icon);
            edit_icon = (ImageView) view.findViewById(R.id.edit_icon);
//            tallyorderno = (TextView) view.findViewById(R.id.tallyorderno);
//            customerImage = (ImageView) view.findViewById(R.id.customerImage);


            PreferenceHelper sharedpreference = new PreferenceHelper(mContext, AppConfig.SHAREDPREFERENCE_STRING);
            sharedpreference.initPref();

            String isOrderstatusPermission = sharedpreference.LoadStringPref(AppConfig.IS_ORDER_STATUS, AppConfig.IS_ORDER_STATUS);
            if (!isOrderstatusPermission.equalsIgnoreCase("True")) {
                edit_icon.setVisibility(View.GONE);
            } else {
                edit_icon.setVisibility(View.VISIBLE);
            }

        }
    }


    public BuzzOrdersListAdapter(Context mContext,
                                 List<OrderListApiResponse.ResultBean> customerListMain,
                                 OnItemSelectedListener selectedListener, String imageHostPath,
                                 OnLoadMoreListener onLoadMoreListenerTemp,
                                 OnShareSelectedListener onShareSelectedListener,
                                 boolean isCustomerLogin) {
        this.mContext = mContext;
        this.orderListMain = customerListMain;
        this.listner = selectedListener;
        this.imageHostpathGolob = imageHostPath;
        this.loadMoreListener = onLoadMoreListenerTemp;
        this.isCustomerLogin = isCustomerLogin;
        this.shareListener = onShareSelectedListener;

    }


    public interface OnItemSelectedListener {
        void onSelected();

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnShareSelectedListener {
        void onShareChoosed(OrderListApiResponse.ResultBean singleObj);

        void onEditClicked(OrderListApiResponse.ResultBean singleObj, int position);

    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buzz_order_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.customerName.setText(orderListMain.get(position).getCustomerName());
        holder.orderNo.setText(orderListMain.get(position).getOrderNo());
        holder.orderDate.setText(orderListMain.get(position).getOrderDate());
        holder.totalArticles.setText("Articles : " + orderListMain.get(position).getTotalArticles());
        holder.totalMtrs.setText("Mtrs : " + orderListMain.get(position).getTotalMtrs());
        if (!orderListMain.get(position).getOrderStatus().equalsIgnoreCase("")) {
            holder.orderStatus.setVisibility(View.VISIBLE);
            holder.orderStatus.setText(orderListMain.get(position).getOrderStatus());
        } else {
            holder.orderStatus.setVisibility(View.GONE);
        }
        String statusId = orderListMain.get(position).getIStatusId();
        //yellow_bg
        if (statusId.equalsIgnoreCase("5")) {
            holder.orderStatus.setText("Saved");
            holder.orderStatus.getBackground().setColorFilter(Color.parseColor("#fdd835"), PorterDuff.Mode.SRC_ATOP);
            /*if(color_hashcode.equalsIgnoreCase(selectedHaxBGColor)){
                holder.color_ic.setImageResource(R.drawable.ic_done_white_24dp);
            }*/
        } else if (statusId.equalsIgnoreCase("0")) {
            holder.orderStatus.setText("Submitted");
            holder.orderStatus.getBackground().setColorFilter(Color.parseColor("#7fd460"), PorterDuff.Mode.SRC_ATOP);
        } else if (statusId.equalsIgnoreCase("4")) {
            holder.orderStatus.setText("Cancelled");
            holder.orderStatus.getBackground().setColorFilter(Color.parseColor("#ea3535"), PorterDuff.Mode.SRC_ATOP);
        }
        holder.orderNo.setText(orderListMain.get(position).getOrderNo());
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
        holder.share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareListener.onShareChoosed(orderListMain.get(position));

                /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        mContext,
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Download");
                arrayAdapter.add("Email");
                builderSingle.setNegativeButton(
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                String strName = arrayAdapter.getItem(position);
                                //Download
                                if (position == 0) {
                                    callDownloadAPI(orderListMain.get(position).getCustomerEmail(), orderListMain.get(position).getIOrderId());
                                }
                                //Email
                                if (position == 1) {
                                    sendEMAILAPI(orderListMain.get(position).getCustomerEmail(), orderListMain.get(position).getIOrderId());
                                }
                            }
                        });
                builderSingle.show();*/

            }
        });


        holder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareListener.onEditClicked(orderListMain.get(position), position);

                /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        mContext,
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Download");
                arrayAdapter.add("Email");
                builderSingle.setNegativeButton(
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                String strName = arrayAdapter.getItem(position);
                                //Download
                                if (position == 0) {
                                    callDownloadAPI(orderListMain.get(position).getCustomerEmail(), orderListMain.get(position).getIOrderId());
                                }
                                //Email
                                if (position == 1) {
                                    sendEMAILAPI(orderListMain.get(position).getCustomerEmail(), orderListMain.get(position).getIOrderId());
                                }
                            }
                        });
                builderSingle.show();*/

            }
        });


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