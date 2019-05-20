package ibt.pahadisabzi.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.adapter.AddressShowAdapter;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.User;
import ibt.pahadisabzi.model.address_add_responce.AddAddressModel;
import ibt.pahadisabzi.model.address_show_responce.Address;
import ibt.pahadisabzi.model.address_show_responce.AddressShowModel;
import ibt.pahadisabzi.model.signup_responce.SignUpModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.ui.activity.HomeActivity;
import ibt.pahadisabzi.ui.activity.SignInActivity;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseFragment;
import ibt.pahadisabzi.utils.ConnectionDirector;
import ibt.pahadisabzi.utils.SessionManager;
import ibt.pahadisabzi.utils.Utility;
import retrofit2.Response;

import static ibt.pahadisabzi.ui.activity.CheckOutActivity.tv_address;
import static ibt.pahadisabzi.ui.activity.CheckOutActivity.tv_confirmation;

@SuppressLint("ValidFragment")
public class ShoppingFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    AddressShowAdapter addressShowAdapter;
    ArrayList<ibt.pahadisabzi.model.address_show_responce.Address> addressArrayList = new ArrayList<>();
    RecyclerView rvAddress;

    Context ctx;
    Activity activity;
    LinearLayout continue_pay_ll;
    private FloatingActionButton fbCheck;

    EditText name_et, mobile_et, address_et, country_et, state_et, city_et, zipcode_et, hourse_no_et, address_type_et, et_newaddress_landmark;
    EditText name_et1, mobile_et1, address_et1, country_et1, state_et1, city_et1, zipcode_et1;
    Spinner sp_newaddress_type;
    String address = "";
    LinearLayout btn_current_location;
    // ConnectionDetector connectionDetector;
    SessionManager sessionManager;
    FloatingActionButton btn_placeorder;

    //ArrayList<SaveAddress> address_list;

    double latitude; // latitude
    double longitude; // longitude
    private Dialog dialog;
    private String strName, strAddress, strHouseNo, strLandMark, strType, strCity, strState, strCountry, strZipCode, strAddressType, strLat, strLong;
    String[] AddressType = {"Home", "Office"};

    @SuppressLint("ValidFragment")
    public ShoppingFragment(Context ctx) {
        //  connectionDetector = new ConnectionDetector();
        // http://freshveggie.infobitetechnology.tech/api/add-user-address.php
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

        tv_confirmation.setBackgroundColor(getResources().getColor(R.color.gray_c));
        tv_address.setBackgroundColor(getResources().getColor(R.color.red_d));

        initXml(view);
        setData();
        return view;
    }

    private void initXml(View view) {

        ctx = getActivity();
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
        et_newaddress_landmark = view.findViewById(R.id.et_newaddress_landmark);
        sp_newaddress_type = view.findViewById(R.id.sp_newaddress_type);
        address_et1 = view.findViewById(R.id.et_newaddress_adress1);
        country_et1 = view.findViewById(R.id.et_newaddress_country1);
        state_et1 = view.findViewById(R.id.et_newaddress_state1);
        city_et1 = view.findViewById(R.id.et_newaddress_city1);
        zipcode_et1 = view.findViewById(R.id.et_newaddress_zipcode1);
        name_et1 = view.findViewById(R.id.et_newaddress_name1);
        mobile_et1 = view.findViewById(R.id.et_newaddress_number1);
        btn_placeorder = view.findViewById(R.id.btn_placeorder);
        rvAddress = view.findViewById(R.id.rv_address);
        btn_current_location = view.findViewById(R.id.btn_current_location);
        continue_pay_ll.setOnClickListener(this);
        btn_placeorder.setOnClickListener(this);
        btn_current_location.setOnClickListener(this);

       /* sp_newaddress_type.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(ctx,android.R.layout.simple_spinner_item,AddressType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_newaddress_type.setAdapter(aa);*/


        addressShowAdapter = new AddressShowAdapter(mContext, addressArrayList, this);
        rvAddress.setHasFixedSize(true);
        rvAddress.setLayoutManager(new GridLayoutManager(mContext, 1));
        rvAddress.setAdapter(addressShowAdapter);

        getAddressApi();

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(ctx, AddressType[position], Toast.LENGTH_LONG).show();
        if (AddressType[position].equals("Home")) {
            strAddressType = "0";
        } else {
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shopping_continuepay:
                getData();
                break;

            case R.id.btn_placeorder:
                /*Intent intent = new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent);*/
                break;

            case R.id.btn_current_location:
                openAddAddressDialog();
                break;

            case R.id.ll_show_address:
                int pos = Integer.parseInt(v.getTag().toString());
                ibt.pahadisabzi.model.address_show_responce.Address address = addressArrayList.get(pos);

                AppPreference.setStringPreference(ctx, Constant.Name, address.getUserCity());
                AppPreference.setStringPreference(ctx, Constant.Address, address.getAddress());
                AppPreference.setStringPreference(ctx, Constant.ADDRESS_LANDMARK, address.getLocation());
                AppPreference.setStringPreference(ctx, Constant.City, address.getUserCity());
                AppPreference.setStringPreference(ctx, Constant.PinCode, address.getZipcode());
                AppPreference.setStringPreference(ctx, Constant.State, address.getState());
                AppPreference.setStringPreference(ctx, Constant.House_no, address.getHouseNumber());
                AppPreference.setStringPreference(ctx, Constant.Address_Type, address.getAddressType());
                AppPreference.setStringPreference(ctx, Constant.ADDRESS_LAT, address.getLat());
                AppPreference.setStringPreference(ctx, Constant.ADDRESS_LONG, address.getLong());
                AppPreference.setStringPreference(ctx, Constant.CONTRY, "India");
                // AppPreference.setStringPreference(ctx, Constant.ADDRESS_LANDMARK, address.getL);

                ConfirmationFragment fragment = new ConfirmationFragment(ctx);
                Utility.setFragment1(fragment, ctx, Constant.ShoppingFragment);

                break;

            case R.id.btn_edit:
                int pos1 = Integer.parseInt(v.getTag().toString());
                ibt.pahadisabzi.model.address_show_responce.Address address1 = addressArrayList.get(pos1);

                openUpdateDialog(address1);

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
        strLandMark = et_newaddress_landmark.getText().toString();
        //strType = address_type_et.getText().toString();

        String name1 = name_et1.getText().toString();
        String mobile1 = mobile_et1.getText().toString();
        String address1 = address_et1.getText().toString();
        String country1 = country_et1.getText().toString();
        String state1 = state_et1.getText().toString();
        String city1 = city_et1.getText().toString();
        String zipcode1 = zipcode_et1.getText().toString();


        AppPreference.setStringPreference(ctx, Constant.Name, strCity);
        AppPreference.setStringPreference(ctx, Constant.Address, strAddress);
        AppPreference.setStringPreference(ctx, Constant.MobileNumber, strMobile);
        AppPreference.setStringPreference(ctx, Constant.City, strCity);
        AppPreference.setStringPreference(ctx, Constant.PinCode, strZipCode);
        AppPreference.setStringPreference(ctx, Constant.State, strState);
        AppPreference.setStringPreference(ctx, Constant.House_no, strHouseNo);
        AppPreference.setStringPreference(ctx, Constant.Address_Type, strAddressType);
        AppPreference.setStringPreference(ctx, Constant.ADDRESS_LAT, strLat);
        AppPreference.setStringPreference(ctx, Constant.ADDRESS_LONG, strLong);
        AppPreference.setStringPreference(ctx, Constant.ADDRESS_LANDMARK, strLandMark);

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

        // addAddressApi();
        // ((CheckOutActivity) getActivity()).setPosition(1);
        // }
    }

    private void getAddressApi() {
        String strUser_id = AppPreference.getStringPreference(mContext, Constant.User_Id);
        if (cd.isNetWorkAvailable()) {
            addressArrayList.clear();
            RetrofitService.getAddress(new Dialog(mContext), retrofitApiClient.getAddress(strUser_id), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    AddressShowModel addressModel = (AddressShowModel) result.body();
                    if (!addressModel.getError()) {
                        addressArrayList.addAll(addressModel.getAddress());
                        HashSet<ibt.pahadisabzi.model.address_show_responce.Address> hashSet = new HashSet<>();
                        hashSet.addAll(addressArrayList);
                        addressArrayList.clear();
                        addressArrayList.addAll(hashSet);
                    } else {
                        Alerts.show(mContext, addressModel.getMessage());
                    }
                    addressShowAdapter.notifyDataSetChanged();
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        }
    }

    private void openUpdateDialog(Address address1) {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(mContext);
        dialogBox.setCancelable(true);

        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogBoxView = li.inflate(R.layout.dialog_add_address, null);
        dialogBox.setView(dialogBoxView);


        final AlertDialog alertDialog = dialogBox.create();
        alertDialog.show();

        TextView tvTitle = dialogBoxView.findViewById(R.id.tvTitle);
        EditText tvAddress = dialogBoxView.findViewById(R.id.et_newaddress_address1);
        EditText tvLandmark = dialogBoxView.findViewById(R.id.et_newaddress_landmark);
        Button btnConfirmAddress = dialogBoxView.findViewById(R.id.btn_address_confirm);

        tvTitle.setText("Edit Address");
        tvAddress.setText(address1.getAddress());
        tvLandmark.setText(address1.getLocation());
        btnConfirmAddress.setText("Edit Address");

        btnConfirmAddress.setOnClickListener(v -> {
            String address, name, landmark;
            address = tvAddress.getText().toString().trim();
            landmark = tvLandmark.getText().toString().trim();

            if (address.isEmpty()){
                Alerts.show(mContext, "Please enter an Address");
            }else{
                updateAddressApi(address1.getAddressId(), address, landmark);
            }

            alertDialog.dismiss();

        });
    }

    private void updateAddressApi(String addressId, String address, String landmark) {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.updateAddress(new Dialog(mContext), retrofitApiClient.updateAddress(landmark, "", "", "", address, ""
                    , "", "", "", "", "", "1", addressId), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    SignUpModel addressModel = (SignUpModel) result.body();
                    Alerts.show(mContext, addressModel.getMessage());



                    getAddressApi();
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        }
    }

    private void openAddAddressDialog() {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(mContext);
        dialogBox.setCancelable(true);

        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogBoxView = li.inflate(R.layout.dialog_add_address, null);
        dialogBox.setView(dialogBoxView);


        final AlertDialog alertDialog = dialogBox.create();
        alertDialog.show();

        TextView tvAddress = dialogBoxView.findViewById(R.id.et_newaddress_address1);
        TextView tvLandmark = dialogBoxView.findViewById(R.id.et_newaddress_landmark);
        Button btnConfirmAddress = dialogBoxView.findViewById(R.id.btn_address_confirm);

        btnConfirmAddress.setOnClickListener(v -> {
            String address, name, landmark;
            address = tvAddress.getText().toString().trim();
            landmark = tvLandmark.getText().toString().trim();

            if (address.isEmpty()){
                Alerts.show(mContext, "Please enter an Address");
            }else{
                addAddressApi(address, landmark);
            }

            alertDialog.dismiss();

        });

    }

    private void addAddressApi(String address, String landmark) {
        String strUser_id = AppPreference.getStringPreference(mContext, Constant.User_Id);

        if (cd.isNetWorkAvailable()) {
            RetrofitService.addAddress(new Dialog(mContext), retrofitApiClient.addAddress(strUser_id,address,landmark,strCity,strState,strType,strLong,strLat,strHouseNo,strZipCode), new WebResponse() {
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

                        addressArrayList.clear();
                        addressArrayList.addAll(addressModel.getAddress());
                        HashSet<Address> hashSet = new HashSet<>();
                        hashSet.addAll(addressArrayList);
                        addressArrayList.clear();
                        addressArrayList.addAll(hashSet);

                        new android.support.v7.app.AlertDialog.Builder(mContext)
                                .setTitle("New Address")
                                .setMessage("Use This Address.")
                                .setPositiveButton("YES", (dialog, which) -> {

                                    addressShowAdapter.notifyDataSetChanged();

                                    Address newAddress = new Address();

                                    for (Address adrs : addressArrayList){
                                        if (adrs.getAddressId().equals(addressModel.getNewAddressId())){
                                            newAddress = adrs;
                                        }
                                    }

                                    AppPreference.setStringPreference(ctx, Constant.Name, newAddress.getUserCity());
                                    AppPreference.setStringPreference(ctx, Constant.Address, newAddress.getAddress());
                                    AppPreference.setStringPreference(ctx, Constant.ADDRESS_LANDMARK, newAddress.getLocation());
                                    AppPreference.setStringPreference(ctx, Constant.City, newAddress.getUserCity());
                                    AppPreference.setStringPreference(ctx, Constant.PinCode, newAddress.getZipcode());
                                    AppPreference.setStringPreference(ctx, Constant.State, newAddress.getState());
                                    AppPreference.setStringPreference(ctx, Constant.House_no, newAddress.getHouseNumber());
                                    AppPreference.setStringPreference(ctx, Constant.Address_Type, newAddress.getAddressType());
                                    AppPreference.setStringPreference(ctx, Constant.ADDRESS_LAT, newAddress.getLat());
                                    AppPreference.setStringPreference(ctx, Constant.ADDRESS_LONG, newAddress.getLong());
                                    AppPreference.setStringPreference(ctx, Constant.CONTRY, "India");
                                    // AppPreference.setStringPreference(ctx, Constant.ADDRESS_LANDMARK, address.getL);

                                    ConfirmationFragment fragment = new ConfirmationFragment(ctx);
                                    Utility.setFragment1(fragment, ctx, Constant.ShoppingFragment);

                                })
                                .setNegativeButton("NO", null)
                                .create()
                                .show();

                    }else {
                        Alerts.show(mContext, addressModel.getMessage());
                    }
                    addressShowAdapter.notifyDataSetChanged();

                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        }
    }
}
