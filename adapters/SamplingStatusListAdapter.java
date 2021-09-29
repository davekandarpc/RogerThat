package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.SamplingStatusListApiResponse;

import java.util.List;

public class SamplingStatusListAdapter extends RecyclerView.Adapter<SamplingStatusListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<SamplingStatusListApiResponse.ResultBean> inventoryobjectsListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ssTypeTv;
        public TextView ssCustomerNameTv;
        public TextView ssAddressTv;

        public TextView ssReqCodeTv;
        public TextView ssReqDateTv;
        public TextView ssStatusTv;


        public LinearLayout samplingNA;
        public LinearLayout singleSamplingDetails;
//        public ImageView barcodeImage;

        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);

            ssTypeTv = (TextView) view.findViewById(R.id.ssTypeTv);
            ssCustomerNameTv = (TextView) view.findViewById(R.id.ssCustomerNameTv);
            ssAddressTv = (TextView) view.findViewById(R.id.ssAddressTv);

            ssReqCodeTv = (TextView) view.findViewById(R.id.ssReqCodeTv);
            ssReqDateTv = (TextView) view.findViewById(R.id.ssReqDateTv);
            ssStatusTv = (TextView) view.findViewById(R.id.ssStatusTv);

            singleSamplingDetails = (LinearLayout) view.findViewById(R.id.singleSamplingDetails);
            samplingNA = (LinearLayout) view.findViewById(R.id.samplingNA);



        }
    }


    public SamplingStatusListAdapter(Context mContext,
                                     List<SamplingStatusListApiResponse.ResultBean> invenotyrListMain,
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
                    .inflate(R.layout.sampling_status_list_item_saleslogin, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sampling_status_list_item_saleslogin, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.category_name.setText(inventoryobjectsListMain.get(position).getTitle().replaceAll("Module",""));

        holder.ssTypeTv.setText(inventoryobjectsListMain.get(position).getType());
        holder.ssCustomerNameTv.setText(inventoryobjectsListMain.get(position).getName());
        holder.ssAddressTv.setText(inventoryobjectsListMain.get(position).getAddress());
        holder.ssReqCodeTv.setText(inventoryobjectsListMain.get(position).getRequestCode());
        holder.ssReqDateTv.setText(inventoryobjectsListMain.get(position).getRequestDate());
        holder.ssStatusTv.setText(inventoryobjectsListMain.get(position).getStatus());

        if (inventoryobjectsListMain.get(position).getSamplingDetails() != null) {

            if (inventoryobjectsListMain.get(position).getSamplingDetails().size() > 0) {
                holder.samplingNA.setVisibility(View.GONE);


                holder.singleSamplingDetails.setVisibility(View.VISIBLE);
                holder.singleSamplingDetails.removeAllViews();
                holder.singleSamplingDetails.invalidate();

                for (int i = 0; i < inventoryobjectsListMain.get(position).getSamplingDetails().size(); i++) {

                    final SamplingStatusListApiResponse.ResultBean.SamplingDetailsBean singleObj = inventoryobjectsListMain.get(position).getSamplingDetails().get(i);
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = vi.inflate(R.layout.ss_single_ln, null);


                    TextView requestForTv = (TextView) view.findViewById(R.id.requestForTv);
                    TextView itemNameTv = (TextView) view.findViewById(R.id.itemNameTv);
                    TextView reqQtyTv = (TextView) view.findViewById(R.id.reqQtyTv);

                    TextView remarkTv = (TextView) view.findViewById(R.id.remarkTv);
                    TextView dispatchQtyTv = (TextView) view.findViewById(R.id.dispatchQtyTv);
                    TextView dispatchDtTv = (TextView) view.findViewById(R.id.dispatchDtTv);

                    TextView awbNoTv = (TextView) view.findViewById(R.id.awbNoTv);
                    TextView courierNameTv = (TextView) view.findViewById(R.id.courierNameTv);
                    TextView closingRmkTv = (TextView) view.findViewById(R.id.closingRmkTv);


                    requestForTv.setText(singleObj.getRequestFor());
                    itemNameTv.setText(singleObj.getItemName());
                    reqQtyTv.setText(singleObj.getRequestQty());
                    remarkTv.setText(singleObj.getRemark());
                    dispatchQtyTv.setText(singleObj.getDispatchQty());
                    dispatchDtTv.setText(singleObj.getDispatchDate());
                    awbNoTv.setText(singleObj.getAWBNo());
                    courierNameTv.setText(singleObj.getCourierName());
                    closingRmkTv.setText(singleObj.getClosingRemark());
//                partDispatchTv.setText(singleObj.getd);
                    holder.singleSamplingDetails.addView(view);


                }

            }else {

                holder.samplingNA.setVisibility(View.VISIBLE);

            }
        }else {

            holder.samplingNA.setVisibility(View.VISIBLE);

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