package com.example.go4lunch_randa.ui.workmates_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch_randa.databinding.FragmentWorkmateItemBinding;
import com.example.go4lunch_randa.models.Workmate;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Workmates_RecyclerViewAdapter extends RecyclerView.Adapter<Workmates_ViewHolder> {
    private final List<Workmate> mWorkmates;


    public Workmates_RecyclerViewAdapter(List<Workmate> items) {
        mWorkmates = items;
    }


    @NotNull
    @Override
    public Workmates_ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewtype) {


        FragmentWorkmateItemBinding view = FragmentWorkmateItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new Workmates_ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Workmates_ViewHolder holder, int position) {
        holder.updateWithData(this.mWorkmates.get(position));
    }


    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mWorkmates != null) itemCount = mWorkmates.size();
        return itemCount;
    }

    public Workmate getWorkmates(int position) {
        return this.mWorkmates.get(position);
    }
}
