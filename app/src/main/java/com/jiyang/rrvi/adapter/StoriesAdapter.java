package com.jiyang.rrvi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.jiyang.rrvi.R;
import com.jiyang.rrvi.RetrofitManager;
import com.jiyang.rrvi.VolleyManager;
import com.jiyang.rrvi.api.GetBitmap;
import com.jiyang.rrvi.bean.Stories;

import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jy on 2016/11/24.
 * RecyclerView数据适配
 */

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.MyViewHolder> {
    private List<Stories> storiesList; //data list
    private Context context;
    private boolean getPicByRR = true;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;//title
        ImageView ivImg;//small image

        MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }

    public StoriesAdapter(Context context, List<Stories> storiesList) {
        this.storiesList = storiesList;
        this.context = context;
    }

    public void setGetPicByRR(boolean b) {
        this.getPicByRR = b;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stories_show_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Stories stories = storiesList.get(position);
        holder.tvTitle.setText(stories.getTitle());
        if (getPicByRR) {
            //get pic by Retrofit and RxJava
            Action1<Bitmap> bitmapAction1 = new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    holder.ivImg.setImageBitmap(bitmap);
                }
            };
            GetBitmap getBitmap = RetrofitManager.getInstance().getRetrofit().create(GetBitmap.class);
            getBitmap
                    .getPicFromNet(stories.getImages().get(0))
                    .map(new Func1<ResponseBody, Bitmap>() {
                        @Override
                        public Bitmap call(ResponseBody responseBody) {
                            //decode pic
                            return BitmapFactory.decodeStream(responseBody.byteStream());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmapAction1);
        } else {
            //get pic by volley
            ImageLoader imageLoader = new ImageLoader(VolleyManager.getInstance().getRequestQueue(context.getApplicationContext())
                    , new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String url) {
                    return null;
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {

                }
            });
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.ivImg, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            imageLoader.get(stories.getImages().get(0), imageListener);
        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }
}
