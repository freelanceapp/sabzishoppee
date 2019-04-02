package ibt.sabzishoppee.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.database.DatabaseHandler;
import ibt.sabzishoppee.database.HelperManager;
import ibt.sabzishoppee.model.ProductDetail;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    ArrayList<ProductDetail> list;
    Context context;
    HelperManager helperManager;
    int proposition = 0;
    TextView select_col, select_size;
    RadioGroup color_radiogroup, size_radiogroup;
    private View.OnClickListener onClickListener;
    public DatabaseHandler databaseCart;

    public AdapterCart(ArrayList<ProductDetail> list, Context context, View.OnClickListener onClickListener,
                       DatabaseHandler databaseCart) {
        this.list = list;
        this.onClickListener = onClickListener;
        this.context = context;
        this.databaseCart = databaseCart;
        helperManager = new HelperManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adp_cartview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ProductDetail productDetail = list.get(position);
        holder.name_tv.setText(productDetail.getTitle());
        float price = Float.parseFloat(productDetail.getPrice());
        float percent = Float.parseFloat(productDetail.getDiscount());
        float dis1 =  price * ((100-percent)/100);
        float round_pr = Math.round(dis1);
        holder.price_tv.setText("" + round_pr+"Rs");
        holder.type_tv.setText(" " + productDetail.getQuantity_type());
        holder.quantity_tv.setText(productDetail.getOrder_quantity()+"Kg");
        holder.qty_tv.setText(list.get(position).getQuantity() + "");

        if (productDetail.getImage() != null) {

            Glide.with(context).load(productDetail.getImage()).error(R.drawable.logo2).into(holder.pro_image_iv);
        } else {
            holder.pro_image_iv.setImageResource(R.drawable.logo2);
        }

        Log.e("Image", productDetail.getImage());
        int qty = Integer.parseInt(holder.qty_tv.getText().toString());
        if (qty > 1) {
            holder.minus_iv.setImageResource(R.drawable.ic_minus);
        } else {
            holder.minus_iv.setImageResource(R.drawable.ic_delete);
        }

        holder.tv_adpcart_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proposition = position;
                //showDialog(position);
            }
        });

        holder.pro_image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proposition = position;
                //showDialog(position);
            }
        });

        holder.plus_iv.setTag(position);
        holder.plus_iv.setOnClickListener(onClickListener);
        holder.minus_iv.setTag(position);
        holder.minus_iv.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public static TextView name_tv, quantity_tv, type_tv, price_tv, qty_tv, tv_adpcart_edit;
        ImageView pro_image_iv, plus_iv, minus_iv;
        LinearLayout cartLayout;

        public MyViewHolder(View view) {
            super(view);
            name_tv = view.findViewById(R.id.tv_adpcart_name);
            quantity_tv = view.findViewById(R.id.tv_adpcart_quantity);
            type_tv = view.findViewById(R.id.tv_adpcart_type);
            price_tv = view.findViewById(R.id.tv_adpcart_price);
            qty_tv = view.findViewById(R.id.tv_adpcart_qty);
            cartLayout = view.findViewById(R.id.cartLayout);
            pro_image_iv = view.findViewById(R.id.iv_adpcart_image);
            plus_iv = view.findViewById(R.id.iv_adpcart_plus);
            minus_iv = view.findViewById(R.id.iv_adpcart_minus);
            tv_adpcart_edit = view.findViewById(R.id.tv_adpcart_edit);
        }
    }
}
