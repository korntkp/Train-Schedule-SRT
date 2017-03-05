package io.github.shredktp.trainschedulesrt.show_schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.TrainSchedule;

/**
 * Created by Korshreddern on 04-Mar-17.
 */

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    ArrayList<TrainSchedule> trainScheduleArrayList;

    public ScheduleRecyclerViewAdapter(ArrayList<TrainSchedule> trainScheduleArrayList) {
        this.trainScheduleArrayList = trainScheduleArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvType;
        TextView tvStart;
        TextView tvEnd;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvStart = (TextView) itemView.findViewById(R.id.tv_start);
            tvEnd = (TextView) itemView.findViewById(R.id.tv_end);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String trainNumber = trainScheduleArrayList.get(position).getNumber();
        String trainType = trainScheduleArrayList.get(position).getType();
        String startTime = trainScheduleArrayList.get(position).getStartTime();
        String endTime = trainScheduleArrayList.get(position).getEndTime();

        holder.tvNumber.setText(trainNumber);
        holder.tvType.setText(trainType);
        holder.tvStart.setText(startTime);
        holder.tvEnd.setText(endTime);
    }

    @Override
    public int getItemCount() {
        return trainScheduleArrayList.size();
    }
}
