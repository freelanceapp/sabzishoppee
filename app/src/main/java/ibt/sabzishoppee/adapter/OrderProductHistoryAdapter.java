package ibt.sabzishoppee.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.model.history_single_order_responce.Product;


public class OrderProductHistoryAdapter extends RecyclerView.Adapter<OrderProductHistoryAdapter.ViewHolder>  {
    private View rootview;
    private int position;
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private View.OnClickListener onClickListener;
    private String strSubCategoryName;
    private Boolean cheked = false;
    private int pos1;

    public OrderProductHistoryAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener, int pos1) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
        this.pos1 = pos1;
    }

    public OrderProductHistoryAdapter(Context mContext, ArrayList<Product> productArrayList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(mContext);
        rootview = li.inflate(R.layout.custom_order_product_history, null);
        return new ViewHolder(rootview);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = productArrayList.get(i);

        int number = i+1;
        viewHolder.tv_product_name.setText(""+product.getProductName());
        viewHolder.tv_product_price.setText("Rs. "+product.getProductPrice());
        viewHolder.tv_order_product_number.setText(""+number);
        viewHolder.tv_product_quantity.setText(product.getProductQuantity());

       // viewHolder.tv_product_item.setText(""+product.getOrderNumber());


        /*if (position == i) {
            cheked = false;
            ((LinearLayout) rootview.findViewById(R.id.ll_bgchange)).setBackgroundColor(Color.TRANSPARENT);
        } else {
            cheked = true;
            ((LinearLayout) rootview.findViewById(R.id.ll_bgchange)).setBackground((mContext.getResources().getDrawable(R.drawable.bg_yellow)));
        }*/
        viewHolder.ll_history.setTag(i);
        viewHolder.ll_history.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImg;
        private LinearLayout ll_history;
        private TextView tv_product_price, tv_product_quantity, tv_product_name,tv_product_type, tv_order_product_number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            tv_product_quantity = itemView.findViewById(R.id.tv_product_quantity);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_type = itemView.findViewById(R.id.tv_order_product_status);
            tv_order_product_number = itemView.findViewById(R.id.tv_order_product_number);
           // tv_order_number = itemView.findViewById(R.id.tv_order_number);
            ll_history = itemView.findViewById(R.id.ll_product_history);

        }

    }
}
