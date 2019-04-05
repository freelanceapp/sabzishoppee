package ibt.sabzishoppee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.database.HelperManager;
import ibt.sabzishoppee.model.ProductDetail;



public class AdapterConfirmation extends RecyclerView.Adapter<AdapterConfirmation.MyViewHolder> {

    ArrayList<ProductDetail> list;
    Context context;
    HelperManager helperManager;

    public AdapterConfirmation(ArrayList<ProductDetail> list, Context context) {
        this.list = list;
        this.context = context;
        helperManager = new HelperManager(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv, quantity_tv, type_tv, price_tv, qty_tv, tv_adpcart_edit;
        ImageView pro_image_iv, plus_iv, minus_iv;

        public MyViewHolder(View view) {
            super(view);
            name_tv = view.findViewById(R.id.tv_adpcart_name);
            quantity_tv = view.findViewById(R.id.tv_adpcart_quantity);
            type_tv = view.findViewById(R.id.tv_adpcart_type);
            price_tv = view.findViewById(R.id.tv_adpcart_price);
            qty_tv = view.findViewById(R.id.tv_adpcart_qty);
           // cartLayout = view.findViewById(R.id.cartLayout);
            pro_image_iv = view.findViewById(R.id.iv_adpcart_image);
            plus_iv = view.findViewById(R.id.iv_adpcart_plus);
            minus_iv = view.findViewById(R.id.iv_adpcart_minus);
            tv_adpcart_edit = view.findViewById(R.id.tv_adpcart_edit);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adp_cartview1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductDetail productDetail = list.get(position);
        holder.name_tv.setText(productDetail.getTitle());

        float price = Float.parseFloat(productDetail.getPrice());
        float round_pr = Math.round(price);
        holder.price_tv.setText("" + round_pr+"Rs");
        holder.type_tv.setText(" " + productDetail.getQuantity_type());
        holder.quantity_tv.setText(productDetail.getOrder_quantity()+"Kg");
        holder.qty_tv.setText(list.get(position).getQuantity() + "");


        if (productDetail.getImage() != null) {
            Glide.with(context).load(productDetail.getImage()).error(R.drawable.logo2).into(holder.pro_image_iv);
        } else {
            holder.pro_image_iv.setImageResource(R.drawable.logo2);
        }

        /*int qty = Integer.parseInt(holder.qty_tv.getText().toString());
        if (qty > 1) {
            holder.minus_iv.setImageResource(R.drawable.ic_minus);
        } else {
            holder.minus_iv.setImageResource(R.drawable.ic_delete);
        }*/
        holder.plus_iv.setVisibility(View.GONE);
        holder.minus_iv.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}