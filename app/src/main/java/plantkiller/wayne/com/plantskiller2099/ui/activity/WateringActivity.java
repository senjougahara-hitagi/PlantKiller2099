package plantkiller.wayne.com.plantskiller2099.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import plantkiller.wayne.com.plantskiller2099.R;
import plantkiller.wayne.com.plantskiller2099.data.database.TreeDataSource;
import plantkiller.wayne.com.plantskiller2099.data.model.TreeData;

public class WateringActivity extends FragmentActivity implements OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private List<TreeData> mTreeData;
    private List<Marker> mMarkerList;
    private TreeDataSource mDatabase;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng bachkhoa = new LatLng(21.004911, 105.844158);
        setupMap();
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bachkhoa));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public void setupMap() {
        int i;
        mTreeData = mDatabase.getAllTree();
        for (i = 0; i < mTreeData.size(); i++) {
            TreeData tree = mTreeData.get(i);
            MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(tree.getLat(), tree.getLong()))
                .title(tree.getTreeName())
                .snippet(tree.getDes())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tree));
            mMarkerList.add(mMap.addMarker(markerOptions));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
