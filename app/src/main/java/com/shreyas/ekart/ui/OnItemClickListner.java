package com.shreyas.ekart.ui;

import com.shreyas.ekart.models.DetailsModel;

import java.util.List;

public interface OnItemClickListner {

    void OnItemClick(int position, List<DetailsModel> mList, String color);
}
