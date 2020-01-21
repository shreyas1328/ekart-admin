package com.shreyas.ekart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shreyas.ekart.R;
import com.shreyas.ekart.Utiles.MyUtiles;
import com.shreyas.ekart.models.DetailsModel;
import com.shreyas.ekart.ui.OnItemClickListner;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DetailsModel> mList;
    private OnItemClickListner listner;

    public DetailsAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<DetailsModel> mList) {
        this.mList = mList;
        listner = (OnItemClickListner) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_snippet, parent, false);
        int hieght = parent.getMeasuredHeight()/4;
        view.setMinimumHeight(hieght);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            ((Holder) holder).bind(position);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView mIvImage;
        private TextView mTvTitle, mTvShortDesc, mTvPrice;
        private CardView mCvItem;
        private ProgressBar mImageLoader;
        private String randomColor;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_item);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvShortDesc = itemView.findViewById(R.id.tv_short_desc);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mCvItem = itemView.findViewById(R.id.cv_item);
            mImageLoader = itemView.findViewById(R.id.image_loader);

            randomColor = MyUtiles.getRandomColor();
            mIvImage.setBackgroundColor(Color.parseColor(randomColor));


            mCvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listner.OnItemClick(getAdapterPosition(), mList, randomColor);
                }
            });

        }

        private void bind(int position) {
            if (mList.get(position).getImage().getImageContent() != null) {
                Object image = MyUtiles.getGlideLoad(mList.get(position));
                mImageLoader.setVisibility(View.VISIBLE);
            Glide.with(context).load(image).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mImageLoader.setVisibility(View.GONE);
                    mIvImage.setBackgroundColor(Color.parseColor(randomColor));
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    mImageLoader.setVisibility(View.GONE);
                    mIvImage.setBackgroundColor(Color.parseColor("#ffffff"));
                    return false;
                }
            }).into(mIvImage);//uncomment if image exists
            }
            Log.d("iio8", "bind: "+mList.get(position).getPrice());
            mTvTitle.setText(mList.get(position).getTitle());
            mTvShortDesc.setText(mList.get(position).getShortDesc());
            mTvPrice.setText("\u20B9. "+mList.get(position).getPrice());
        }
    }
}
