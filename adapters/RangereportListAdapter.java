package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.model.RangewiseSalesModel;

import java.util.List;

public class RangereportListAdapter extends RecyclerView.Adapter<RangereportListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<RangewiseSalesModel.ResultBean> inventoryobjectsListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    OnAllViewAddedListener onAllViewAddedListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_serachby;
        public TextView txt_serach_by_label;
        public TextView tvcustomerValue;
        public TextView range_sale_details_tv;
        public LinearLayout singleSamplingDetails;
        public LinearLayout samplingNA;


        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);

            txt_serachby = (TextView) view.findViewById(R.id.txt_serachby);
            txt_serach_by_label = (TextView) view.findViewById(R.id.txt_serach_by_label);
            tvcustomerValue = (TextView) view.findViewById(R.id.tvcustomerValue);
            range_sale_details_tv = (TextView) view.findViewById(R.id.range_sale_details_tv);
            singleSamplingDetails = (LinearLayout) view.findViewById(R.id.singleSamplingDetails);
            samplingNA = (LinearLayout) view.findViewById(R.id.samplingNA);


        }
    }


    public RangereportListAdapter(Context mContext,
                                  List<RangewiseSalesModel.ResultBean> invenotyrListMain,
                                  RangereportListAdapter.OnItemSelectedListener selectedListener,
                                  String imageHostPath,
                                  RangereportListAdapter.OnLoadMoreListener onLoadMoreListenerTemp,
                                  RangereportListAdapter.OnAllViewAddedListener onAllViewAddedListener,
                                  boolean isCustomerLogin) {
        this.mContext = mContext;
        this.inventoryobjectsListMain = invenotyrListMain;
        this.listner = selectedListener;
        this.imageHostpathGolob = imageHostPath;
        this.loadMoreListener = onLoadMoreListenerTemp;
        this.onAllViewAddedListener = onAllViewAddedListener;
        this.isCustomerLogin = isCustomerLogin;
    }


    public interface OnItemSelectedListener {
        void onSelected();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public interface OnAllViewAddedListener {
        void onAllViewAdded();
    }

    public void setLoadMoreListener(RangereportListAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setOnAllViewAddedListener(RangereportListAdapter.OnAllViewAddedListener onAllViewAddedListener) {
        this.onAllViewAddedListener = onAllViewAddedListener;
    }


    @Override
    public RangereportListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (isCustomerLogin) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sales_report_searchlist_header, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sales_report_searchlist_header, parent, false);
        }
        return new RangereportListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RangereportListAdapter.MyViewHolder holder, final int position) {

        try {
            holder.txt_serachby.setText(inventoryobjectsListMain.get(position).getSearchBy());
            holder.txt_serach_by_label.setText(inventoryobjectsListMain.get(position).getSearchByLable());
            holder.tvcustomerValue.setText(inventoryobjectsListMain.get(position).getSearchByName());
            holder.range_sale_details_tv.setText(inventoryobjectsListMain.get(position).getReportDetailsName());

            if (inventoryobjectsListMain.get(position).getResultDetails() != null) {
                if (inventoryobjectsListMain.get(position).getResultDetails().size() > 0) {
                    holder.samplingNA.setVisibility(View.GONE);
                    holder.singleSamplingDetails.setVisibility(View.VISIBLE);
                    holder.singleSamplingDetails.removeAllViews();
                    holder.singleSamplingDetails.invalidate();
                    for (int listCounter = 0; listCounter < inventoryobjectsListMain.get(position).getResultDetails().size(); listCounter++) {
                        final RangewiseSalesModel.ResultBean.ResultDetailsBean singleObj = inventoryobjectsListMain.get(position).getResultDetails().get(listCounter);
                        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = vi.inflate(R.layout.rangewise_sales_search_bottom_1, null);
                        LinearLayout detailRow = (LinearLayout) view.findViewById(R.id.detailRow);
                        LinearLayout tablerow = (LinearLayout) view.findViewById(R.id.tablerow);
                        TextView tv_range_value = (TextView) view.findViewById(R.id.tv_range_value);
                        TextView tv_customername_value = (TextView) view.findViewById(R.id.tv_customername_value);
                        TextView tv_total_meter = (TextView) view.findViewById(R.id.tv_total_meter);
                        TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
                        TextView txt_mtr = (TextView) view.findViewById(R.id.txt_mtr);
                        TextView txt_range = (TextView) view.findViewById(R.id.txt_range);
                        TextView txt_customername = (TextView) view.findViewById(R.id.txt_customername);
                        LinearLayout linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
                        tv_detail.setText(inventoryobjectsListMain.get(position).getReportDetailsName());
                        tv_range_value.setText(singleObj.getRangeName());
                        tv_customername_value.setText(singleObj.getCustomerClmName());


                        if (listCounter == 0) {
                            linear_layout.setVisibility(View.VISIBLE);
                            tv_detail.setVisibility(View.VISIBLE);
                            detailRow.setVisibility(View.VISIBLE);

                        } else {
                            linear_layout.setVisibility(View.GONE);
                            tv_detail.setVisibility(View.GONE);
                            detailRow.setVisibility(View.GONE);
                        }
                        Log.d("i  out loop -->>", String.valueOf(listCounter));
                        Log.d("size befo out loop-->>", String.valueOf(inventoryobjectsListMain.size()));

                    /*for the last row set the property bold*/
                        if (listCounter == inventoryobjectsListMain.get(position).getResultDetails().size() - 1) {
                            Log.d("i  in loop", String.valueOf(listCounter));
                            Log.d("size befo in loop-->>", String.valueOf(inventoryobjectsListMain.size()));
                            tv_range_value.setTypeface(tv_range_value.getTypeface(), Typeface.BOLD);
                            tv_customername_value.setTypeface(tv_customername_value.getTypeface(), Typeface.BOLD);
                            tv_total_meter.setTypeface(tv_total_meter.getTypeface(), Typeface.BOLD);
                        }
                    /*chnages in condition  6/8/18 commit by akash matlani  && inventoryobjectsListMain.get(position).getShowTotMtrClm().equalsIgnoreCase("1")*/
                        if (inventoryobjectsListMain.get(position).getShowCustomerClm().equalsIgnoreCase("1")) {

                            //“ShowCustomerClm” is 1,then show “Customer Name” column and hide ”Range” column.
                            txt_customername.setVisibility(View.VISIBLE);
                            txt_range.setVisibility(View.GONE);
                            txt_mtr.setVisibility(View.VISIBLE);

                            tv_total_meter.setVisibility(View.VISIBLE);
                            tv_range_value.setVisibility(View.GONE);
                            tv_customername_value.setVisibility(View.VISIBLE);
                            tv_total_meter.setText(singleObj.getTotalMTR());

                        } else {

                            //when we get “ShowCustomerClm” is 0 that time show ”Range” column and hide “Customer Name” column
                            txt_customername.setVisibility(View.GONE);
                            txt_range.setVisibility(View.VISIBLE);
                            txt_mtr.setVisibility(View.VISIBLE);
                            tv_total_meter.setVisibility(View.VISIBLE);
                            tv_range_value.setVisibility(View.VISIBLE);
                            tv_customername_value.setVisibility(View.GONE);
                            tv_total_meter.setText(singleObj.getTotalMTR());
                            detailRow.removeView(txt_customername);
                            tablerow.removeView(tv_customername_value);

                        }
                        holder.singleSamplingDetails.addView(view);
                    }
                    onAllViewAddedListener.onAllViewAdded();
                    //tv_detail.setText(inventoryobjectsListMain.get(position).getReportDetailName());

                } else {

                    holder.samplingNA.setVisibility(View.VISIBLE);
                }
            } else {
                holder.samplingNA.setVisibility(View.VISIBLE);
            }

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
        } catch (Exception e) {
            e.printStackTrace();
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


