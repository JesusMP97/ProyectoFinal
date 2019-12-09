package org.izv.proyecto.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.izv.proyecto.R;
import org.izv.proyecto.model.data.Factura;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private List<Factura> facturas;
    View v;

    public HistoryAdapter(List<Factura> facturas) {
        this.facturas = facturas;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Factura factura = facturas.get(position);
        holder.tvIdFactura.setText("Factura " + factura.getId());
        holder.tvIdMesa.setText("" + factura.getIdmesa());
        holder.tvHoraInicio.setText("" + factura.getHorainicio());
        holder.tvHoraCierre.setText("" + factura.getHoracierre());
        holder.tvEmpInicio.setText("" + factura.getIdempleadoinicio());
        holder.tvEmpCierre.setText("" + factura.getIdempleadocierre());
        holder.tvTotal.setText("" + factura.getTotal());
    }

    @Override
    public int getItemCount() {
        return facturas.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvIdFactura, tvIdMesa, tvHoraInicio, tvHoraCierre, tvEmpInicio, tvEmpCierre, tvTotal;


        public HistoryViewHolder(View itemView) {
            super(itemView);
            tvIdFactura = itemView.findViewById(R.id.tvIdFactura);
            tvIdMesa = itemView.findViewById(R.id.tvIdMesa);
            tvHoraInicio = itemView.findViewById(R.id.tvHoraInicio);
            tvHoraCierre = itemView.findViewById(R.id.tvHoraCierre);
            tvEmpInicio = itemView.findViewById(R.id.tvIdEmpleadoInicio);
            tvEmpCierre = itemView.findViewById(R.id.tvIdEmpleadoCierre);
            tvTotal = itemView.findViewById(R.id.tvHistoryTotalPrice);
        }

    }
}
