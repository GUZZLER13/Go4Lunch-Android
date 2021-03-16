package com.guzzler.go4lunch_p7.ui.list_restaurants;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListRestaurantsViewHolder extends RecyclerView.ViewHolder {
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 200;

    @BindView(R.id.name_restaurant)
    public TextView mNameRestaurant;
    @BindView(R.id.adress_restaurant)
    public TextView mAdressRestaurant;
    @BindView(R.id.item_avatar_restaurant)
    public ImageView mAvatarRestaurant;


    public ListRestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(ResultSearch resultSearch, String location) {
        RequestManager glide = Glide.with(itemView);

        this.mNameRestaurant.setText(resultSearch.getName());
        this.mAdressRestaurant.setText(resultSearch.getVicinity());

        //Chargement de la photo dans la liste des restaus
        if (!(resultSearch.getPhotos() == null)) {
            if (!(resultSearch.getPhotos().isEmpty())) {
                glide.load(BASE_URL + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultSearch.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.api_key).apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
//                Glide.with(mAvatarRestaurant).load(BASE_URL + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultSearch.getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.api_key)
//                        .into(mAvatarRestaurant);
            }
        } else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
        }
    }
}