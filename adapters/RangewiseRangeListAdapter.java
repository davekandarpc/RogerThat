package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.fragment.RangewiseSearchFragment;
import com.rogerthat.rlvltd.com.model.RangeModel;
import com.rogerthat.rlvltd.com.util.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class RangewiseRangeListAdapter extends RecyclerView.Adapter<RangewiseRangeListAdapter.MyViewHolder>   implements Filterable {

    private Context mContext;
    private List<RangeModel.ResultBean> rangeList;
    private List<RangeModel.ResultBean> mFilteredList;

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = rangeList;
                } else {

                    List<RangeModel.ResultBean> filteredList = new ArrayList<>();

                    for (RangeModel.ResultBean androidVersion : rangeList) {

                        if (androidVersion.getRangeName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<RangeModel.ResultBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView firstLetterTV;


        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.planTv);
            firstLetterTV = (ImageView) view.findViewById(R.id.firstLetterTV);

        }
    }


    public RangewiseRangeListAdapter(Context mContext, List<RangeModel.ResultBean> rangeList) {
        this.mContext = mContext;
        this.rangeList = rangeList;
        this.mFilteredList = rangeList;
    }

    @Override
    public RangewiseRangeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customerlist_item, parent, false);

        return new RangewiseRangeListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RangewiseRangeListAdapter.MyViewHolder holder, final int position) {



        holder.nameTv.setText(mFilteredList.get(position).getRangeName());

        final String customerName = mFilteredList.get(position).getRangeName();
        char first = customerName.toUpperCase().charAt(0);

        // declare the builder object once.
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(2) /* thickness in px */
                .toUpperCase()
                .endConfig()
                .round();

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable ic1 = builder.build(Character.toString(first), color1);
        holder.firstLetterTV.setImageDrawable(ic1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle args = new Bundle();
                args.putString(AppConfig.RANGE_NAME, customerName);
                args.putString(AppConfig.RANGE_ID, mFilteredList.get(position).getRangeCode());
                RangewiseSearchFragment yojf = new RangewiseSearchFragment();
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, yojf).addToBackStack("");
                yojf.setArguments(args);
                fragmentTransaction.commit();
             }
        });


    }


    @Override
    public int getItemCount() {
//        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
        return mFilteredList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void filterList(List<RangeModel.ResultBean> filterdNames) {
        this.mFilteredList = filterdNames;
        notifyDataSetChanged();
    }





}