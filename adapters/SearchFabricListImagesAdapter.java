package com.rogerthat.rlvltd.com.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;
import com.rogerthat.rlvltd.com.R;
import com.rogerthat.rlvltd.com.model.Results;

import java.util.List;

public class SearchFabricListImagesAdapter extends PagerAdapter {
    //ArrayList<String> alImage;
    Context context;
    List<Results> alImage;

    public SearchFabricListImagesAdapter(Context context, List<Results> alImage) {
        this.context = context;
        this.alImage = alImage;
    }

    @Override
    public int getCount() {
        return alImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.search_fabric_images_adapter, container, false);
        final BigImageView imgThumbnail = (BigImageView) itemView.findViewById(R.id.search_imageview);

        //String baseUrl = "https://portal.rogerlaviale.com/storage/artical_gallery/" + alImage.get(position).getImage();
        String baseUrl = "http://portal.rogerlaviale.com/storage/artical_gallery/" + alImage.get(position).getImage();

        try {
            BigImageViewer.prefetch(Uri.parse(baseUrl));
            imgThumbnail.showImage(Uri.parse(baseUrl));
            ProgressPieIndicator pie = new ProgressPieIndicator();
            imgThumbnail.setProgressIndicator(pie);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Glide.with(context)
                .load(baseUrl)
                .placeholder(R.drawable.default_new_img)
                .dontAnimate()
                .into(imgThumbnail);*/

            /*
            //run this into background thread
            baseUrl = baseUrl + alImage.get(position).getImage();
            Bitmap theBitmap = Glide.
                    with(context).
                    load(baseUrl).
                    asBitmap().
                    into(400, 200). // Width and height
                    get();

            imgThumbnail.setImageBitmap(theBitmap);*/
        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }


}
