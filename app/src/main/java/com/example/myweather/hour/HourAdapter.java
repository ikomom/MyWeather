package com.example.myweather.hour;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.myweather.R;

import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {
    private List<Hour> hourList;

    public HourAdapter(List<Hour> hourList) {
        this.hourList = hourList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourDegree;
        TextView hourTime;
        TextView hourText;

        public ViewHolder(View itemView) {
            super(itemView);
            hourDegree = (TextView)itemView.findViewById(R.id.hour_degree);
            hourTime = (TextView)itemView.findViewById(R.id.hout_time);
            hourText = (TextView)itemView.findViewById(R.id.hour_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hourly_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hour hour = hourList.get(position);
        holder.hourDegree.setText(hour.getDegree());
        holder.hourText.setText(hour.getText());
        holder.hourTime.setText(hour.getTime());
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }
}
