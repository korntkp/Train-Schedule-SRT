package io.github.shredktp.trainschedulesrt.select_station;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.Station;

import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.INTENT_EXTRA_KEY_STATION;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.REQUEST_CODE_END_STATION;
import static io.github.shredktp.trainschedulesrt.select_station.SelectStationActivity.REQUEST_CODE_START_STATION;

/**
 * Created by Korshreddern on 31-Jan-17.
 */

public class SelectStationAdapter extends RecyclerView.Adapter<SelectStationAdapter.ViewHolder> {

    private static final String TAG = "SelctStaAdapt";
    private ArrayList<Station> stationArrayList;
    private Context contextActivity;
    private int req;

    public SelectStationAdapter(ArrayList<Station> stationArrayList, Context contextActivity, int req) {
        this.stationArrayList = stationArrayList;
        this.contextActivity = contextActivity;
        this.req = req;
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
    public SelectStationAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new SelectStationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectStationAdapter.ViewHolder holder, final int position) {
        final String name = stationArrayList.get(position).getName();
        String line = stationArrayList.get(position).getLine();

        holder.tvName.setText(name);
        holder.tvLine.setText(line);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Station Name: " + name);
                returnStationResult(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationArrayList.size();
    }

    private void returnStationResult(String stationName) {
        Intent resultIntent = new Intent();
        if (req == REQUEST_CODE_START_STATION) {
            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, stationName);
        } else if (req == REQUEST_CODE_END_STATION) {
            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, stationName);
        } else {
            resultIntent.putExtra(INTENT_EXTRA_KEY_STATION, "-");
        }
        ((SelectStationActivity) contextActivity).setResult(Activity.RESULT_OK, resultIntent);
        ((SelectStationActivity) contextActivity).finish();
    }
}