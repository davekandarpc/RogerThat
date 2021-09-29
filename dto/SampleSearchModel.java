package com.rogerthat.rlvltd.com.dto;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by macbookair on 23/05/17.
 */

public class SampleSearchModel implements Searchable {
    private String mTitle;
    private String mId;

    public SampleSearchModel(String title, String id) {
        mTitle = title;
        mId = id;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public String getmId() {
        return mId;
    }

    public SampleSearchModel setTitle(String title) {
        mTitle = title;
        return this;
    }


    public SampleSearchModel setmId(String mid) {
        mId = mid;
        return this;
    }
}