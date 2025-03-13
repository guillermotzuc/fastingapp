package com.health.personaltracker.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.health.personaltracker.R;
import com.health.personaltracker.model.HistoryItem;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    private Context context;
    private List<HistoryItem> items;

    public HistoryAdapter(Context context, List<HistoryItem> items) {
        super(context, R.layout.history_list_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the custom layout if convertView is null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false);
        }

        // Get the current item
        HistoryItem item = items.get(position);

        // Bind data to the views
        TextView labelTextView = convertView.findViewById(R.id.labelHistoryTextView);
        labelTextView.setText(item.getLabel());

        return convertView;
    }
}
