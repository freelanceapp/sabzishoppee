package ibt.sabzishoppee.ui.fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.adapter.AdapterConfirmation;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.database.DatabaseHandler;
import ibt.sabzishoppee.model.ProductDetail;
import ibt.sabzishoppee.model.address_add_responce.AddAddressModel;
import ibt.sabzishoppee.model.order_responce.Order;
import ibt.sabzishoppee.model.order_responce.OrderModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.ui.activity.HomeActivity;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseFragment;
import ibt.sabzishoppee.utils.ConnectionDirector;
import ibt.sabzishoppee.utils.SessionManager;
import ibt.sabzishoppee.utils.Utility;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static ibt.sabzishoppee.ui.activity.SplashActivity.mypreference;

@SuppressLint("ValidFragment")
public class ConfirmationFragment extends BaseFragment implements View.OnClickListener {

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
        totalAmount1 = prefs.getString("total_price", "0");//"No name defined" is the default value.

        recyclerView = view.findViewById(R.id.rv_conforder_recycler);
        total_tv = view.findViewById(R.id.tv_confirmation_total);
        tv_payment = view.findViewById(R.id.tv_payment);
        etNote = view.findViewById(R.id.etNote);


        tvorderNow = view.findViewById(R.id.tv_orderNow);
        tvorderNow.setOnClickListener(this);

        total_tv.setText(" " + totalAmount1);
        tv_payment.setText(" " + totalAmount1);

        /*total_tv.setText(Utility.getCartTotal(databaseCart));
        Payment_Package = Utility.getCartTotal(databaseCart);*/
        Payment_Package = totalAmount1;
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

        SharedPreferences prefs = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);
        totalAmount1 = prefs.getString("total_price", "0");//"No name defined" is the default value.
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SharedPreferences prefs = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);
            totalAmount1 = prefs.getString("total_price", "0");//"No name defined" is the default value.        }
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

            String mobile = AppPreference.getStringPreference(ctx, Constant.MobileNumber);
            String address = AppPreference.getStringPreference(ctx, Constant.Address);
            String city = AppPreference.getStringPreference(ctx, Constant.City);
            String state = AppPreference.getStringPreference(ctx, Constant.State);
            String country = sessionManager.getData(SessionManager.KEY_ORDER_COUNTRY);
            String code = AppPreference.getStringPreference(ctx, Constant.PinCode);
            String paytype = sessionManager.getData(SessionManager.KEY_PAYMENT_TYPE);
            float tot = 0;
            ArrayList<ProductDetail> list = databaseCart.getAllUrlList();
            for (int i = 0; i < list.size(); i++) {
                tot = list.get(i).getQuantity() * Float.parseFloat(list.get(i).getPrice());

                Order productDataModel = new Order();
                productDataModel.setProductId(list.get(i).getId());
                productDataModel.setProductDiscount(list.get(i).getDiscount());
                productDataModel.setProductPrice(String.valueOf(tot));
                productDataModel.setProductQuantity(list.get(i).getOrder_quantity());
                productDataModel.setProductType(list.get(i).getType());


                productDataModelArrayList.add(productDataModel);
            }

            Gson gson = new GsonBuilder().setLenient().create();
            String data = gson.toJson(productDataModelArrayList);

            RetrofitService.setOrder(new Dialog(mContext), retrofitApiClient.setOrder(user_id, strPaymentType, "0", address,strHourseNo,"",city,strAddressType,totalAmount1,address,"72.123123","123.123123",state,code,
                    strNote,data), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    OrderModel orderModel = (OrderModel) result.body();

                    if (!orderModel.getError())
                    {
                        Alerts.show(mContext, orderModel.getMessage());

                        if (databaseCart.getContactsCount()) {
                            databaseCart.deleteallCart();
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            getActivity().finish();


                        }


                    }else {
                        Alerts.show(mContext, orderModel.getMessage());
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
