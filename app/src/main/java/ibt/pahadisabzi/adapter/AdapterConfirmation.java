package ibt.pahadisabzi.adapter;

import android.content.Context;
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

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.database.HelperManager;
import ibt.pahadisabzi.model.ProductDetail;



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

        TextView name_tv, quantity_tv, type_tv, price_tv, qty_tv, tv_adpcart_edit, tv_adpcart_total;
        ImageView pro_image_iv, plus_iv, minus_iv;
        LinearLayout cartLayout;

        public MyViewHolder(View view) {
            super(view);
            name_tv = view.findViewById(R.id.tv_adpcart_name);
            quantity_tv = view.findViewById(R.id.tv_adpcart_quantity);
            type_tv = view.findViewById(R.id.tv_adpcart_type);
            price_tv = view.findViewById(R.id.tv_adpcart_price);
            qty_tv = view.findViewById(R.id.tv_adpcart_qty1);
           // cartLayout = view.findViewById(R.id.cartLayout);
            pro_image_iv = view.findViewById(R.id.iv_adpcart_image);
            plus_iv = view.findViewById(R.id.iv_adpcart_plus1);
            minus_iv = view.findViewById(R.id.iv_adpcart_minus1);
            tv_adpcart_edit = view.findViewById(R.id.tv_adpcart_edit);
            tv_adpcart_total = view.findViewById(R.id.tv_adpcart_total);
            cartLayout = view.findViewById(R.id.cartLayout);
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
        float percent = Float.parseFloat(productDetail.getDiscount());
        float dis1 =  price * ((100-percent)/100);
        //float round_pr = Math.round(dis1);


        if (productDetail.getAvailability().equals("0"))
        {
            holder.cartLayout.setVisibility(View.GONE);
        }else {
            holder.cartLayout.setVisibility(View.VISIBLE);
        }
       // holder.price_tv.setText("" + dis1+"Rs");
        holder.type_tv.setText(" " + productDetail.getQuantity_type());

        if (productDetail.getQuantity_type().equals("0"))
        {
            holder.quantity_tv.setText("Rs. "+new DecimalFormat("##.##").format(dis1)+" for "+productDetail.getDescription()+" Pcs");
        }else if (productDetail.getQuantity_type().equals("1"))
        {
            holder.quantity_tv.setText("Rs. "+new DecimalFormat("##.##").format(dis1)+" for "+productDetail.getDescription()+" Kg");

           // holder.quantity_tv.setText(productDetail.getOrder_quantity()+" Kg");
        }else {
            holder.quantity_tv.setText("Rs. "+new DecimalFormat("##.##").format(dis1)+" for "+productDetail.getDescription()+" gm");

           // holder.quantity_tv.setText(productDetail.getOrder_quantity()+" Gm");
        }


        holder.qty_tv.setText(list.get(position).getQuantity() + "");

        double single_price = list.get(position).getQuantity()* dis1;
        holder.tv_adpcart_total.setText("Total Item Price : "+new DecimalFormat("##.##").format(single_price));


        if (productDetail.getImage() != null) {
            Glide.with(context).load(productDetail.getImage()).error(R.drawable.splash_logo).into(holder.pro_image_iv);
            holder.pro_image_iv.setVisibility(View.GONE);
        } else {
            holder.pro_image_iv.setImageResource(R.drawable.splash_logo);
            holder.pro_image_iv.setVisibility(View.GONE);
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
