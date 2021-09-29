package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.GetMenusApiResponse;

import java.util.List;


public class GeneralGridAdapter extends RecyclerView.Adapter<GeneralGridAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<GetMenusApiResponse.ResultBean> generalgridListmain;
    OnItemSelectedListener listner;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView category_pic;
        public TextView category_name;

//        public ImageView barcodeImage;

        public MyViewHolder(View view) {
            super(view);
            category_pic = (ImageView) view.findViewById(R.id.category_pic);
            category_name = (TextView) view.findViewById(R.id.category_name);

        }
    }


    public GeneralGridAdapter(Context mContext,
                              List<GetMenusApiResponse.ResultBean> generalgridList,
                              OnItemSelectedListener selectedListener) {

        this.mContext = mContext;
        this.generalgridListmain = generalgridList;
        this.listner = selectedListener;

    }


    public interface OnItemSelectedListener {
        void onSelected();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.category_name.setText(generalgridListmain.get(position).getTitle().replaceAll("Module", ""));

        int size = (int) mContext.getResources().getDimension(R.dimen.gridicon_width); // 250dp

        Glide
                .with(mContext)
                .load(generalgridListmain.get(position).getImage())
                .placeholder(R.drawable.star)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate() // This solved the problem
                .into(holder.category_pic);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(generalgridListmain.get(position));
                listner.onSelected();


            }
        });

    }

    public String getSingleObj() {
        return singleObj;
    }


    @Override
    public int getItemCount() {
//        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
        return generalgridListmain.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

}