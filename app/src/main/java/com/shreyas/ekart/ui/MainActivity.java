package com.shreyas.ekart.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyas.ekart.R;
import com.shreyas.ekart.Utiles.MyUtiles;
import com.shreyas.ekart.Utiles.SpacesItemDecor;
import com.shreyas.ekart.ViewModel.ContentViewModel;
import com.shreyas.ekart.adapter.DetailsAdapter;
import com.shreyas.ekart.models.DetailsModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListner {

    private RecyclerView mRvContent;
    //    private ArrayList<DetailsModel> mList;
    private DetailsAdapter adapter;
    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private boolean isClicked = false;
    private TextView mTvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        MyUtiles.checkReadPermission(this);
        mRvContent = findViewById(R.id.rv_content);
        mTvEmpty = findViewById(R.id.tv_empty);

        setDataContent();

    }


    private void setDataContent() {

        ArrayList<DetailsModel> mList = new ArrayList<>();
        mRvContent.hasFixedSize();
        mRvContent.setLayoutManager(new GridLayoutManager(this, 2));
        mRvContent.addItemDecoration(new SpacesItemDecor(30));

    }

    private void setAdapter() {
        adapter = new DetailsAdapter(this);
        ContentViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(ContentViewModel.class);
        viewModel.getAllData().observe(this, new Observer<List<DetailsModel>>() {
            @Override
            public void onChanged(List<DetailsModel> detailsModels) {
                adapter = new DetailsAdapter(MainActivity.this);
                adapter.setList(detailsModels);
                mRvContent.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                if (detailsModels.size() > 0) {
                    mTvEmpty.setVisibility(View.GONE);
                }else {
                    mTvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();

        setAdapter();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab) {
            isClicked = true;
//            fab.hide();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.frame_container, new InsertDataFragment());
            transaction.commit();

        }
    }

    @Override
    public void OnItemClick(int position, List<DetailsModel> mList, String color) {
        Log.d("popiu", "OnItemClick: " + position + "  " + mList.get(position).getId() + "  " + color);
        Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
        intent.putExtra("databaseID", String.valueOf(mList.get(position).getId()));
        intent.putExtra("color", color);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Log.d("pop00", "onBackPressed: ");
        if (isClicked) {

        } else {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_CALL_LOG)) {
                        // now, user has denied permission (but not permanently!)

                    } else {
                        MyUtiles.permissionError(MainActivity.this);

                    }

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
