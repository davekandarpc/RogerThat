package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.model.CustomerSelcectionModel;

import java.util.List;

public class SalesReportSearchListAdapter extends RecyclerView.Adapter<SalesReportSearchListAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<CustomerSelcectionModel.Result> inventoryobjectsListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvReportTypeValue;
        public TextView tvsalesUserValue;
        public TextView tvcustomerValue;

        public TextView txt_serachby;
        public TextView txt_serach_by_label;
        public LinearLayout samplingNA;
        public LinearLayout singleSamplingDetails;
//        public ImageView barcodeImage;

        public MyViewHolder(View view) {
            super(view);
//            category_pic = (ImageView) view.findViewById(R.id.category_pic);

            txt_serachby = (TextView) view.findViewById(R.id.txt_serachby);
            txt_serach_by_label = (TextView) view.findViewById(R.id.txt_serach_by_label);
            tvReportTypeValue = (TextView) view.findViewById(R.id.tvReportTypeValue);
            tvsalesUserValue = (TextView) view.findViewById(R.id.tvsalesUserValue);
            tvcustomerValue = (TextView) view.findViewById(R.id.tvcustomerValue);

            singleSamplingDetails = (LinearLayout) view.findViewById(R.id.singleSamplingDetails);
            samplingNA = (LinearLayout) view.findViewById(R.id.samplingNA);


        }
    }


    public SalesReportSearchListAdapter(Context mContext,
                                        List<CustomerSelcectionModel.Result> invenotyrListMain,
                                        SalesReportSearchListAdapter.OnItemSelectedListener selectedListener, String imageHostPath, SalesReportSearchListAdapter.OnLoadMoreListener onLoadMoreListenerTemp, boolean isCustomerLogin) {
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

    public void setLoadMoreListener(SalesReportSearchListAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public SalesReportSearchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (isCustomerLogin) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sales_report_searchlist_header, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sales_report_searchlist_header, parent, false);
        }
        return new SalesReportSearchListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SalesReportSearchListAdapter.MyViewHolder holder, final int position) {
        holder.txt_serach_by_label.setText(inventoryobjectsListMain.get(position).getSearchByLable());
        holder.txt_serachby.setText(inventoryobjectsListMain.get(position).getSearchBy());
        holder.tvReportTypeValue.setText(inventoryobjectsListMain.get(position).getReportTypeName());
        holder.tvsalesUserValue.setText(inventoryobjectsListMain.get(position).getSalesUserName());
        holder.tvcustomerValue.setText(inventoryobjectsListMain.get(position).getSearchByName());
        //holder.ssTypeTv.setText(inventoryobjectsListMain.get(position).getCustomerName().replaceAll("Module",""));

        //getShowTotMtrClm

        if (inventoryobjectsListMain.get(position).getResultDetails() != null) {
            if (inventoryobjectsListMain.get(position).getResultDetails().size() > 0) {
                holder.samplingNA.setVisibility(View.GONE);
                holder.singleSamplingDetails.setVisibility(View.VISIBLE);
                holder.singleSamplingDetails.removeAllViews();
                holder.singleSamplingDetails.invalidate();
                for (int i = 0; i < inventoryobjectsListMain.get(position).getResultDetails().size(); i++) {
                    final CustomerSelcectionModel.Result.ResultDetails singleObj = inventoryobjectsListMain.get(position).getResultDetails().get(i);
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = vi.inflate(R.layout.sales_report_search_list_bottom_1, null);
                    TableRow detailRow = (TableRow) view.findViewById(R.id.detailRow);
                    TableRow tablerow = (TableRow) view.findViewById(R.id.tablerow);
                    TextView tv_month_value = (TextView) view.findViewById(R.id.tv_month_value);
                    TextView tv_inr_value = (TextView) view.findViewById(R.id.tv_inr_value);
                    TextView tv_total_meter = (TextView) view.findViewById(R.id.tv_total_meter);
                    TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
                    TextView txt_mtr = (TextView) view.findViewById(R.id.txt_mtr);
                    LinearLayout linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
                    tv_detail.setText(inventoryobjectsListMain.get(position).getReportDetailName());
                    tv_month_value.setText(singleObj.getMonth());
                    tv_inr_value.setText(singleObj.getInr());


                    //txt_mtr  6/8/18 commit by akash matlani
                   /* if ( inventoryobjectsListMain.get(position).getShowTotMtrClm().equalsIgnoreCase("1"))

                    {
                        txt_mtr.setVisibility(View.VISIBLE);
                        tv_total_meter.setVisibility(View.VISIBLE);
                    }
                    else {
                        txt_mtr.setVisibility(View.GONE);
                        tv_total_meter.setVisibility(View.GONE);

                    }*/
                    /*when i==0 layout remove */

                    if (i == 0) {
                        linear_layout.setVisibility(View.VISIBLE);
                        tv_detail.setVisibility(View.VISIBLE);
                        detailRow.setVisibility(View.VISIBLE);

                    } else {
                        linear_layout.setVisibility(View.GONE);
                        tv_detail.setVisibility(View.GONE);
                        detailRow.setVisibility(View.GONE);
                    }
                    Log.d("i  out loop -->>", String.valueOf(i));
                    Log.d("size befo out loop-->>", String.valueOf(inventoryobjectsListMain.size()));

                    /*for the last row set the property bold*/
                    if (i == inventoryobjectsListMain.get(position).getResultDetails().size() - 1) {
                        Log.d("i  in loop", String.valueOf(i));
                        Log.d("size befo in loop-->>", String.valueOf(inventoryobjectsListMain.size()));
                        tv_month_value.setTypeface(tv_month_value.getTypeface(), Typeface.BOLD);
                        tv_inr_value.setTypeface(tv_inr_value.getTypeface(), Typeface.BOLD);
                        tv_total_meter.setTypeface(tv_total_meter.getTypeface(), Typeface.BOLD);
                    }
                    /*chnages in condition  6/8/18 commit by akash matlani  && inventoryobjectsListMain.get(position).getShowTotMtrClm().equalsIgnoreCase("1")*/
                    if (inventoryobjectsListMain.get(position).getReportType().equalsIgnoreCase("1") && inventoryobjectsListMain.get(position).getShowTotMtrClm().equalsIgnoreCase("1")) {
                        txt_mtr.setVisibility(View.VISIBLE);
                        tv_total_meter.setVisibility(View.VISIBLE);
                        tv_total_meter.setText(singleObj.getTotalMtr());
                    } else {
                        txt_mtr.setVisibility(View.GONE);
                        tv_total_meter.setVisibility(View.GONE);
                        /*akash matlani chnages 7/8/18*/
                        detailRow.removeView(txt_mtr);
                        tablerow.removeView(tv_total_meter);
                    }
                    holder.singleSamplingDetails.addView(view);
                }
                //tv_detail.setText(inventoryobjectsListMain.get(position).getReportDetailName());

            } else {

                holder.samplingNA.setVisibility(View.VISIBLE);
            }
        } else {
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


