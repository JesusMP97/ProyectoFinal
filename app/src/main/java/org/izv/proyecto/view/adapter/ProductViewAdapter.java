package org.izv.proyecto.view.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
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
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.utils.CustomShape;
import org.izv.proyecto.view.utils.Time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ItemHolder> implements Filterable {

    private static final int BLUE_COLOR = 0;
    private static final int FIRST_LETTER_POSITION = 0;
    private static final int GREEN_COLOR = 2;
    private static Context context;
    private ProductViewAdapterFilter filter;
    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private List<Producto> products, productsAll;
    private static String search = "";
    private SpannableStringBuilder fullText;

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
    private void bindItem(TextView item, String text) {
        fullText = new SpannableStringBuilder(text);
        item.setText(highlightSearchText(fullText, search));
    }
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if (products != null) {
            final Producto current = products.get(position);
            //holder.ivProductItem.setImageBitmap(getBitmapFromURL(url+current.getId()));
            holder.bind(current);
            String name = current.getNombre();
            bindItem(holder.tvProductItemName, name);
            String price = String.valueOf(current.getPrecio());
            bindItem(holder.tvProductItemPrice, price);
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

    public List<Producto> getProducts() {
        return products;
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
        private void add(Producto current, String attribute, String sequence, List<Producto> filtered) {
            if (attribute.toLowerCase().contains(sequence.toLowerCase())) {
                if (!filtered.contains(current)) {
                    search = sequence;
                    filtered.add(current);
                }
            } else {
                search = sequence;
            }
        }

        private List<Producto> getFilteredInvoices(CharSequence charSequence) {
            List<Producto> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.toString().isEmpty()) {
                search = charSequence.toString();
                filtered.addAll(productsAll);
            } else {
                Log.v("MainActivity", "ENTREASSADDASSDDAS");
                for (Producto product : productsAll) {
                    String name = product.getNombre();
                    add(product, name, charSequence.toString(), filtered);
                    String price = String.valueOf(product.getPrecio());
                    add(product, price, charSequence.toString(), filtered);
                }
            }
            return filtered;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = getFilteredInvoices(charSequence);
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products.clear();
            products.addAll((Collection<? extends Producto>) results.values);
            notifyDataSetChanged();
        }
    }
    public static SpannableStringBuilder highlightSearchText(SpannableStringBuilder fullText, String searchText) {
        if (searchText.isEmpty()) {
            return fullText;
        }
        SpannableStringBuilder wordSpan = new SpannableStringBuilder(fullText);
        Pattern p = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fullText);
        while (m.find()) {
            int wordStart = m.start();
            int wordEnd = m.end();
            setWordSpan(wordSpan, wordStart, wordEnd);
        }
        return wordSpan;
    }

    private static void setWordSpan(SpannableStringBuilder wordSpan, int wordStart, int wordEnd) {
        int color = context.getResources().getColor(R.color.colorAccent);
        ColorStateList redColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{color});
        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, redColor, null);
        wordSpan.setSpan(highlightSpan, wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //wordSpan.setSpan(new BackgroundColorSpan(0xFFFCFF48), wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new RelativeSizeSpan(1.15f), wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
