package ibt.sabzishoppee.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.adapter.SearchListAdapter;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.model.productlist_responce.Product;
import ibt.sabzishoppee.model.productlist_responce.ProductListModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseActivity;
import ibt.sabzishoppee.utils.ConnectionDirector;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchListAdapter.SearchAdapterListener{


    private List<Product> allUserLists = new ArrayList<>();
    private RecyclerView gridDetailrclv;
    private SearchListAdapter searchListAdapter;
    private EditText edtSearch;
    private String strUserId = "";
    private ImageView backActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        strUserId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        edtSearch = findViewById(R.id.et_search);
        //findViewById(R.id.imgBack).setOnClickListener(this);

        gridDetailrclv = findViewById(R.id.gridDetailrclv);
        backActivity = findViewById(R.id.ic_back_search);
        backActivity.setOnClickListener(this);
        gridDetailrclv.setHasFixedSize(true);

        searchListAdapter = new SearchListAdapter(allUserLists, mContext, this, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        gridDetailrclv.setLayoutManager(gridLayoutManager);
        gridDetailrclv.setItemAnimator(new DefaultItemAnimator());
        gridDetailrclv.setAdapter(searchListAdapter);

        productDetailApi();


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    private void productDetailApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getProductData(new Dialog(mContext), retrofitApiClient.productData(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ProductListModel productListModel = (ProductListModel) result.body();
                    allUserLists.clear();

                    if (!productListModel.getError())
                    {
                        Alerts.show(mContext, productListModel.getMessage());

                        allUserLists.addAll(productListModel.getProduct());

                    }else {
                        Alerts.show(mContext, productListModel.getMessage());
                    }
                    searchListAdapter.notifyDataSetChanged();


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_product1:

                break;
            case R.id.ic_back_search:
                finish();
                break;
        }

    }

    @Override
    public void onSearchSelected(Product contact) {
        Intent intent = new Intent(mContext , ProductDetailsActivity.class);
        intent.putExtra("ProductID", contact.getId());
        mContext.startActivity(intent);
    }
}
