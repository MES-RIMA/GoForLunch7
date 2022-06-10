package com.example.go4lunch_randa.ui.restaurants_list;

import static com.example.go4lunch_randa.utils.Constants.BASE_URL_PLACE_PHOTO;
import static com.example.go4lunch_randa.utils.Constants.CLOSED;
import static com.example.go4lunch_randa.utils.Constants.CLOSING_SOON;
import static com.example.go4lunch_randa.utils.Constants.MAX_HEIGHT;
import static com.example.go4lunch_randa.utils.Constants.MAX_WIDTH;
import static com.example.go4lunch_randa.utils.Constants.OPEN;
import static com.example.go4lunch_randa.utils.Constants.OPENING_HOURS_NOT_KNOW;
import static com.example.go4lunch_randa.utils.FormatTime.formatTime;

import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch_randa.BuildConfig;
import com.example.go4lunch_randa.R;
import com.example.go4lunch_randa.api.firebase.RestaurantsHelper;
import com.example.go4lunch_randa.databinding.FragmentRestaurantItemBinding;
import com.example.go4lunch_randa.models.googleplaces_gson.ResultDetails;
import com.example.go4lunch_randa.utils.DisplayRating;
import com.example.go4lunch_randa.utils.GetTodayDate;

import java.util.Calendar;

public class RestaurantsList_ViewHolder extends RecyclerView.ViewHolder {

    FragmentRestaurantItemBinding binding;


    public RestaurantsList_ViewHolder(@NonNull FragmentRestaurantItemBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public void updateWithData(ResultDetails resultDetails, String mLocation) {
        RequestManager glide = Glide.with(itemView);

        // NUMBER OF WORKMATES AT THIS RESTAURANT
        RestaurantsHelper.getTodayBooking(resultDetails.getPlaceId(), GetTodayDate.getTodayDate()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                binding.workmateOnRestaurant.setText(itemView.getResources().getString(R.string.number_workmates, String.valueOf(task.getResult().size())));
            }
        });

        // NAME
        binding.nameRestaurant.setText(resultDetails.getName());

        // ADDRESS
        binding.adressRestaurant.setText(resultDetails.getFormattedAddress());

        // DISTANCE
        binding.distanceRestaurant.setText(itemView.getResources().getString(R.string.unit_distance, String.valueOf(resultDetails.getDistance())));

        // RATING
        DisplayRating.displayRating(resultDetails, binding.starRestaurant);

        // PHOTO RESTAURANT
        if (!(resultDetails.getPhotos() == null)) {
            if (!(resultDetails.getPhotos().isEmpty())) {
                glide.load(BASE_URL_PLACE_PHOTO + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultDetails.getPhotos().get(0)
                        .getPhotoReference() + "&key=" + BuildConfig.api_key)
                        .apply(RequestOptions.centerCropTransform()).into(binding.itemAvatarRestaurant);
            }
        } else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(binding.itemAvatarRestaurant);
        }

        // OPENING HOURS
        if (resultDetails.getOpeningHours() != null) {
            if (resultDetails.getOpeningHours().getOpenNow().toString().equals("false")) {
                displayOpeningHour(CLOSED, null);
            } else {
                getOpeningHoursInfo(resultDetails);
            }
        } else {
            displayOpeningHour(OPENING_HOURS_NOT_KNOW, null);
        }
    }


    private void getOpeningHoursInfo(ResultDetails resultDetails) {
        int[] daysArray = {0, 1, 2, 3, 4, 5, 6};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minOfDay = calendar.get(Calendar.MINUTE);
        if (minOfDay < 10) {
            minOfDay = '0' + minOfDay;
        }
        String currentHourString = hourOfDay + String.valueOf(minOfDay);
        int currentHour = Integer.parseInt(currentHourString);

        for (int i = 0; i < resultDetails.getOpeningHours().getPeriods().size(); i++) {
            if (resultDetails.getOpeningHours().getPeriods().get(i).getOpen().getDay() == daysArray[day] && resultDetails.getOpeningHours().getPeriods().get(i).getClose() != null) {
                String closeHour = resultDetails.getOpeningHours().getPeriods().get(i).getClose().getTime();
                if (currentHour < Integer.parseInt(closeHour) || daysArray[day] < resultDetails.getOpeningHours().getPeriods().get(i).getClose().getDay()) {
                    int timeDifference = Integer.parseInt(closeHour) - currentHour;
                    if (timeDifference <= 30 && daysArray[day] == resultDetails.getOpeningHours().getPeriods().get(i).getClose().getDay()) {
                        displayOpeningHour(CLOSING_SOON, closeHour);
                    } else {
                        displayOpeningHour(OPEN, resultDetails.getOpeningHours().getPeriods().get(i).getClose().getTime());
                    }
                }
                break;
            }
        }
    }

    private void displayOpeningHour(String type, String hour) {
        switch (type) {
            case OPEN:
                binding.OpenHour.setText(itemView.getResources().getString(R.string.open_until, formatTime(itemView.getContext(), hour)));
                binding.OpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                binding.OpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case CLOSED:
                binding.OpenHour.setText(R.string.restaurant_closed);
                binding.OpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorError));
                binding.OpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case CLOSING_SOON:
                binding.OpenHour.setText(itemView.getResources().getString(R.string.closing_soon, formatTime(itemView.getContext(), hour)));
                binding.OpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorCloseSoon));
                binding.OpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case OPENING_HOURS_NOT_KNOW:
                binding.OpenHour.setText(R.string.restaurant_opening_not_know);
                binding.OpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorCloseSoon));
                binding.OpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
        }
    }
}