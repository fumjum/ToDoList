package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by mark on 7/4/17.
 */

public class AddToDoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText toDo;
    private DatePicker dp;
    private Button add;
    private Spinner spinner;
    private String category;
    private String isDone = "Not Done";
    private final String TAG = "addtodofragment";

    public AddToDoFragment() {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //sets the String category to the spinner selection in the fragment
        category = spinner.getItemAtPosition(position).toString();
    }

    //Does nothing but is required when implementing OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //To have a way for the activity to get the data from the dialog
    public interface OnDialogCloseListener {
        void closeDialog(int year, int month, int day, String category, String isDone, String description);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
        toDo = (EditText) view.findViewById(R.id.toDo);
        dp = (DatePicker) view.findViewById(R.id.datePicker);
        add = (Button) view.findViewById(R.id.add);

        //added spinner variable that is taken from the category
        spinner = (Spinner) view.findViewById(R.id.category);
        //set the adapter to listen for a selection from the spinner
        spinner.setOnItemSelectedListener(this);

        //ArrayAdapter is used to set the entries in the spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);

        //ArrayAdapter requires a TextView for some reason
        adapter.setDropDownViewResource(R.layout.spinner);

        //sets the adapter to the spinner, populating the entries
        spinner.setAdapter(adapter);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dp.updateDate(year, month, day);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDialogCloseListener activity = (OnDialogCloseListener) getActivity();
                activity.closeDialog(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), category, isDone, toDo.getText().toString());
                AddToDoFragment.this.dismiss();
            }
        });

        return view;
    }
}



