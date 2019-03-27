package ibt.sabzishoppee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.model.productlist_responce.Product;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>  {
    private View rootview;
    private int position;
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private View.OnClickListener onClickListener;
    private String strSubCategoryName;
    private Boolean cheked = false;
    private int pos1;

    public ProductListAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener, int pos1) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
        this.pos1 = pos1;
    }

    public ProductListAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
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
        Product product = productArrayList.get(i);

        viewHolder.tvProductName.setText(product.getTitle());
        viewHolder.tvProductsellingPrice.setText("Rs. "+product.getSellingPrice());

        if (product.getQuantityType().equals("1")) {
            viewHolder.tvProductQuality.setText(product.getQuantity()+" Kg");
            viewHolder.tvMinOrder.setText("Min Order Qty: "+product.getMinQuantity()+" Kg");
        }else {
            viewHolder.tvProductQuality.setText(product.getQuantity()+" ");
            viewHolder.tvMinOrder.setText("Min Order Qty: "+product.getMinQuantity()+" ");
        }

        if (product.getImage() != null) {
            Glide.with(mContext).load(product.getImage()).error(R.drawable.logo2).into(viewHolder.ivProductImg);
        } else {
            viewHolder.ivProductImg.setImageResource(R.drawable.logo2);
        }
        if (product.getType().equals("1")) {
            viewHolder.tvProductType.setText("Fruits");
        }else {
            viewHolder.tvProductType.setText("Vegitable");
        }

        double percent = Double.parseDouble(product.getDiscount());
        double salling = Double.parseDouble(product.getSellingPrice());

        double dis = 100-percent;

        double orizinal_price = (salling*100)/dis;

        viewHolder.tvProductPrice.setText("Rs. "+new DecimalFormat("##.##").format(orizinal_price));
        //viewHolder.tvProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tvProductPrice.setPaintFlags(viewHolder.tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );


        /*if (position == i) {
            cheked = false;
            ((LinearLayout) rootview.findViewById(R.id.ll_bgchange)).setBackgroundColor(Color.TRANSPARENT);
        } else {
            cheked = true;
            ((LinearLayout) rootview.findViewById(R.id.ll_bgchange)).setBackground((mContext.getResources().getDrawable(R.drawable.bg_yellow)));
        }*/
        viewHolder.btnAdd.setTag(i);
        viewHolder.btnAdd.setOnClickListener(onClickListener);
        viewHolder.ivProductImg.setTag(i);
        viewHolder.ivProductImg.setOnClickListener(onClickListener);

        viewHolder.iv_product_plus.setTag(position);
        viewHolder.iv_product_plus.setOnClickListener(onClickListener);
        viewHolder.iv_product_minus.setTag(position);
        viewHolder.iv_product_minus.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImg;
        private LinearLayout llSubcategory;
        private TextView btnAdd,tvProductPrice,tvProductQuality,tvProductName,tvMinOrder, tvProductType, tvProductsellingPrice, tv_product_qty;
        private ImageView iv_product_plus, iv_product_minus;
        private CardView llItem;

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
            llItem = itemView.findViewById(R.id.llItem);

        }

    }
}
