package org.izv.proyecto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.izv.circularfloatingbutton.FloatingActionButton;
import org.izv.circularfloatingbutton.FloatingActionMenu;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Contenedor;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.adapter.CommandsSavedAdapter;
import org.izv.proyecto.view.model.CommandsSavedViewModel;
import org.izv.proyecto.view.splash.OnSplash;
import org.izv.proyecto.view.splash.Splash;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandsSavedActivity extends AppCompatActivity {

    private static final long EMPTY = 0;
    private static final String KEY_INVOICE = "invoice";
    private static final String PRICE_FORMAT = "%.2f";
    private CommandsSavedAdapter adapter;
    private List<Comanda> commands;
    private FloatingActionButton fab;
    private ImageView ivLoading;
    private TypedArray loadingBg;
    private AlertDialog loadingDialog;
    private List<Producto> products;
    private RecyclerView rvList;
    private Splash splash;
    private Toolbar tb;
    private TextView tvTotalPrice;
    private CommandsSavedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commands_saved);
        initComponents()
                .initFabComponents()
                .assignEvents()
                .initLoadingAlertDialogComponents();
    }

    @Override
    protected void onStop() {
        splash.setLoading(false);
        splash.setLoading2(false);
        super.onStop();
    }

    private CommandsSavedActivity afterSplash() {
        if (!this.isDestroyed()) {
            loadingDialog.cancel();
            List<Contenedor.CommandDetail> all = getAll();
            adapter.setData(all);
            String total = getString(R.string.total) + " " + getTotalPrice() + " " + getString(R.string.euro);
            tvTotalPrice.setText(total);
        }
        return this;
    }

    private CommandsSavedActivity assignEvents() {
        viewModel.commandViewModel.getAll().observe(this, new Observer<List<Comanda>>() {
            @Override
            public void onChanged(List<Comanda> comandas) {
                commands = comandas;
                splash.setLoading(false);
            }
        });

        viewModel.productoViewModel.getAll().observe(this, new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> productos) {
                products = productos;
                splash.setLoading2(false);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommandsSavedActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        return this;
    }

    private List<Contenedor.CommandDetail> getAll() {
        List<Comanda> filteredCommands = getFilteredCommands();
        List<Contenedor.CommandDetail> commandDetailList = new ArrayList<>();
        for (Comanda command : filteredCommands) {
            for (Producto product : products) {
                if (command.getIdproducto() == product.getId()) {
                    Contenedor.CommandDetail commandDetail = new Contenedor.CommandDetail();
                    commandDetail.setCommand(command);
                    commandDetail.setProduct(product);
                    long units = getCommandUnits(command, filteredCommands);
                    commandDetail.getCommand().setUnidades(units);
                    if (!commandDetailList.contains(commandDetail)) {
                        commandDetailList.add(commandDetail);
                    }
                }
            }
        }
        return commandDetailList;
    }

    private long getCommandUnits(Comanda current, List<Comanda> commands) {
        long units = EMPTY;
        for (Comanda command : commands) {
            if (command.getIdproducto() == current.getIdproducto()) {
                units++;
            }
        }

        return units;
    }

    private List<Comanda> getFilteredCommands() {
        Factura invoice = getIntent().getParcelableExtra(KEY_INVOICE);
        List<Comanda> filtered = new ArrayList<>();
        for (Comanda command : commands) {
            if (invoice.getId() == command.getIdfactura() && !filtered.contains(command)) {
                filtered.add(command);
            }
        }
        return filtered;
    }

    private String getTotalPrice() {
        float total = EMPTY;
        List<Contenedor.CommandDetail> all = getAll();
        for (Contenedor.CommandDetail commandDetail : all) {
            total += commandDetail.getCommand().getUnidades() * commandDetail.getProduct().getPrecio();
        }
        return String.format(Locale.GERMAN, PRICE_FORMAT, total);
    }

    private CommandsSavedActivity initComponents() {
        viewModel = ViewModelProviders.of(this).get(CommandsSavedViewModel.class);
        rvList = findViewById(R.id.rvProductValueList);
        tvTotalPrice = findViewById(R.id.tvCommandTotal);
        tb = findViewById(R.id.tb);
        adapter = new CommandsSavedAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return this;
    }

    private CommandsSavedActivity initFabComponents() {
        ImageView ivFab = new ImageView(this);
        ivFab.setImageDrawable(getDrawable(R.drawable.ic_settings_black_24dp));
        fab = new FloatingActionButton.Builder(this)
                .setContentView(ivFab)
                .build();
        new FloatingActionMenu.Builder(this)
                .attachTo(fab)
                .build();
        return this;
    }

    private CommandsSavedActivity initLoadingAlertDialogComponents() {
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.loadingDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.charging, null);
        dialogBuilder.setView(dialogView);
        loadingDialog = dialogBuilder.create();
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingBg = getResources().obtainTypedArray(R.array.loading_background);
        ivLoading = loadingDialog.findViewById(R.id.ivCharging);
        splash = new Splash(loadingBg, ivLoading, loadingDialog, new OnSplash() {
            @Override
            public void onFinished() {
                afterSplash();
            }
        });
        splash.setLoading2(true);
        splash.execute();
        return this;
    }
}
