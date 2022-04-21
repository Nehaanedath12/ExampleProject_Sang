package com.example.exampleproject.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.exampleproject.databinding.ActivityDateBinding;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateActivity extends AppCompatActivity {

    ActivityDateBinding binding;

    private LocalDate selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityDateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        selectedDate = LocalDate.now();
        setMonthView();




    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        List<Holiday>holidayList=new ArrayList<>();
        Holiday holiday=new Holiday("23-12-2021","leave1");
        holidayList.add(holiday);

        binding.monthYearTV.setText(String.format("%s %s", monthFromDate(selectedDate), yearFromDate(selectedDate)));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        List<DateClass>classList=new ArrayList<>();
        for (int i=0;i<daysInMonth.size();i++) {
            classList.add(new DateClass(daysInMonth.get(i),monthFromDate(selectedDate),Integer.parseInt(intMonthFromDate(selectedDate)),yearFromDate(selectedDate)));
        }

        Log.d("ppppppppppppp", monthFromDate(selectedDate)+"  "+Integer.parseInt(intMonthFromDate(selectedDate))+" "+yearFromDate(selectedDate));

        CalendarAdapter calendarAdapter = new CalendarAdapter(classList, this,holidayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);




        calendarAdapter.setOnclickListener(new CalendarAdapter.OnClickListener() {
            @Override
            public void onItemClick(List<DateClass> list, int position) {

//                Toast.makeText(DateActivity.this, list.get(position).getDay(), Toast.LENGTH_SHORT).show();
                String message =  list.get(position).day + " " +  list.get(position).monthInt+" "+  list.get(position).year;
                Toast.makeText(DateActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String intMonthFromDate(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthFromDate(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String yearFromDate(LocalDate date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }


}