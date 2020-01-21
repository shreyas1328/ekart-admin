package com.shreyas.ekart.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shreyas.ekart.R;
import com.shreyas.ekart.Utiles.MyUtiles;
import com.shreyas.ekart.ViewModel.ContentViewModel;
import com.shreyas.ekart.models.DetailsModel;
import com.shreyas.ekart.models.SaveImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsertDataFragment extends Fragment implements View.OnClickListener {

    private EditText mEtName, mEtShortDesc, mEtLongDesc, mEtAmount, mEtQuantity, mEtImageURL;
    private Button mBtnPost, mBtnCancel;
    private ContentViewModel viewModel;
    private DetailsModel model;
    private TextView tvtitle;
    private Button mBtnbtnGallery, mBtnCamera, mBtnLink;
    private EditText editText;
    private String imageContent;
    public static final int PICK_IMAGE = 1;
    public static final int CAMERA_PIC_REQUEST = 2;
    private String source;


    public InsertDataFragment() {
        // Required empty public constructor
    }

    public InsertDataFragment(DetailsModel model) {
        this.model = model;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert_data, container, false);
        setInitData(view);
        return view;
    }

    private void setInitData(View view) {
        mEtName = view.findViewById(R.id.et_item_name);
        mEtShortDesc = view.findViewById(R.id.et_item_short_desc);
        mEtLongDesc = view.findViewById(R.id.et_item_long_desc);
        mEtAmount = view.findViewById(R.id.et_item_amount);
        mEtQuantity = view.findViewById(R.id.et_item_quantity);
        mBtnPost = view.findViewById(R.id.btn_post);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        tvtitle = view.findViewById(R.id.textView);

        mBtnbtnGallery = view.findViewById(R.id.btn_choose_gallery);
        mBtnCamera = view.findViewById(R.id.btn_camera);
        mBtnLink = view.findViewById(R.id.btn_link);

        mBtnPost.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mBtnbtnGallery.setOnClickListener(this);
        mBtnCamera.setOnClickListener(this);
        mBtnLink.setOnClickListener(this);

        setUpdateData();
    }

    private void setUpdateData() {
        if (model != null) {
            tvtitle.setText(R.string.update_item);
            mEtName.setText(model.getTitle());
            mEtLongDesc.setText(model.getDesciption());
            mEtShortDesc.setText(model.getShortDesc());
            mEtAmount.setText(model.getPrice());
            mEtQuantity.setText(model.getQuantity());
            mBtnPost.setText(R.string.update);
            setButtonColor(model.getImage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_post:
                submitButton();
                break;

            case R.id.btn_camera:
                opencamera();
                break;

            case R.id.btn_choose_gallery:
                galleryImage();
                break;

            case R.id.btn_link:
                setDialogBox();
                break;

            case R.id.btn_cancel:
                closeFragment();
                break;
        }
    }

    private void submitButton() {
        if (mBtnPost.getText().toString().equals(getString(R.string.btn_post))) {
            if (mEtName.getText().toString().isEmpty() || mEtQuantity.getText().toString().isEmpty() || mEtAmount.getText().toString().isEmpty()) {
                mEtName.setError(getString(R.string.cannot_be_empty));
                mEtQuantity.setError(getString(R.string.cannot_be_empty));
                mEtAmount.setError(getString(R.string.cannot_be_empty));
            } else {
                setViewModel();
            }
        } else {
            if (mEtName.getText().toString().isEmpty() || mEtQuantity.getText().toString().isEmpty() || mEtAmount.getText().toString().isEmpty()) {
                mEtName.setError(getString(R.string.cannot_be_empty));
                mEtQuantity.setError(getString(R.string.cannot_be_empty));
                mEtAmount.setError(getString(R.string.cannot_be_empty));
            } else {
                updateData(model);
            }
        }
    }

    private void opencamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    private void galleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_image_title)), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("code123", "onActivityResult: " + requestCode + "   " + resultCode);
        try {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                imageContent = MyUtiles.getPath(getActivity(), imageUri);
                source = getString(R.string.gallery);
                setColor(mBtnbtnGallery, mBtnLink, mBtnCamera);
            } else if (requestCode == CAMERA_PIC_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imageContent = MyUtiles.bitMapToString(image);
                source = getString(R.string.camera);
                setColor(mBtnCamera, mBtnLink, mBtnbtnGallery);
            }
        } catch (Exception e) {
            Log.d("code123", "Exception: " + e.getMessage());
            MyUtiles.checkReadPermission(getContext());
        }
    }

    private void setDialogBox() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertView = inflater.inflate(R.layout.link_layout, null);
        EditText editText = alertView.findViewById(R.id.et_link);
        Button mBtnCancel = alertView.findViewById(R.id.btn_link_cancel);
        Button mBtnOk = alertView.findViewById(R.id.btn_link_ok);
        if (model != null && model.getImage().getImageSource().equals("link")) {
            editText.setText(model.getImage().getImageContent());
        }
        alert.setCancelable(false);
        alert.setView(alertView);
        final AlertDialog dialog = alert.create();
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.empty_link, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("jji", "onClick:11 " + editText.getText().toString());
                    setColor(mBtnLink, mBtnCamera, mBtnbtnGallery);
                    source = getString(R.string.link);
                    imageContent = editText.getText().toString();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void updateData(DetailsModel model) {
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(ContentViewModel.class);
        DetailsModel detailsModel = new DetailsModel();
        SaveImage saveImage = new SaveImage();
        saveImage.setImageContent(imageContent);
        saveImage.setImageSource(source);
        detailsModel.setId(model.getId());
        detailsModel.setTitle(mEtName.getText().toString());
        detailsModel.setShortDesc(mEtShortDesc.getText().toString());
        detailsModel.setDesciption(mEtLongDesc.getText().toString());
        detailsModel.setImage(saveImage);
        detailsModel.setPrice(mEtAmount.getText().toString());
        detailsModel.setQuantity(mEtQuantity.getText().toString());
        viewModel.updateData(detailsModel);
        closeFragment();
    }

    private void setButtonColor(SaveImage saveImage) {
        if (saveImage.getImageSource() != null) {
            if (saveImage.getImageSource().equals(getString(R.string.camera))) {
                setColor(mBtnCamera, mBtnbtnGallery, mBtnLink);
            } else if (saveImage.getImageSource().equals(getString(R.string.gallery))) {
                setColor(mBtnbtnGallery, mBtnCamera, mBtnLink);
            } else if (saveImage.getImageSource().equals(getString(R.string.link))) {
                setColor(mBtnLink, mBtnCamera, mBtnbtnGallery);
            }
        }
    }

    private void setColor(Button btn1, Button btn2, Button btn3) {
        btn1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab)));
        btn2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        btn3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
    }

    private void closeFragment() {
        getFragmentManager().beginTransaction().remove(this).commit();
//        getActivity().overridePendingTransition(0, 0);
//        getActivity().recreate();
    }

    private void setViewModel() {
        Log.d("pop98", "setViewModel: " + model);
        if (model == null) {
            viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(ContentViewModel.class);
            DetailsModel model = new DetailsModel();
            SaveImage saveImage = new SaveImage();
            saveImage.setImageContent(imageContent);
            saveImage.setImageSource(source);
            model.setTitle(mEtName.getText().toString());
            model.setShortDesc(mEtShortDesc.getText().toString());
            model.setDesciption(mEtLongDesc.getText().toString());
            model.setImage(saveImage);
            model.setPrice(mEtAmount.getText().toString());
            model.setQuantity(mEtQuantity.getText().toString());
            viewModel.insertData(model);
            closeFragment();
        }
    }
}
