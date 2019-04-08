package ibt.sabziwala.adapter;

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

import ibt.sabziwala.R;
import ibt.sabziwala.model.order_history_responce.Order;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>  {
    private View rootview;
    private int position;
    private Context mContext;
    private ArrayList<Order> productArrayList;
    private View.OnClickListener onClickListener;
    private String strSubCategoryName;
    private Boolean cheked = false;
    private int pos1;

    public OrderHistoryAdapter(Context mContext, ArrayList<Order> productArrayList, View.OnClickListener onClickListener, int pos1) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
        this.pos1 = pos1;
    }

    public OrderHistoryAdapter(Context mContext, ArrayList<Order> productArrayList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(mContext);
        rootview = li.inflate(R.layout.custom_order_history, null);
        return new ViewHolder(rootview);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order product = productArrayList.get(i);

        int number = i+1;
        viewHolder.tv_item_number.setText(""+product.getProduct().size()+" Items");
        viewHolder.tv_order_date.setText(product.getOrderDate());
        viewHolder.tv_order_number.setText(""+number);
        viewHolder.tv_price.setText("Rs."+product.getOrderAmount());

        viewHolder.tv_product_item.setText(""+product.getOrderNumber());


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
        private TextView tv_order_date,tv_price,tv_product_item,tv_item_number,tv_order_number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_product_item = itemView.findViewById(R.id.tv_product_item);
            tv_item_number = itemView.findViewById(R.id.tv_item_number);
            tv_order_number = itemView.findViewById(R.id.tv_order_number);
            ll_history = itemView.findViewById(R.id.ll_history);

        }

    }
}
