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
import com.rogerthat.rlvltd.com.fragment.SalesReportSearchFragment;
import com.rogerthat.rlvltd.com.model.CustomerResult;
import com.rogerthat.rlvltd.com.util.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class SSSCustomerListAdapterWithoutDb extends RecyclerView.Adapter<SSSCustomerListAdapterWithoutDb.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<CustomerResult> salesUsersList;
    private List<CustomerResult> mFilteredList;
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

                    List<CustomerResult> filteredList = new ArrayList<>();

                    for (CustomerResult androidVersion : salesUsersList) {

                        if (androidVersion.getNameofAccount().toLowerCase().contains(charString.toLowerCase())) {

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
                mFilteredList = (List<CustomerResult>) filterResults.values;
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


    public SSSCustomerListAdapterWithoutDb(Context mContext, List<CustomerResult> salesUsersListnee,
                                           String fromType) {
        this.mContext = mContext;
        this.salesUsersList = salesUsersListnee;
        this.mFilteredList = salesUsersListnee;
        //SALES_REPORT OR RANGE_WISE
        this.from_page = fromType;


    }

    @Override
    public SSSCustomerListAdapterWithoutDb.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customerlist_item, parent, false);

        return new SSSCustomerListAdapterWithoutDb.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SSSCustomerListAdapterWithoutDb.MyViewHolder holder, final int position) {


        holder.nameTv.setText(mFilteredList.get(position).getNameofAccount());

        final String customerName = mFilteredList.get(position).getNameofAccount();
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
                //SALES_REPORT OR RANGE_WISE
                if (from_page.equalsIgnoreCase("SALES_REPORT")) {
                    Bundle args = new Bundle();
                    args.putString(AppConfig.CUSTOMER_NAME, customerName);
                    args.putString(AppConfig.CUSTOMER_ID, mFilteredList.get(position).getCustomerId());
                    // SalesReportSearchFragment yojf = new SamplingStatusSearchFragment();
                    SalesReportSearchFragment yojf = new SalesReportSearchFragment();
                    yojf.setArguments(args);
                    FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                /*10/8/18 akash matlani codes chnage start */
                    fragmentTransaction.replace(R.id.container, yojf).addToBackStack(null);
                /*10/8/18 akash matlani codes chnage ended */
                /*fragmentTransaction.replace(R.id.container, yojf);
                fragmentTransaction.disallowAddToBackStack();*/
                    fragmentTransaction.commit();
//                ((MainActivity)mContext).getSupportActionBar().setTitle(mContext.getResources().getString(R.string.sitetimeLine));
                } else {
                    Bundle args = new Bundle();
                    args.putString(AppConfig.CUSTOMER_NAME, customerName);
                    args.putString(AppConfig.CUSTOMER_ID, mFilteredList.get(position).getCustomerId());
                    RangewiseSearchFragment rangewiseSearchFragment = new RangewiseSearchFragment();
                    rangewiseSearchFragment.setArguments(args);
                    FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, rangewiseSearchFragment).addToBackStack(null);
                    fragmentTransaction.commit();
                }
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

    public void filterList(List<CustomerResult> filterdNames) {
        this.mFilteredList = filterdNames;
        notifyDataSetChanged();
    }


}