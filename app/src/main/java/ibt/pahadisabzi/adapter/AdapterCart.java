package ibt.pahadisabzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.database.DatabaseHandler;
import ibt.pahadisabzi.database.HelperManager;
import ibt.pahadisabzi.model.ProductDetail;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    ArrayList<ProductDetail> list;
    ArrayList<ProductDetail> dList;
    Context context;
    private View.OnLongClickListener longClickListener;
    HelperManager helperManager;
    int proposition = 0;
    TextView select_col, select_size;
    RadioGroup color_radiogroup, size_radiogroup;
    private View.OnClickListener onClickListener;
    public DatabaseHandler databaseCart;

    public AdapterCart(ArrayList<ProductDetail> listt, Context context, View.OnClickListener onClickListener,
                       DatabaseHandler databaseCart, View.OnLongClickListener longListener) {
        this.list = listt;
        this.onClickListener = onClickListener;
        this.context = context;
        this.databaseCart = databaseCart;
        helperManager = new HelperManager(context);
        this.dList = listt;
        this.longClickListener = longListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adp_cartview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int minQty = 1;
        ProductDetail productDetail = dList.get(position);
        holder.name_tv.setText(productDetail.getTitle());
        float price = Float.parseFloat(productDetail.getPrice());
        float percent = Float.parseFloat(productDetail.getDiscount());
        float dis1 =  price * ((100-percent)/100);
        float round_pr = Math.round(dis1);
       // holder.price_tv.setText("" + dis1+"Rs");

        if (productDetail.getQuantity_type().equals("0"))
        {
            /*holder.quantity_tv.setText(productDetail.getMin_quantity()+" Pcs");
            holder.tv_adpcart_qty_type.setText(" Pcs");
            holder.price_tv.setText("" +  new DecimalFormat("##.##").format(dis1)+"Rs");*/
            holder.tv_adpcart_rate_quantity.setText("Rs. "+new DecimalFormat("##.##").format(dis1)+" - " +productDetail.getDescription()+ " Pcs" );
        }else if (productDetail.getQuantity_type().equals("1"))
        {
           /* holder.quantity_tv.setText(productDetail.getMin_quantity()+" Kg");
            holder.tv_adpcart_qty_type.setText(" Kg");
            holder.price_tv.setText("" +  new DecimalFormat("##.##").format(dis1)+"Rs");*/
            //  holder.tv_adpcart_rate_quantity.setText(productDetail.getDescription()+ " Kg/ "+ dis1+" Rs");
            holder.tv_adpcart_rate_quantity.setText("Rs. "+new DecimalFormat("##.##").format(dis1)+" - " +productDetail.getDescription()+ " Kg" );

        }else {
          //  holder.quantity_tv.setText(productDetail.getMin_quantity()+" Gm");
         //   holder.tv_adpcart_qty_type.setText(" Gm");
            holder.tv_adpcart_rate_quantity.setText("Rs. "+new DecimalFormat("##.##").format(dis1)+" - " +productDetail.getDescription()+ " Gm" );
        }
        holder.qty_tv.setText(dList.get(position).getQuantity() + "");

       /* float total = (float) (dis1 * list.get(position).getQuantity());
        holder.type_tv.setText(" " + total);*/

        if (productDetail.getImage() != null) {

            Glide.with(context).load(productDetail.getImage()).error(R.drawable.splash_logo).into(holder.pro_image_iv);
            holder.pro_image_iv.setVisibility(View.GONE);
        } else {
            holder.pro_image_iv.setImageResource(R.drawable.splash_logo);
            holder.pro_image_iv.setVisibility(View.GONE);
        }

        try {
            minQty = Integer.parseInt(productDetail.getMin_quantity());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Log.e("Image", productDetail.getImage());
        int qty = Integer.parseInt(holder.qty_tv.getText().toString());

        if (qty > minQty) {
            holder.minus_iv.setImageResource(R.drawable.icf_round_minus);
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
        holder.qty_tv.setTag(position);
        holder.qty_tv.setOnClickListener(onClickListener);
        holder.cartLayout.setTag(position);
        holder.cartLayout.setOnLongClickListener(longClickListener);

    }

    @Override
    public int getItemCount() {
        return dList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public static TextView name_tv, quantity_tv, type_tv, price_tv, qty_tv, tv_adpcart_edit, tv_adpcart_qty_type, tv_adpcart_rate_quantity;
        ImageView pro_image_iv, plus_iv, minus_iv;
        LinearLayout cartLayout;

        public MyViewHolder(View view) {
            super(view);
            name_tv = view.findViewById(R.id.tv_adpcart_name);
            quantity_tv = view.findViewById(R.id.tv_adpcart_quantity);
           // type_tv = view.findViewById(R.id.tv_adpcart_type);
            price_tv = view.findViewById(R.id.tv_adpcart_price);
            qty_tv = view.findViewById(R.id.tv_adpcart_qty);
            cartLayout = view.findViewById(R.id.cartLayout);
            pro_image_iv = view.findViewById(R.id.iv_adpcart_image);
            plus_iv = view.findViewById(R.id.iv_adpcart_plus);
            minus_iv = view.findViewById(R.id.iv_adpcart_minus);
            tv_adpcart_edit = view.findViewById(R.id.tv_adpcart_edit);
            tv_adpcart_qty_type = view.findViewById(R.id.tv_adpcart_qty_type);
            tv_adpcart_rate_quantity = view.findViewById(R.id.tv_adpcart_rate_quantity);
        }
    }
}
