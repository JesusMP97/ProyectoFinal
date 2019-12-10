package org.izv.proyecto.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.view.utils.CustomAnimation;
import org.izv.proyecto.view.utils.CustomShape;
import org.izv.proyecto.view.utils.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ItemHolder> implements Filterable {
    private static final String FORMAT = "HH:mm";
    private static final long EMPTY = 0;
    private static Context context;
    private OnCreateContextMenuListener contextMenuListener;
    private int currentSelectedPos;
    private MainViewAdapterFilter filter;
    private LayoutInflater inflater;
    private List<Factura> invoices, invoicesAll;
    private OnClickListener onClickListener;
    private SparseBooleanArray selectedItems;
    private List<Mesa> tables;
    private static String search = "";
    private SpannableStringBuilder fullText;
    private int position;

    public MainViewAdapter(Context context) {
        filter = new MainViewAdapterFilter();
        selectedItems = new SparseBooleanArray();
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
        return new ItemHolder(inflater.inflate(R.layout.invoice_item, parent, false));
    }

    private void bindItem(TextView item, String text) {
        fullText = new SpannableStringBuilder(text);
        item.setText(highlightSearchText(fullText, search));
    }

    private String getTableNumbers(Mesa table, List<Mesa> tables) {
        String tableNumbers = "  ";
        if (table.getMesaprincipal() > EMPTY) {
            if (table.getMesaprincipal() == table.getId()) {
                tableNumbers += table.getId() + context.getString(R.string.coma) + " ";
                for (Mesa current : tables) {
                    if (current.getMesaprincipal() != current.getId() && current.getMesaprincipal() == table.getMesaprincipal()) {
                        tableNumbers += current.getId() + context.getString(R.string.coma) + " ";
                    }
                }
            }
        } else {
            tableNumbers += table.getId() + context.getString(R.string.coma) + " ";
        }
        return tableNumbers.substring(0, tableNumbers.length() - 2);
    }

    private long getTotalClients(Mesa table, List<Mesa> tables) {
        long total = EMPTY;
        if (table.getMesaprincipal() > EMPTY) {
            if (table.getMesaprincipal() == table.getId()) {
                for (Mesa current : tables) {
                    if (current.getMesaprincipal() == table.getMesaprincipal()) {
                        total += current.getCapacidad();
                    }
                }
            }
        } else {
            total = table.getCapacidad();
        }
        return total;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        if (invoices != null && tables != null) {
            this.position = position;
            Factura current = invoices.get(position);
            Mesa currentTable = getCurrentTable(current);
            holder.bind(current);
            String place = context.getString(R.string.place) + " " + currentTable.getZona();
            bindItem(holder.tvItemPlace, place);
            String tableNumber = context.getString(R.string.table) + " " + getTableNumbers(currentTable, tables).trim();
            bindItem(holder.tvItemDestination, tableNumber);
            String clients = context.getString(R.string.totalClients) + " " + getTotalClients(currentTable, tables);
            bindItem(holder.tvItemTotalClients, clients);
            String startUpTime = Time.getTimeFormatted(FORMAT, current.getHorainicio());
            bindItem(holder.tvItemStartUpTime, startUpTime);
            String totalPrice = context.getString(R.string.totalPrice) + " " + current.getTotal() + context.getString(R.string.euro);
            bindItem(holder.tvItemTotalPrice, totalPrice);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedItems.size() > 0 && onClickListener != null)
                        onClickListener.onItemClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null)
                        onClickListener.onItemLongClick(position);
                    return true;
                }
            });
            holder.tvItemMenu.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    Factura current = invoices.get(position);
                    contextMenuListener.onCreateContextMenu(menu, current, position);
                }
            });
        }
        if (currentSelectedPos == position) currentSelectedPos = -1;
    }

    public Mesa getCurrentTable(Factura current) {
        Mesa mesa = new Mesa();
        for (Mesa table : tables) {
            if (table.getId() == current.getIdmesa()) {
                mesa = table;
            }
        }
        return mesa;
    }

    @Override
    public int getItemCount() {
        int elements = 0;
        if (invoices != null) {
            elements = invoices.size();
        }
        return elements;
    }

    public void deleteCommands() {
        List<Factura> productList = new ArrayList<>();
        for (Factura invoice : this.invoices) {
            if (invoice.isSelected()) {
                productList.add(invoice);
            }
        }
        this.invoices.removeAll(productList);
        notifyDataSetChanged();
        currentSelectedPos = -1;
    }

    public List<Factura> getInvoices() {
        return invoices;
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    public void setContextMenuListener(OnCreateContextMenuListener contextMenuListener) {
        this.contextMenuListener = contextMenuListener;
    }

    public void setData(List<Factura> invoices) {
        this.invoices = invoices;
        notifyDataSetChanged();
        invoicesAll = new ArrayList<>();
        if (invoices != null) {
            invoicesAll.addAll(invoices);
        }
    }

    public void setTables(List<Mesa> tables) {
        this.tables = tables;
        notifyDataSetChanged();

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void toggleSelection(int position) {
        currentSelectedPos = position;
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
            invoices.get(position).setSelected(false);
        } else {
            selectedItems.put(position, true);
            invoices.get(position).setSelected(true);
        }
        notifyItemChanged(position);
    }

    public interface OnCreateContextMenuListener {
        void onCreateContextMenu(ContextMenu menu, Factura invoice, int position);
    }

    public interface OnClickListener {
        void onItemClick(int pos);

        void onItemLongClick(int pos);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clItem;
        private LinearLayout lyItem;
        private TextView tvItemDestination, tvItemDestinationImg, tvItemDestinationBackground, tvItemMenu, tvItemPlace, tvItemTotalClients, tvItemStartUpTime, tvItemTotalPrice;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvItemDestination = itemView.findViewById(R.id.tvItemDestination);
            tvItemDestinationImg = itemView.findViewById(R.id.tvItemDestinationImg);
            tvItemDestinationBackground = itemView.findViewById(R.id.tvItemDestinationBackground);
            tvItemMenu = itemView.findViewById(R.id.tvItemMenu);
            tvItemPlace = itemView.findViewById(R.id.tvItemPlace);
            tvItemStartUpTime = itemView.findViewById(R.id.tvItemStartUpTime);
            tvItemTotalClients = itemView.findViewById(R.id.tvItemTotalClients);
            tvItemTotalPrice = itemView.findViewById(R.id.tvItemTotalPrice);
            clItem = itemView.findViewById(R.id.clItem);
            lyItem = itemView.findViewById(R.id.lyItem);
        }

        public void bind(Factura invoice) {
            if (invoice.isSelected()) {
                tvItemDestinationBackground.setBackground(CustomShape.oval(context.getResources().getColor(R.color.colorAccent), tvItemDestinationBackground));
                tvItemDestinationImg.setBackground(null);
                clItem.setBackground(context.getDrawable(R.drawable.cl_item_selected_background));
                lyItem.setBackground(context.getDrawable(R.drawable.ly_item_selected_background));
            } else {
                tvItemDestinationBackground.setBackground(CustomShape.oval(context.getResources().getColor(R.color.lightPurple1), tvItemDestinationBackground));
                tvItemDestinationImg.setText("");
                tvItemDestinationImg.setBackground(context.getDrawable(R.drawable.table_definitive));
                clItem.setBackground(context.getDrawable(R.drawable.cl_item_background));
                lyItem.setBackground(context.getDrawable(R.drawable.ly_item_background));
            }
            if (selectedItems.size() > 0) {
                CustomAnimation.animate(tvItemDestinationBackground, invoice);
                CustomAnimation.animate(tvItemDestinationImg, invoice);
            }
        }
    }


    public class MainViewAdapterFilter extends Filter {
        private void add(Factura current, String attribute, String sequence, List<Factura> filtered) {
            if (attribute.toLowerCase().contains(sequence.toLowerCase())) {
                if (!filtered.contains(current)) {
                    search = sequence;
                    filtered.add(current);
                }
            } else {
                search = sequence;
            }
        }

        private List<Factura> getFilteredInvoices(CharSequence charSequence) {
            List<Factura> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.toString().isEmpty()) {
                search = charSequence.toString();
                filtered.addAll(invoicesAll);
            } else {
                for (Factura invoice : invoicesAll) {
                    String totalPrice = context.getString(R.string.totalPrice) + " " + invoice.getTotal() + context.getString(R.string.euro);
                    add(invoice, totalPrice, charSequence.toString(), filtered);
                    String startUpTime = Time.getTimeFormatted(FORMAT, invoice.getHorainicio());
                    add(invoice, startUpTime, charSequence.toString(), filtered);
                    for (Mesa table : tables) {
                        if (table.getId() == invoice.getIdmesa()) {
                            String tableNumber = context.getString(R.string.table) + " " + getTableNumbers(table, tables);
                            add(invoice, tableNumber, charSequence.toString(), filtered);
                            String place = context.getString(R.string.place) + " " + table.getZona();
                            add(invoice, place, charSequence.toString(), filtered);
                            String clients = context.getString(R.string.totalClients) + " " + getTotalClients(table, tables);
                            add(invoice, clients, charSequence.toString(), filtered);
                        }
                    }
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
            invoices.clear();
            invoices.addAll((Collection<? extends Factura>) results.values);
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