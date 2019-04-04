package ibt.sabzishoppee.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ibt.sabzishoppee.R;
import ibt.sabzishoppee.adapter.ProductListAdapter;
import ibt.sabzishoppee.adapter.SearchListAdapter;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.database.DatabaseHandler;
import ibt.sabzishoppee.model.ProductDetail;
import ibt.sabzishoppee.model.productlist_responce.Product;
import ibt.sabzishoppee.model.productlist_responce.ProductListModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseActivity;
import ibt.sabzishoppee.utils.ConnectionDirector;
import retrofit2.Response;

import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_count;
import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_number;
import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_price;

public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchListAdapter.SearchAdapterListener{


    private List<Product> allUserLists = new ArrayList<>();
    private RecyclerView gridDetailrclv;
    //private SearchListAdapter searchListAdapter;
    private ProductListAdapter searchListAdapter;
    private EditText edtSearch;
    private String strUserId = "";
    private ImageView backActivity;

    public CircleImageView btn_float_cart;

    private String DATABASE_CART = "cart.db";
    private String searchProductDetail;
    private String DATABASE_WISHLIST = "wishlist.db";
    private DatabaseHandler databaseCart, databaseWishlist;
    private ArrayList<ProductDetail> cartProductList = new ArrayList<>();
    private ProductDetail productDetail, productDetail1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        databaseCart = new DatabaseHandler(mContext, DATABASE_CART);
        databaseWishlist = new DatabaseHandler(mContext, DATABASE_WISHLIST);
        cartProductList.clear();
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }

        strUserId = AppPreference.getStringPreference(mContext, Constant.User_Id);
        edtSearch = findViewById(R.id.et_search);
        //findViewById(R.id.imgBack).setOnClickListener(this);

        gridDetailrclv = findViewById(R.id.gridDetailrclv);
        backActivity = findViewById(R.id.ic_back_search);
        btn_float_cart = findViewById(R.id.btn_float_cart);
        backActivity.setOnClickListener(this);
        gridDetailrclv.setHasFixedSize(true);
        btn_float_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, AddtoCartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        searchListAdapter = new ProductListAdapter( mContext, (ArrayList<Product>) allUserLists, this, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        gridDetailrclv.setLayoutManager(linearLayoutManager);
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
                        allUserLists.addAll(productListModel.getProduct());

                        if (databaseCart.getContactsCount()) {
                            cartProductList = databaseCart.getAllUrlList();
                        }
                        if (cartProductList.size()>0){
                            int x,y;
                            for(x=0; x<allUserLists.size(); x++){
                                Product prdct = allUserLists.get(x);
                                for(y=0; y<cartProductList.size(); y++){
                                    ProductDetail crtPrdct = cartProductList.get(y);
                                    if (prdct.getId().equals(crtPrdct.getId())){
                                        allUserLists.get(x).setInCart(true);
                                        allUserLists.get(x).setProductQuantity(String.valueOf(crtPrdct.getQuantity()));
                                    }
                                }
                            }
                        }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llItem:
                String prdId = view.getTag().toString();
                Intent intent = new Intent(mContext , ProductDetailsActivity.class);
                intent.putExtra("ProductID", prdId);
                startActivity(intent);
                break;
            case R.id.ic_back_search:
                finish();
                break;
            case R.id.btnAdd :
                int position = 0;
                String prId = view.getTag().toString();
                for (int i = 0; i<allUserLists.size(); i++){
                    if (allUserLists.get(i).getId().equals(prId))
                        position = i;
                }
                productDetail = new ProductDetail();
                productDetail.setTitle(allUserLists.get(position).getTitle());
                productDetail.setRating(allUserLists.get(position).getRating());
                productDetail.setImage(allUserLists.get(position).getImage());
                productDetail.setDiscount(allUserLists.get(position).getDiscount());
                productDetail.setAvailability(allUserLists.get(position).getAvailability());
                productDetail.setMin_quantity(allUserLists.get(position).getMinQuantity());
                productDetail.setQuantity_type(allUserLists.get(position).getQuantityType());
                productDetail.setOrder_quantity(allUserLists.get(position).getQuantity());
                productDetail.setDescription(allUserLists.get(position).getDescription());
                productDetail.setId(allUserLists.get(position).getId());
                productDetail.setPrice(allUserLists.get(position).getSellingPrice());
                productDetail.setType(allUserLists.get(position).getType());
                productDetail.setQuantity(Integer.parseInt(allUserLists.get(position).getMinQuantity()));
                addToCart(position);
                break;


            case R.id.iv_product_plus :
                int pos = 0;
                String pId = view.getTag().toString();
                for (int i = 0; i<allUserLists.size(); i++){
                    if (allUserLists.get(i).getId().equals(pId))
                        pos = i;
                }
                plusItem(view, pos);
                break;

            case R.id.iv_product_minus :
                minusItem(view);

                break;
        }

    }

    private void addToCart(int pos) {

        View v = gridDetailrclv.getChildAt(pos);

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        if (cartProductList.size() > 5) {
            Toast.makeText(mContext, "Cart full", Toast.LENGTH_SHORT).show();
        } else {
            if (cartProductList.size() > 0) {
                if (databaseCart.verification(productDetail.getId())) {
                    Toast.makeText(mContext, "Already added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    cart_count = cart_count + 1;
                    cart_number.setText("" + cart_count);
                    AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                    Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                    allUserLists.get(pos).setInCart(true);
                    searchListAdapter.notifyDataSetChanged();
                    v.findViewById(R.id.btnAdd).setVisibility(View.GONE);
                    v.findViewById(R.id.ll_product_action).setVisibility(View.VISIBLE);
                    databaseCart.addItemCart(productDetail);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                allUserLists.get(pos).setInCart(true);
                searchListAdapter.notifyDataSetChanged();
                v.findViewById(R.id.btnAdd).setVisibility(View.GONE);
                v.findViewById(R.id.ll_product_action).setVisibility(View.VISIBLE);
                databaseCart.addItemCart(productDetail);
            }
        }
    }

    private void plusItem(View view, int pos) {
        productDetail = new ProductDetail();
        productDetail.setTitle(allUserLists.get(pos).getTitle());
        productDetail.setRating(allUserLists.get(pos).getRating());
        productDetail.setImage(allUserLists.get(pos).getImage());
        productDetail.setDiscount(allUserLists.get(pos).getDiscount());
        productDetail.setAvailability(allUserLists.get(pos).getAvailability());
        productDetail.setMin_quantity(allUserLists.get(pos).getMinQuantity());
        productDetail.setQuantity_type(allUserLists.get(pos).getQuantityType());
        productDetail.setOrder_quantity(allUserLists.get(pos).getQuantity());
        productDetail.setDescription(allUserLists.get(pos).getDescription());
        productDetail.setId(allUserLists.get(pos).getId());
        productDetail.setPrice(allUserLists.get(pos).getSellingPrice());
        productDetail.setType(allUserLists.get(pos).getType());
        productDetail.setQuantity(1);

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        if (cartProductList.size() > 0) {
            if (databaseCart.verification(productDetail.getId())) {
                int q = 0;
                for(int p = 0; p<cartProductList.size(); p++){
                    ProductDetail pd = cartProductList.get(p);
                    if (pd.getId().equals(productDetail.getId())){
                        q=p;
                    }
                }
                //Toast.makeText(mContext, "position : "+exctPos, Toast.LENGTH_SHORT).show();
                ProductDetail productDetail = cartProductList.get(q);//why cart product list
                View v = gridDetailrclv.getChildAt(pos);
                TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);

                int qty = Integer.parseInt(tvQty.getText().toString());
                if (qty < Integer.parseInt(allUserLists.get(pos).getQuantity()))
                {
                    qty++;
                    productDetail.setQuantity(qty);
                    allUserLists.get(pos).setProductQuantity(""+qty);
                    databaseCart.updateUrl(productDetail);
                    searchListAdapter.notifyDataSetChanged();
                }else {

                }
                //tvQty.setText(qty + "");
                setTotal();
                if (qty > 1) {
                    minus_iv.setImageResource(R.drawable.icf_round_minus);
                } else {
                    // minus_iv.setImageResource(R.drawable.ic_delete);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                databaseCart.addItemCart(productDetail);
            }
        } else {
            cart_count = cart_count + 1;
            cart_number.setText("" + cart_count);
            AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
            databaseCart.addItemCart(productDetail);
        }
        searchListAdapter.notifyDataSetChanged();
        //AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());
    }

    private void minusItem(View view) {
        int pos = Integer.parseInt(view.getTag().toString());
        productDetail = new ProductDetail();
        productDetail.setTitle(allUserLists.get(pos).getTitle());
        productDetail.setRating(allUserLists.get(pos).getRating());
        productDetail.setImage(allUserLists.get(pos).getImage());
        productDetail.setDiscount(allUserLists.get(pos).getDiscount());
        productDetail.setAvailability(allUserLists.get(pos).getAvailability());
        productDetail.setMin_quantity(allUserLists.get(pos).getMinQuantity());
        productDetail.setQuantity_type(allUserLists.get(pos).getQuantityType());
        productDetail.setOrder_quantity(allUserLists.get(pos).getQuantity());
        productDetail.setDescription(allUserLists.get(pos).getDescription());
        productDetail.setId(allUserLists.get(pos).getId());
        productDetail.setPrice(allUserLists.get(pos).getSellingPrice());
        productDetail.setType(allUserLists.get(pos).getType());
        productDetail.setQuantity(Integer.parseInt(allUserLists.get(pos).getMinQuantity()));

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        if (cartProductList.size() > 0) {
            if (databaseCart.verification(productDetail.getId())) {
                int minQty = 0;
                int q = 0;
                for(int p = 0; p<cartProductList.size(); p++){
                    ProductDetail pd = cartProductList.get(p);
                    if (pd.getId().equals(productDetail.getId())){
                        q=p;
                    }
                }
                ProductDetail productDetail = cartProductList.get(q);
                View v = gridDetailrclv.getChildAt(pos);
                TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);
                int qty = Integer.parseInt(tvQty.getText().toString());
                try {
                    minQty = Integer.parseInt(productDetail.getMin_quantity());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (qty == minQty) {
                       /* databaseCart.deleteContact(productDetail);
                        cartProductList.remove(pos);
                        adapter.notifyDataSetChanged();*/
                } else {
                    qty--;
                    productDetail.setQuantity(qty);
                    allUserLists.get(pos).setProductQuantity(""+qty);
                    databaseCart.updateUrl(productDetail);
                    //tvQty.setText(qty + "");
                }
                if (qty > 1) {
                    minus_iv.setImageResource(R.drawable.icf_round_minus);
                } else {
                    // minus_iv.setImageResource(R.drawable.ic_delete);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                databaseCart.addItemCart(productDetail);
            }
        } else {
            cart_count = cart_count + 1;
            cart_number.setText("" + cart_count);
            AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
            Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
            databaseCart.addItemCart(productDetail);
        }


        searchListAdapter.notifyDataSetChanged();
        setTotal();
        //AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, list.size());
    }

    public void setTotal() {
        float total = 0;
        ArrayList<ProductDetail> total_list = databaseCart.getAllUrlList();
        cart_number.setText("" + total_list.size());
        AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, total_list.size());
        for (int i = 0; i < total_list.size(); i++) {

            float percent = Float.parseFloat(total_list.get(i).getDiscount());
            float pr = Float.parseFloat(total_list.get(i).getPrice());
            float dis1 =  pr * ((100-percent)/100);
            int qty = total_list.get(i).getQuantity();

            float tot = dis1 * qty;
            total += tot;
            total = Math.round(total);
        }
        // place_bt.setText("Place this Order :   Rs " + total);

        // tvTotalItem.setText("Total Items :"+total_list.size());
        cart_price.setText(""+total);

    }

    @Override
    public void onSearchSelected(Product contact) {
       /* Intent intent = new Intent(mContext , ProductDetailsActivity.class);
        intent.putExtra("ProductID", contact.getId());
        mContext.startActivity(intent);*/

       // position = Integer.parseInt(view.getTag().toString());

        productDetail = new ProductDetail();
        productDetail.setTitle(contact.getTitle());
        productDetail.setRating(contact.getRating());
        productDetail.setImage(contact.getImage());
        productDetail.setDiscount(contact.getDiscount());
        productDetail.setAvailability(contact.getAvailability());
        productDetail.setMin_quantity(contact.getMinQuantity());
        productDetail.setQuantity_type(contact.getQuantityType());
        productDetail.setOrder_quantity(contact.getQuantity());
        productDetail.setDescription(contact.getDescription());
        productDetail.setId(contact.getId());
        productDetail.setPrice(contact.getSellingPrice());
        productDetail.setType(contact.getType());
        productDetail.setQuantity(1);
        addtoCart();

    }


    private void addtoCart() {
        /*********************************************************************************************************/
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }

        if (cartProductList.size() > 10) {
            //Alerts.show(this, "Cart full");
            Toast.makeText(mContext, "Cart full", Toast.LENGTH_SHORT).show();
        } else {
            if (cartProductList.size() > 0) {
                if (databaseCart.verification(productDetail.getId())) {
                    //Alerts.show(ctx, "Already added to Cart");

                    Toast.makeText(mContext, "Already added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    cart_count = cart_count + 1;
                    cart_number.setText("" + cart_count);
                    AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                    Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                    databaseCart.addItemCart(productDetail);
                    btn_float_cart.setVisibility(View.VISIBLE);

                }
            } else {
                // productDetail.setSelected_size(selected_size);
                // productDetail.setSelected_color(selected_color);
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                //Alerts.show(mContext, "Added to Cart");
                databaseCart.addItemCart(productDetail);

            }
        }
    }

    private void plusItem(int pos) {
        productDetail = new ProductDetail();
        productDetail.setTitle(allUserLists.get(pos).getTitle());
        productDetail.setRating(allUserLists.get(pos).getRating());
        productDetail.setImage(allUserLists.get(pos).getImage());
        productDetail.setDiscount(allUserLists.get(pos).getDiscount());
        productDetail.setAvailability(allUserLists.get(pos).getAvailability());
        productDetail.setMin_quantity(allUserLists.get(pos).getMinQuantity());
        productDetail.setQuantity_type(allUserLists.get(pos).getQuantityType());
        productDetail.setOrder_quantity(allUserLists.get(pos).getQuantity());
        productDetail.setDescription(allUserLists.get(pos).getDescription());
        productDetail.setId(allUserLists.get(pos).getId());
        productDetail.setPrice(allUserLists.get(pos).getSellingPrice());
        productDetail.setType(allUserLists.get(pos).getType());
        productDetail.setQuantity(1);

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        if (cartProductList.size() > 0) {
            if (databaseCart.verification(productDetail.getId())) {
                ProductDetail productDetail = cartProductList.get(pos);
                View v = gridDetailrclv.getChildAt(pos);
                TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);

                int qty = Integer.parseInt(tvQty.getText().toString());
                qty++;
                productDetail.setQuantity(qty);
                allUserLists.get(pos).setProductQuantity(""+qty);
                databaseCart.updateUrl(productDetail);
                //tvQty.setText(qty + "");
                //setTotal();
                if (qty > 1) {
                    minus_iv.setImageResource(R.drawable.ic_minus);
                } else {
                    // minus_iv.setImageResource(R.drawable.ic_delete);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                databaseCart.addItemCart(productDetail);
            }
        } else {
            cart_count = cart_count + 1;
            cart_number.setText("" + cart_count);
            AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
            databaseCart.addItemCart(productDetail);
        }
        searchListAdapter.notifyDataSetChanged();
        //AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());
    }

    private void minusItem(View view, int pos) {
        /*int pos = Integer.parseInt(view.getTag().toString());
        productDetail = new ProductDetail();
        productDetail.setTitle(allUserLists.get(pos).getTitle());
        productDetail.setRating(allUserLists.get(pos).getRating());
        productDetail.setImage(allUserLists.get(pos).getImage());
        productDetail.setDiscount(allUserLists.get(pos).getDiscount());
        productDetail.setAvailability(allUserLists.get(pos).getAvailability());
        productDetail.setMin_quantity(allUserLists.get(pos).getMinQuantity());
        productDetail.setQuantity_type(allUserLists.get(pos).getQuantityType());
        productDetail.setOrder_quantity(allUserLists.get(pos).getQuantity());
        productDetail.setDescription(allUserLists.get(pos).getDescription());
        productDetail.setId(allUserLists.get(pos).getId());
        productDetail.setPrice(allUserLists.get(pos).getSellingPrice());
        productDetail.setType(allUserLists.get(pos).getType());
        productDetail.setQuantity(1);

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        if (cartProductList.size() > 0) {
            if (databaseCart.verification(productDetail.getId())) {
                ProductDetail productDetail = cartProductList.get(pos);
                View v = gridDetailrclv.getChildAt(pos);
                TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);
                int qty = Integer.parseInt(tvQty.getText().toString());
                if (qty == 1) {
                       *//* databaseCart.deleteContact(productDetail);
                        cartProductList.remove(pos);
                        adapter.notifyDataSetChanged();*//*
                } else {
                    qty--;
                    productDetail.setQuantity(qty);
                    allUserLists.get(pos).setProductQuantity(""+qty);
                    databaseCart.updateUrl(productDetail);
                    // tvQty.setText(qty + "");
                }
                if (qty > 1) {
                    minus_iv.setImageResource(R.drawable.ic_minus);
                } else {
                    // minus_iv.setImageResource(R.drawable.ic_delete);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                databaseCart.addItemCart(productDetail);
            }
        } else {
            cart_count = cart_count + 1;
            cart_number.setText("" + cart_count);
            AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
            Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
            databaseCart.addItemCart(productDetail);
        }


        searchListAdapter.notifyDataSetChanged();*/
       // setTotal();
        //AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, list.size());
    }
}
