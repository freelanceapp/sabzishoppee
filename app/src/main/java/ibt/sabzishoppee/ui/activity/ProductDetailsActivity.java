package ibt.sabzishoppee.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.database.DatabaseHandler;
import ibt.sabzishoppee.model.ProductDetail;
import ibt.sabzishoppee.model.productdetail_responce.ProductDetailModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseActivity;
import ibt.sabzishoppee.utils.ConnectionDirector;
import retrofit2.Response;

import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_count;
import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_number;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvProductDetailName,tvProductDetailPrice,tvProductDetailSallingPrice,tvProductDetailType,tvProductDetailCostPrice,tvProductDetailMinOrder
    ,tvProductDetailStockQuantity , tvProductDetailAvailability, tvProductDetaildiscount , tvProductDetailDescription ;

    private LinearLayout llProductDetaildiscount,llProductDetailAvailability, llProductDetailStockQuantity, llProductDetailMinOrder, llProductDetailCostPrice,
            llProductDetailType;

    private ImageView ivProductDetailImg;
    private Button btnAddtoCart;

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
        llProductDetailAvailability = findViewById(R.id.llProductDetailAvailability);
        llProductDetailCostPrice = findViewById(R.id.llProductDetailCostPrice);
        llProductDetaildiscount = findViewById(R.id.llProductDetaildiscount);
        llProductDetailMinOrder = findViewById(R.id.llProductDetailMinOrder);
        llProductDetailStockQuantity = findViewById(R.id.llProductDetailStockQuantity);
        llProductDetailType = findViewById(R.id.llProductDetailType);
        btnAddtoCart = findViewById(R.id.btnAddtoCart);
        btnAddtoCart.setOnClickListener(this);

        productDetailApi();
    }

    private void productDetailApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getProductDetail(new Dialog(mContext), retrofitApiClient.getProductDetail(ProductID), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ProductDetailModel productDetailModel = (ProductDetailModel) result.body();
                    if (!productDetailModel.getError())
                    {
                        Alerts.show(mContext, productDetailModel.getMessage());

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

                        tvProductDetailPrice.setText("Rs. "+new DecimalFormat("##.##").format(dis));
                        tvProductDetailCostPrice.setText("Rs. "+new DecimalFormat("##.##").format(dis));
                        tvProductDetaildiscount.setText(productDetailModel.getProduct().getDiscount()+"%");
                        if (productDetailModel.getProduct().getQuantityType().equals("1")) {
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
                        productDetail.setDescription(productDetailModel.getProduct().getDescription());
                        productDetail.setId(productDetailModel.getProduct().getId());
                        productDetail.setPrice(productDetailModel.getProduct().getSellingPrice());
                        productDetail.setType(productDetailModel.getProduct().getType());
                        productDetail.setQuantity(1);

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


    private void addtoCart() {
        /*********************************************************************************************************/
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }

        if (cartProductList.size() > 2) {
            //Alerts.show(this, "Cart full");
            Toast.makeText(mContext, "Cart full", Toast.LENGTH_SHORT).show();
        } else {
            if (cartProductList.size() > 0) {
                if (databaseCart.verification(productDetail.getId())) {
                    //Alerts.show(ctx, "Already added to Cart");
                    Toast.makeText(mContext, "Already added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    // productDetail.setSelected_size(selected_size);
                    // productDetail.setSelected_color(selected_color);
                    cart_count = cart_count + 1;
                    cart_number.setText("" + cart_count);
                    AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                    Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                    //Alerts.show(mContext, "Added to Cart");
                    databaseCart.addItemCart(productDetail);
                    Intent intent = new Intent(ProductDetailsActivity.this , AddtoCartActivity.class);
                    startActivity(intent);
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
                Intent intent = new Intent(ProductDetailsActivity.this , AddtoCartActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAddtoCart :
                addtoCart();
                break;
        }
    }
}
