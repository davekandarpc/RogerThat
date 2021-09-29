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
import com.rogerthat.rlvltd.com.model.Result;
import com.rogerthat.rlvltd.com.util.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class RangewiseSalesListAdapter extends RecyclerView.Adapter<RangewiseSalesListAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<Result> salesUsersList;
    private List<Result> mFilteredList;
    private String from_page = "";

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = salesUsersList;
                } else {

                    List<Result> filteredList = new ArrayList<>();

                    for (Result androidVersion : salesUsersList) {

                        if (androidVersion.getSalesPerson().toLowerCase().contains(charString.toLowerCase())) {

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
                mFilteredList = (List<Result>) filterResults.values;
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


    public RangewiseSalesListAdapter(Context mContext, List<Result> salesUsersListnee, String fromType) {
        this.mContext = mContext;
        this.salesUsersList = salesUsersListnee;
        this.mFilteredList = salesUsersListnee;
        this.from_page = fromType;


    }

    @Override
    public RangewiseSalesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customerlist_item, parent, false);

        return new RangewiseSalesListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RangewiseSalesListAdapter.MyViewHolder holder, final int position) {


        holder.nameTv.setText(mFilteredList.get(position).getSalesPerson());

        final String customerName = mFilteredList.get(position).getSalesPerson();
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
                args.putString(AppConfig.SALES_NAME, customerName);
                args.putString(AppConfig.SALES_ID, mFilteredList.get(position).getSalesUserId());
                RangewiseSearchFragment rangewiseSearchFragment = new RangewiseSearchFragment();
                rangewiseSearchFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, rangewiseSearchFragment).addToBackStack(null);
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

    public void filterList(List<Result> filterdNames) {
        this.mFilteredList = filterdNames;
        notifyDataSetChanged();
    }


}