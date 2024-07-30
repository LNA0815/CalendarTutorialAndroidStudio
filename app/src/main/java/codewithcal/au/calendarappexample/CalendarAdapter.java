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

        // Überprüfe, ob der Tag dem ausgewählten Datum entspricht
        if (!daysOfMonth.get(position).equals("") && Integer.parseInt(daysOfMonth.get(position)) == selectedDate.getDayOfMonth())
        {
            holder.selectedDayBackground.setVisibility(View.VISIBLE); // Zeige die Umrandung
        }
        else
        {
            holder.selectedDayBackground.setVisibility(View.GONE); // Verberge die Umrandung
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
