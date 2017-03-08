package io.github.shredktp.trainschedulesrt.history_search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.offline_schedule.OfflineScheduleActivity;

/**
 * Created by Korshreddern on 08-Mar-17.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    private ArrayList<PairStation> pairStationArrayList;
    private Context context;

    public HistoryRecyclerAdapter(ArrayList<PairStation> pairStationArrayList, Context context) {
        this.pairStationArrayList = pairStationArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartStion;
        TextView tvEndStion;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStartStion = (TextView) itemView.findViewById(R.id.tv_start_station);
            tvEndStion = (TextView) itemView.findViewById(R.id.tv_end_station);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pair_station, parent, false);
        return new HistoryRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String startStation = pairStationArrayList.get(position).getStartStation();
        final String endStation = pairStationArrayList.get(position).getEndStation();

        holder.tvStartStion.setText(startStation);
        holder.tvEndStion.setText(endStation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OfflineScheduleActivity.class);
                intent.putExtra("startStation", startStation);
                intent.putExtra("endStation", endStation);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pairStationArrayList.size();
    }
}
