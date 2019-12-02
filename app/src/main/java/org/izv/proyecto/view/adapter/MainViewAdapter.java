package org.izv.proyecto.view.adapter;

import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.proyecto.MainActivity;
import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.view.utils.CustomAnimation;
import org.izv.proyecto.view.utils.CustomShape;
import org.izv.proyecto.view.utils.Time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ItemHolder> implements Filterable {

    private Context context;
    private OnCreateContextMenuListener contextMenuListener;
    private int currentSelectedPos;
    private MainViewAdapterFilter filter;
    private LayoutInflater inflater;
    private List<Factura> invoices, invoicesAll;
    private OnClickListener onClickListener;
    private SparseBooleanArray selectedItems;
    private List<Mesa> tables;


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

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        if (invoices != null) {
            Factura current = invoices.get(position);
            holder.bind(current);

            holder.tvItemDestination.setText(position+"");
            holder.tvItemTotalClients.setText("Clientes totales" + position);
            holder.tvItemCommandNumber.setText(position + "");
            holder.tvItemStartupEmployee.setText(String.valueOf(current.getIdempleadoinicio()));
            holder.tvItemStartUpTime.setText(current.getHorainicio());
            holder.tvItemTotalPrice.setText(String.valueOf(current.getTotal()));
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
        } else {
            //holder.tvTableNumber.setText("No user available");
        }
        if (currentSelectedPos == position) currentSelectedPos = -1;
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
        private TextView tvItemDestination, tvItemDestinationImg, tvItemDestinationBackground, tvItemMenu, tvItemCommandNumber, tvItemTotalClients, tvItemStartupEmployee, tvItemStartUpTime, tvItemTotalPrice;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvItemDestination = itemView.findViewById(R.id.tvItemDestination);
            tvItemDestinationImg = itemView.findViewById(R.id.tvItemDestinationImg);
            tvItemDestinationBackground = itemView.findViewById(R.id.tvItemDestinationBackground);
            tvItemMenu = itemView.findViewById(R.id.tvItemMenu);
            tvItemCommandNumber = itemView.findViewById(R.id.tvItemCommandNumber);
            tvItemStartupEmployee = itemView.findViewById(R.id.tvItemDestination);
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

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Factura> filtered = new ArrayList<>();
            if (charSequence == null || charSequence.toString().isEmpty()) {
                filtered.addAll(invoicesAll);
            } else {
                for (Factura invoice : invoicesAll) {
                    if (String.valueOf(invoice.getIdempleadocierre()).toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(invoice);
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
            invoices.clear();
            invoices.addAll((Collection<? extends Factura>) results.values);
            notifyDataSetChanged();
        }
    }
}