package org.izv.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.view.adapter.HistoryAdapter;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    /*Jesus*/

    private static final String KEY_HISTORY = "history";
    private Toolbar tb;
    RecyclerView rvItems;
    HistoryAdapter adapter;
    List<Factura> facturas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initComponents();
        setSupportActionBarValues();
    }

    private void initComponents() {
        tb = findViewById(R.id.tb);
        rvItems = findViewById(R.id.rvHistory);
        getBundle();
        initRecyclerView();
    }

    private void getBundle() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        facturas = bundle.getParcelableArrayList(KEY_HISTORY);
    }


    private void initRecyclerView() {
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(lim);
        adapter = new HistoryAdapter(facturas);
        rvItems.setAdapter(adapter);
    }

    private HistoryActivity setSupportActionBarValues() {
        setSupportActionBar(tb);
        getSupportActionBar().setTitle(getString(R.string.tbInvoicesTitle));
        return this;
    }
}
