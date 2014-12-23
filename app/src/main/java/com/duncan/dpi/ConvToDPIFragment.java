package com.duncan.dpi;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by duncan on 21/10/14.
 */
public class ConvToDPIFragment extends Fragment {
    private EditText selected, width, height, size;
    private TextView zero, one, two, three, four, five, six, seven, eight, nine, del, plus, minus, equals, result, dot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.conv_to_dpi, container, false);
        width = (EditText)v.findViewById(R.id.main_width);
        height = (EditText)v.findViewById(R.id.main_height);
        size = (EditText)v.findViewById(R.id.main_size);

        setCurrent(width);
        setCurrent(height);
        setCurrent(size);

        zero = getTextView(v, R.id.zero);
        one = getTextView(v, R.id.one);
        two = getTextView(v, R.id.two);
        three = getTextView(v, R.id.three);
        four = getTextView(v, R.id.four);
        five = getTextView(v, R.id.five);
        six = getTextView(v, R.id.six);
        seven = getTextView(v, R.id.seven);
        eight = getTextView(v, R.id.eight);
        nine = getTextView(v, R.id.nine);

        del = (TextView)v.findViewById(R.id.del);
        plus = (TextView)v.findViewById(R.id.plus);
        minus = (TextView)v.findViewById(R.id.minus);
        equals = (TextView)v.findViewById(R.id.equals);
        dot = (TextView)v.findViewById(R.id.dot);
        result = (TextView)v.findViewById(R.id.main_result);

        //Initial
        selected = width;
        result.setText("0 dpi");

        setupOthers();
        return v;
    }

    private void setCurrent(final EditText et) {
        et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                selected = et;
                return false;
            }
        });
    }

    private void setupOthers() {
        //Other stuff
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curText = selected.getText().toString();
                if (curText.length() == 0) {
                    return;
                }
                selected.setText(curText.substring(0, curText.length() - 1));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double g = Double.parseDouble(selected.getText().toString());
                    g += 1;
                    selected.setText("" + g);
                } catch (Exception e) {}
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double g = Double.parseDouble(selected.getText().toString());
                    g -= 1;
                    selected.setText("" + g);
                } catch (Exception e) {}
            }
        });
        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double w = Double.parseDouble(width.getText().toString());
                    double h = Double.parseDouble(height.getText().toString());
                    double s = Double.parseDouble(size.getText().toString());
                    double dpi = Math.sqrt((w * w) + (h * h)) / (double)s;
                    result.setText(String.format("%.2f dpi", dpi));
                } catch (Exception g) {
                    Toast.makeText(getActivity(), "Invalid input!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selected.getText().toString().contains(".")) {
                    selected.append(".");
                }
            }
        });
    }

    private TextView getTextView(View v, int id) {
        final TextView t = (TextView)v.findViewById(id);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected.append(t.getText());
            }
        });
        return t;
    }
}
