package io.github.shredktp.trainschedulesrt.unused;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.data.PairStation;

/**
 * Created by Korshreddern on 13-Feb-17.
 */

public class PairStationAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PairStation> pairStationArrayList;

    public PairStationAdapter(Context context, ArrayList<PairStation> pairStationArrayList) {
        this.context = context;
        this.pairStationArrayList = pairStationArrayList;
    }

    @Override
    public int getCount() {
        return pairStationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return pairStationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.item_pair_station, parent, false);

        TextView tvStartStation = (TextView) convertView.findViewById(R.id.tv_start_station);
        TextView tvEndStation = (TextView) convertView.findViewById(R.id.tv_end_station);
//        TextView tvStart = (TextView) convertView.findViewById(R.id.tv_start);
//        TextView tvEnd = (TextView) convertView.findViewById(R.id.tv_end);

        String startStation = pairStationArrayList.get(position).getStartStation();
        String endStation = pairStationArrayList.get(position).getEndStation();
//        String startTime = pairStationArrayList.get(position).isSeeItFirst();
//        String endTime = pairStationArrayList.get(position).getTimestamp();

        tvStartStation.setText(startStation);
        tvEndStation.setText(endStation);
//        tvStart.setText(startTime);
//        tvEnd.setText(endTime);

        return convertView;
    }
}
