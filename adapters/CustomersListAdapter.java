package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.database.DatabaseHelper;
import com.rogerthat.rlvltd.com.fragment.OrderStatusSearchFragment;
import com.rogerthat.rlvltd.com.util.AppConfig;

import java.util.List;


public class CustomersListAdapter extends RecyclerView.Adapter<CustomersListAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> customersList;
    private List<String> customersIDList;


    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor sharededitor;

    private DatabaseHelper dbHelper;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView planTv;
        public ImageView firstLetterTV;


        public MyViewHolder(View view) {
            super(view);
            planTv = (TextView) view.findViewById(R.id.planTv);
            firstLetterTV = (ImageView) view.findViewById(R.id.firstLetterTV);

        }
    }


    public CustomersListAdapter(Context mContext,
                                List<String> customersListTemp,
                                List<String> customersIdListTemp,
                                DatabaseHelper dbhelpertemp) {
        this.mContext = mContext;
        this.customersList = customersListTemp;
        this.customersIDList = customersIdListTemp;
        this.dbHelper = dbhelpertemp;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customerlist_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.planTv.setText(customersList.get(position));

        final String customerName = customersList.get(position);
        final String customerID = customersIDList.get(position);
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
                args.putString(AppConfig.CUSTOMER_NAME, customerName);
                args.putString(AppConfig.CUSTOMER_ID, customerID);

                OrderStatusSearchFragment yojf = new OrderStatusSearchFragment();
                yojf.setArguments(args);

                FragmentTransaction fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, yojf).addToBackStack("");
                fragmentTransaction.commit();
//                ((MainActivity)mContext).getSupportActionBar().setTitle(mContext.getResources().getString(R.string.sitetimeLine));
            }
        });


    }


    @Override
    public int getItemCount() {
//        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
        return customersList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }



}