package ibt.sabziwala.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ibt.sabziwala.R;
import ibt.sabziwala.adapter.OrderProductHistoryAdapter;
import ibt.sabziwala.model.history_single_order_responce.HistorySingleOrderModel;
import ibt.sabziwala.model.history_single_order_responce.Product;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.BaseActivity;
import ibt.sabziwala.utils.ConnectionDirector;
import retrofit2.Response;

public class OrderProductHistoryActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rv_order_product_list;
    OrderProductHistoryAdapter orderProductHistoryAdapter;
    ArrayList<Product> productArrayList = new ArrayList<>();
    ImageView btn_product_back;
    String data, strOrderId;
    TextView tv_order_number,tv_order_amount, tv_order_date1, tv_transaction_id, tv_order_Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product_history);

        init();
    }

    private void init()
    {
        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        tv_order_number = findViewById(R.id.tv_order_number);
        tv_order_amount = findViewById(R.id.tv_order_amount);
        tv_order_date1 = findViewById(R.id.tv_order_date1);
        tv_transaction_id = findViewById(R.id.tv_transaction_id);
        tv_order_Address = findViewById(R.id.tv_order_Address);

        strOrderId = getIntent().getStringExtra("OrderId");
        //productArrayList = getIntent().getParcelableArrayListExtra("ProductData");
        rv_order_product_list = findViewById(R.id.rv_order_product_list);
        orderProductHistoryAdapter = new OrderProductHistoryAdapter(mContext, productArrayList, this );
        rv_order_product_list.setHasFixedSize(true);
        rv_order_product_list.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_order_product_list.setAdapter(orderProductHistoryAdapter);

        btn_product_back = findViewById(R.id.btn_product_back);
        btn_product_back.setOnClickListener(this);

        historyDetailApi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_product_back :
                finish();
             break;
        }
    }

    private void historyDetailApi() {
        if (cd.isNetWorkAvailable()) {
           // String userId = AppPreference.getStringPreference(mContext, Constant.User_Id);
            RetrofitService.getOrderHistory(new Dialog(mContext), retrofitApiClient.orderDetails(strOrderId), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    HistorySingleOrderModel orderHistoryModel = (HistorySingleOrderModel) result.body();
                    if (!orderHistoryModel.getError())
                    {
                        Alerts.show(mContext, orderHistoryModel.getMessage());
                        tv_order_number.setText(orderHistoryModel.getOrder().get(0).getOrderNumber());
                        tv_order_amount.setText("Total Amount : "+orderHistoryModel.getOrder().get(0).getOrderAmount());
                        tv_order_date1.setText("Date : "+orderHistoryModel.getOrder().get(0).getOrderDate());
                        tv_transaction_id.setText("Transaction Id : "+orderHistoryModel.getOrder().get(0).getOrderTransactionId());
                        tv_order_Address.setText("Address : "+orderHistoryModel.getOrder().get(0).getOrderHouseNumber()+" "+orderHistoryModel.getOrder().get(0).getOrderStreetName()
                        +" "+orderHistoryModel.getOrder().get(0).getOrderLandmark()+" "+orderHistoryModel.getOrder().get(0).getOrderCity()+" "+orderHistoryModel.getOrder().get(0).getOrderState());
                        for (int i = 0 ; i > orderHistoryModel.getOrder().get(0).getProduct().size() ; i++)
                        {
                            Log.e("Name", String.valueOf(orderHistoryModel.getOrder().get(0).getProduct().get(i).getProductName()));
                        }
                        productArrayList.addAll(orderHistoryModel.getOrder().get(0).getProduct());
                        // orderArrayList.addAll(orderHistoryModel.getOrder());
                    }else {
                        Alerts.show(mContext, orderHistoryModel.getMessage());
                    }
                    orderProductHistoryAdapter.notifyDataSetChanged();
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
