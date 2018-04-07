package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.database.TreeDataSource;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;

public class HUSTActivity extends FragmentActivity
    implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
    .OnConnectionFailedListener, LocationListener,
    GoogleMap.OnMapClickListener, View.OnClickListener, FloatingActionMenu.OnMenuToggleListener {
    private GoogleMap mMap;
    private Marker myMarker;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    RelativeLayout mInfor;
    RelativeLayout mMainLayout;
    FloatingActionButton mBtnStart;
    FloatingActionButton mBtnLocation;
    private FloatingActionMenu fam;
    private FloatingActionButton fabEdit, fabDelete, fabAdd;
    private List<TreeData> mTree;
    private List<TreeData> mTreeData;
    ArrayList markerPoints = new ArrayList();
    private TreeDataSource mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hust);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();
        setMenuPosition();
        setButton();
    }

    private void initView() {
        mInfor = (RelativeLayout) findViewById(R.id.treeInfor);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mBtnStart = (FloatingActionButton) findViewById(R.id.btn_start);
        mBtnLocation = (FloatingActionButton) findViewById(R.id.btn_location);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab1);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab2);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab3);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);
    }

    public void setButton() {
        mInfor.setOnClickListener(this);
        mBtnLocation.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        fam.setOnMenuToggleListener(this);
        fabDelete.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabAdd.setOnClickListener(this);
        fam.setOnClickListener(this);
    }

    void setMenuPosition() {
        RelativeLayout.LayoutParams lp_1 = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lp_2 = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp_1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp_2.addRule(RelativeLayout.ALIGN_PARENT_END);
        if (mInfor.getVisibility() == View.GONE) {
            lp_1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp_2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBtnStart.setLayoutParams(lp_1);
            mBtnLocation.setLayoutParams(lp_2);
        } else {
            lp_1.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp_1.addRule(RelativeLayout.ABOVE, mInfor.getId());
            lp_2.addRule(RelativeLayout.ABOVE, mInfor.getId());
            mBtnStart.setLayoutParams(lp_1);
            mBtnLocation.setLayoutParams(lp_2);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng bachkhoa = new LatLng(21.004911, 105.844158);
        setupMap();
        //mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bachkhoa));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public void setupMap() {
        /*status
        1: DEAD
        2: normal
        3: dying
        4: need watering
         */
        mTree = new ArrayList<>();
        mDatabase = new TreeDataSource(getApplicationContext());
        int i = 0;
/*
       myMarker = mMap.addMarker(new MarkerOptions()
            .position(new LatLng(21.004886, 105.844284))
            .title("tree_1")
            .snippet("batman was here")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)));
       Marker myMarker_2 = mMap.addMarker(new MarkerOptions()
            .position(new LatLng(58.500184, 83.189844))
            .title("tree_2")
            .snippet("superman was here")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree)));
*/
        mTree
            .add(new TreeData(1, "tree_0", 21.004954, 105.844448, 1, 1, "this is " +
                "Stark's tree"));
        mTree.add(new TreeData(2, "tree_1", 21.004954, 105.843542, 2, 3, "this is " +
            "Tony's tree"));
        mTree.add(new TreeData(3, "tree_2", 21.005776, 105.841578, 3, 3, "this is " +
            "Batman's tree"));
        mTree.add(new TreeData(4, "tree_3", 21.006577, 105.845162, 4, 3, "this is " +
            "Superman's tree"));
        mTree.add(new TreeData(5, "tree_4", 21.005129, 105.845575, 1, 3, "this is " +
            "Trump's tree"));
        for (i = 0; i < mTree.size(); i++) {
            if (!mDatabase.isIndb(mTree.get(i).getId()))
                mDatabase.insertTree(mTree.get(i));
        }
        mTreeData = mDatabase.getAllTree();
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            LatLng latLng = new LatLng(tree.getLat(), tree.getLong());
            markerPoints.add(latLng);
            MarkerOptions options = new MarkerOptions();
            options.position(latLng)
                .title(tree.getTreeName())
                .snippet(tree.getDes());
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.tree));
            if(tree.getStatus() == 2) options.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            else if(tree.getStatus() == 1) options.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            else if(tree.getStatus() == 3) options.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            else if(tree.getStatus() == 4) options.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(options);
        }
        Toast.makeText(this, String.valueOf(markerPoints.size()), Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
        mGoogleApiClient.connect();
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
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        if (!isInBk(latitude, longitude)) initDialog();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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

    /*@Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTitle().equals(myMarker.getTitle())) {
            mInfor.setVisibility(View.VISIBLE);
            Toast.makeText(this, "hey hey hey", Toast.LENGTH_LONG).show();
            setMenuPosition();
        }
        return true;
    }*/

    @Override
    public void onMapClick(LatLng latLng) {
        mInfor.setVisibility(View.GONE);
        Toast.makeText(this, "hey hey hey", Toast.LENGTH_SHORT).show();
        setMenuPosition();
        if (fam.isOpened()) {
            fam.close(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.treeInfor:
                Intent myIntent_1 = new Intent(this, TreeInformation.class);
                this.startActivity(myIntent_1);
                break;
            case R.id.btn_location:
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
                break;
            case R.id.btn_start:
                Intent myIntent_2 = new Intent(this, WateringActivity.class);
                this.startActivity(myIntent_2);
                break;
            case R.id.fab_menu:
                if (fam.isOpened()) {
                    fam.close(true);
                }
                break;
            case R.id.fab1:
                showToast("Button Add clicked");
                fam.close(true);
                finish();
                break;
            case R.id.fab2:
                showToast("Button delete clicked");
                fam.close(true);
                break;
            case R.id.fab3:
                showToast("Button edit clicked");
                fam.close(true);
                break;
        }
    }

    void initDialog() {
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

    @Override
    public void onMenuToggle(boolean opened) {
        if (opened) {
            showToast("Menu is opened");
        } else {
            showToast("Menu is closed");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
