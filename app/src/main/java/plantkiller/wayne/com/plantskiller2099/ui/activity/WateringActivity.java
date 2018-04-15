package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import plantkiller.wayne.com.plantskiller2099.DirectionsJSONParser;
import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.database.TreeDataSource;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;

public class WateringActivity extends FragmentActivity implements OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, View.OnClickListener {
    private GoogleMap mMap;
    private List<TreeData> mTreeData;
    private TreeDataSource mDatabase;
    private List<LatLng> markerPoints;
    private int i = 0;
    private FloatingActionButton mBtnStartMisson;
    private FloatingActionButton mBtnCancelMisson;

    /*
    PlantKiller2099 update 04/02/2018
    update Database, link to new Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mBtnStartMisson = (FloatingActionButton) findViewById(R.id.btn_startMisson);
        mBtnCancelMisson = (FloatingActionButton) findViewById(R.id.btn_cancelMisson);
        setDb();
        mBtnStartMisson.setOnClickListener(this);
        mBtnCancelMisson.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng bachkhoa = new LatLng(21.004911, 105.844158);
        mTreeData = new ArrayList<>();
        markerPoints = new ArrayList<>();
        setDb();
        setupMap();
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bachkhoa));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    void setDb() {
        mDatabase = new TreeDataSource(getApplicationContext());
        mTreeData = mDatabase.getAllTree();
        for (i = 0; i < mTreeData.size(); i++) {
            mTreeData.get(i).setChoosen(false);
        }
    }

    public void setupMap() {
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            LatLng latLng = new LatLng(tree.getLat(), tree.getLong());
            MarkerOptions options = new MarkerOptions();
            options.position(latLng)
                .title(tree.getTreeName())
                .snippet(tree.getDes());
            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree));
            Marker marker = mMap.addMarker(options);
            setIcon(tree.isChoosen(), tree.getStatus(), marker);
        }
    }

    void setIcon(boolean chosen, int status, Marker marker) {
        if (chosen) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                .HUE_BLUE));
        } else {
            setIcon2(status, marker);
        }
    }

    void setIcon2(int status, Marker marker) {
        if (status == 2) marker.setIcon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        else if (status == 1) marker.setIcon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        else if (status == 3) marker.setIcon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        else if (status == 4) marker.setIcon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            if (marker.getTitle().equals(tree.getTreeName())) {
                if (!tree.isChoosen()) {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                        .HUE_BLUE));
                    tree.setChoosen(true);
                } else {
                    setIcon2(tree.getStatus(), marker);
                    tree.setChoosen(false);
                }
            }
        }
        return false;
    }

    void getDirection() {
        markerPoints.clear();
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            if (tree.isChoosen()) {
                LatLng latLng = new LatLng(tree.getLat(), tree.getLong());
                markerPoints.add(latLng);
            }
        }

        for (i = 0; i < markerPoints.size() - 1; i++) {
            double z = markerPoints.get(i).latitude;
            double y = markerPoints.get(i).longitude;

            LatLng origin = (LatLng) markerPoints.get(i);
            LatLng dest = (LatLng) markerPoints.get(i + 1);
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startMisson:
                mMap.clear();
                for (i = mTreeData.size()-1; i >= 0; i--) {
                    if (!mTreeData.get(i).isChoosen()) mTreeData.remove(mTreeData.get(i));
                }
                setupMap();
                getDirection();
                mBtnCancelMisson.setVisibility(View.VISIBLE);
                mBtnStartMisson.setVisibility(View.GONE);
                break;
            case R.id.btn_cancelMisson:
                mMap.clear();
                mTreeData.clear();
                markerPoints.clear();
                setDb();
                setupMap();
                mBtnCancelMisson.setVisibility(View.GONE);
                mBtnStartMisson.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask
        extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.width(2);
            //lineOptions.color();
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
            }
            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
}

