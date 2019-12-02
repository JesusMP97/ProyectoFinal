package org.izv.proyecto.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Contenedor;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.utils.CustomShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ItemHolder> implements Filterable {

    private static final int BLUE_COLOR = 0;
    private static final int FIRST_LETTER_POSITION = 0;
    private static final int GREEN_COLOR = 2;
    private Context context;
    private ProductViewAdapterFilter filter;
    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private List<Producto> products, productsAll;

    public ProductViewAdapter(Context context) {
        filter = new ProductViewAdapterFilter();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if (products != null) {
            final Producto current = products.get(position);
            //holder.ivProductItem.setImageBitmap(getBitmapFromURL(url+current.getId()));
            holder.bind(current);
            holder.tvProductItemName.setText(current.getNombre());
            holder.tvProductItemPrice.setText(String.valueOf(current.getPrecio()));
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Producto current = products.get(position);
                    onClickListener.onItemLongClick(current);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int elements = 0;
        if (products != null) {
            elements = products.size();
        }
        return elements;
    }

    public void setData(List<Producto> products) {
        this.products = products;
        notifyDataSetChanged();
        productsAll = new ArrayList<>();
        productsAll.addAll(products);
    }

    public void setOnClickListener(ProductViewAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onItemLongClick(Producto product);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvProductItem;
        private TextView tvProductItemName, tvProductItemPrice;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvProductItem = itemView.findViewById(R.id.tvProductItem);
            tvProductItemName = itemView.findViewById(R.id.tvProductItemName);
            tvProductItemPrice = itemView.findViewById(R.id.tvProductItemPrice);
        }

        private void bind(Producto product) {
            int hash = product.getNombre().hashCode();
            tvProductItem.setText(String.valueOf(product.getNombre().charAt(FIRST_LETTER_POSITION)).toUpperCase());
            tvProductItem.setBackground(CustomShape.oval(Color.rgb(hash, hash / GREEN_COLOR, BLUE_COLOR), tvProductItem));
        }
    }

    public class ProductViewAdapterFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Producto> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.toString().isEmpty()) {
                filtered.addAll(productsAll);
            } else {
                for (Producto product : productsAll) {
                    if (String.valueOf(product.getNombre()).toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(product);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (products != null) {
                products.clear();
                products.addAll((Collection<? extends Producto>) results.values);
                notifyDataSetChanged();
            }
        }
    }
}
