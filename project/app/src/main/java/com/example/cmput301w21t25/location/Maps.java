package com.example.cmput301w21t25.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301w21t25.R;

import com.example.cmput301w21t25.trials.Trial;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Maps extends Fragment{
    private Location TrialLocation = new Location("dummyprovider");
    private ArrayList<Trial> TrialList;
    private OnMapReadyCallback callbackTrial = new SetTrialLocation();
    private OnMapReadyCallback callbackExp = new ExperimentMap();
    private String mode;
    private Activity activity;


    /**
     * Fragment for setting the location of trials
     */
    public class SetTrialLocation implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {


        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }

        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            //Get marker position
            LatLng pos = marker.getPosition();
            TrialLocation.setLatitude(pos.latitude);
            TrialLocation.setLongitude(pos.longitude);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng mytrial = new LatLng(TrialLocation.getLatitude(), TrialLocation.getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.draggable(true);
            Marker marker = googleMap.addMarker(options.position(mytrial).title("Click and hold to drag me"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(mytrial));
            activity.findViewById(R.id.button3).setVisibility(View.VISIBLE);

            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMarkerDragListener(this);

        }
    }

    /**
     * Fragment for viewing the map of trials in an experiment
     */
    public class ExperimentMap implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {


        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MarkerOptions options = new MarkerOptions();
            options.draggable(false);
            LatLng myTrial = null;

            for (Trial trial : TrialList) {
                //String title = trial.getUser() + "'s Trial";
                String title = "Trial Location";
                GeoPoint geoPoint = trial.getGeoPoint();
                myTrial = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                Marker marker = googleMap.addMarker(options.position(myTrial).title(title));
            }

            //show exit button
            activity.findViewById(R.id.button3).setVisibility(View.VISIBLE);

            if (!TrialList.isEmpty()) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myTrial));
            }
            googleMap.setOnMarkerClickListener(this);


        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mode = getArguments().getString("MODE");
        if (mode.equals("Trial")) {
            TrialLocation = getArguments().getParcelable("TrialLocation");
        }
        else {
            TrialList = (ArrayList<Trial>) getArguments().getSerializable("TrialList");
            if (TrialList == null) {
                TrialList = new ArrayList<Trial>();
            }
        }


        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        activity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            if (mode.equals("Trial")) {
                mapFragment.getMapAsync(callbackTrial);
            }
            else {
                mapFragment.getMapAsync(callbackExp);
            }
        }
    }

    public Location getTrialLocation() {
        return TrialLocation;
    }

    public void setTrialLocation(Location trialLocation) {
        TrialLocation = trialLocation;
    }
}