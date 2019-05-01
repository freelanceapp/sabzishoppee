package ibt.pahadisabzi.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.address_add_responce.AddAddressModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.AppProgressDialog;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.ConnectionDirector;
import ibt.pahadisabzi.utils.GpsTracker;
import ibt.pahadisabzi.utils.Utility;
import retrofit2.Response;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Dialog dialog, dialogPaid;
    private LatLng latLngPick;
    private double endLatitude = 0.0;
    private double endLongitude = 0.0;
    private double startLatitude = 0.0;
    private double startLongitude = 0.0;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private LatLng latLng;
    private LatLng latLngPick1;
    private String strName, strAddress, strHouseNo, strLandMark, strType, strCity, strState, strCountry, strZipCode, strAddressType, strLat,strLong;
    private TextView tvAddressShow;
    private ImageView btn_map_back;
    BottomSheetBehavior behavior;
    CoordinatorLayout coordinatorLayout;
    private Button btn_add_more, btn_address_confirm, btn_confirm_location;
    private EditText et_newaddress_adress, et_newaddress_hounse_no, et_newaddress_landmark;
    String[] AddressType={"Home","Office"};
    Spinner sp_newaddress_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        init();
    }
    private void init() {

        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        tvAddressShow = findViewById(R.id.tvAddressShow);
        btn_map_back = findViewById(R.id.btn_map_back);
        btn_add_more = findViewById(R.id.btn_add_more);
        et_newaddress_adress = findViewById(R.id.et_newaddress_adress);
        et_newaddress_landmark = findViewById(R.id.et_newaddress_landmark);
        et_newaddress_hounse_no = findViewById(R.id.et_newaddress_hounse_no);
        btn_address_confirm = findViewById(R.id.btn_address_confirm);
        btn_confirm_location = findViewById(R.id.btn_confirm_location);
        tvAddressShow.setSelected(true);
        btn_map_back.setOnClickListener(this);
        btn_add_more.setOnClickListener(this);
        et_newaddress_adress.setOnClickListener(this);
        btn_address_confirm.setOnClickListener(this);
        btn_confirm_location.setOnClickListener(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        sp_newaddress_type = findViewById(R.id.sp_newaddress_type);

        sp_newaddress_type.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,AddressType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_newaddress_type.setAdapter(aa);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Alerts.show(MapsActivity.this, "Please enable location permission...!!!");
            return;
        }

        //get latlong for corners for specified place
        LatLng one = new LatLng(31.225831, 78.034881);
        LatLng two = new LatLng(28.8452547,79.8174343);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //add them to builder
        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();

        //get width and height to current display screen
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 20% padding
        int padding = (int) (width * 0.20);

        //set latlong bounds
        mMap.setLatLngBoundsForCameraTarget(bounds);

        //move camera to fill the bound to screen
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        getLatLong();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLngPick) {
                MarkerOptions markerOptions = new MarkerOptions();
                mMap.clear();
                latLngPick1 = new LatLng(latLngPick.latitude, latLngPick.longitude);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                markerOptions.position(latLngPick1);
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngPick));
                markerOptions.title("Current Location");
                mMap.addMarker(markerOptions);
                getAddressList(latLngPick);
            }
        });

        if (latitude > 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            mMap.animateCamera(cameraUpdate);
        }
    }

    private void getLatLong() {
        GpsTracker gpsTracker = new GpsTracker(MapsActivity.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        latLng = new LatLng(latitude, longitude);
        getAddressList();
        getAddressList(latLng);
    }

    private void getAddressList() {
        AppProgressDialog.show(dialog);
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                AppProgressDialog.hide(dialog);
            } else {
                AppProgressDialog.show(dialog);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLatLong();
                    }
                }, 3000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAddressList(LatLng latLng) {
        AppProgressDialog.show(dialog);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                AppProgressDialog.hide(dialog);

                strLat = String.valueOf(addresses.get(0).getLatitude());
                strLong = String.valueOf(addresses.get(0).getLongitude());
                strAddress = addresses.get(0).getAddressLine(0);
                strCity = addresses.get(0).getLocality();
                strState = addresses.get(0).getAdminArea();
                strCountry = addresses.get(0).getCountryName();
                strZipCode = addresses.get(0).getPostalCode();

                //Toast.makeText(this,"Address : "+addresses.get(0).getAddressLine(0),Toast.LENGTH_LONG).show();
                tvAddressShow.setText(strAddress);
                et_newaddress_adress.setText(strAddress);

            } else {
                AppProgressDialog.show(dialog);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLatLong();
                    }
                }, 3000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_map_back :
                finish();
                break;

            case R.id.btn_add_more :
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.btn_address_confirm :
                strHouseNo = et_newaddress_hounse_no.getText().toString();
                strLandMark = et_newaddress_landmark.getText().toString();

                if (strHouseNo.equals(""))
                {
                    Utility.toastView(mContext, "Enter all details");
                }else {
                    addAddressApi();
                }
                break;

            case R.id.btn_confirm_location :

                addAddressApi();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
      //  Toast.makeText(this, AddressType[position], Toast.LENGTH_LONG).show();
        if (AddressType[position].equals("Home"))
        {
            strAddressType = "0";
        }else {
            strAddressType = "1";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void addAddressApi() {
        String strUser_id = AppPreference.getStringPreference(mContext, Constant.User_Id);

        if (cd.isNetWorkAvailable()) {
            RetrofitService.addAddress(new Dialog(mContext), retrofitApiClient.addAddress(strUser_id,strAddress,strAddress,strCity,strState,strAddressType,strLong,strLat,strHouseNo,strZipCode), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    AddAddressModel addressModel = (AddAddressModel) result.body();

                    if (!addressModel.getError())
                    {

                        AppPreference.setStringPreference(mContext, Constant.Name, strCity);
                        AppPreference.setStringPreference(mContext, Constant.Address, strAddress);
                        AppPreference.setStringPreference(mContext, Constant.City, strCity);
                        AppPreference.setStringPreference(mContext, Constant.PinCode, strZipCode);
                        AppPreference.setStringPreference(mContext, Constant.State, strState);
                        AppPreference.setStringPreference(mContext, Constant.House_no, strHouseNo);
                        AppPreference.setStringPreference(mContext, Constant.Address_Type, strAddressType);
                        AppPreference.setStringPreference(mContext, Constant.ADDRESS_LAT, strLat);
                        AppPreference.setStringPreference(mContext, Constant.ADDRESS_LONG, strLong);
                        AppPreference.setStringPreference(mContext, Constant.ADDRESS_LANDMARK, strLandMark);

                        Intent intent = new Intent(mContext, CheckOutActivity.class);
                        intent.putExtra("FragmentPass", "ConfirmationFragment");
                        startActivity(intent);

                    }else {
                        Alerts.show(mContext, addressModel.getMessage());
                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        }else {
            cd.show(mContext);
        }
    }
}
