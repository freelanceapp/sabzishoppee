package ibt.sabziwala.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.sabziwala.R;
import ibt.sabziwala.constant.Constant;
import ibt.sabziwala.database.DatabaseHandler;
import ibt.sabziwala.model.ProductDetail;
import ibt.sabziwala.model.productlist_responce.Product;
import ibt.sabziwala.ui.activity.ProductDetailsActivity;
import ibt.sabziwala.utils.AppPreference;

import static ibt.sabziwala.ui.activity.HomeActivity.cart_count;
import static ibt.sabziwala.ui.activity.HomeActivity.cart_number;
import static ibt.sabziwala.ui.activity.HomeActivity.cart_price;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements Filterable {
    private View rootview;
    private int position;
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private ArrayList<Product> productFilteredList;
    private View.OnClickListener onClickListener;
    private String strSubCategoryName;
    private Boolean cheked = false;
    private int pos1;

    private boolean isSearch = false;

    private String DATABASE_CART = "cart.db";
    private String searchProductDetail;
    private String DATABASE_WISHLIST = "wishlist.db";
    private DatabaseHandler databaseCart, databaseWishlist;
    private ArrayList<ProductDetail> cartProductList = new ArrayList<>();
    private ProductDetail productDetail, productDetail1;




    public ProductListAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener, int pos1) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.productFilteredList = productArrayList;
        this.onClickListener = onClickListener;
        this.pos1 = pos1;
    }

    public ProductListAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.productFilteredList = productArrayList;
        this.onClickListener = onClickListener;
    }

    public ProductListAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener, boolean fromSearch) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.productFilteredList = productArrayList;
        this.isSearch = fromSearch;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(mContext);
        rootview = li.inflate(R.layout.custom_item, null);
        return new ViewHolder(rootview);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        databaseCart = new DatabaseHandler(mContext, DATABASE_CART);
        databaseWishlist = new DatabaseHandler(mContext, DATABASE_WISHLIST);
        cartProductList.clear();
        if (databaseCart.getContactsCount()) {
            cartProductList = databaseCart.getAllUrlList();
        }

        Product product = productFilteredList.get(i);

        viewHolder.tvProductName.setText(product.getTitle());

        if (product.getQuantityType().equals("1")) {
            viewHolder.tvProductQuality.setText(product.getQuantity()+" Kg");
            viewHolder.tvMinOrder.setText("Min Order Qty: "+product.getMinQuantity()+" Kg");
        }else {
            viewHolder.tvProductQuality.setText(product.getQuantity()+" ");
            viewHolder.tvMinOrder.setText("Min Order Qty: "+product.getMinQuantity()+" ");
        }

        if (product.getImage() != null) {
            Glide.with(mContext).load(product.getImage()).into(viewHolder.ivProductImg);
        } else {
            viewHolder.ivProductImg.setImageResource(R.drawable.logo2);
        }
        if (product.getType().equals("1")) {
            viewHolder.tvProductType.setText("Fruits");
        }else {
            viewHolder.tvProductType.setText("Vegitable");
        }

        double percent = Double.parseDouble(product.getDiscount());
        double cost = Double.parseDouble(product.getSellingPrice());

        double dis =  cost * ((100-percent)/100);
        double orizinal_price = cost - dis;

        viewHolder.tvProductsellingPrice.setText("Rs. "+new DecimalFormat("##.##").format(dis));
        viewHolder.tvProductPrice.setText("Rs. "+product.getSellingPrice());
        //viewHolder.tvProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tvProductPrice.setPaintFlags(viewHolder.tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );

        if (isSearch) {
            viewHolder.btnAdd.setTag(product.getId());
            viewHolder.btnAdd.setOnClickListener(v -> addToCart(i, viewHolder));

            viewHolder.iv_product_plus.setTag(product.getId());
            viewHolder.iv_product_plus.setOnClickListener(v -> plusItem(i, viewHolder));

            viewHolder.iv_product_minus.setTag(product.getId());
            viewHolder.iv_product_minus.setOnClickListener(v -> minusItem(i, viewHolder));

            viewHolder.iv_product_minus.setTag(product.getId());
            viewHolder.iv_product_minus.setOnClickListener(v -> minusItem(i, viewHolder));

            viewHolder.llItem.setTag(product.getId());
            viewHolder.llItem.setOnClickListener(v -> openProduct(i));
        } else {
            viewHolder.btnAdd.setTag(i);
            viewHolder.btnAdd.setOnClickListener(onClickListener);
            viewHolder.llItem.setTag(i);
            viewHolder.llItem.setOnClickListener(onClickListener);

            viewHolder.iv_product_plus.setTag(i);
            viewHolder.iv_product_plus.setOnClickListener(onClickListener);
            viewHolder.iv_product_minus.setTag(i);
            viewHolder.iv_product_minus.setOnClickListener(onClickListener);
        }


        if (product.isInCart()){
            viewHolder.btnAdd.setVisibility(View.GONE);
            viewHolder.ll_product_action.setVisibility(View.VISIBLE);
        }else{
            viewHolder.btnAdd.setVisibility(View.VISIBLE);
            viewHolder.ll_product_action.setVisibility(View.GONE);
        }

        int minQty = Integer.parseInt(product.getMinQuantity());

        if (product.getProductQuantity().equals("0")) {
            viewHolder.tv_product_qty.setText(product.getMinQuantity());
        } else {
            viewHolder.tv_product_qty.setText(product.getProductQuantity());
        }

        int qty = Integer.parseInt(viewHolder.tv_product_qty.getText().toString());
        if (qty > minQty) {
            viewHolder.iv_product_minus.setImageResource(R.drawable.icf_round_minus);
        } else {
            viewHolder.iv_product_minus.setImageResource(R.drawable.ic_delete);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (productArrayList!=null && productArrayList.size()>0) {
                    if (charString.isEmpty()) {
                        productFilteredList = productArrayList;
                    } else {
                        ArrayList<Product> filteredList = null;
                        filteredList = new ArrayList<>();
                        for (Product row : productArrayList) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getType().contains(constraint)) {
                                filteredList.add(row);
                            }
                        }


                        productFilteredList = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productFilteredList = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImg;
        private LinearLayout llSubcategory;
        private RelativeLayout llItem, ll_product_action;
        private TextView tvProductPrice,tvProductQuality,tvProductName,tvMinOrder, tvProductType, tvProductsellingPrice, tv_product_qty;
        private ImageView iv_product_plus, iv_product_minus;
        private CardView btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuality = itemView.findViewById(R.id.tvProductQuality);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvMinOrder = itemView.findViewById(R.id.tvMinOrder);
            tvProductType = itemView.findViewById(R.id.tvProductType);
            tvProductsellingPrice = itemView.findViewById(R.id.tvProductsellingPrice);
            tv_product_qty = itemView.findViewById(R.id.tv_product_qty);
            iv_product_plus = itemView.findViewById(R.id.iv_product_plus);
            iv_product_minus = itemView.findViewById(R.id.iv_product_minus);
            ll_product_action = itemView.findViewById(R.id.ll_product_action);
            llItem = itemView.findViewById(R.id.llItem);
        }

    }

    private void addToCart(int pos, ProductListAdapter.ViewHolder holder) {

        productDetail = new ProductDetail();
        productDetail.setTitle(productFilteredList.get(pos).getTitle());
        productDetail.setRating(productFilteredList.get(pos).getRating());
        productDetail.setImage(productFilteredList.get(pos).getImage());
        productDetail.setDiscount(productFilteredList.get(pos).getDiscount());
        productDetail.setAvailability(productFilteredList.get(pos).getAvailability());
        productDetail.setMin_quantity(productFilteredList.get(pos).getMinQuantity());
        productDetail.setQuantity_type(productFilteredList.get(pos).getQuantityType());
        productDetail.setOrder_quantity(productFilteredList.get(pos).getQuantity());
        productDetail.setDescription(productFilteredList.get(pos).getDescription());
        productDetail.setId(productFilteredList.get(pos).getId());
        productDetail.setPrice(productFilteredList.get(pos).getSellingPrice());
        productDetail.setType(productFilteredList.get(pos).getType());
        productDetail.setQuantity(Integer.parseInt(productFilteredList.get(pos).getMinQuantity()));

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
                    productFilteredList.get(pos).setInCart(true);
                    //searchListAdapter.notifyDataSetChanged();
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.ll_product_action.setVisibility(View.VISIBLE);
                    databaseCart.addItemCart(productDetail);
                }
            } else {
                cart_count = cart_count + 1;
                cart_number.setText("" + cart_count);
                AppPreference.setIntegerPreference(mContext, Constant.CART_ITEM_COUNT, cart_count);
                Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
                productFilteredList.get(pos).setInCart(true);
                //searchListAdapter.notifyDataSetChanged();
                holder.btnAdd.setVisibility(View.GONE);
                holder.ll_product_action.setVisibility(View.VISIBLE);
                databaseCart.addItemCart(productDetail);
            }
        }
    }

    private void plusItem(int pos, ProductListAdapter.ViewHolder holder) {
        productDetail = new ProductDetail();
        productDetail.setTitle(productFilteredList.get(pos).getTitle());
        productDetail.setRating(productFilteredList.get(pos).getRating());
        productDetail.setImage(productFilteredList.get(pos).getImage());
        productDetail.setDiscount(productFilteredList.get(pos).getDiscount());
        productDetail.setAvailability(productFilteredList.get(pos).getAvailability());
        productDetail.setMin_quantity(productFilteredList.get(pos).getMinQuantity());
        productDetail.setQuantity_type(productFilteredList.get(pos).getQuantityType());
        productDetail.setOrder_quantity(productFilteredList.get(pos).getQuantity());
        productDetail.setDescription(productFilteredList.get(pos).getDescription());
        productDetail.setId(productFilteredList.get(pos).getId());
        productDetail.setPrice(productFilteredList.get(pos).getSellingPrice());
        productDetail.setType(productFilteredList.get(pos).getType());
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

                int qty = Integer.parseInt(holder.tv_product_qty.getText().toString());
                if (qty < Integer.parseInt(productFilteredList.get(pos).getQuantity()))
                {
                    qty++;
                    productDetail.setQuantity(qty);
                    productFilteredList.get(pos).setProductQuantity(""+qty);
                    databaseCart.updateUrl(productDetail);
                }else {

                }
                //tvQty.setText(qty + "");
                setTotal();
                if (qty > 1) {
                    holder.iv_product_minus.setImageResource(R.drawable.icf_round_minus);
                    holder.tv_product_qty.setText(qty+"");
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
    }

    private void minusItem(int pos, ProductListAdapter.ViewHolder holder) {
        productDetail = new ProductDetail();
        productDetail.setTitle(productFilteredList.get(pos).getTitle());
        productDetail.setRating(productFilteredList.get(pos).getRating());
        productDetail.setImage(productFilteredList.get(pos).getImage());
        productDetail.setDiscount(productFilteredList.get(pos).getDiscount());
        productDetail.setAvailability(productFilteredList.get(pos).getAvailability());
        productDetail.setMin_quantity(productFilteredList.get(pos).getMinQuantity());
        productDetail.setQuantity_type(productFilteredList.get(pos).getQuantityType());
        productDetail.setOrder_quantity(productFilteredList.get(pos).getQuantity());
        productDetail.setDescription(productFilteredList.get(pos).getDescription());
        productDetail.setId(productFilteredList.get(pos).getId());
        productDetail.setPrice(productFilteredList.get(pos).getSellingPrice());
        productDetail.setType(productFilteredList.get(pos).getType());
        productDetail.setQuantity(1);

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

                int qty = Integer.parseInt(holder.tv_product_qty.getText().toString());
                try {
                    minQty = Integer.parseInt(productDetail.getMin_quantity());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (qty == minQty) {
                    databaseCart.deleteContact(productDetail);
                    //cartProductList.remove(pos);
                    productFilteredList.get(pos).setInCart(false);
                    holder.btnAdd.setVisibility(View.VISIBLE);
                    holder.ll_product_action.setVisibility(View.GONE);
                       /* databaseCart.deleteContact(productDetail);
                        cartProductList.remove(pos);
                        adapter.notifyDataSetChanged();*/
                } else {
                    qty--;
                    productDetail.setQuantity(qty);
                    productFilteredList.get(pos).setProductQuantity(""+qty);
                    databaseCart.updateUrl(productDetail);
                    //tvQty.setText(qty + "");
                }
                if (qty > minQty) {
                    holder.iv_product_minus.setImageResource(R.drawable.icf_round_minus);
                    holder.tv_product_qty.setText(qty+"");
                } else {
                    holder.iv_product_minus.setImageResource(R.drawable.ic_delete);
                    holder.tv_product_qty.setText(qty+"");
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
        setTotal();
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

    private void openProduct(int pos) {
        Intent intent = new Intent(mContext , ProductDetailsActivity.class);
        intent.putExtra("ProductID", productFilteredList.get(pos).getId());
        mContext.startActivity(intent);
    }

}
