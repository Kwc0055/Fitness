package com.kennethwcox.fitness;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kenneth on 10/21/15.
 */
public class ListAdapter extends ArrayAdapter<String> {
    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        //TextView lap = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.laplistview, null);
        }

        String p = getItem(position);
        String t;
        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.lapListText);

            if (tt1 != null) {
                if(p.startsWith("r")){
                    t = p.substring(1);
                    tt1.setTextColor(Color.RED);
                    tt1.setText(t);
                }else if(p.startsWith("g")){
                    t = p.substring(1);
                    tt1.setTextColor(Color.GREEN);
                    tt1.setText(t);
                }else{
                    tt1.setTextColor(Color.BLACK);
                    tt1.setText(p);
                }

            }

        }

        return v;
    }

}
