package io.github.shredktp.trainschedulesrt.select_station;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.model.Station;

/**
 * Created by Korshreddern on 31-Jan-17.
 */

public class SelectStationAdapter extends RecyclerView.Adapter<SelectStationAdapter.ViewHolder> {

    private ArrayList<Station> stationArrayList;

    public SelectStationAdapter(ArrayList<Station> stationArrayList) {
        this.stationArrayList = stationArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvLine;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_station);
            tvLine = (TextView) itemView.findViewById(R.id.tv_line_station);
        }
    }

    @Override
    public SelectStationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new SelectStationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectStationAdapter.ViewHolder holder, int position) {
        String name = stationArrayList.get(position).getName();
        String line = stationArrayList.get(position).getLine();

        holder.tvName.setText(name);
        holder.tvLine.setText(line);
    }

    @Override
    public int getItemCount() {
        return stationArrayList.size();
    }
}