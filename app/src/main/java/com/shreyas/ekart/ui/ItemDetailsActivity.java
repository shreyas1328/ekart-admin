package com.shreyas.ekart.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyas.ekart.R;
import com.shreyas.ekart.Utiles.FABAnimation;
import com.shreyas.ekart.Utiles.MyUtiles;
import com.shreyas.ekart.ViewModel.ContentViewModel;
import com.shreyas.ekart.models.DetailsModel;

public class ItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvContentImage, mIvBack;
    private TextView mTvContentTitle, mTvContentDesc, mTvContentAmount, mTvQuantity;
    private FloatingActionButton fab, fabDelete, fabEdit;
    private ContentViewModel viewModel;
    private String id, color;
    private boolean isRotate = false;
    private DetailsModel detailsModel;
    private boolean isClicked = false;
    private AppBarLayout appbarlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        id = getIntent().getExtras().getString("databaseID");
        color = getIntent().getExtras().getString("color");
        setDataInit();

        Log.d("ooo9", "onCreate: "+id+"  "+color);
    }

    private void setDataInit() {
        fab = findViewById(R.id.content_fab);
        mIvBack = findViewById(R.id.iv_back);
        mIvContentImage = findViewById(R.id.iv_content_image);
        mTvContentTitle = findViewById(R.id.tv_content_title);
        mTvContentDesc = findViewById(R.id.tv_content_desc);
        mTvContentAmount = findViewById(R.id.tv_content_amount);
        mTvQuantity = findViewById(R.id.tv_content_quantity);
        fabDelete = findViewById(R.id.fab_delete);
        fabEdit = findViewById(R.id.fab_edit);
        appbarlayout = findViewById(R.id.appbarLayout);

        if (color != null) {
            mIvContentImage.setBackgroundColor(Color.parseColor(color));
        }

        FABAnimation.init(fabEdit);
        FABAnimation.init(fabDelete);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = FABAnimation.rotateFab(v, !isRotate);
                if (isRotate) {
                    FABAnimation.showIn(fabEdit);
                    FABAnimation.showIn(fabDelete);
                }else {
                    FABAnimation.showOut(fabEdit);
                    FABAnimation.showOut(fabDelete);
                }

            }
        });
        mIvBack.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabDelete.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
    }

    private void setData() {
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(ContentViewModel.class);
        viewModel.getParticularItem(Integer.parseInt(id)).observe(this, new Observer<DetailsModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(DetailsModel model) {
                detailsModel = model;
                if (model != null) {
                    Log.d("qwer12", "onChanged: " + model.getImage());
                    if (model.getImage().getImageContent() != null) {
                        Object image = MyUtiles.getGlideLoad(model);
                        mIvContentImage.setBackgroundColor(getResources().getColor(R.color.white));
                        Glide.with(ItemDetailsActivity.this).load(image).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mIvContentImage.setBackgroundColor(Color.parseColor(color));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mIvContentImage.setBackgroundColor(Color.parseColor(getString(R.string.white_color)));
                                return false;
                            }
                        }).into(mIvContentImage); //uncomment if image exists
                    }
                    mTvContentDesc.setText(model.getDesciption());
                    if (model.getDesciption().equals("")) {
                        mTvContentDesc.setText(model.getShortDesc());
                    }
                    mTvContentTitle.setText(model.getTitle());
                    mTvContentAmount.setText(getString(R.string.curency) + model.getPrice());
                    mTvQuantity.setText(getString(R.string.qty_text)+model.getQuantity());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_edit:
                getFragment();
                restFab();
//                hideViews();
            break;

            case R.id.fab_delete:
                deleteItem();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void restFab() {
        FABAnimation.showOut(fabEdit);
        FABAnimation.showOut(fabDelete);
        FABAnimation.rotateFab(fab, false);
    }

    private void deleteItem() {
        finish();
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(ContentViewModel.class);
        viewModel.deleteData(Integer.parseInt(id));
    }

    @SuppressLint("RestrictedApi")
    private void hideViews() {
        fab.hide();
        fabDelete.hide();
        fabEdit.hide();
    }

    private void getFragment() {
        isClicked = true;
//        appbarlayout.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.edit_item_container, new InsertDataFragment(detailsModel));
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (isClicked) {

        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //if permission is made dont show again
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ItemDetailsActivity.this,
                            Manifest.permission.READ_CALL_LOG)) {
                        // now, user has denied permission (but not permanently!)

                    } else {
                        MyUtiles.permissionError(ItemDetailsActivity.this);

                    }

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
