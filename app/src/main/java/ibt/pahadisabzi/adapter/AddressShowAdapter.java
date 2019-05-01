package ibt.pahadisabzi.adapter;

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

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.model.address_show_responce.Address;


public class AddressShowAdapter extends RecyclerView.Adapter<AddressShowAdapter.ViewHolder>  {
    private View rootview;
    private int position;
    private Context mContext;
    private ArrayList<Address> productArrayList;
    private View.OnClickListener onClickListener;
    private String strSubCategoryName;
    private Boolean cheked = false;
    private int pos1;
    public AddressShowAdapter(Context mContext, ArrayList<Address> productArrayList, View.OnClickListener onClickListener, int pos1) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;
        this.pos1 = pos1;
    }

    public AddressShowAdapter(Context mContext, ArrayList<Address> productArrayList, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(mContext);
        rootview = li.inflate(R.layout.custom_address_show, null);
        return new ViewHolder(rootview);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Address address = productArrayList.get(i);

        viewHolder.tv_address1.setText(address.getLocation());
        viewHolder.tv_city.setText(address.getUserCity());
        viewHolder.tv_state.setText(address.getState());
        viewHolder.ll_show_address.setTag(i);
        viewHolder.ll_show_address.setOnClickListener(onClickListener);
        viewHolder.btn_edit.setTag(i);
        viewHolder.btn_edit.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private LinearLayout ll_show_address;
        private TextView tv_state, tv_city, tv_address1;
        private ImageView btn_edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address1 = itemView.findViewById(R.id.tv_address1);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_state = itemView.findViewById(R.id.tv_state);
            ll_show_address = itemView.findViewById(R.id.ll_show_address);
            btn_edit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
