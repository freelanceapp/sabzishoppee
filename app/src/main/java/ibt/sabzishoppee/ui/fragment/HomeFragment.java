package ibt.sabzishoppee.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Arrays;
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
import ibt.sabzishoppee.model.PriceProductSorter;
import ibt.sabzishoppee.model.ProductDetail;
import ibt.sabzishoppee.model.User;
import ibt.sabzishoppee.model.banner_responce.BannerModel;
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
import static ibt.sabzishoppee.ui.activity.HomeActivity.cart_price;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TextView btnFilter, btn_fruits, btn_vagitable, btn_all, tv_categoryName, tv_LowtoHigh, tv_HightoLow, tv_a_to_z;
    private LinearLayout llFilter;
    private boolean filterValue = true;
    private RecyclerView rvproductList;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<Product> productArrayListFruits = new ArrayList<>();
    private ArrayList<Product> productArrayListVagitable = new ArrayList<>();
    private ArrayList<Product> productArrayListAll = new ArrayList<>();
    private ArrayList<Product> productArrayListAtoZ = new ArrayList<>();

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
    private ProductDetail productDetail, productDetail1;

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
        tv_a_to_z = (TextView) view.findViewById(R.id.tv_a_to_z);
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
        tv_a_to_z.setOnClickListener(this);

        productDetailApi();
        bannerApi();
        setTotal();
    }


    private void bannerApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getBannerData(new Dialog(mContext), retrofitApiClient.bannerImage(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    BannerModel bannerModel = (BannerModel) result.body();
                    if (bannerModel == null)
                        return;
                    if (bannerModel.getAppslider().size() > 0) {
                        Alerts.show(mContext, bannerModel.getMessage());
                        for (int i = 0; i < bannerModel.getAppslider().size(); i++) {
                            ImagesArray.add(bannerModel.getAppslider().get(i).getImage());
                        }
                        init(bannerModel.getAppslider().size());
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
    }

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

            case  R.id.llItem :
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
                System.out.println("-----Sorted JobCandidate by name: Ascending-----");
                PriceProductSorter nameProductSorter = new PriceProductSorter(productArrayListAll);
                ArrayList<Product> sortedJobCandidate = nameProductSorter.getSortedProductByHightoLow();
                productArrayListAtoZ.clear();
                for (Product jobCandidate : sortedJobCandidate) {
                    Log.e("Name", jobCandidate.getTitle());
                    Log.e("Price", jobCandidate.getSellingPrice());
                    productArrayListAtoZ.add(jobCandidate);
                }
                productArrayListAll.clear();
                productArrayListAll.addAll(productArrayListAtoZ);
                adapter.notifyDataSetChanged();

                break;

            case R.id.tv_LowtoHigh :
                PriceProductSorter priceProductSorter = new PriceProductSorter(productArrayListAll);
                ArrayList<Product> sortedJobCandidate1 = priceProductSorter.getSortedJobCandidateByAge();
                System.out.println("-----Sorted JobCandidate by age: Ascending-----");
                productArrayListAtoZ.clear();
                for (Product jobCandidate : sortedJobCandidate1) {
                    Log.e("Name", jobCandidate.getTitle());
                    Log.e("Price", jobCandidate.getSellingPrice());
                    productArrayListAtoZ.add(jobCandidate);
                }
                productArrayListAll.clear();
                productArrayListAll.addAll(productArrayListAtoZ);
               /* adapter = new ProductListAdapter(mContext, productArrayListAtoZ, this );
                rvproductList.setHasFixedSize(true);
                rvproductList.setLayoutManager(new GridLayoutManager(mContext, 2));
                rvproductList.setAdapter(adapter);*/

                adapter.notifyDataSetChanged();

                break;

            case R.id.tv_a_to_z :
                PriceProductSorter nameProductSorter2 = new PriceProductSorter(productArrayListAll);
                ArrayList<Product> sortedJobCandidate2 = nameProductSorter2.getSortedJobCandidateByName();
                productArrayListAtoZ.clear();
                for (Product jobCandidate : sortedJobCandidate2) {
                    Log.e("Name", jobCandidate.getTitle());
                    Log.e("Price", jobCandidate.getSellingPrice());
                    productArrayListAtoZ.add(jobCandidate);
                }
                productArrayListAll.clear();
                productArrayListAll.addAll(productArrayListAtoZ);
                adapter.notifyDataSetChanged();

                break;

            case R.id.iv_product_plus :
                int pos = Integer.parseInt(view.getTag().toString());
                plusItem(view, pos);

                break;

            case R.id.iv_product_minus :
                minusItem(view);

                break;
        }
    }


    private void addtoCart() {
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
                    databaseCart.addItemCart(productDetail);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                databaseCart.addItemCart(productDetail);
            }
        }
    }

    private void plusItem(View view, int pos) {
        productDetail = new ProductDetail();
        productDetail.setTitle(productArrayList.get(pos).getTitle());
        productDetail.setRating(productArrayList.get(pos).getRating());
        productDetail.setImage(productArrayList.get(pos).getImage());
        productDetail.setDiscount(productArrayList.get(pos).getDiscount());
        productDetail.setAvailability(productArrayList.get(pos).getAvailability());
        productDetail.setMin_quantity(productArrayList.get(pos).getMinQuantity());
        productDetail.setQuantity_type(productArrayList.get(pos).getQuantityType());
        productDetail.setOrder_quantity(productArrayList.get(pos).getQuantity());
        productDetail.setDescription(productArrayList.get(pos).getDescription());
        productDetail.setId(productArrayList.get(pos).getId());
        productDetail.setPrice(productArrayList.get(pos).getSellingPrice());
        productDetail.setType(productArrayList.get(pos).getType());
        productDetail.setQuantity(1);

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
            if (cartProductList.size() > 0) {
                if (databaseCart.verification(productDetail.getId())) {
                    ProductDetail productDetail = cartProductList.get(pos);
                    View v = rvproductList.getChildAt(pos);
                    TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                    ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);

                    int qty = Integer.parseInt(tvQty.getText().toString());
                    if (qty < Integer.parseInt(productArrayListAll.get(pos).getQuantity()))
                    {
                        qty++;
                        productDetail.setQuantity(qty);
                        productArrayListAll.get(pos).setProductQuantity(""+qty);
                        databaseCart.updateUrl(productDetail);
                    }else {

                    }
                    //tvQty.setText(qty + "");
                     setTotal();
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
            adapter.notifyDataSetChanged();
        //AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());
    }

    private void minusItem(View view) {
        int pos = Integer.parseInt(view.getTag().toString());
        productDetail = new ProductDetail();
        productDetail.setTitle(productArrayList.get(pos).getTitle());
        productDetail.setRating(productArrayList.get(pos).getRating());
        productDetail.setImage(productArrayList.get(pos).getImage());
        productDetail.setDiscount(productArrayList.get(pos).getDiscount());
        productDetail.setAvailability(productArrayList.get(pos).getAvailability());
        productDetail.setMin_quantity(productArrayList.get(pos).getMinQuantity());
        productDetail.setQuantity_type(productArrayList.get(pos).getQuantityType());
        productDetail.setOrder_quantity(productArrayList.get(pos).getQuantity());
        productDetail.setDescription(productArrayList.get(pos).getDescription());
        productDetail.setId(productArrayList.get(pos).getId());
        productDetail.setPrice(productArrayList.get(pos).getSellingPrice());
        productDetail.setType(productArrayList.get(pos).getType());
        productDetail.setQuantity(1);

        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }
            if (cartProductList.size() > 0) {
                if (databaseCart.verification(productDetail.getId())) {
                    ProductDetail productDetail = cartProductList.get(pos);
                    View v = rvproductList.getChildAt(pos);
                    TextView tvQty = (TextView) v.findViewById(R.id.tv_product_qty);
                    ImageView minus_iv = (ImageView) v.findViewById(R.id.iv_product_minus);
                    int qty = Integer.parseInt(tvQty.getText().toString());
                    if (qty == 1) {
                       /* databaseCart.deleteContact(productDetail);
                        cartProductList.remove(pos);
                        adapter.notifyDataSetChanged();*/
                    } else {
                        qty--;
                        productDetail.setQuantity(qty);
                        productArrayListAll.get(pos).setProductQuantity(""+qty);
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


        adapter.notifyDataSetChanged();
        setTotal();
        //AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, list.size());
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
                            //productArrayListAtoZ.addAll(productListModel.getProduct());
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
