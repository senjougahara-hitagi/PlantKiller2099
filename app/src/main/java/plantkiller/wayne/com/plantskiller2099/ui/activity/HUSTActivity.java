package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.database.TreeDataSource;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;
import plantkiller.wayne.com.plantskiller2099.ui.fragment.FormFragment;

public class HUSTActivity extends FragmentActivity
    implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
    .OnConnectionFailedListener, LocationListener, View.OnClickListener,
    FloatingActionMenu.OnMenuToggleListener,
    GoogleMap.OnMarkerClickListener,
    InfoWindowManager.WindowShowListener {
    private GoogleMap mMap;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    Location mMyLocation;
    LocationRequest mLocationRequest;
    FloatingActionButton mBtnWatering;
    FloatingActionButton mBtnStart;
    FloatingActionButton mBtnStop;
    FloatingActionButton mBtnLocation;
    private FloatingActionMenu fam;
    private FloatingActionButton fabEdit, fabDelete, fabAdd;
    private List<TreeData> mTree;
    private List<TreeData> mTreeData;
    private List<LatLng> mMarkerPoints;
    private TreeDataSource mDatabase;
    private ImageView mImage;
    private TextView mText;
    private TreeData mResult;
    private InfoWindowManager infoWindowManager;
    private Boolean isStarted = false;
    private int i;
    Marker mWater_1;
    Marker mWater_2;
    Marker mWater_3;
    Marker mWater_4;
    Marker mWater_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hust);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        final MapInfoWindowFragment mapInfoWindowFragment =
            (MapInfoWindowFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapInfoWindowFragment.getMapAsync(this);
        initView();
        setButton();
        infoWindowManager = mapInfoWindowFragment.infoWindowManager();
        infoWindowManager.setHideOnFling(true);
        infoWindowManager.setWindowShowListener(HUSTActivity.this);
        mTree = new ArrayList<>();
        mTreeData = new ArrayList<>();
        mMarkerPoints = new ArrayList<>();
        mDatabase = new TreeDataSource(getApplicationContext());
        addTree();
        setDb();
    }

    private void initView() {
        mBtnStart = (FloatingActionButton) findViewById(R.id.btn_start);
        mBtnLocation = (FloatingActionButton) findViewById(R.id.btn_location);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab1);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab2);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab3);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);
        mBtnWatering = findViewById(R.id.btn_watering);
        mBtnStop = findViewById(R.id.btn_cancel);
        mImage = findViewById(R.id.image_batman);
        mText = findViewById(R.id.text_batman);
    }

    public void setButton() {
        mBtnLocation.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        fam.setOnMenuToggleListener(this);
        fabDelete.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabAdd.setOnClickListener(this);
        fam.setOnClickListener(this);
        mBtnWatering.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng bachkhoa = new LatLng(21.004911, 105.844158);
        setDb();
        setupMap();
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bachkhoa));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMarkerClickListener(this);
        setWaterSpot();
    }

    public void addTree() {
        //tre
        mTree.add(new TreeData(1, "Tree_1", 21.004517, 105.843447, 1, 1, "Bamboo"));
        //cây cảnh
        mTree.add(new TreeData(2, "Tree_2", 21.003879, 105.844043, 1, 2, "Bonsai"));
        mTree.add(new TreeData(3, "Tree_3", 21.003883, 105.844131, 2, 2, "Bonsai"));
        //hoa
        mTree.add(new TreeData(4, "Tree_4", 21.004197, 105.844754, 1, 3, "Flower"));
        //cỏ
        mTree.add(new TreeData(5, "Tree_5", 21.006535, 105.842971, 1, 4, "Grass"));
        mTree.add(new TreeData(6, "Tree_6", 21.006526, 105.843310, 2, 4, "Grass"));
        mTree.add(new TreeData(7, "Tree_7", 21.006071, 105.842985, 2, 4, "Grass"));
        mTree.add(new TreeData(8, "Tree_8", 21.006046, 105.843297, 1, 4, "Grass"));
        //Old Tree
        mTree.add(new TreeData(9, "Tree_9", 21.005200, 105.842931, 1, 5, "Old tree"));
        mTree.add(new TreeData(10, "Tree_10", 21.005205, 105.843269, 2, 5, "Old tree"));
        mTree.add(new TreeData(11, "Tree_11", 21.005196, 105.842281, 1, 5, "Old tree"));
        //pine
        mTree.add(new TreeData(12, "Tree_12", 21.004590, 105.844487, 1, 6, "Pine"));
        //
        mTree.add(new TreeData(13, "Tree_13", 21.005378, 105.844710, 2, 8, "Pine"));
        //cây
        mTree.add(new TreeData(14, "Tree_14", 21.004799, 105.844343, 1, 9, "Tree"));
        mTree.add(new TreeData(15, "Tree_15", 21.004830, 105.843956, 2, 9, "Tree"));
        mTree.add(new TreeData(16, "Tree_16", 21.004947, 105.843386, 1, 9, "Tree"));
        //
        mTree.add(new TreeData(17, "Tree_17", 21.004421, 105.843599, 1, 10, "Williow"));
    }

    public void setDb() {
        mDatabase = new TreeDataSource(getApplicationContext());
        for (i = 0; i < mTree.size(); i++) {
            if (!mDatabase.isIndb(mTree.get(i).getId()))
                mDatabase.insertTree(mTree.get(i));
        }
        mTreeData = mDatabase.getAllTree();
        for (i = 0; i < mTreeData.size(); i++) {
            mTreeData.get(i).setChoosen(false);
        }
    }

    public void setupMap() {
        getCurrLocation();
        mMap.addMarker(new MarkerOptions().position(new LatLng(mMyLocation.getLatitude(),
            mMyLocation.getLongitude())).title("MyLocation"));
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            LatLng latLng = new LatLng(tree.getLat(), tree.getLong());
            MarkerOptions options = new MarkerOptions();
            options.position(latLng)
                .title(tree.getTreeName())
                .snippet(tree.getDes());
            MarkerDrawer.DrawMarker(tree, options);
            mMap.addMarker(options);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMenuToggle(boolean opened) {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void getCurrLocation() {
        mMyLocation = null;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = lm.getBestProvider(crit, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMyLocation = lm.getLastKnownLocation(provider);
    }

    public boolean isInBk(double latitude, double longitude) {
        if (latitude > 21.005958 ||
            latitude < 21.004966 ||
            longitude > 105.845287 ||
            longitude < 105.841652)
            return false;
        else return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
        final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);
        final InfoWindow.MarkerSpecification markerSpec =
            new InfoWindow.MarkerSpecification(offsetX, offsetY);
        InfoWindow infoWindow = null;
        for (int i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            if (marker.getTitle().equals(tree.getTreeName())) {
                mResult = tree;
                Bundle bundle = new Bundle();
                bundle.putParcelable("key", mResult);
                FormFragment formFragment = new FormFragment();
                formFragment.setArguments(bundle);
                infoWindow = new InfoWindow(marker, markerSpec, formFragment);
                if (isStarted) {
                    if (!tree.isChoosen()) {
                        MarkerDrawer.DrawMarkerChoosen(tree, marker);
                        tree.setChoosen(true);
                    } else {
                        MarkerDrawer.DrawMarkerMarker(tree, marker);
                        tree.setChoosen(false);
                    }
                }
            }
        }
        if (infoWindow != null) {
            infoWindowManager.toggle(infoWindow, true);
        }
        return true;
    }

    public void setWaterSpot() {
        mWater_1 = mMap.addMarker(new MarkerOptions().title("WaterSpot_1").position(new LatLng
            (21.004705,
                105.844675)).icon(BitmapDescriptorFactory.fromResource(R.drawable.water_spot)));
        mWater_2 = mMap.addMarker(
            new MarkerOptions().title("WaterSpot_1").position(new LatLng(21.004049, 105.844549))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.water_spot)));
        mWater_3 = mMap.addMarker(
            new MarkerOptions().title("WaterSpot_1").position(new LatLng(21.005756, 105.842630))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.water_spot)));
        mWater_4 = mMap.addMarker(
            new MarkerOptions().title("WaterSpot_1").position(new LatLng(21.006802, 105.843074))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.water_spot)));
        mWater_5 = mMap.addMarker(
            new MarkerOptions().title("WaterSpot_1").position(new LatLng(21.006203, 105.844561))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.water_spot)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location:
                if (ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                if (mCurrLocationMarker != null) mCurrLocationMarker.remove();
                getCurrLocation();
                LatLng latLng =
                    new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                if (!isInBk(latitude, longitude)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Sorry!")
                        .setMessage("You are not in the territory of HUST")
                        .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }
                break;
            case R.id.btn_watering:
                if (!isStarted) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Are you sure?")
                        .setMessage("Start Watering protocol?")
                        .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isStarted = true;
                                    for (i = mTreeData.size() - 1; i >= 0; i--) {
                                        if (mTreeData.get(i).getStatus()==1)
                                            mTreeData.remove(mTreeData.get(i));
                                    }
                                    mMap.clear();
                                    setupMap();
                                    mBtnWatering.setVisibility(View.GONE);
                                    mImage.setVisibility(View.VISIBLE);
                                    mText.setVisibility(View.VISIBLE);
                                    mBtnStart.setVisibility(View.VISIBLE);
                                }
                            })
                        .setNegativeButton(android.R.string.no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isStarted = false;
                                }
                            })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                }
                break;
            case R.id.btn_start:
                AlertDialog.Builder builder_1 = new AlertDialog.Builder(this);
                builder_1.setTitle("Are you sure?")
                    .setMessage("Begin to water the chosen trees?")
                    .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.clear();
                                for (i = mTreeData.size() - 1; i >= 0; i--) {
                                    if (!mTreeData.get(i).isChoosen())
                                        mTreeData.remove(mTreeData.get(i));
                                }
                                mWater_1 = mMap.addMarker(
                                    new MarkerOptions().title("WaterSpot_1").position(new LatLng
                                        (21.004705,
                                            105.844675)).icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.water_spot)));
                                setupMap();
                                getDirection();
                                mBtnStart.setVisibility(View.GONE);
                                mText.setVisibility(View.GONE);
                                mBtnStop.setVisibility(View.VISIBLE);
                            }
                        })
                    .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
                break;
            case R.id.btn_cancel:
                AlertDialog.Builder builder_2 = new AlertDialog.Builder(this);
                builder_2.setTitle("Are you sure?")
                    .setMessage("Watered Tree: 0" + "\n" + "Tree remain: " + String.valueOf
                        (mTreeData.size()) + "\n" + "Are you sure to cancel misson?")
                    .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.clear();
                                mTreeData.clear();
                                mMarkerPoints.clear();
                                setDb();
                                setupMap();
                                setWaterSpot();
                                isStarted = false;
                                mBtnStop.setVisibility(View.GONE);
                                mBtnWatering.setVisibility(View.VISIBLE);
                                mImage.setVisibility(View.GONE);
                                mText.setVisibility(View.GONE);
                            }
                        })
                    .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
                break;
            case R.id.fab_menu:
                if (fam.isOpened()) {
                    fam.close(true);
                }
                getCurrLocation();
                break;
            case R.id.fab1:
                fam.close(true);
                finish();
                break;
            case R.id.fab2:
                Intent myIntent_3 = new Intent(this, HistoryActivity.class);
                this.startActivity(myIntent_3);
                fam.close(true);
                break;
            case R.id.fab3:
                Intent getIntent = getIntent();
                String value = getIntent.getStringExtra("name_1");
                Intent myIntent_4 = new Intent(this, ProfileActivity.class);
                myIntent_4.putExtra("name_2", value);
                this.startActivity(myIntent_4);
                fam.close(true);
                break;
        }
    }

    float distanceOfTwoPoint(LatLng p1, LatLng p2) {
        float[] results = new float[1];
        Location.distanceBetween(p1.latitude, p1.longitude,
            p2.latitude, p2.longitude,
            results);
        return results[0];
    }

    void getDirection() {
        mMarkerPoints.clear();
        getCurrLocation();
        mMarkerPoints.add(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()));
        mMarkerPoints.add(new LatLng(mWater_1.getPosition().latitude, mWater_1.getPosition()
            .longitude));
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            if (tree.isChoosen()) {
                LatLng latLng = new LatLng(tree.getLat(), tree.getLong());
                mMarkerPoints.add(latLng);
            }
        }
        float minDistance = 9999999;
        int minPoint;
        for (i = 2; i < mMarkerPoints.size() - 1; i++) {
            minPoint = i + 1;
            for (int j = i + 1; j < mMarkerPoints.size(); j++) {
                if (distanceOfTwoPoint(mMarkerPoints.get(i), mMarkerPoints.get(j)) <= minDistance) {
                    minPoint = j;
                    minDistance = distanceOfTwoPoint(mMarkerPoints.get(i), mMarkerPoints.get(j));
                }
            }
            minDistance = 9999999;
            if (minPoint != i + 1)
                Collections.swap(mMarkerPoints, i + 1, minPoint);
        }
        for (i = 0; i < mMarkerPoints.size() - 1; i++) {
            LatLng origin = (LatLng) mMarkerPoints.get(i);
            LatLng dest = (LatLng) mMarkerPoints.get(i + 1);
            String url = getDirectionsUrl(origin, dest);
            HUSTActivity.DownloadTask downloadTask = new HUSTActivity.DownloadTask();
            downloadTask.execute(url);
        }
    }

    @Override
    public void onWindowShowStarted(@NonNull InfoWindow infoWindow) {
    }

    @Override
    public void onWindowShown(@NonNull InfoWindow infoWindow) {
    }

    @Override
    public void onWindowHideStarted(@NonNull InfoWindow infoWindow) {
    }

    @Override
    public void onWindowHidden(@NonNull InfoWindow infoWindow) {
    }
//***********************************************************************8

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
            HUSTActivity.ParserTask parserTask = new HUSTActivity.ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
}
