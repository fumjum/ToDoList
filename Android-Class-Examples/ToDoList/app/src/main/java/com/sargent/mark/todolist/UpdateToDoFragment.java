package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

/**
 * Created by mark on 7/5/17.
 */

public class UpdateToDoFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private EditText toDo;
    private DatePicker dp;
    private Button add;
    private Button done;
    private Spinner spinner;
    private String category;
    private final String TAG = "updatetodofragment";
    private String isDone;
    private long id;


    public UpdateToDoFragment(){}

    //added category and isDone variables in newInstance method so the app will update these items
    public static UpdateToDoFragment newInstance(int year, int month, int day, String description, String category, String isDone, long id) {
        UpdateToDoFragment f = new UpdateToDoFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putLong("id", id);
        args.putString("description", description);
        args.putString("category", category);
        args.putString("isDone", isDone);

        f.setArguments(args);

        return f;
    }

    //To have a way for the activity to get the data from the dialog
    public interface OnUpdateDialogCloseListener {
        void closeUpdateDialog(int year, int month, int day, String description, String category, String isDone, long id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
        toDo = (EditText) view.findViewById(R.id.toDo);
        dp = (DatePicker) view.findViewById(R.id.datePicker);
        done = (Button) view.findViewById(R.id.done);
        add = (Button) view.findViewById(R.id.add);

        //gets the spinner from category.xml
        spinner = (Spinner) view.findViewById(R.id.category);

        //tells the spinner to listen for selection
        spinner.setOnItemSelectedListener(this);

        //Adapter to fill the spinner with flair
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);

        //ArrayAdapter needs a TextView for some reason
        adapter.setDropDownViewResource(R.layout.spinner);

        //places the adapter into the spinner
        spinner.setAdapter(adapter);
        //puts the selected item from the spinner into the category variable as a string
        //category = spinner.getSelectedItem().toString();

        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
        id = getArguments().getLong("id");
        String description = getArguments().getString("description");
        //category = getArguments().getString("category");
        //for some reason updating an item placed the calendar at the wrong month so I changed it
        dp.updateDate(year, month - 1, day);

        toDo.setText(description);

        //sets the Mark as Done button visible in the fragment to do adder layout
        done.setVisibility(View.VISIBLE);
        //sets a click listener to update the database when the Mark as Done button is clicked
        done.setOnClickListener(new View.OnClickListener() {
            //when Mark as Done button is clicked it updates the database BUT DOES NOT CLOSE THE DIALOG BOX LIKE WHEN YOU CLICK THE UPDATE BUTTON
            @Override
            public void onClick(View view) {
                // sets isDone for the selected item to the string "done" which will allow a different thing to happen in the ToDoListAdapter bind method
                isDone = "done";

                //if Mark as Done is clicked, Button changes to instruct user to click Update
                done.setText("Marked Done. Click Update to Return");

                //UpdateToDoFragment.OnUpdateDialogCloseListener activity = (UpdateToDoFragment.OnUpdateDialogCloseListener) getActivity();
                //Log.d(TAG, "id: " + id);
                //activity.closeUpdateDialog(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), toDo.getText().toString(), category, isDone, id);
            }
        });

        add.setText("Update");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDone == null)
                    isDone = "not done";
                //Change back to Mark as Done in case item is clicked after Done
                done.setText("Mark as Done");
                UpdateToDoFragment.OnUpdateDialogCloseListener activity = (UpdateToDoFragment.OnUpdateDialogCloseListener) getActivity();
                Log.d(TAG, "id: " + id);
                //The months in update dialog are one month behind for some reason so a changed it to + 1
                activity.closeUpdateDialog(dp.getYear(), dp.getMonth() + 1, dp.getDayOfMonth(), toDo.getText().toString(), category, isDone, id);
                UpdateToDoFragment.this.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        category = spinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}