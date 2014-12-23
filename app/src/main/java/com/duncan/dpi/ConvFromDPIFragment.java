package com.duncan.dpi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duncan on 21/10/14.
 */
public class ConvFromDPIFragment extends Fragment {
    private EditText selected, dpi, size;
    private TextView zero, one, two, three, four, five, six, seven, eight, nine, del, plus, minus, equals, dot;
    private ListView result;
    private List<String> data = new ArrayList<String>();
    private List<Double> ratioLibrary = new ArrayList<Double>();
    private ArrayAdapter<String> arrayAdapter;
    private CheckBox checker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.conv_fr_dpi, container, false);
        dpi = (EditText)v.findViewById(R.id.sub_dpi);
        size = (EditText)v.findViewById(R.id.sub_size);

        populateLibrary();

        setCurrent(dpi);
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
        result = (ListView)v.findViewById(R.id.sub_result);
        checker = (CheckBox)v.findViewById(R.id.sub_checker);

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, data);
        result.setAdapter(arrayAdapter);

        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();


        //Initial
        selected = dpi;

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
                calculateResult(checker.isChecked());
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
        checker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!checker.isChecked()) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    calculateResult(false);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    checker.setChecked(true);
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Non-standard aspect ratio calculation is CPU-Intensive!\nAre you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } else {
                    calculateResult(checker.isChecked());
                }
            }
        });
    }

    private void calculateResult(boolean standard) {
        try {
            double dpiValue = Double.parseDouble(dpi.getText().toString());
            double sizeValue = Double.parseDouble(size.getText().toString());
            double squares = Math.pow(dpiValue * sizeValue, 2);
            arrayAdapter.clear();
            for (int i = 0; i <= 10000; i++) {
                int a = (int)Math.round(Math.sqrt(squares - (i * i)));
                if (standard) {
                    try {
                        double max = Math.max(i, a), min = Math.min(i, a);
                        double ratio = max / min;

                        for (double r : ratioLibrary) {
                            if (Math.abs(r - ratio) < 0.007) {
                                arrayAdapter.add(String.format("%d x %d", a, i));
                            }
                        }
                    } catch (Exception g) {}
                } else {
                    arrayAdapter.add(String.format("%d x %d", a, i));
                }
            }
            arrayAdapter.notifyDataSetChanged();
        } catch (Exception g) {
            //g.printStackTrace();
            Toast.makeText(getActivity(), "Invalid input!", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateLibrary() {
        ratioLibrary.add(16.0 / 9);
        ratioLibrary.add(16.0 / 10);
        ratioLibrary.add(4.0 / 3);
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

    private int FindHCF(int m, int n)
    {
        int temp, reminder;
        if (m < n)
        {
            temp = m;
            m = n;
            n = temp;
        }
        while (true)
        {
            reminder = m % n;
            if (reminder == 0)
                return n;
            else
                m = n;
            n = reminder;
        }
    }
}
