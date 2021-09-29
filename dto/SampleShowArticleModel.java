package com.rogerthat.rlvltd.com.dto;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by kandarp3 on 09/01/20.
 */

public class SampleShowArticleModel implements Searchable{
    private String sTitle;

    public SampleShowArticleModel(String title) {
        this.sTitle = title;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public SampleShowArticleModel setTitle(String title) {
        sTitle = title;
        return this;
    }

}
