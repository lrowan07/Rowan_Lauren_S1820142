package org.me.gcu.rowan_lauren_s1820142;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import org.me.gcu.rowan_lauren_s1820142.Models.FeedItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Lauren Rowan S1820142

public class ListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner listTypeSpinner;
    RecyclerView list;
    ListAdapter la;

    EditText txtSearchName;
    EditText txtSearchDate;

    Button clearButton;

    ArrayList<FeedItem> items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);


        //Initialise the spinner (dropdown)
        listTypeSpinner = (Spinner) view.findViewById(R.id.listTypeSpinner);
        listTypeSpinner.setOnItemSelectedListener(this);

        list = (RecyclerView) view.findViewById(R.id.list);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.item_types, android.R.layout.simple_spinner_item);
        listTypeSpinner.setAdapter(spinnerAdapter);

        txtSearchName = (EditText) view.findViewById(R.id.txtSearchName);
        txtSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    populateList();
                    handled = true;
                }
                return handled;
            }
        });

        txtSearchDate = (EditText) view.findViewById(R.id.txtSearchDate);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        txtSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+ 1;
                        String date = day+"/"+month+"/"+year;
                        txtSearchDate.setText(date);

                        populateList();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        clearButton = (Button) view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearchName.setText("");
                txtSearchDate.setText("");
                populateList();
            }
        });




        // Inflate the layout for this fragment
        return view;
    }

    private void populateList(){

        switch(listTypeSpinner.getSelectedItemPosition()){
            case 0:
                items.clear();
                break;
            case 1:
                items = ((TabActivity)getActivity()).currentRoadworksItems;
                break;
            case 2:
                items = ((TabActivity)getActivity()).plannedRoadworksItems;
                break;
            case 3:
                items = ((TabActivity)getActivity()).currentIncidentsItems;
                break;
        }

        ArrayList<FeedItem> filteredItems = new ArrayList<FeedItem>();
        for(int i = 0; i < items.size(); i++){
            //If there is a search term specified
            if(txtSearchName.getText().toString().length() > 0) {
                //If the title of the loop item contains the search term
                if (items.get(i).getTitle().toLowerCase().contains(txtSearchName.getText().toString().toLowerCase())) {
                    //If there is a date selected
                    if (!txtSearchDate.getText().toString().equals("")) {
                        //And the item is not an incident (has a date)
                        if (!items.get(i).getType().equals("INCIDENT")) {
                            //And the selected date is between the start and end date
                            try {
                                Date parsedSelectedDate = new SimpleDateFormat("dd/MM/yyyy").parse(txtSearchDate.getText().toString());
                                if (parsedSelectedDate.after(items.get(i).getStartDate()) && parsedSelectedDate.before(items.get(i).getEndDate())) {
                                    filteredItems.add(items.get(i));
                                }
                            } catch (ParseException e) {
                                Log.i("Error", "Could not parse input date to date object");
                            }
                        } else {//Else the item is an incident, so we should check the pubDate not start/end
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                if (sdf.format(items.get(i).getPublishDate()).equals(sdf.parse(txtSearchDate.getText().toString()))) {
                                    filteredItems.add(items.get(i));
                                }
                            } catch(ParseException e){
                                Log.i("Error", "Could not parse input date to date object");
                            }
                        }
                    } else { //else there is no date selected, add it anyway
                        filteredItems.add(items.get(i));
                    }


                }
            } else {
                filteredItems.add(items.get(i));
            }
        }

        la = new ListAdapter(filteredItems);
        list.setAdapter(la);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        populateList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}