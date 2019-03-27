package ibt.sabzishoppee.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.model.User;
import ibt.sabzishoppee.model.address_add_responce.AddAddressModel;
import ibt.sabzishoppee.model.productlist_responce.ProductListModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.ui.activity.ManualLocationActivity;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.AppProgressDialog;
import ibt.sabzishoppee.utils.BaseFragment;
import ibt.sabzishoppee.utils.ConnectionDirector;
import ibt.sabzishoppee.utils.GpsTracker;
import ibt.sabzishoppee.utils.SessionManager;
import ibt.sabzishoppee.utils.Utility;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class ShoppingFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Context ctx;
    Activity activity;
    LinearLayout continue_pay_ll;
    private FloatingActionButton fbCheck;

    EditText name_et, mobile_et, address_et, country_et, state_et, city_et, zipcode_et, hourse_no_et, address_type_et;
    EditText name_et1, mobile_et1, address_et1, country_et1, state_et1, city_et1, zipcode_et1;
    Spinner sp_newaddress_type;
    String address = "";
   // ConnectionDetector connectionDetector;
    SessionManager sessionManager;
    FloatingActionButton btn_placeorder;

    //ArrayList<SaveAddress> address_list;

    double latitude; // latitude
    double longitude; // longitude
    private Dialog dialog;
    private String strName, strAddress, strHouseNo, strType, strCity, strState, strCountry, strZipCode, strAddressType;
    String[] AddressType={"Home","Office"};
    @SuppressLint("ValidFragment")
    public ShoppingFragment(Context ctx) {
      //  connectionDetector = new ConnectionDetector();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, null);
        ctx = getActivity();
        sessionManager = new SessionManager(ctx);
        activity = getActivity();
        //requestLocationPermission();

        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        initXml(view);
        setData();
        return view;
    }

    private void initXml(View view) {

        ctx = getActivity();
        getAddressApi();
        continue_pay_ll = view.findViewById(R.id.ll_shopping_continuepay);
        address_et = view.findViewById(R.id.et_newaddress_adress);
        country_et = view.findViewById(R.id.et_newaddress_country);
        state_et = view.findViewById(R.id.et_newaddress_state);
        city_et = view.findViewById(R.id.et_newaddress_city);
        zipcode_et = view.findViewById(R.id.et_newaddress_zipcode);
        name_et = view.findViewById(R.id.et_newaddress_name);
        name_et = view.findViewById(R.id.et_newaddress_name);
        mobile_et = view.findViewById(R.id.et_newaddress_number);
        hourse_no_et = view.findViewById(R.id.et_newaddress_hounse_no);
        sp_newaddress_type = view.findViewById(R.id.sp_newaddress_type);
        address_et1 = view.findViewById(R.id.et_newaddress_adress1);
        country_et1 = view.findViewById(R.id.et_newaddress_country1);
        state_et1 = view.findViewById(R.id.et_newaddress_state1);
        city_et1 = view.findViewById(R.id.et_newaddress_city1);
        zipcode_et1 = view.findViewById(R.id.et_newaddress_zipcode1);
        name_et1 = view.findViewById(R.id.et_newaddress_name1);
        mobile_et1 = view.findViewById(R.id.et_newaddress_number1);
        btn_placeorder = view.findViewById(R.id.btn_placeorder);

        continue_pay_ll.setOnClickListener(this);
        btn_placeorder.setOnClickListener(this);

        sp_newaddress_type.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(ctx,android.R.layout.simple_spinner_item,AddressType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_newaddress_type.setAdapter(aa);
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(ctx, AddressType[position], Toast.LENGTH_LONG).show();
        if (AddressType[position].equals("Home"))
        {
            strAddressType = "0";
        }else {
            strAddressType = "1";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }
    private void setData() {
        dialog = new Dialog(ctx);
        String name = User.getUser().getUser().getUserName();
        String mobile = User.getUser().getUser().getUserContact();
        name_et.setText(name);
        mobile_et.setText(mobile);

      //  getLatLong();
    }

    /*private void getLatLong() {
        GpsTracker gpsTracker = new GpsTracker(ctx);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        getAddressList();
    }

    private void getAddressList() {
        AppProgressDialog.show(dialog);
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                AppProgressDialog.hide(dialog);
                address_et.setText(addresses.get(0).getAddressLine(0));
                city_et.setText(addresses.get(0).getLocality());
                state_et.setText(addresses.get(0).getAdminArea());
                country_et.setText(addresses.get(0).getCountryName());
                zipcode_et.setText(addresses.get(0).getPostalCode());
            } else {
                AppProgressDialog.show(dialog);
                //    new Handler().postDelayed(new Runnable() {
                @Override
                public void run () {
                    getLatLong();

                }, 3000);

        } catch (
    IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shopping_continuepay:
                getData();
                break;

            case R.id.btn_placeorder:
                Intent intent = new Intent(getActivity(), ManualLocationActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void getData() {
         strName = name_et.getText().toString();
        String strMobile = mobile_et.getText().toString();
         strAddress = address_et.getText().toString();
         strCountry = country_et.getText().toString();
         strState = state_et.getText().toString();
         strCity = city_et.getText().toString();
         strZipCode = zipcode_et.getText().toString();
         strHouseNo = hourse_no_et.getText().toString();
         strType = address_type_et.getText().toString();

        String name1 = name_et1.getText().toString();
        String mobile1 = mobile_et1.getText().toString();
        String address1 = address_et1.getText().toString();
        String country1 = country_et1.getText().toString();
        String state1 = state_et1.getText().toString();
        String city1 = city_et1.getText().toString();
        String zipcode1 = zipcode_et1.getText().toString();

        if (strName.equals("") || strAddress.equals("") || strCountry.equals("") || strState.equals("") ||
                strCity.equals("") || strZipCode.equals("")) {

            Utility.toastView(ctx, "Enter all details");
        } /*else if (mobile.length()>10 || mobile.length()<10){
            Alerts.show(getActivity().getApplicationContext(),"Please enter valid mobile number");
        }*/ else{

            AppPreference.setStringPreference(ctx, Constant.Name, strCity);
            AppPreference.setStringPreference(ctx, Constant.Address, strAddress);
            AppPreference.setStringPreference(ctx, Constant.MobileNumber, strMobile);
            AppPreference.setStringPreference(ctx, Constant.City, strCity);
            AppPreference.setStringPreference(ctx, Constant.PinCode, strZipCode);
            AppPreference.setStringPreference(ctx, Constant.State, strState);
            AppPreference.setStringPreference(ctx, Constant.House_no, strHouseNo);
            AppPreference.setStringPreference(ctx, Constant.Address_Type, strAddressType);

            sessionManager.setData(SessionManager.KEY_ORDER_NAME, strName);
            sessionManager.setData(SessionManager.KEY_ORDER_MOBILE, strMobile);
            sessionManager.setData(SessionManager.KEY_ORDER_ADDRESS, address);
            sessionManager.setData(SessionManager.KEY_ORDER_STATE, strState);
            sessionManager.setData(SessionManager.KEY_ORDER_COUNTRY, strCountry);
            sessionManager.setData(SessionManager.KEY_ORDER_ZIPCODE, strZipCode);
            sessionManager.setData(SessionManager.KEY_ORDER_CITY, strCity);

            sessionManager.setData(SessionManager.KEY_ORDER_NAME1, name1);
            sessionManager.setData(SessionManager.KEY_ORDER_MOBILE1, mobile1);
            sessionManager.setData(SessionManager.KEY_ORDER_ADDRESS1, address1);
            sessionManager.setData(SessionManager.KEY_ORDER_STATE1, state1);
            sessionManager.setData(SessionManager.KEY_ORDER_COUNTRY1, country1);
            sessionManager.setData(SessionManager.KEY_ORDER_ZIPCODE1, zipcode1);
            sessionManager.setData(SessionManager.KEY_ORDER_CITY1, city1);

            addAddressApi();


            // ((CheckOutActivity) getActivity()).setPosition(1);
        }
    }


    private void addAddressApi() {
        String strUser_id = AppPreference.getStringPreference(mContext, Constant.User_Id);
        if (cd.isNetWorkAvailable()) {
            RetrofitService.addAddress(new Dialog(mContext), retrofitApiClient.addAddress(strUser_id,strAddress,strAddress,strCity,strState,strAddressType,"7.231322","123.131212",strHouseNo,"450404"), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    AddAddressModel addressModel = (AddAddressModel) result.body();

                    if (!addressModel.getError())
                    {
                        Alerts.show(mContext, addressModel.getMessage());
                        PaymentFragment fragment = new PaymentFragment(ctx);
                        Utility.setFragment1(fragment, ctx, Constant.ShoppingFragment);

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


    private void getAddressApi() {
        String strUser_id = AppPreference.getStringPreference(mContext, Constant.User_Id);
        if (cd.isNetWorkAvailable()) {
            RetrofitService.addAddress(new Dialog(mContext), retrofitApiClient.getAddress(strUser_id), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    AddAddressModel addressModel = (AddAddressModel) result.body();

                    if (!addressModel.getError())
                    {
                        Alerts.show(mContext, addressModel.getMessage());

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
