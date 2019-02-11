package com.example.user.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txvShowRankingScoreItem;

        private ViewHolder(View itemView) {
            super(itemView);
            txvShowRankingScoreItem = itemView.findViewById(R.id.txv_show_ranking_score_item);
        }
    }

    private List<String> scoreData;

    public RankingAdapter(List<String> data) {
        this.scoreData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // connect layout file: ranking_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolder)viewHolder).txvShowRankingScoreItem.setText(scoreData.get(position));
    }

    @Override
    public int getItemCount() {
        return scoreData.size();
    }
}
