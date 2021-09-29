package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rogerthat.rlvltd.com.R;

import java.util.ArrayList;
import java.util.List;


public class SpAdapter extends BaseAdapter{

    Context context;
    List<String> list = new ArrayList<>();

    public SpAdapter(Context context, List<String> listLoc) {
        this.context = context;
        this.list = listLoc;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        TextView txt = (TextView)view.findViewById(R.id.txt_spinner);
        txt.setText(list.get(position));
        return view;
    }
}

