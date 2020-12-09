package com.abhijeet14.cetbus.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abhijeet14.cetbus.R;

public class RunningViewHolder extends RecyclerView.ViewHolder {
    private TextView routeNameText;
    private CardView runningCardMain;
    public RunningViewHolder(@NonNull View itemView) {
        super(itemView);
        routeNameText = itemView.findViewById(R.id.route_name);
        runningCardMain = itemView.findViewById(R.id.bus_card_main);
    }
    public void setRouteName(String routeName){
        routeNameText.setText(routeName);
    }
    public CardView routeCard(){
        return runningCardMain;
    }
}
