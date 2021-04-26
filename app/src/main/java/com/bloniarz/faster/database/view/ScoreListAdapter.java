package com.bloniarz.faster.database.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bloniarz.faster.database.Score;

import java.text.DateFormat;

public class ScoreListAdapter extends ListAdapter<Score, ScoreViewHolder> {

    public ScoreListAdapter(@NonNull DiffUtil.ItemCallback<Score> diffCallback){
        super(diffCallback);
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ScoreViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score current = getItem(position);
        holder.bind(DateFormat.getDateInstance().format(current.date) + " " + current.score);
    }

    public static class ScoreDiff extends DiffUtil.ItemCallback<Score>{

        @Override
        public boolean areItemsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Score oldItem, @NonNull Score newItem) { //TODO
            return oldItem.date.equals(newItem.date);
        }
    }
}
