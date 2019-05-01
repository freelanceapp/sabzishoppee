package ibt.pahadisabzi.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.adapter.OrderHistoryAdapter;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.model.order_history_responce.Order;
import ibt.pahadisabzi.model.order_history_responce.OrderHistoryModel;
import ibt.pahadisabzi.model.order_history_responce.Product;
import ibt.pahadisabzi.model.signup_responce.SignUpModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.ui.fragment.ForgotPasswordFragment;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.ConnectionDirector;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    ImageView btn_history_back, img_no_order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        img_no_order = findViewById(R.id.img_no_order);
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

            case R.id.btn_cancel :
                int pos1 = Integer.parseInt(view.getTag().toString());
                String product_id = orderArrayList.get(pos1).getOrderId();
                orderCancel(product_id);
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
                        if (orderArrayList.size()>0) {
                            img_no_order.setVisibility(View.VISIBLE);
                            rvOrderHistory.setVisibility(View.GONE);
                        }else {
                            orderArrayList.addAll(orderHistoryModel.getOrder());
                            img_no_order.setVisibility(View.GONE);
                            rvOrderHistory.setVisibility(View.VISIBLE);
                        }
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


    private void orderCancel(String orderId)
    {
        String userId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        RetrofitService.getSignData(new Dialog(mContext), retrofitApiClient.orderCancel(orderId, userId), new WebResponse() {
            @Override
            public void onResponseSuccess(Response<?> result) {
                SignUpModel responseBody = (SignUpModel) result.body();
                    Alerts.show(mContext , responseBody.getMessage());
                    historyDetailApi();
            }
            @Override
            public void onResponseFailed(String error) {
                Alerts.show(mContext, error);
            }
        });
    }

}
