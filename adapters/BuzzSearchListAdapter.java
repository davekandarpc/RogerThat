package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rogerthat.rlvltd.com.Activities.ImageDetailViewActivity;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.apiresponsemodels.InventorySearchApiResponse;
import com.rogerthat.rlvltd.com.util.CommonUses;
import com.rogerthat.rlvltd.com.views.ItemClick;
import com.rogerthat.rlvltd.com.views.ItemClickListener;

import java.util.List;

public class BuzzSearchListAdapter extends RecyclerView.Adapter<BuzzSearchListAdapter.MyViewHolder> {

    String singleObj = "";
    String imageHostpathGolob;

    private Context mContext;
    private List<InventorySearchApiResponse.ResultBean> inventoryobjectsListMain;

    boolean isLoading = false, isMoreDataAvailable = true;
    boolean isCustomerLogin;

    OnItemSelectedListener onItemSelectedListener;
    OnLoadMoreListener loadMoreListener;
    ItemClickListener itemClickListener;
    ItemClick itemClick;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView category_image;
        public ImageView gallery_image;
        public CheckBox checkbox_icon;
        public TextView tv_arical_no;
        public TextView statusTv;

        public MyViewHolder(View view) {
            super(view);
            category_image = (ImageView) view.findViewById(R.id.category_image);
            gallery_image = (ImageView) view.findViewById(R.id.gallery_image);
            checkbox_icon = (CheckBox) view.findViewById(R.id.checkbox_icon);
            statusTv = (TextView) view.findViewById(R.id.statusTv);
            tv_arical_no = (TextView) view.findViewById(R.id.tv_arical_no);
        //checkbox_icon.setSelected(true);
        //checkbox_icon.setChecked(true);

            /*txt_add_to_order.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(inventoryobjectsListMain.get(getAdapterPosition()), getAdapterPosition());
        }
    }


    public BuzzSearchListAdapter(Context mContext,
                                 List<InventorySearchApiResponse.ResultBean> invenotyrListMain,
                                 BuzzSearchListAdapter.OnItemSelectedListener selectedListener,
                                 String imageHostPath, BuzzSearchListAdapter.OnLoadMoreListener onLoadMoreListenerTemp,
                                 boolean isCustomerLogin, ItemClickListener itemClickListener, ItemClick itemClick) {
        this.mContext = mContext;
        this.inventoryobjectsListMain = invenotyrListMain;
        this.onItemSelectedListener = selectedListener;
        this.imageHostpathGolob = imageHostPath;
        this.loadMoreListener = onLoadMoreListenerTemp;
        this.isCustomerLogin = isCustomerLogin;
        this.itemClickListener = itemClickListener;
        this.itemClick = itemClick;
    }

    public interface OnItemSelectedListener {
        void onSelected(int position, String itmId, boolean isChecked);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(BuzzSearchListAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public BuzzSearchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buzz_search_list_item, parent, false);

        return new BuzzSearchListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BuzzSearchListAdapter.MyViewHolder holder, final int position) {

        holder.checkbox_icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                onItemSelectedListener.onSelected(position, inventoryobjectsListMain.get(position).getItemNo(), isChecked);
            }
        });
        holder.gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.OnClickViewMore(holder.gallery_image, inventoryobjectsListMain.get(position), position);
            }
        });
        holder.statusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.OnClickViewMore(holder.statusTv, inventoryobjectsListMain.get(position), position);
            }
        });
        if (inventoryobjectsListMain.get(position).getItemNo() != null) {
            holder.tv_arical_no.setText(inventoryobjectsListMain.get(position).getItemNo());
        }
        if (!inventoryobjectsListMain.get(position).getImage().equalsIgnoreCase("no_image.png")) {
            Glide.with(mContext)
                    .load(imageHostpathGolob.concat(inventoryobjectsListMain.get(position).getImage()))
                    .placeholder(R.drawable.default_new_img)
                    .dontAnimate()
                    .into(holder.category_image);

        } else {
            Glide.with(mContext)
                    .load(imageHostpathGolob.concat(inventoryobjectsListMain.get(position).getImage()))
                    .placeholder(R.drawable.default_new_img)
                    .dontAnimate()
                    .into(holder.category_image);
            //holder.category_image.setImageResource(R.drawable.default_ic);

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
        //MORE IMAGE FLAG - MORE IMAGE ICON
        if (Integer.parseInt(inventoryobjectsListMain.get(position).getArticleMoreImgFlag()) > 0) {
            holder.gallery_image.setVisibility(View.VISIBLE);
        } else {
            holder.gallery_image.setVisibility(View.GONE);
        }
        //Status TextView
        String statusStr = inventoryobjectsListMain.get(position).getIndiaStatus();
        if (TextUtils.isEmpty(statusStr)) {
            statusStr = "Not Available/Details";
        } else {
            statusStr = statusStr + "/Details";
        }
        holder.statusTv.setText(statusStr);

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


