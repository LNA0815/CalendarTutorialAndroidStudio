package codewithcal.au.calendarappexample;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final ArrayList<Integer> backgroundColors;
    private final OnItemListener onItemListener;
    private final LocalDate selectedDate;

    public CalendarAdapter(ArrayList<String> daysOfMonth, ArrayList<Integer> backgroundColors, OnItemListener onItemListener, LocalDate selectedDate)
    {
        this.daysOfMonth = daysOfMonth;
        this.backgroundColors = backgroundColors;
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.itemView.setBackgroundColor(backgroundColors.get(position));

        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        // Überprüfe, ob der Tag dem ausgewählten Datum entspricht
        if (!daysOfMonth.get(position).equals("") && Integer.parseInt(daysOfMonth.get(position)) == selectedDate.getDayOfMonth())
        {
            holder.selectedDayBackground.setVisibility(View.VISIBLE); // Zeige die Umrandung
        }
        else
        {
            holder.selectedDayBackground.setVisibility(View.GONE); // Verberge die Umrandung
        }

        // Überprüfen, ob der Tag der heutige Tag ist
        if (!dayText.equals("") && Integer.parseInt(dayText) == LocalDate.now().getDayOfMonth()
                && selectedDate.getMonth() == LocalDate.now().getMonth()
                && selectedDate.getYear() == LocalDate.now().getYear()) {
            holder.dayOfMonth.setTypeface(null, android.graphics.Typeface.BOLD); // Heute fett darstellen
            holder.dayOfMonth.setTextColor(Color.RED); // Färbt den Text rot, wenn es der aktuelle Tag ist
        } else {
            holder.dayOfMonth.setTypeface(null, android.graphics.Typeface.NORMAL); // Normale Schriftart
            holder.dayOfMonth.setTextColor(Color.BLACK); // Färbt den Text rot, wenn es der aktuelle Tag ist

        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
