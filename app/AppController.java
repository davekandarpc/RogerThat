package com.rogerthat.rlvltd.com.app;


import android.app.Application;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.rogerthat.rlvltd.com.util.TypefaceUtil;


public class AppController extends Application {

	public static final String TAG = AppController.class
			.getSimpleName();

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "LatoRegular.ttf");
		BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));


	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}


}
