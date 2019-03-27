package ibt.sabzishoppee.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.adapter.ProductListAdapter;
import ibt.sabzishoppee.adapter.SlidingImage_Adapter1;
import ibt.sabzishoppee.constant.Constant;
import ibt.sabzishoppee.database.DatabaseHandler;
import ibt.sabzishoppee.model.ProductDetail;
import ibt.sabzishoppee.model.User;
import ibt.sabzishoppee.model.login_responce.LoginModel;
import ibt.sabzishoppee.model.productlist_responce.Product;
import ibt.sabzishoppee.model.productlist_responce.ProductListModel;
import ibt.sabzishoppee.retrofit_provider.RetrofitService;
import ibt.sabzishoppee.retrofit_provider.WebResponse;
import ibt.sabzishoppee.ui.activity.AddtoCartActivity;
import ibt.sabzishoppee.ui.activity.HomeActivity;
import ibt.sabzishoppee.ui.activity.ProductDetailsActivity;
import ibt.sabzishoppee.utils.Alerts;
import ibt.sabzishoppee.utils.AppPreference;
import ibt.sabzishoppee.utils.BaseFragment;
import ibt.sabzishoppee.utils.ConnectionDetector;
import ibt.sabzishoppee.utils.ConnectionDirector;
import ibt.sabzishoppee.utils.EmailChecker;
import retrofit2.Response;

import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_count;
import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_number;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TextView btnFilter, btn_fruits, btn_vagitable, btn_all, tv_categoryName, tv_LowtoHigh, tv_HightoLow;
    private LinearLayout llFilter;
    private boolean filterValue = true;
    private RecyclerView rvproductList;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<Product> productArrayListFruits = new ArrayList<>();
    private ArrayList<Product> productArrayListVagitable = new ArrayList<>();
    private ArrayList<Product> productArrayListAll = new ArrayList<>();
    private ProductListAdapter adapter;
    private int position;

    CirclePageIndicator indicator;
    ConnectionDetector connectionDetector;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
   // private static final Integer[] IMAGES = {R.drawable.banner1, R.drawable.img2, R.drawable.img1, R.drawable.img2};
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private String DATABASE_CART = "cart.db";
    private String searchProductDetail;
    private String DATABASE_WISHLIST = "wishlist.db";
    private DatabaseHandler databaseCart, databaseWishlist;
    private ArrayList<ProductDetail> cartProductList = new ArrayList<>();
    private ProductDetail productDetail;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();
        databaseCart = new DatabaseHandler(mContext, DATABASE_CART);
        databaseWishlist = new DatabaseHandler(mContext, DATABASE_WISHLIST);
        cartProductList.clear();
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
        init(view);
        return view;
    }

    private void init(View view)
    {
        btnFilter = (TextView) view.findViewById(R.id.btnFilter);
        btn_all = (TextView) view.findViewById(R.id.btn_all);
        btn_fruits = (TextView) view.findViewById(R.id.btn_fruits);
        btn_vagitable = (TextView) view.findViewById(R.id.btn_vagitable);
        tv_categoryName = (TextView) view.findViewById(R.id.tv_categoryName);
        tv_HightoLow = (TextView) view.findViewById(R.id.tv_HightoLow);
        tv_LowtoHigh = (TextView) view.findViewById(R.id.tv_LowtoHigh);
        llFilter = (LinearLayout) view.findViewById(R.id.llFilter);
        rvproductList = (RecyclerView) view.findViewById(R.id.rvproductList);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        btnFilter.setOnClickListener(this);
        llFilter.setVisibility(View.GONE);

        adapter = new ProductListAdapter(mContext, productArrayListAll, this );
        rvproductList.setHasFixedSize(true);
        rvproductList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvproductList.setAdapter(adapter);

        btn_fruits.setOnClickListener(this);
        btn_vagitable.setOnClickListener(this);
        btn_all.setOnClickListener(this);
        tv_HightoLow.setOnClickListener(this);
        tv_LowtoHigh.setOnClickListener(this);

        productDetailApi();
    }


   /* private void bannerApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getBannerData(new Dialog(mContext), retrofitApiClient.bannerImage(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    BannerModel bannerModel = (BannerModel) result.body();
                    if (bannerModel == null)
                        return;
                    if (bannerModel.getData().size() > 0) {
                        for (int i = 0; i < bannerModel.getData().size(); i++) {
                            ImagesArray.add(bannerModel.getData().get(i).getBimage());
                        }
                        init(bannerModel.getData().size());
                    }
                }

                @Override
                public void onResponseFailed(String error) {
                    Alerts.show(mContext, error);
                }
            });
        } else {
            cd.show(mContext);
        }
    }*/

    private void init(int bannerLength) {
        SlidingImage_Adapter1 image_adapter1 = new SlidingImage_Adapter1(mContext, ImagesArray);
        mPager.setAdapter(image_adapter1);
        indicator.setViewPager(mPager);
        image_adapter1.notifyDataSetChanged();
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);
        NUM_PAGES = bannerLength;
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            mPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }



    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnFilter :
                if (filterValue)
                {
                    llFilter.setVisibility(View.VISIBLE);
                    filterValue = false;
                }else {
                    llFilter.setVisibility(View.GONE);

                    filterValue = true;
                }
                break;

            case  R.id.ivProductImg :
                position = Integer.parseInt(view.getTag().toString());
                Intent intent = new Intent(mContext , ProductDetailsActivity.class);
                intent.putExtra("ProductID", productArrayList.get(position).getId());
                mContext.startActivity(intent);

                break;
            case R.id.btnAdd :
                position = Integer.parseInt(view.getTag().toString());

                productDetail = new ProductDetail();
                productDetail.setTitle(productArrayList.get(position).getTitle());
                productDetail.setRating(productArrayList.get(position).getRating());
                productDetail.setImage(productArrayList.get(position).getImage());
                productDetail.setDiscount(productArrayList.get(position).getDiscount());
                productDetail.setAvailability(productArrayList.get(position).getAvailability());
                productDetail.setMin_quantity(productArrayList.get(position).getMinQuantity());
                productDetail.setQuantity_type(productArrayList.get(position).getQuantityType());
                productDetail.setOrder_quantity(productArrayList.get(position).getQuantity());
                productDetail.setDescription(productArrayList.get(position).getDescription());
                productDetail.setId(productArrayList.get(position).getId());
                productDetail.setPrice(productArrayList.get(position).getSellingPrice());
                productDetail.setType(productArrayList.get(position).getType());
                productDetail.setQuantity(1);
                addtoCart();
                break;

            case R.id.btn_fruits :
                tv_categoryName.setText("Fruits");
                btn_fruits.setBackgroundResource(R.color.green_dark);
                btn_vagitable.setBackgroundResource(R.color.green_50);
                btn_all.setBackgroundResource(R.color.green_50);
                productArrayListAll.clear();
                productArrayListAll.addAll(productArrayListFruits);
                adapter.notifyDataSetChanged();

                break;

            case R.id.btn_vagitable :
                tv_categoryName.setText("Vagitable");
                btn_fruits.setBackgroundResource(R.color.green_50);
                btn_vagitable.setBackgroundResource(R.color.green_dark);
                btn_all.setBackgroundResource(R.color.green_50);
                productArrayListAll.clear();
                productArrayListAll.addAll(productArrayListVagitable);
                adapter.notifyDataSetChanged();
                break;

            case R.id.btn_all :
                tv_categoryName.setText("All");
                btn_fruits.setBackgroundResource(R.color.green_50);
                btn_vagitable.setBackgroundResource(R.color.green_50);
                btn_all.setBackgroundResource(R.color.green_dark);
                productArrayListAll.clear();
                productArrayListAll.addAll(productArrayList);
                adapter.notifyDataSetChanged();
                break;

            case R.id.tv_HightoLow :
                productArrayListAll.clear();
                Collections.sort(productArrayList, Collections.<Product>reverseOrder());
                productArrayListAll.addAll(productArrayList);
                adapter.notifyDataSetChanged();
                break;

            case R.id.tv_LowtoHigh :
              /*  productArrayListAll.clear();
                Collections.sort(productArrayList);
                productArrayListAll.addAll(productArrayList);
                adapter.notifyDataSetChanged();*/

                adapter.notifyDataSetChanged();
                break;

            case R.id.iv_product_plus :

                break;

            case R.id.iv_product_minus :
                break;
        }
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





    private void productDetailApi() {
        if (cd.isNetWorkAvailable()) {
                RetrofitService.getProductData(new Dialog(mContext), retrofitApiClient.productData(), new WebResponse() {
                    @Override
                    public void onResponseSuccess(Response<?> result) {
                        ProductListModel productListModel = (ProductListModel) result.body();
                        productArrayList.clear();

                        if (!productListModel.getError())
                        {
                            Alerts.show(mContext, productListModel.getMessage());

                            productArrayList.addAll(productListModel.getProduct());
                            productArrayListFruits.clear();
                            productArrayListVagitable.clear();
                            if (productArrayList.size() > 0)
                            {
                                for (int i = 0 ; i < productArrayList.size(); i++)
                                {
                                    if (productArrayList.get(i).getType().equals("0"))
                                    {
                                        productArrayListFruits.add(productArrayList.get(i));
                                    }else {
                                        productArrayListVagitable.add(productArrayList.get(i));
                                    }
                                }
                                productArrayListAll.addAll(productArrayList);
                               // productTreeSet.addAll(productListModel.getProduct());



                            }

                        }else {
                            Alerts.show(mContext, productListModel.getMessage());
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
