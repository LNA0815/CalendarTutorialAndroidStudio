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

    // Phasenfarben
    private final Map<String, String> phasesColors = new HashMap<String, String>() {{
        put("EFP_1", "#FFCCCC");
        put("EFP_2", "#FFEBEB");
        put("MFP", "#E0F7FA");
        put("LFP", "#E0FCE4");
        put("ELP", "#FFDAB9");
        put("MLP", "#FFFACD");
        put("LLP", "#D3D3D3");
    }};

    // Längen der Phasen (Anzahl der Tage)
    private final Map<String, Integer> phasesLengths = new HashMap<String, Integer>() {{
        put("EFP_1", 5);
        put("EFP_2", 3);
        put("MFP", 7);
        put("LFP", 4);
        put("ELP", 6);
        put("MLP", 2);
        put("LLP", 3);
    }};

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
            // Beispiel für zufällige Farben (du kannst hier deine eigene Logik verwenden)
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

    private int getColorForDate(LocalDate date, LocalDate startDate) {
        int daysFromStart = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, date);
        int totalDays = 0;

        for (Map.Entry<String, Integer> entry : phasesLengths.entrySet()) {
            String phase = entry.getKey();
            int length = entry.getValue();

            if (daysFromStart < totalDays + length) {
                return Color.parseColor(phasesColors.get(phase));
            }

            totalDays += length;
        }

        return Color.WHITE; // Fallback-Farbe
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








