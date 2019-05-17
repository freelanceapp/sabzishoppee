package ibt.pahadisabzi.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.adapter.AdapterCart;
import ibt.pahadisabzi.constant.Constant;
import ibt.pahadisabzi.database.DatabaseHandler;
import ibt.pahadisabzi.database.HelperManager;
import ibt.pahadisabzi.model.ProductDetail;
import ibt.pahadisabzi.model.cart_responce.AddtoCartModel;
import ibt.pahadisabzi.model.cart_responce.CartDatum;
import ibt.pahadisabzi.model.contact_responce.ContcatModel;
import ibt.pahadisabzi.retrofit_provider.RetrofitService;
import ibt.pahadisabzi.retrofit_provider.WebResponse;
import ibt.pahadisabzi.utils.Alerts;
import ibt.pahadisabzi.utils.AppPreference;
import ibt.pahadisabzi.utils.BaseActivity;
import ibt.pahadisabzi.utils.GpsTracker;
import retrofit2.Response;

import static ibt.pahadisabzi.ui.activity.HomeActivity.cart_count;
import static ibt.pahadisabzi.ui.activity.HomeActivity.cart_number;
import static ibt.pahadisabzi.ui.activity.HomeActivity.cart_price;


public class AddToCartActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {
    Context ctx;
    RecyclerView recyclerView;
    Button place_bt;
    ArrayList<ProductDetail> list = new ArrayList<>();
    HelperManager helperManager;
    Activity activity;
    String user_id = "0";
    private String DATABASE_CART = "cart.db";
    public DatabaseHandler databaseCart;
    private ArrayList<ProductDetail> cartProductList = new ArrayList<>();
    private AdapterCart adapterCart;
    TextView tvTotalItem, tvTotalPrice;
    float total = 0;
    TextView tvQty;
    ImageView minus_iv;
    String AppMinimumAmount = "0";
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);
        ctx = this;

        recyclerView = findViewById(R.id.rv_wishlist_recyclerview);
        place_bt = findViewById(R.id.bt_wishlist_placeorder);
        helperManager = new HelperManager(ctx);
        contactApi();
        list = helperManager.readAllCart();
        place_bt.setVisibility(View.VISIBLE);
        place_bt.setOnClickListener(this);

        tvTotalItem = findViewById(R.id.tvTotalItem);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.tvClearCart).setOnClickListener(this);
        initDatabase();
        setTotal();
    }

    private void initDatabase() {
        databaseCart = new DatabaseHandler(ctx, DATABASE_CART);
        cartProductList.clear();
        if (databaseCart.getContactsCount()) {
            cartProductList.addAll(databaseCart.getAllUrlList());

            Gson gson = new GsonBuilder().setLenient().create();
            data = gson.toJson(cartProductList);
            Log.e("Cart Json", " "+data);

            CartDataApi();
        }
        adapterCart = new AdapterCart(cartProductList, ctx, this, databaseCart, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterCart);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                ImageView picture = (ImageView) view.findViewById(R.id.iv_adpcart_plus);
                tvQty = (TextView) view.findViewById(R.id.tv_adpcart_qty);
                minus_iv = (ImageView) view.findViewById(R.id.iv_adpcart_minus);
                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(AddToCartActivity.this, "Single Click on Image :"+position, Toast.LENGTH_SHORT).show();
                        plusItem(view, position);
                    }
                });
                ImageView iv_adpcart_minus = (ImageView) view.findViewById(R.id.iv_adpcart_minus);
                iv_adpcart_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(AddToCartActivity.this, "Single Click on Image :"+position, Toast.LENGTH_SHORT).show();
                        minusItem(view, position);
                    }
                });

                TextView tv_adpcart_qty = (TextView) view.findViewById(R.id.tv_adpcart_qty);
                tv_adpcart_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductDetail productDetail = cartProductList.get(position);
                        if (productDetail.getQuantity_type().equals("2")) {
                            dialogBox(view, position);
                        } else {

                        }
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(AddToCartActivity.this, "Long press on position :" + position, Toast.LENGTH_LONG).show();
            }
        }));
    }

    public void setTotal() {
        ArrayList<ProductDetail> total_list = databaseCart.getAllUrlList();
        cart_number.setText("" + total_list.size());
        total = 0;
        AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, total_list.size());
        for (int i = 0; i < total_list.size(); i++) {
            if (total_list.get(i).getAvailability().equals("0"))
            {

            }else {
                float percent = Float.parseFloat(total_list.get(i).getDiscount());
                float pr = Float.parseFloat(total_list.get(i).getPrice());
                float dis1 = pr * ((100 - percent) / 100);
                int qty = total_list.get(i).getQuantity();
                float tot = dis1 * qty;
                total += tot;
            }
            //total = Math.round(total);
        }
        // place_bt.setText("Place this Order :   Rs " + total);
        tvTotalItem.setText("Total Items :" + total_list.size());
        tvTotalPrice.setText("Rs. " + new DecimalFormat("##.##").format(total));
        cart_price.setText("" + new DecimalFormat("##.##").format(total));
    }

    public String getTotal() {
        float total = 0;
        float round_total = 0;
        ArrayList<ProductDetail> total_list = databaseCart.getAllUrlList();
        cart_number.setText(total_list.size());
        AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, total_list.size());
        for (int i = 0; i < total_list.size(); i++) {
            if (total_list.get(i).getAvailability().equals("0"))
            {

            }else {
                float pr = Float.parseFloat(total_list.get(i).getPrice());
                int qty = total_list.get(i).getQuantity();
                float tot = pr * qty;
                total += tot;
                round_total = Math.round(total);
            }
        }
        return String.valueOf(round_total);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_wishlist_placeorder:
                //CartDataApi();
                locationPermission();
                //placeThisOrder();
                break;
           /* case R.id.iv_adpcart_minus:
                minusItem(view);
                break;*/
           /* case R.id.tv_adpcart_qty:
                int pos = Integer.parseInt(view.getTag().toString());
                Toast.makeText(mContext, "quantity "+ pos, Toast.LENGTH_SHORT).show();
                dialogBox();
                break;*/
            case R.id.btnBack:
                // onBackClick();
                finish();
                break;
            case R.id.tvClearCart:
                clearCart();
                break;
        }
    }

    private void clearCart() {
        new AlertDialog.Builder(mContext)
                .setTitle("Clear Cart")
                .setMessage("Are you sure want to delete all items from cart ?")
                .setPositiveButton("YES", (dialog, which) -> {
                    databaseCart.deleteallCart();
                    AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT,0);
                    cart_price.setText("0" );
                    cart_number.setText("0");
                    Toast.makeText(ctx, "Cart has empty.", Toast.LENGTH_SHORT).show();
                   /* Intent intent = new Intent(AddToCartActivity.this, HomeActivity.class);
                    startActivity(intent);*/
                    finish();
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }

    private void plusItem(View view, int pos) {
        int minQty = 1;
        // pos = Integer.parseInt(view.getTag().toString());
        ProductDetail productDetail = cartProductList.get(pos);
        View v = recyclerView.getChildAt(pos);
        int qty = Integer.parseInt(tvQty.getText().toString());
        int maxQty = Integer.parseInt(productDetail.getOrder_quantity());

            if (qty < 2500) {
                qty++;
            } else {
                Toast.makeText(ctx, "You have reached maximum order quantity.", Toast.LENGTH_SHORT).show();
            }

        productDetail.setQuantity(qty);
        databaseCart.updateUrl(productDetail);
        tvQty.setText(qty + "");
        setTotal();
        cart_number.setText("" + cart_count);
        try {
            minQty = Integer.parseInt(productDetail.getMin_quantity());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (qty > minQty) {
            minus_iv.setImageResource(R.drawable.icf_round_minus);
        } else {
            minus_iv.setImageResource(R.drawable.ic_delete);
        }

        AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());
    }

    private void minusItem(View view, int pos) {
        int minQty = 1;
        ProductDetail productDetail = cartProductList.get(pos);
        View v = recyclerView.getChildAt(pos);
        int qty = Integer.parseInt(tvQty.getText().toString());
        try {
            productDetail.setImage(cartProductList.get(pos).getImage());
            minQty = Integer.parseInt(productDetail.getMin_quantity());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (qty == minQty) {
            databaseCart.deleteContact(productDetail);
            startActivity(new Intent(mContext, AddToCartActivity.class));
            finish();
        } else {
            qty--;
            productDetail.setQuantity(qty);
            databaseCart.updateUrl(productDetail);
            tvQty.setText(qty + "");
            //cart_number.setText("" + cart_count);
        }
        if (qty > minQty) {
            minus_iv.setImageResource(R.drawable.icf_round_minus);
        } else {
            minus_iv.setImageResource(R.drawable.ic_delete);
        }
        setTotal();
        //cart_number.setText("" + qty);
        cart_count = cartProductList.size();
        AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());
    }

    private void locationPermission() {
        try {
            if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                turnGPSOn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnGPSOn() {
        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            GpsTracker gpsTracker = new GpsTracker(ctx);
            placeThisOrder();
        }
    }

    private void placeThisOrder() {
        if (!AppPreference.getBooleanPreference(mContext, Constant.Is_Login)) {
            startActivity(new Intent(ctx, SignInActivity.class));
            finish();
        } else {
            if (Integer.parseInt(AppMinimumAmount) < total) {
                ArrayList<ProductDetail> cartlist = databaseCart.getAllUrlList();
                AppPreference.setStringPreference(ctx, Constant.TOTAL_AMOUNT, String.valueOf(total));
                if (cartlist.size() > 0) {
                    Intent intent = new Intent(mContext, CheckOutActivity.class);
                    intent.putExtra("FragmentPass", "ShoppingFragment");
                    startActivity(intent);
                    //finish();
                }
            }else {
                Alerts.show(mContext, "minimun "+ AppMinimumAmount + " Order");
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.cartLayout) {
            int tag = (int) v.getTag();
            deleteItem(tag);
        }
        return true;
    }

    private void deleteItem(int tag) {
        new AlertDialog.Builder(mContext)
                .setTitle("Delete" +
                        "")
                .setMessage("Are you sure want to delete ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProductDetail pDetail = cartProductList.get(tag);
                        databaseCart.deleteContact(pDetail);
                        startActivity(new Intent(mContext, AddToCartActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }


    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an Interface for single tap and long press
     * - Parameters are its respective view and its position
     */

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    public void dialogBox(View view, int pos) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_item_add);
        dialog.setTitle("Add Item...");

        EditText text = (EditText) dialog.findViewById(R.id.text);
        text.setText(tvQty.getText().toString());
        // set the custom dialog components - text, image and button
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int minQty = 1;
                // pos = Integer.parseInt(view.getTag().toString());
                ProductDetail productDetail = cartProductList.get(pos);
                View v = recyclerView.getChildAt(pos);
                int txtqty = Integer.parseInt(text.getText().toString());
                int qty = Integer.parseInt(tvQty.getText().toString());
                int maxQty = Integer.parseInt(productDetail.getOrder_quantity());
                minQty = Integer.parseInt(productDetail.getMin_quantity());
                if (txtqty < maxQty) {
                    qty = txtqty;
                } else {
                    qty = maxQty;
                    Toast.makeText(ctx, "You have reached maximum order quantity.", Toast.LENGTH_SHORT).show();
                }
                if (txtqty > minQty) {
                    qty = txtqty;
                } else {
                    qty = minQty;
                    Toast.makeText(ctx, "You have reached minimun order quantity.", Toast.LENGTH_SHORT).show();
                }


                productDetail.setQuantity(qty);
                databaseCart.updateUrl(productDetail);
                tvQty.setText(qty + "");
                setTotal();
                cart_number.setText("" + cart_count);
                try {
                    minQty = Integer.parseInt(productDetail.getMin_quantity());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (qty > minQty) {
                    minus_iv.setImageResource(R.drawable.icf_round_minus);
                } else {
                    minus_iv.setImageResource(R.drawable.ic_delete);
                }
                AppPreference.setIntegerPreference(ctx, Constant.CART_ITEM_COUNT, cartProductList.size());

                dialog.dismiss();
            }
        });
        // set the custom dialog components - text, image and button
        Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


  /*  @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddToCartActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(AddToCartActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/


    private void contactApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getContactData(new Dialog(mContext), retrofitApiClient.contact(), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    ContcatModel contcatModel = (ContcatModel) result.body();
                    if (!contcatModel.getResult()) {
                        AppPreference.setStringPreference(mContext, Constant.MINIMUN_PRICE, contcatModel.getContent().get(5).getContent());
                        AppMinimumAmount = contcatModel.getContent().get(5).getContent();
                    }else {

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

    private void CartDataApi() {
        if (cd.isNetWorkAvailable()) {
            RetrofitService.getCartData(new Dialog(mContext), retrofitApiClient.getCartData(data), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) {
                    AddtoCartModel addtoCartModel = (AddtoCartModel) result.body();
                    if (addtoCartModel.getError())
                    {
                       // Alerts.show(mContext, addtoCartModel.getMessage());
                        cartProductList.clear();
                        for (int i = 0 ; i < addtoCartModel.getCartData().size() ; i++)
                        {
                            ProductDetail productDetail = new ProductDetail();
                            productDetail.setId(addtoCartModel.getCartData().get(i).getId());
                            productDetail.setDescription(addtoCartModel.getCartData().get(i).getDescription());
                            productDetail.setAvailability(addtoCartModel.getCartData().get(i).getAvailability());
                            productDetail.setDiscount(addtoCartModel.getCartData().get(i).getDiscount());
                            productDetail.setImage(addtoCartModel.getCartData().get(i).getImage());
                            productDetail.setInCart(addtoCartModel.getCartData().get(i).getInCart());
                            productDetail.setKeyId(addtoCartModel.getCartData().get(i).getKeyId());
                            productDetail.setMin_quantity(addtoCartModel.getCartData().get(i).getMinQuantity());
                            productDetail.setOrder_quantity(addtoCartModel.getCartData().get(i).getOrderQuantity());
                            productDetail.setPrice(addtoCartModel.getCartData().get(i).getPrice());
                            productDetail.setQuantity(addtoCartModel.getCartData().get(i).getQuantity());
                            productDetail.setQuantity_type(addtoCartModel.getCartData().get(i).getQuantityType());
                            productDetail.setRating(addtoCartModel.getCartData().get(i).getRating());
                            productDetail.setTitle(addtoCartModel.getCartData().get(i).getTitle());
                            productDetail.setType(addtoCartModel.getCartData().get(i).getType());

                            cartProductList.add(productDetail);
                            databaseCart.updateUrl(productDetail);
                            adapterCart.notifyDataSetChanged();

                            setTotal();
                        }

                    }else {

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
}
