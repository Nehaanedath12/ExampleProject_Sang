package com.example.exampleproject.Date;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exampleproject.R;

import java.util.List;

public class CalendarAdapter  extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{

    private List<DateClass> list;
    Context context;
    List<Holiday> holidayList;
    public OnClickListener onClickListener;

    public CalendarAdapter(List<DateClass> list, Context context, List<Holiday> holidayList) {
        this.context=context;
        this.list=list;
        this.holidayList=holidayList;
    }


    public void setOnclickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

    }
    public interface OnClickListener {

        void onItemClick(List<DateClass> list, int position);
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_cell, parent, false);
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        holder.dayOfMonth.setText(list.get(position).day);


        for (int i=0;i<holidayList.size();i++){
            String day=list.get(position).day;
            if(list.get(position).day.length()==1){
                day="0"+day;
            }

            String month=list.get(position).monthInt+"";
            if(list.get(position).monthInt<=9){
                month="0"+month;
            }

           String date=day+"-"+month+"-"+list.get(position).year;
           if(date.equals(holidayList.get(i).date)){
               holder.parent.setBackgroundColor(Color.GREEN);
           }
        }



        holder.dayOfMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(list,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfMonth;
        LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            parent = itemView.findViewById(R.id.parent);

        }
    }
}
