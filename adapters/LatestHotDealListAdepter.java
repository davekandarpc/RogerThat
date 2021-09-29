package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.rogerthat.rlvltd.com.apiresponsemodels.HotDealListApiResponse;

import java.util.List;

/**
 * Created by kandarp3 on 20/03/20.
 */

public class LatestHotDealListAdepter extends RecyclerView.Adapter<LatestHotDealListAdepter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<HotDealListApiResponse.ResultBean> hotResultBeans;
    OnItemSelectedListener listner;

    public LatestHotDealListAdepter(Context mContext, List<HotDealListApiResponse.ResultBean> hotResultBeans, OnItemSelectedListener selectedListener) {
        this.mContext = mContext;
        this.hotResultBeans = hotResultBeans;
        this.listner = selectedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_list_item, parent, false);

        return new LatestHotDealListAdepter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.category_name.setText(hotResultBeans.get(position).getTitle().replaceAll("Module", ""));

        int size = (int) mContext.getResources().getDimension(R.dimen.gridicon_width); // 250dp

        Glide.with(mContext)
                .load(hotResultBeans.get(position).getIcon())
                .placeholder(R.drawable.star)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate() // This solved the problem
                .into(holder.category_pic);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                singleObj = gson.toJson(hotResultBeans.get(position));
                listner.onSelected();

                Log.i("SELECTED_ITEM", String.valueOf(hotResultBeans.get(position).getId()));

            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView category_pic;
        public TextView category_name;
        public MyViewHolder( View itemView) {
            super(itemView);
            category_pic = (ImageView) itemView.findViewById(R.id.category_pic);
            category_name = (TextView) itemView.findViewById(R.id.category_name);


        }
    }

    public String getSingleObj() {
        return singleObj;
    }

    @Override
    public int getItemCount() {
        return hotResultBeans.size();
    }



    public interface OnItemSelectedListener {
        void onSelected();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
