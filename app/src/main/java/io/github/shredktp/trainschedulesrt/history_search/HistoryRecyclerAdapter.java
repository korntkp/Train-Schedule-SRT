package io.github.shredktp.trainschedulesrt.history_search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.PairStation;
import io.github.shredktp.trainschedulesrt.data.source.pair_station.PairStationLocalDataSource;
import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;
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
        TextView tvStartStation;
        TextView tvEndStation;
        ImageView imageViewDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStartStation = (TextView) itemView.findViewById(R.id.tv_start_station);
            tvEndStation = (TextView) itemView.findViewById(R.id.tv_end_station);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.image_view_delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pair_station, parent, false);
        return new HistoryRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String startStation = pairStationArrayList.get(holder.getAdapterPosition()).getStartStation();
        final String endStation = pairStationArrayList.get(holder.getAdapterPosition()).getEndStation();

        holder.tvStartStation.setText(startStation);
        holder.tvEndStation.setText(endStation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OfflineScheduleActivity.class);
                intent.putExtra("startStation", startStation);
                intent.putExtra("endStation", endStation);
                context.startActivity(intent);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 12-Mar-17 Show Dialog
                pairStationArrayList.remove(holder.getAdapterPosition());
                int deletePairStation = PairStationLocalDataSource.getInstance(context).deleteByStation(startStation, endStation);
                int deleteTrainSchedule = TrainScheduleLocalDataSource.getInstance(context).deleteByStation(startStation, endStation);
                Log.d("Delete", "deletePairStation: " + deletePairStation);
                Log.d("Delete", "deleteTrainSchedule: " + deleteTrainSchedule);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pairStationArrayList.size();
    }
}
