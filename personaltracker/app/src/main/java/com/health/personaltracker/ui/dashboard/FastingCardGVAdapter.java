package com.health.personaltracker.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.health.personaltracker.R;
import com.health.personaltracker.model.FastingCardModel;

import java.util.ArrayList;

public class FastingCardGVAdapter extends ArrayAdapter<FastingCardModel> {

    public FastingCardGVAdapter(@NonNull Context context, ArrayList<FastingCardModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.dashboard_card_item, parent, false);
        }

        FastingCardModel courseModel = getItem(position);
        TextView cardTV = listitemView.findViewById(R.id.dashboard_card_item_textview_id);
        ImageView cardIV = listitemView.findViewById(R.id.dashboard_card_item_image_view_id);

        cardTV.setText(courseModel.getCourse_name());
        cardIV.setImageResource(courseModel.getImgid());
        return listitemView;
    }
}
