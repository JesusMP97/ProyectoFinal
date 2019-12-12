package org.izv.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.circularfloatingbutton.FloatingActionButton;
import org.izv.circularfloatingbutton.FloatingActionMenu;
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
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initComponents();
        initFabComponents();
        assignEvents();
        setSupportActionBarValues();
    }

    private void initComponents() {
        tb = findViewById(R.id.tb);
        rvItems = findViewById(R.id.rvHistory);
        getBundle();
        initRecyclerView();
    }

    private HistoryActivity assignEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        return this;
    }

    private HistoryActivity initFabComponents() {
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
        getSupportActionBar().setTitle(getString(R.string.history));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return this;
    }
}
