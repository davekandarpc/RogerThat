package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rogerthat.rlvltd.com.Activities.ImageDetailViewActivity;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.InventorySearchApiResponse;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.views.AddToOrderInterface;
import com.rogerthat.rlvltd.com.views.ItemClick;
import com.rogerthat.rlvltd.com.views.ItemClickListener;

import java.util.List;

public class SearchFabricAdapter extends RecyclerView.Adapter<SearchFabricAdapter.MyViewHolder> {

    String singleObj = "";
    private Context mContext;
    private List<InventorySearchApiResponse.ResultBean> inventoryobjectsListMain;
    OnItemSelectedListener listner;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    String imageHostpathGolob;
    ItemClickListener itemClickListener;
    ItemClick itemClick;
    AddToOrderInterface addToOrderInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView articleNoTv;
        public TextView txt_order;
        public TextView txt_view_more_images;
        public TextView txt_new;
        public ImageView category_image;
        public TextView statusTv;
        public TextView viewmoreimags;
        public TextView txt_add_to_order;


        public MyViewHolder(View view) {
            super(view);
            articleNoTv = (TextView) view.findViewById(R.id.tv_arical_no);
            txt_order = (TextView) view.findViewById(R.id.txt_order);
            txt_view_more_images = (TextView) view.findViewById(R.id.txt_view_more_images);
            txt_new = (TextView) view.findViewById(R.id.txt_new);

            category_image = (ImageView) view.findViewById(R.id.category_image);

            statusTv = (TextView) view.findViewById(R.id.statusTv);

            viewmoreimags = (TextView) view.findViewById(R.id.description);

            txt_add_to_order = (TextView) view.findViewById(R.id.txt_add_to_order);


            txt_add_to_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToOrderInterface.OnClickAddToOrder(inventoryobjectsListMain.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            viewmoreimags.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.OnClickViewMore(viewmoreimags, inventoryobjectsListMain.get(getAdapterPosition()), getAdapterPosition());

                }
            });
            statusTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.OnClickViewMore(statusTv, inventoryobjectsListMain.get(getAdapterPosition()), getAdapterPosition());

                }
            });
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(inventoryobjectsListMain.get(getAdapterPosition()), getAdapterPosition());
        }
    }


    public SearchFabricAdapter(Context mContext,
                               List<InventorySearchApiResponse.ResultBean> invenotyrListMain,
                               SearchFabricAdapter.OnItemSelectedListener selectedListener, String imageHostPath, SearchFabricAdapter.OnLoadMoreListener onLoadMoreListenerTemp, boolean isCustomerLogin, ItemClickListener itemClickListener, ItemClick itemClick, AddToOrderInterface addToOrderInterface) {

        this.mContext = mContext;
        this.inventoryobjectsListMain = invenotyrListMain;
        this.listner = selectedListener;
        this.imageHostpathGolob = imageHostPath;
        this.loadMoreListener = onLoadMoreListenerTemp;
        this.isCustomerLogin = isCustomerLogin;
        this.itemClickListener = itemClickListener;
        this.itemClick = itemClick;
        this.addToOrderInterface = addToOrderInterface;
    }

    public interface OnItemSelectedListener {
        void onSelected();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(SearchFabricAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public SearchFabricAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (isCustomerLogin) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_fabric_item_customerlogin_one, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    // .inflate(R.layout.search_fabric_item_saleslogin, parent, false);
                    .inflate(R.layout.sales_fabric_item_saleslogin_one, parent, false);
        }


        return new SearchFabricAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchFabricAdapter.MyViewHolder holder, final int position) {
        if (inventoryobjectsListMain.get(position).getItemNo() != null) {
            holder.articleNoTv.setText(inventoryobjectsListMain.get(position).getItemNo());
        }
        if (!inventoryobjectsListMain.get(position).getImage().equalsIgnoreCase("no_image.png")) {

            Log.d("string-->>", imageHostpathGolob);
            Log.d("image-->>", inventoryobjectsListMain.get(position).getImage());
            Log.d("final_image-->>", imageHostpathGolob + inventoryobjectsListMain.get(position).getImage());
            Glide.with(mContext)
                    .load(imageHostpathGolob.concat(inventoryobjectsListMain.get(position).getImage()))
                    .placeholder(R.drawable.star)
                    .dontAnimate()
                    .into(holder.category_image);

        }

        holder.category_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inventoryobjectsListMain.get(position).getBigImage().equalsIgnoreCase("no_image.png")) {
                    if (!inventoryobjectsListMain.get(position).getBigImage().isEmpty()) {
                        try {
                            Intent in = new Intent(mContext, ImageDetailViewActivity.class);
                            in.putExtra("URL", imageHostpathGolob + inventoryobjectsListMain.get(position).getBigImage());
                            mContext.startActivity(in);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUses.showToastwithDuration(2500, "No Big Image is Available.", mContext);
                    }

                } else {
                    CommonUses.showToastwithDuration(2500, "No Big Image is Available.", mContext);
                }
            }
        });

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        /*String mainIMageURL = inventoryobjectsListMain.get(position).getImage();
        if (mainIMageURL.equalsIgnoreCase("no_image.png")) {
            holder.category_image.setVisibility(View.GONE);
        } else {
            holder.category_image.setVisibility(View.VISIBLE);
        }*/

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


