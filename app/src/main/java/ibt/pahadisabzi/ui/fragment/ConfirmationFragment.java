package ibt.pahadisabzi.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.adapter.AdapterConfirmation;
import ibt.pahadisabzi.adapter.DelivaryTimeAdapter;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.database.DatabaseHandler;
import ibt.pahadisabzi.model.ProductDetail;
import ibt.pahadisabzi.model.delivary_time_responce.DelivaryTimeModel;
import ibt.pahadisabzi.model.delivary_time_responce.Deliverytiming;
import ibt.pahadisabzi.model.order_responce.Order;
import ibt.pahadisabzi.model.order_responce.OrderModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.ui.activity.HomeActivity;
import ibt.pahadisabzi.ui.activity.ThankyouActivity;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseFragment;
import ibt.pahadisabzi.utils.ConnectionDirector;
import ibt.pahadisabzi.utils.SessionManager;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static ibt.pahadisabzi.ui.activity.CheckOutActivity.tv_address;
import static ibt.pahadisabzi.ui.activity.CheckOutActivity.tv_confirmation;
import static ibt.pahadisabzi.ui.activity.SplashActivity.mypreference;

@SuppressLint("ValidFragment")
public class ConfirmationFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Context ctx;
    RecyclerView recyclerView;
    TextView total_tv, tv_payment;
    EditText etNote;
    LinearLayout ordernow_ll;
    public static String Payment_Package = "";
    SessionManager sessionManager;
    public DatabaseHandler databaseCart;
    private String DATABASE_CART = "cart.db";
    //ConnectionDetector connectionDetector;
    String totalAmount1 = "0";
    String Offer_Amount = "0";
    private Button tvorderNow;

    ArrayList<Order> productDataModelArrayList = new ArrayList<>();
    String strDelivaryTime = "";
    Spinner spDelivaryTime;
    ArrayList<Deliverytiming> deliverytimings = new ArrayList<>();
    @SuppressLint("ValidFragment")
    public ConfirmationFragment(Context ctx) {
        this.ctx = ctx;
        sessionManager = new SessionManager(ctx);
        databaseCart = new DatabaseHandler(ctx, DATABASE_CART);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirmation, null);

        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        tv_address.setBackgroundColor(getResources().getColor(R.color.gray_c));
        tv_confirmation.setBackgroundColor(getResources().getColor(R.color.red_d));

        initXml(view);
        setOrder();
        return view;
    }

    private void setOrder() {

        ArrayList<ProductDetail> orderlist = databaseCart.getAllUrlList();
        AdapterConfirmation adapter = new AdapterConfirmation(orderlist, ctx);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void initXml(View view) {
        //cart_count = AppPreference.getIntegerPreference(ctx, Constant.TOTAL_AMOUNT); //0 is the default value.

       /* SharedPreferences prefs = getActivity().getSharedPreferences(Constant.TOTAL_AMOUNT, MODE_PRIVATE);
        totalAmount = prefs.getString("Total", "0");//"No name defined" is the default value.
        Offer_Amount = prefs.getString("Offer", "0");//"No name defined" is the default value.
        Log.e("Total ",".."+totalAmount);*/

       /* totalAmount = sessionManager.getData(SessionManager.KEY_ORDER_PRICE);
        Log.e("total ",".."+totalAmount);*/

        SharedPreferences prefs = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);

       // totalAmount1 = prefs.getString("total_price", "0");//"No name defined" is the default value.
        totalAmount1 = AppPreference.getStringPreference(ctx, Constant.TOTAL_AMOUNT);
        recyclerView = view.findViewById(R.id.rv_conforder_recycler);
        total_tv = view.findViewById(R.id.tv_confirmation_total);
        tv_payment = view.findViewById(R.id.tv_payment);
        etNote = view.findViewById(R.id.etNote);
        tvorderNow = view.findViewById(R.id.tv_orderNow);
        tvorderNow.setOnClickListener(this);

        spDelivaryTime = view.findViewById(R.id.spDelivaryTime);
        double i2 = Double.parseDouble(totalAmount1);
        total_tv.setText(new DecimalFormat("##.##").format(i2));
        tv_payment.setText(new DecimalFormat("##.##").format(i2));


        /*total_tv.setText(Utility.getCartTotal(databaseCart));
        Payment_Package = Utility.getCartTotal(databaseCart);*/
        Payment_Package = totalAmount1;
        delivaryTimeApi();
        spDelivaryTime.setOnItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_orderNow:
                orderApi();
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

       /* SharedPreferences prefs = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);
        totalAmount1 = prefs.getString("total_price", "0");//"No name defined" is the default value// .*/

        totalAmount1 = AppPreference.getStringPreference(ctx, Constant.TOTAL_AMOUNT);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            totalAmount1 = AppPreference.getStringPreference(ctx, Constant.TOTAL_AMOUNT);

           /* SharedPreferences prefs = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);
            totalAmount1 = prefs.getString("total_price", "0");*///"No name defined" is the default value.        }
        }
    }

    private void delivaryTimeApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getDelivaryDate(new Dialog(mContext), retrofitApiClient.getDelivaryTime(), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        DelivaryTimeModel delivaryTimeModel = (DelivaryTimeModel) result.body();
                        if (!delivaryTimeModel.getResult()) {

                            deliverytimings.addAll(delivaryTimeModel.getDeliverytiming());
                            DelivaryTimeAdapter customAdapter = new DelivaryTimeAdapter(mContext, deliverytimings);
                            spDelivaryTime.setAdapter(customAdapter);
                        } else {
                            Alerts.show(mContext, delivaryTimeModel.getMessage());
                        }
                    }

                    @Override
                    public void onResponseFailed(String error) {
                        Alerts.show(mContext, error);
                    }
                });

        }else{
            cd.show(mContext);
        }
    }


    private void orderApi() {
        if (cd.isNetWorkAvailable()) {

            String strNote = etNote.getText().toString();
            String name = AppPreference.getStringPreference(ctx, Constant.Name);
            String user_id = AppPreference.getStringPreference(ctx, Constant.User_Id);
            String strHourseNo = AppPreference.getStringPreference(ctx, Constant.House_no);
            String strAddressType = AppPreference.getStringPreference(ctx, Constant.Address_Type);
            String strPaymentType = AppPreference.getStringPreference(ctx, Constant.PaymentMethord);
            String strLandMark = AppPreference.getStringPreference(ctx, Constant.ADDRESS_LANDMARK);
            String strLat = AppPreference.getStringPreference(ctx, Constant.ADDRESS_LAT);
            String strLong = AppPreference.getStringPreference(ctx, Constant.ADDRESS_LONG);

            String mobile = AppPreference.getStringPreference(ctx, Constant.MobileNumber);
            String address = AppPreference.getStringPreference(ctx, Constant.Address);
            String city = AppPreference.getStringPreference(ctx, Constant.City);
            String state = AppPreference.getStringPreference(ctx, Constant.State);
            String country = sessionManager.getData(SessionManager.KEY_ORDER_COUNTRY);
            String code = AppPreference.getStringPreference(ctx, Constant.PinCode);
            String paytype = sessionManager.getData(SessionManager.KEY_PAYMENT_TYPE);
            float tot = 0;
            float gmquty = 0;
            ArrayList<ProductDetail> list;
            list = databaseCart.getAllUrlList();
            for (int i = 0; i < list.size(); i++) {

                double percent = Double.parseDouble(list.get(i).getDiscount());
                double cost = Double.parseDouble(list.get(i).getPrice());
                double dis = cost * ((100 - percent) / 100);

                tot = list.get(i).getQuantity() * Float.parseFloat(String.valueOf(dis));

                gmquty = list.get(i).getQuantity() * Float.parseFloat(list.get(i).getDescription());

                Order productDataModel = new Order();
                productDataModel.setProductId(list.get(i).getId());
                productDataModel.setProductDiscount(list.get(i).getDiscount());
                productDataModel.setProductPrice(String.valueOf(tot));
                productDataModel.setProductQuantity(String.valueOf(gmquty));
                productDataModel.setProductType(list.get(i).getType());
                productDataModel.setQuantityType(list.get(i).getQuantity_type());
                productDataModelArrayList.add(productDataModel);
            }
            Gson gson = new GsonBuilder().setLenient().create();
            String data = gson.toJson(productDataModelArrayList);

            if (strDelivaryTime.equals("")) {
                Toast.makeText(mContext, "Please select delivery time", Toast.LENGTH_SHORT).show();
            } else {
                RetrofitService.setOrder(new Dialog(mContext), retrofitApiClient.setOrder(user_id, "0", "0", strDelivaryTime, address, strHourseNo, strLandMark, city, strAddressType, totalAmount1, address, strLat, strLong, state, code, strNote, data), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        OrderModel orderModel = (OrderModel) result.body();
                        if (!orderModel.getError()) {
                           // Alerts.show(mContext, orderModel.getMessage());
                            if (databaseCart.getContactsCount()) {
                                databaseCart.deleteallCart();
                                Intent intent = new Intent(mContext, ThankyouActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            Alerts.show(mContext, orderModel.getMessage());
                        }
                    }

                    @Override
                    public void onResponseFailed(String error) {
                        Alerts.show(mContext, error);
                    }
                });
            }
            }else{
                cd.show(mContext);
            }
        }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Alerts.show(mContext, deliverytimings.get(i).getDeliveryTimingStartTime()+ " - "+ deliverytimings.get(i).getDeliveryTimingEndTime());
        strDelivaryTime = deliverytimings.get(i).getDeliveryTimingStartTime()+ " - "+ deliverytimings.get(i).getDeliveryTimingEndTime();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
