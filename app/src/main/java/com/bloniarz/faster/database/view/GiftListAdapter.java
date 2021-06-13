package com.bloniarz.faster.database.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bloniarz.faster.MainActivity;
import com.bloniarz.faster.R;
import com.bloniarz.faster.database.Gift;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class GiftListAdapter extends ArrayAdapter<Gift> {
    private final Context context;

    public GiftListAdapter(@NonNull Context context, List<Gift> giftArrayList){
        super(context, 0, giftArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.gift_item, parent, false);
        }
        Gift gift = getItem(position);
        TextView courseTV = listItemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listItemView.findViewById(R.id.idIVcourse);
        courseTV.setText(gift.name);
        courseIV.setImageResource(gift.thumbnailId);
        return listItemView;
    }
}
