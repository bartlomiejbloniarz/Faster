package com.bloniarz.faster.database.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bloniarz.faster.R;

public class ScoreViewHolder extends RecyclerView.ViewHolder {
    private final TextView scoreItemView;

    public ScoreViewHolder(@NonNull View itemView) {
        super(itemView);
        scoreItemView = itemView.findViewById(R.id.scoreboard_text_view);
    }

    public void bind(String text){
        scoreItemView.setText(text);
    }

    static ScoreViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoreboard_view_item, parent, false);
        return new ScoreViewHolder(view);
    }
}
