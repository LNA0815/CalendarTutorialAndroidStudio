package codewithcal.au.calendarappexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    // Farben und Phasenlängen
    private String[] colors = {"#FFCCCC", "#FFEBEB", "#E0F7FA", "#E0FCE4", "#FFDAB9", "#FFFACD", "#D3D3D3"};
    private int[] phaseLengths = {1, 5, 5, 3, 6, 6, 7};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        ArrayList<Integer> backgroundColors = new ArrayList<>();

        // Starte ab einem bestimmten Datum (z.B. 1. Juli)
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate currentDate = startDate;

        for (int i = 0; i < daysInMonth.size(); i++) {
            if (!daysInMonth.get(i).equals("")) {
                LocalDate cellDate = selectedDate.withDayOfMonth(Integer.parseInt(daysInMonth.get(i)));
                int color = getColorForDate(cellDate, startDate);
                backgroundColors.add(color);
            } else {
                backgroundColors.add(Color.WHITE);
            }
        }

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, backgroundColors, this, selectedDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue()-1;

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

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private int getColorForDate(LocalDate date, LocalDate startDate)
    {
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, date);

        int accumulatedDays = 0;
        for (int i = 0; i < phaseLengths.length; i++) {
            accumulatedDays += phaseLengths[i];
            if (daysBetween < accumulatedDays) {
                return Color.parseColor(colors[i]);
            }
        }

        // Fallback (Falls die Tage außerhalb der Phasenlängen liegen)
        return Color.WHITE;
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            // Speichere den ausgewählten Tag und aktualisiere das RecyclerView
            selectedDate = selectedDate.withDayOfMonth(Integer.parseInt(dayText));
            setMonthView();
        }
    }
}
