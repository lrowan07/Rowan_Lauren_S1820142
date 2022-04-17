package org.me.gcu.rowan_lauren_s1820142;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.rowan_lauren_s1820142.Models.FeedItem;

import java.util.ArrayList;

//Lauren Rowan S1820142

public class MapFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    Spinner mapTypeSpinner;
    GoogleMap map;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Initialise the spinner (dropdown)
        mapTypeSpinner = (Spinner) view.findViewById(R.id.mapTypeSpinner);
        mapTypeSpinner.setOnItemSelectedListener(this);
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        mapFragment.getMapAsync(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.item_types, android.R.layout.simple_spinner_item);

        mapTypeSpinner.setAdapter(spinnerAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        populateMap(((TabActivity)getActivity()).currentIncidentsItems);

    }

    public void populateMap(ArrayList<FeedItem> mapItems){
        //Remove any other points off the map
        map.clear();

        //Create object to hold the map coordinates and can return boundaries for the map based on its data
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        //Iterate over the items in the data and add the latlngs to the map and builder
        for(int i = 0; i < mapItems.size(); i++){
            LatLng coords = new LatLng(mapItems.get(i).getLatitude(), mapItems.get(i).getLongitude());
            map.addMarker(new MarkerOptions().position(coords).title(mapItems.get(i).getTitle()));
            boundsBuilder.include(coords);
        }

        if(mapItems.size() > 0){
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 20));
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                map.clear();
                break;
            case 1:
                populateMap(((TabActivity)getActivity()).currentRoadworksItems);
                break;
            case 2:
                populateMap(((TabActivity)getActivity()).plannedRoadworksItems);
                break;
            case 3:
                populateMap(((TabActivity)getActivity()).currentIncidentsItems);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}