package ibt.sabzishoppee.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ibt.sabzishoppee.R;
import ibt.sabzishoppee.model.productlist_responce.Product;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> implements Filterable {

    private List<Product> filteredAllUserLists;
    private List<Product> allUserLists;
    private Context context;
    private SearchAdapterListener searchAdapterListener;
    private View.OnClickListener onClickListener;

    public SearchListAdapter(List<Product> allUser, Context context, SearchAdapterListener searchAdapterListener , View.OnClickListener onClickListener) {
        filteredAllUserLists = new ArrayList<>();
        allUserLists = new ArrayList<>();
        this.allUserLists = allUser;
        this.filteredAllUserLists = allUser;
        this.context = context;
        this.searchAdapterListener = searchAdapterListener;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(context);
        View viewt = li.inflate(R.layout.custom_item, null);
        return new ViewHolder(viewt);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Product product = filteredAllUserLists.get(i);
        if (product.getImage() != null) {
            Glide.with(context).load(product.getImage()).error(R.drawable.logo2).into(viewHolder.ivProductImg);
        } else {
            viewHolder.ivProductImg.setImageResource(R.drawable.logo2);
        }

        viewHolder.tvProductName.setText(product.getTitle());
        viewHolder.tvProductPrice.setText(product.getSellingPrice()+" Rs");

        if (product.getQuantityType().equals("1")) {
            viewHolder.tvProductQuality.setText(product.getQuantity()+" Kg");
            viewHolder.tvMinOrder.setText("Min Order Qty: "+product.getMinQuantity()+" Kg");
        }else {
            viewHolder.tvProductQuality.setText(product.getQuantity()+" ");
            viewHolder.tvMinOrder.setText("Min Order Qty: "+product.getMinQuantity()+" ");
        }

        if (product.getType().equals("1")) {
            viewHolder.tvProductType.setText("Fruits");
        }else {
            viewHolder.tvProductType.setText("Vegitable");
        }

        viewHolder.ll_product1.setTag(i);
        viewHolder.ll_product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAdapterListener.onSearchSelected(filteredAllUserLists.get(i));
            }
        });
       /* viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAdapterListener.onSearchSelected(filteredAllUserLists.get(i));
            }
        });*/

        if (i == 0 || i == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(12, 28, 12, 12);
            viewHolder.llItem.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(12, 6, 12, 6);
            viewHolder.llItem.setLayoutParams(params);
        }


        viewHolder.iv_product_plus.setTag(i);
        viewHolder.iv_product_plus.setOnClickListener(onClickListener);
        viewHolder.iv_product_minus.setTag(i);
        viewHolder.iv_product_minus.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return filteredAllUserLists.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredAllUserLists = allUserLists;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : allUserLists) {
                        String regex = "(.)*(\\d)(.)*";
                        Pattern pattern = Pattern.compile(regex);
                        String msg = row.getTitle();
                        boolean containsNumber = pattern.matcher(msg).matches();
                        if (containsNumber) {
                           // filteredList.add(row);
                        } else {
                            if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                    }
                    filteredAllUserLists = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredAllUserLists;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredAllUserLists = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImg;
        private LinearLayout ll_product1;
        private RelativeLayout llItem, ll_product_action;
        private TextView tvProductPrice,tvProductQuality,tvProductName,tvMinOrder, tvProductType, tv_product_qty;
        private ImageView iv_product_plus, iv_product_minus;
        CardView btnAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuality = itemView.findViewById(R.id.tvProductQuality);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvMinOrder = itemView.findViewById(R.id.tvMinOrder);
            tvProductType = itemView.findViewById(R.id.tvProductType);
            llItem = itemView.findViewById(R.id.llItem);
            tv_product_qty = itemView.findViewById(R.id.tv_product_qty);
            iv_product_plus = itemView.findViewById(R.id.iv_product_plus);
            iv_product_minus = itemView.findViewById(R.id.iv_product_minus);
            ll_product1 = itemView.findViewById(R.id.ll_product1);
            ll_product_action = itemView.findViewById(R.id.ll_product_action);
            ll_product_action.setVisibility(View.GONE);
        }
    }

    public interface SearchAdapterListener {
        void onSearchSelected(Product contact);
    }
}

