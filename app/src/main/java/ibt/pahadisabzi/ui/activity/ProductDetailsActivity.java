package ibt.pahadisabzi.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.database.DatabaseHandler;
import ibt.pahadisabzi.model.ProductDetail;
import ibt.pahadisabzi.model.productdetail_responce.ProductDetailModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.ConnectionDirector;
import retrofit2.Response;

import static ibt.pahadisabzi.ui.activity.HomeActivity.cart_count;
import static ibt.pahadisabzi.ui.activity.HomeActivity.cart_number;
import static ibt.pahadisabzi.ui.activity.HomeActivity.cart_price;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvProductDetailName,tvProductDetailPrice,tvProductDetailSallingPrice,tvProductDetailType,tvProductDetailCostPrice,tvProductDetailMinOrder
    ,tvProductDetailStockQuantity , tvProductDetailAvailability, tvProductDetaildiscount , tvProductDetailDescription ;

    private LinearLayout llProductDetaildiscount,llProductDetailAvailability, llProductDetailStockQuantity, llProductDetailMinOrder, llProductDetailCostPrice,
            llProductDetailType;

    private ImageView ivProductDetailImg, iv_product_plus, iv_product_minus, btn_product_detail_back;
    private TextView tv_product_qty, tvProductDetailRating;
    private Button btnAddtoCart;
    private CardView btnAdd;
    private RelativeLayout ll_product_action;

    private String ProductID;
    private ProductDetail productDetail;
    private String DATABASE_CART = "cart.db";
    private String searchProductDetail;
    private String DATABASE_WISHLIST = "wishlist.db";
    private DatabaseHandler databaseCart, databaseWishlist;
    private ArrayList<ProductDetail> cartProductList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mContext = this;
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();

        ProductID = getIntent().getStringExtra("ProductID");


    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseCart = new DatabaseHandler(mContext, DATABASE_CART);
        databaseWishlist = new DatabaseHandler(mContext, DATABASE_WISHLIST);
        cartProductList.clear();
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        init();
    }

    private void init()
    {
        tvProductDetailAvailability = findViewById(R.id.tvProductDetailAvailability);
        tvProductDetailCostPrice = findViewById(R.id.tvProductDetailCostPrice);
        tvProductDetailDescription = findViewById(R.id.tvProductDetailDescription);
        tvProductDetaildiscount = findViewById(R.id.tvProductDetaildiscount);
        tvProductDetailMinOrder = findViewById(R.id.tvProductDetailMinOrder);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        tvProductDetailPrice = findViewById(R.id.tvProductDetailPrice);
        tvProductDetailSallingPrice = findViewById(R.id.tvProductDetailSallingPrice);
        tvProductDetailStockQuantity = findViewById(R.id.tvProductDetailStockQuantity);
        tvProductDetailType = findViewById(R.id.tvProductDetailType);
        ivProductDetailImg = findViewById(R.id.ivProductDetailImg);
        btn_product_detail_back = findViewById(R.id.btn_product_detail_back);
        tvProductDetailRating = findViewById(R.id.tvProductDetailRating);

        iv_product_plus = findViewById(R.id.iv_product_plus);
        iv_product_minus = findViewById(R.id.iv_product_minus);
        tv_product_qty = findViewById(R.id.tv_product_qty);

        llProductDetailAvailability = findViewById(R.id.llProductDetailAvailability);
        llProductDetailCostPrice = findViewById(R.id.llProductDetailCostPrice);
        llProductDetaildiscount = findViewById(R.id.llProductDetaildiscount);
        llProductDetailMinOrder = findViewById(R.id.llProductDetailMinOrder);
        llProductDetailStockQuantity = findViewById(R.id.llProductDetailStockQuantity);
        llProductDetailType = findViewById(R.id.llProductDetailType);
        ll_product_action = findViewById(R.id.ll_product_action);
        btnAdd = findViewById(R.id.btnAdd);
        btnAddtoCart = findViewById(R.id.btnAddtoCart);
        btnAddtoCart.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        iv_product_plus.setOnClickListener(this);
        iv_product_minus.setOnClickListener(this);
        btn_product_detail_back.setOnClickListener(this);

        productDetailApi();
    }

    private void productDetailApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getProductDetail(new Dialog(mContext), retrofitApiClient.getProductDetail(ProductID), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ProductDetailModel productDetailModel = (ProductDetailModel) result.body();

                    if (databaseCart.getContactsCount()) {
                        cartProductList = databaseCart.getAllUrlList();
                    }

                    if (!productDetailModel.getError())
                    {
                        tvProductDetailName.setText(productDetailModel.getProduct().getTitle());
                        if (productDetailModel.getProduct().getAvailability().equals("1")) {
                            tvProductDetailAvailability.setText("Active");
                            btnAddtoCart.setVisibility(View.VISIBLE);
                        }else {
                            tvProductDetailAvailability.setText("Not Active");
                            btnAddtoCart.setVisibility(View.GONE);
                        }
                        if (productDetailModel.getProduct().getType().equals("1")) {
                            tvProductDetailType .setText("Fruits");
                        }else {
                            tvProductDetailType.setText("Vegitable");
                        }

                        double percent = Double.parseDouble(productDetailModel.getProduct().getDiscount());
                        double salling = Double.parseDouble(productDetailModel.getProduct().getSellingPrice());
                        double dis =  salling * ((100-percent)/100);
                       // double dis = 100-percent;
                       // double orizinal_price = (salling*100)/dis;

                        tvProductDetailSallingPrice.setText("Rs. "+productDetailModel.getProduct().getSellingPrice());
                        tvProductDetailSallingPrice.setPaintFlags(tvProductDetailSallingPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
                        tvProductDetailRating.setText(""+productDetailModel.getProduct().getRating());
                        tvProductDetailPrice.setText("Rs. "+new DecimalFormat("##.##").format(dis));
                        tvProductDetailCostPrice.setText("Rs. "+new DecimalFormat("##.##").format(dis));
                        tvProductDetaildiscount.setText(productDetailModel.getProduct().getDiscount()+"%");
                        if (productDetailModel.getProduct().getQuantityType().equals("0")) {
                            tvProductDetailMinOrder.setText(productDetailModel.getProduct().getMinQuantity() + " Gm");
                            tvProductDetailStockQuantity.setText(productDetailModel.getProduct().getQuantity() + " Gm" );
                        }else if (productDetailModel.getProduct().getQuantityType().equals("1")) {
                            tvProductDetailMinOrder.setText(productDetailModel.getProduct().getMinQuantity() + " Kg");
                            tvProductDetailStockQuantity.setText(productDetailModel.getProduct().getQuantity() + " Kg" );
                        }else {
                            tvProductDetailMinOrder.setText(productDetailModel.getProduct().getMinQuantity() + " " );
                            tvProductDetailStockQuantity.setText(productDetailModel.getProduct().getQuantity() + " ");
                        }
                        tvProductDetailDescription.setText(productDetailModel.getProduct().getDescription());

                        if (productDetailModel.getProduct().getImage() != null) {
                            Glide.with(mContext).load(productDetailModel.getProduct().getImage()).error(R.drawable.logo2).into(ivProductDetailImg);
                        } else {
                            ivProductDetailImg.setImageResource(R.drawable.logo2);
                        }

                        productDetail = new ProductDetail();
                        productDetail.setTitle(productDetailModel.getProduct().getTitle());
                        productDetail.setRating(productDetailModel.getProduct().getRating());
                        productDetail.setImage(productDetailModel.getProduct().getImage());
                        productDetail.setDiscount(productDetailModel.getProduct().getDiscount());
                        productDetail.setAvailability(productDetailModel.getProduct().getAvailability());
                        productDetail.setMin_quantity(productDetailModel.getProduct().getMinQuantity());
                        productDetail.setQuantity_type(productDetailModel.getProduct().getQuantityType());
                        productDetail.setOrder_quantity(productDetailModel.getProduct().getQuantity());
                        productDetail.setDescription(productDetailModel.getProduct().getRateQuantity());
                        productDetail.setId(productDetailModel.getProduct().getId());
                        productDetail.setPrice(productDetailModel.getProduct().getSellingPrice());
                        productDetail.setType(productDetailModel.getProduct().getType());
                        productDetail.setQuantity(Integer.parseInt(productDetailModel.getProduct().getMinQuantity()));
                        productDetail.setQuantity(Integer.parseInt(productDetailModel.getProduct().getMinQuantity()));

                    if (cartProductList.size()>0){
                        int x,y;
                            for(y=0; y<cartProductList.size(); y++){
                                ProductDetail crtPrdct = cartProductList.get(y);
                                if (productDetail.getId().equals(crtPrdct.getId())){
                                    productDetail.setInCart(true);
                                    productDetail.setQuantity(crtPrdct.getQuantity());
                                    btnAdd.setVisibility(View.GONE);
                                    ll_product_action.setVisibility(View.VISIBLE);
                                    tv_product_qty.setText(productDetail.getQuantity()+"");
                                    if (productDetail.getQuantity()==Integer.parseInt(productDetail.getMin_quantity())){
                                        iv_product_minus.setImageResource(R.drawable.ic_delete);
                                    }
                                }
                            }
                        }

                    }else {
                        Alerts.show(mContext, productDetailModel.getMessage());
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAddtoCart :
                ArrayList<ProductDetail> cartProductList = new ArrayList<>();
                if (databaseCart.getContactsCount()) {
                    cartProductList = databaseCart.getAllUrlList();
                }
                if (cartProductList.size() > 0) {
                    Intent intent = new Intent(ProductDetailsActivity.this, AddToCartActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "Cart is empty.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAdd :
                productDetail.setQuantity(Integer.parseInt(productDetail.getMin_quantity()));
                addToCart();
                break;
            case R.id.iv_product_plus :
                plusItem();
                break;
            case R.id.iv_product_minus :
                minusItem();
                break;
            case R.id.btn_product_detail_back :
                finish();
                break;
        }
    }

    private void addToCart() {
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
       /* if (cartProductList.size() > 5) {
            Toast.makeText(mContext, "Cart full", Toast.LENGTH_SHORT).show();
        } else {*/
            if (cartProductList.size() > 0) {
                if (databaseCart.verification(productDetail.getId())) {
                    Toast.makeText(mContext, "Already added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    cart_count = cart_count + 1;
                    cart_number.setText("" + cart_count);
                    AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                    Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                    productDetail.setInCart(true);
                    btnAdd.setVisibility(View.GONE);
                    ll_product_action.setVisibility(View.VISIBLE);
                    tv_product_qty.setText(productDetail.getMin_quantity());
                    databaseCart.addItemCart(productDetail);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                productDetail.setInCart(true);
                btnAdd.setVisibility(View.GONE);
                ll_product_action.setVisibility(View.VISIBLE);
                tv_product_qty.setText(productDetail.getMin_quantity());
                databaseCart.addItemCart(productDetail);
            }
       // }
    }

    private void plusItem() {
        productDetail.setQuantity(Integer.parseInt(productDetail.getMin_quantity()));

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
                /*View v = rvproductList.getChildAt(pos);
                TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);*/

                int qty = Integer.parseInt(tv_product_qty.getText().toString());
                if (qty < Integer.parseInt(String.valueOf(productDetail.getOrder_quantity())))
                {
                    qty++;
                    productDetail.setQuantity(qty);
                    productDetail.setQuantity(qty);
                    databaseCart.updateUrl(productDetail);
                    tv_product_qty.setText(qty+"");
                    //adapter.notifyDataSetChanged();
                }else {

                }
                //tvQty.setText(qty + "");
                setTotal();
                if (qty > 1) {
                    iv_product_minus.setImageResource(R.drawable.icf_round_minus);
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
        //adapter.notifyDataSetChanged();
        //AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());
    }

    private void minusItem() {
        productDetail.setQuantity(Integer.parseInt(productDetail.getMin_quantity()));
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
                ProductDetail prDetails = cartProductList.get(q);
                /*View v = rvproductList.getChildAt(pos);
                TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);*/
                int qty = Integer.parseInt(tv_product_qty.getText().toString());
                try {
                    minQty = Integer.parseInt(prDetails.getMin_quantity());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (qty == minQty) {
                    databaseCart.deleteContact(prDetails);
                    cartProductList.remove(q);
                        ll_product_action.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.VISIBLE);
                } else {
                    qty--;
                    prDetails.setQuantity(qty);
                    databaseCart.updateUrl(prDetails);
                    tv_product_qty.setText(qty + "");
                }
                if (qty > Integer.parseInt(productDetail.getMin_quantity())) {
                    iv_product_minus.setImageResource(R.drawable.icf_round_minus);
                } else {
                    iv_product_minus.setImageResource(R.drawable.ic_delete);
                    Toast.makeText(mContext, "Item removed from cart", Toast.LENGTH_SHORT).show();
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


        //adapter.notifyDataSetChanged();
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
        cart_price.setText(""+new DecimalFormat("##.##").format(total));

    }

    public String getTotal() {
        float total = 0;
        float round_total = 0;
        ArrayList<ProductDetail> total_list = databaseCart.getAllUrlList();
        cart_number.setText(total_list.size());
        AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, total_list.size());
        for (int i = 0; i < total_list.size(); i++) {
            float pr = Float.parseFloat(total_list.get(i).getPrice());
            int qty = total_list.get(i).getQuantity();

            float tot = pr * qty;
            total += tot;
            round_total = Math.round(total);
        }
        return String.valueOf(round_total);
    }
}
