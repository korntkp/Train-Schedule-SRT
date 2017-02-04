package io.github.shredktp.trainschedulesrt.show_schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.R;
import io.github.shredktp.trainschedulesrt.model.TrainSchedule;

/**
 * Created by Korshreddern on 04-Feb-17.
 */

public class ScheduleAdapter extends BaseAdapter {
    Context context;
    ArrayList<TrainSchedule> trainScheduleArrayList;

    public ScheduleAdapter(Context context, ArrayList<TrainSchedule> trainScheduleArrayList) {
        this.context = context;
        this.trainScheduleArrayList = trainScheduleArrayList;
    }

    @Override
    public int getCount() {
        return trainScheduleArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return trainScheduleArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.item_schedule, parent, false);

        TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
        TextView tvStart = (TextView) convertView.findViewById(R.id.tv_start);
        TextView tvEnd = (TextView) convertView.findViewById(R.id.tv_end);

        String trainNumber = trainScheduleArrayList.get(position).getNumber();
        String trainType = trainScheduleArrayList.get(position).getType();
        String startTime = trainScheduleArrayList.get(position).getStartTime();
        String endTime = trainScheduleArrayList.get(position).getEndTime();

        tvNumber.setText(trainNumber);
        tvType.setText(trainType);
        tvStart.setText(startTime);
        tvEnd.setText(endTime);

        return convertView;
    }
}
