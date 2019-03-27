package ibt.sabzishoppee.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.adapter.OrderHistoryAdapter;
import ibt.sabzishoppee.adapter.OrderProductHistoryAdapter;
import ibt.sabzishoppee.model.order_history_responce.Product;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.utils.BaseActivity;
import ibt.sabzishoppee.utils.ConnectionDirector;

public class OrderProductHistoryActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rv_order_product_list;
    OrderProductHistoryAdapter orderProductHistoryAdapter;
    ArrayList<Product> productArrayList = new ArrayList<>();

    String data;

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

        productArrayList = getIntent().getParcelableArrayListExtra("ProductData");


        rv_order_product_list = findViewById(R.id.rv_order_product_list);

        orderProductHistoryAdapter = new OrderProductHistoryAdapter(mContext, productArrayList, this );
        rv_order_product_list.setHasFixedSize(true);
        rv_order_product_list.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_order_product_list.setAdapter(orderProductHistoryAdapter);


    }

    @Override
    public void onClick(View view) {

    }
}
