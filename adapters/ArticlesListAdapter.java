package com.rogerthat.rlvltd.com.adapters;

import android.app.Service;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.model.ArticleModel;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.adapters.SearchDialogAdapter;

/**
 * Created by kandarp3 on 01/02/20.
 */

public class ArticlesListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ArticleModel> ModelArrayList;

    public ArticlesListAdapter(Context context, ArrayList<ArticleModel> modelArrayList) {
        super();
        this.context = context;
        ModelArrayList = modelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getCount() {
        return ModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.articles_item_list, null);

            holder.tvname = (TextView) convertView.findViewById(R.id.tv);

            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Log.i("INVIEW",ModelArrayList.get(position).getName());
        holder.tvname.setText(ModelArrayList.get(position).getName());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelArrayList.remove(position);

                notifyDataSetChanged();
            }
        });
        return convertView;

    }
    public void remove(int position) {
        ModelArrayList.remove(position);
        notifyDataSetChanged();
    }
    private class ViewHolder {

        protected TextView tvname;
        private Button btnDelete;
         //Log.i("INVIEW");


    }
}
