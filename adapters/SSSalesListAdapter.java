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
import com.rogerthat.rlvltd.com.database.SalesNewDbHelper;
import com.rogerthat.rlvltd.com.fragment.SamplingStatusSearchFragment;

import java.util.List;

public class SSSalesListAdapter extends RecyclerView.Adapter<SSSalesListAdapter.MyViewHolder> {

    private Context mContext;
    private List<SalesNewDbHelper> salesUsersList;


    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor sharededitor;

    private DatabaseHelper dbHelper;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView firstLetterTV;


        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.planTv);
            firstLetterTV = (ImageView) view.findViewById(R.id.firstLetterTV);

        }
    }


    public SSSalesListAdapter(Context mContext,
                              List<SalesNewDbHelper> salesUsersListnee,
                              DatabaseHelper dbhelpertemp) {
        this.mContext = mContext;
        this.salesUsersList = salesUsersListnee;
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



        holder.nameTv.setText(salesUsersList.get(position).getSalesPerson());

        final String customerName = salesUsersList.get(position).getSalesPerson();
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
                args.putString("SalesName", customerName);
                args.putString("SalesNameId", salesUsersList.get(position).getSalesUserId());

                SamplingStatusSearchFragment yojf = new SamplingStatusSearchFragment();
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
        return salesUsersList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }



}