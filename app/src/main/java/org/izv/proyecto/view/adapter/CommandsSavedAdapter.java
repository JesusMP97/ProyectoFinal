package org.izv.proyecto.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Contenedor;
import org.izv.proyecto.model.data.Producto;

import java.util.List;
import java.util.Locale;

public class CommandsSavedAdapter extends RecyclerView.Adapter<CommandsSavedAdapter.ItemHolder> {
    private static final int EMPTY = 0;
    private static final String PRICE_FORMAT = "%.2f";
    private List<Contenedor.CommandDetail> commands;
    private Context context;
    private LayoutInflater inflater;

    public CommandsSavedAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Contenedor.CommandDetail> commands) {
        this.commands = commands;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.command_saved_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if (commands != null) {
            final Contenedor.CommandDetail current = commands.get(position);
            final Comanda currentCommand = current.getCommand();
            final Producto currentProduct = current.getProduct();
            holder.tvCommandItemName.setText(currentProduct.getNombre());
            holder.tvCommandItemAmount.setText(String.valueOf(currentCommand.getUnidades()));
            String total = String.format(Locale.GERMAN, PRICE_FORMAT, currentProduct.getPrecio() * currentCommand.getUnidades()) + " " + context.getString(R.string.euro);
            holder.tvCommandItemPrice.setText(total);
        }
    }

    @Override
    public int getItemCount() {
        int elements = EMPTY;
        if (commands != null) {
            elements = commands.size();
        }
        return elements;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvCommandItemName, tvCommandItemPrice, tvCommandItemAmount;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvCommandItemName = itemView.findViewById(R.id.tvCommandItemName);
            tvCommandItemPrice = itemView.findViewById(R.id.tvCommandItemPrice);
            tvCommandItemAmount = itemView.findViewById(R.id.tvCommandItemAmount);
        }
    }
}
