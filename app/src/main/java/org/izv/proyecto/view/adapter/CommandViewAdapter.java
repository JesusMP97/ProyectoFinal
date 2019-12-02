package org.izv.proyecto.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Contenedor;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Producto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CommandViewAdapter extends RecyclerView.Adapter<CommandViewAdapter.ItemHolder> {

    private static final String PRICE_FORMAT = "%.2f";
    private List<Contenedor.CommandDetail> commands;
    private Context context;
    private LayoutInflater inflater;
    private CommandViewAdapter.OnClickListener onClickListener;


    public CommandViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.command_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if (commands != null) {
            final Contenedor.CommandDetail current = commands.get(position);
            final Comanda currentCommand = current.getCommand();
            final Producto currentProduct = current.getProduct();
            holder.tvCommandItemName.setText(currentProduct.getNombre());
            holder.tvCommandItemAmount.setText(String.valueOf(currentCommand.getUnidades()));
            holder.tvCommandItemPrice.setText(String.format(Locale.GERMAN, PRICE_FORMAT, currentCommand.getPrecio()));
            holder.tvCommandItemLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(current,v);
                }
            });
            holder.tvCommandItemMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(current,v);
                }
            });
            holder.tvCommandItemClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(current,v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int elements = 0;
        if (commands != null) {
            elements = commands.size();
        }
        return elements;
    }

    public void addCommand(Contenedor.CommandDetail command) {
        commands.add(command);
        setData(commands);
        notifyDataSetChanged();
    }

    public List<Contenedor.CommandDetail> getCommands() {
        if (commands == null) {
            commands = new ArrayList<>();
        }
        return commands;
    }

    public void setData(List<Contenedor.CommandDetail> commands) {
        this.commands = commands;
        notifyDataSetChanged();
    }

    public void setOnClickListener(CommandViewAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onItemClick(Contenedor.CommandDetail commands, View v);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvCommandItemName, tvCommandItemPrice, tvCommandItemAmount, tvCommandItemLess, tvCommandItemClear, tvCommandItemMore;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvCommandItemName = itemView.findViewById(R.id.tvCommandItemName);
            tvCommandItemPrice = itemView.findViewById(R.id.tvCommandItemPrice);
            tvCommandItemAmount = itemView.findViewById(R.id.tvCommandItemAmount);
            tvCommandItemLess = itemView.findViewById(R.id.tvCommandItemLess);
            tvCommandItemClear = itemView.findViewById(R.id.tvCommandItemClear);
            tvCommandItemMore = itemView.findViewById(R.id.tvCommandItemMore);
        }
    }
}
