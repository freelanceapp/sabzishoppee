package ibt.sabziwala.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import ibt.sabziwala.R;
import ibt.sabziwala.adapter.OrderHistoryAdapter;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.model.order_history_responce.Order;
import ibt.sabziwala.model.order_history_responce.OrderHistoryModel;
import ibt.sabziwala.model.order_history_responce.Product;
import ibt.sabziwala.retrofit_provider.RetrofitService;
import ibt.sabziwala.retrofit_provider.WebResponse;
import ibt.sabziwala.utils.Alerts;
import ibt.sabziwala.utils.AppPreference;
import ibt.sabziwala.utils.BaseActivity;
import ibt.sabziwala.utils.ConnectionDirector;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    ImageView btn_history_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        adapter = new OrderHistoryAdapter(mContext, orderArrayList, this );
        rvOrderHistory.setHasFixedSize(true);
        rvOrderHistory.setLayoutManager(new GridLayoutManager(mContext, 1));
        rvOrderHistory.setAdapter(adapter);

        btn_history_back = findViewById(R.id.btn_history_back);
        btn_history_back.setOnClickListener(this);
        historyDetailApi();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ll_history :
                int pos = Integer.parseInt(view.getTag().toString());
                products.addAll(orderArrayList.get(pos).getProduct());
                Intent intent = new Intent(OrderHistoryActivity.this, OrderProductHistoryActivity.class);
                intent.putExtra("OrderId", orderArrayList.get(pos).getOrderId());
                intent.putParcelableArrayListExtra("ProductData", products);
                startActivity(intent);
                break;

            case R.id.btn_history_back :
                finish();
                break;
        }
    }


    private void historyDetailApi() {
        if (cd.isNetWorkAvailable()) {
            String userId = AppPreference.getStringPreference(mContext, Constant.User_Id);
            RetrofitService.getOrderHistory1(new Dialog(mContext), retrofitApiClient.getOrderHistory(userId), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    OrderHistoryModel orderHistoryModel = (OrderHistoryModel) result.body();
                    orderArrayList.clear();

                    if (!orderHistoryModel.getError())
                    {
                        Alerts.show(mContext, orderHistoryModel.getMessage());

                        orderArrayList.addAll(orderHistoryModel.getOrder());

                    }else {
                        Alerts.show(mContext, orderHistoryModel.getMessage());
                    }
                    adapter.notifyDataSetChanged();


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
