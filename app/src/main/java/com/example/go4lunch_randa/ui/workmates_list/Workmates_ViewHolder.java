package com.example.go4lunch_randa.ui.workmates_list;

import static com.example.go4lunch_randa.utils.GetTodayDate.getTodayDate;

import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch_randa.R;
import com.example.go4lunch_randa.api.firebase.RestaurantsHelper;
import com.example.go4lunch_randa.databinding.FragmentWorkmateItemBinding;
import com.example.go4lunch_randa.models.Workmate;
import com.example.go4lunch_randa.utils.ChangeColorWorkmate;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Workmates_ViewHolder extends RecyclerView.ViewHolder {

    FragmentWorkmateItemBinding binding;

    public Workmates_ViewHolder(FragmentWorkmateItemBinding itemview) {
        super(itemview.getRoot());
        binding = itemview;

    }

    public void updateWithData(Workmate result) {
        if (result.getUrlPicture() != null) {
            Glide.with(itemView).load(result.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(binding.itemListAvatar);
        } else {
            Glide.with(itemView).load(R.drawable.ic_anon_user_48dp).apply(RequestOptions.circleCropTransform()).into(binding.itemListAvatar);
        }
        RestaurantsHelper.getBooking(result.getUid(), getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().size() == 1) { // This user has made his (only) choice
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        this.binding.itemListName.setText(itemView.getResources().getString(R.string.eating_at, result.getName(), restaurant.getData().get("restaurantName")));
                        binding.itemListName.setTypeface(binding.itemListName.getTypeface(), Typeface.BOLD);
                    }

                    //  This user hasn't decided today
                } else {
                    binding.itemListName.setText(itemView.getResources().getString(R.string.hasnt_decided, result.getName()));
                    ChangeColorWorkmate.changeTextColor(R.color.colorGrey1, binding.itemListName);
                    binding.itemListName.setTypeface(binding.itemListName.getTypeface(), Typeface.ITALIC);
                }
            }
        });
    }
}
