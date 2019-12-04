package org.izv.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import org.izv.circularfloatingbutton.FloatingActionButton;
import org.izv.circularfloatingbutton.FloatingActionMenu;
import org.izv.circularfloatingbutton.SubActionButton;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.view.adapter.MainViewAdapter;
import org.izv.proyecto.view.model.MainViewModel;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Time;
import org.izv.proyecto.view.utils.Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private static final int DOUBLE_TABLE = 2;
    private static final String FILE_INVOICE = "invoice";
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final int FREE_TABLE = 1;
    private static final long HORIZONTAL_TABLE_ID = 7;
    private static final String KEY_ACTION_MODE = "actionmode";
    private static final String KEY_DEFAULT_VALUE = "0";
    private static final String KEY_INVOICE = "invoice";
    private static final String KEY_INVOICE_ID = "invoiceId";
    private static final int KEY_MAIN_INTENT = 1;
    private static final String KEY_TABLE = "table";
    private static final String KEY_URL = "url";
    private static final int OCCUPIED_TABLE = 0;
    private static final int OCTA_TABLE = 8;
    private static final int QUADRUPLE_TABLE = 4;
    private static final int RESERVED_TABLE = 2;
    private static final int SINGLE_TABLE = 1;
    private static final String TAG = "xyz";
    private ActionMode actionMode;
    private MainViewAdapter adapter;
    private AlertDialog mapDialog, loadingDialog;
    private SubActionButton btLogOut, btProfile, btSettings;
    private Mesa current;
    private FloatingActionButton fab;
    private Factura invoice;
    private ImageView ivDoubleTable1, ivDoubleTable2, ivDoubleTable3, ivDoubleTable4, ivDoubleTable5, ivDoubleTable6, ivDoubleTable7, ivDoubleTable8, ivQuadrupleTable1, ivQuadrupleTable2, ivQuadrupleTable3, ivQuadrupleTable4, ivOctaTable1, ivOctaTable2, ivOctaTable3, ivBar1, ivBar2, ivBar3, ivBar4, ivBar5;
    private RecyclerView rvList;
    private SearchView svSearch;
    private List<ImageView> tables;
    private Toolbar tb;
    private String url;
    private MainViewModel viewModel;
    private List<Mesa> tableList;
    private Bundle savedInstanceState;
    private boolean tablesChanged, invoicesChanged, changedActivity;
    private int cont = 0;
    private Timer timer;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_MAIN_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (mapDialog != null) {
                    mapDialog.cancel();
                }
                changedActivity = true;
                Log.v("xyz", " CAMBIA");
                recreate();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        if (!this.url.equalsIgnoreCase(url)) {
            viewModel.setUrl(url);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        initComponents()
                .initFabComponents()
                .setSupportActionBarValues()
                .assignEvents()
                .setSavedInstanceValues();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.loadingDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.charging, null);
        dialogBuilder.setView(dialogView);
        loadingDialog = dialogBuilder.create();
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        final ImageView view = loadingDialog.findViewById(R.id.ivCharging);

        timer = new Timer();
        final TypedArray loadingBg = getResources().obtainTypedArray(R.array.loading_background);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (cont < loadingBg.length()) {
                    view.setImageDrawable(loadingBg.getDrawable(cont));
                } else {
                    cont = 0;
                }
                if (invoicesChanged && tablesChanged) {
                    timer.cancel();
                    loadingDialog.cancel();
                }
                if (savedInstanceState != null && invoicesChanged) {
                    timer.cancel();
                    loadingDialog.cancel();
                }
                Log.v("xyz",cont+"");
                cont++;
            }
        };
        timer.schedule(timerTask, 0, 1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<Factura> list = adapter.getInvoices();
        if (actionMode != null) {
            outState.putSerializable(KEY_ACTION_MODE, (Serializable) list);
        }
        outState.putBoolean("ACTIVITY", changedActivity);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.itSearch);
        svSearch = (SearchView) menuItem.getActionView();
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itSort:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_1:
                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_2:
                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private MainActivity assignDialogEvents() {
        for (final ImageView iv : tables) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkTableState(iv);
                }
            });
        }
        return this;
    }

    private MainActivity assignEvents() {
        Log.v("xyz", " ENTRA ASSIGN" + changedActivity);
        viewModel.getLiveTables().observe(this, new Observer<List<Mesa>>() {
            @Override
            public void onChanged(List<Mesa> mesas) {
                tableList = mesas;
                adapter.setTables(mesas);
                tablesChanged = true;
            }
        });
        viewModel.getLiveInvoices().observe(this, new Observer<List<Factura>>() {
            @Override
            public void onChanged(List<Factura> facturas) {
                if (savedInstanceState != null && savedInstanceState.getSerializable(KEY_ACTION_MODE) == null) {
                    adapter.setData(facturas);
                }
                if (savedInstanceState == null) {
                    adapter.setData(facturas);
                    invoicesChanged = true;
                }
                if (savedInstanceState != null && savedInstanceState.getBoolean("ACTIVITY")) {
                    Log.v("xyz","entra en save");
                    invoicesChanged = true;
                }
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createDialog()
                        .initDialogComponents()
                        .assignDialogEvents();
                //startActivity(new Intent(MainActivity.this, CommandActivity.class));
                return true;
            }
        });
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setResult(Activity.RESULT_OK, new Intent());
                finish();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionMode != null) {
                    actionMode.finish();
                }
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        adapter.setContextMenuListener(new MainViewAdapter.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, Factura invoice, int position) {
                menu.setHeaderTitle(position + "");
                getMenuInflater().inflate(R.menu.item_menu, menu);
            }
        });
        adapter.setOnClickListener(new MainViewAdapter.OnClickListener() {
            @Override
            public void onItemClick(int pos) {
                enableActionMode(pos);
            }

            @Override
            public void onItemLongClick(int pos) {
                enableActionMode(pos);
            }
        });
        return this;
    }

    public MainActivity checkTableState(ImageView iv) {
        long tableId = Long.parseLong(iv.getContentDescription().toString());
        if (isFree(tableId)) {
            createInvoice(tableId)
                    .startActivityFromResult();
//                    .startActivity();
            Toast.makeText(this, getString(R.string.table) + " " + tableId + " " + getString(R.string.tableAdded), Toast.LENGTH_SHORT).show();
        } else {
            for (Mesa table : tableList) {
                if (table.getId() == tableId) {
                    int state = (int) table.getEstado();
                    setOnClickMessage(state, tableId);
                }
            }
        }
        return this;
    }

    private MainActivity createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.map, null);
        dialogBuilder.setView(dialogView);
        mapDialog = dialogBuilder.create();
        mapDialog.show();
        return this;
    }

    public MainActivity createInvoice(long idTable) {
        long idEmp = Long.parseLong(IO.readPreferences(MainActivity.this, Login.FILE_LOGIN, Login.KEY_LOGIN_ID, KEY_DEFAULT_VALUE));
        invoice = new Factura()
                .setIdempleadoinicio(idEmp)
                .setIdempleadocierre(idEmp)
                .setHorainicio(Time.getCurrentTime())
                .setIdmesa(idTable);
        //viewModel.add(invoice);
        IO.savePreferences(MainActivity.this, FILE_INVOICE, KEY_INVOICE_ID, String.valueOf(invoice.getId()));
        return this;
    }

    private void enableActionMode(final int position) {
        if (actionMode == null)
            actionMode = startActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    Tools.setSystemBarColor(MainActivity.this, R.color.darkPurple1);
                    mode.getMenuInflater().inflate(R.menu.menu_multiselection, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        adapter.deleteCommands();
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.getSelectedItems().clear();
                    List<Factura> invoices = adapter.getInvoices();
                    for (Factura invoice : invoices) {
                        if (invoice.isSelected())
                            invoice.setSelected(false);
                    }
                    adapter.notifyDataSetChanged();
                    actionMode = null;
                    savedInstanceState = null;
                    Tools.setSystemBarColor(MainActivity.this, R.color.colorPrimaryDark);
                }
            });

        adapter.toggleSelection(position);
        final int size = adapter.getSelectedItems().size();
        if (size == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(size));
            actionMode.invalidate();
        }
    }

    private MainActivity findMapViewById(ImageView view, int id) {
        view = mapDialog.findViewById(id);
        setSelectedTableValues(view);
        tables.add(view);
        return this;
    }

    public TypedArray getSelectedTableBackgrounds(int capacity, long tableId) {
        TypedArray tablesBg = null;
        switch (capacity) {
            case DOUBLE_TABLE:
                if (tableId < HORIZONTAL_TABLE_ID) {
                    tablesBg = getResources().obtainTypedArray(R.array.horizontal_double_table_background);
                } else {
                    tablesBg = getResources().obtainTypedArray(R.array.vertical_double_table_background);
                }
                break;
            case QUADRUPLE_TABLE:
                tablesBg = getResources().obtainTypedArray(R.array.quadruple_table_background);
                break;
            case OCTA_TABLE:
                tablesBg = getResources().obtainTypedArray(R.array.octa_table_background);
                break;
            case SINGLE_TABLE:
                tablesBg = getResources().obtainTypedArray(R.array.single_table_background);
                break;
        }
        return tablesBg;
    }

    private MainActivity initComponents() {
        tb = findViewById(R.id.tb);
        rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        adapter = new MainViewAdapter(this);
        rvList.setAdapter(adapter);
        tables = new ArrayList<>();
        tableList = new ArrayList<>();
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHandler(0,
                        ItemTouchHelper.LEFT)
        );
        helper.attachToRecyclerView(rvList);
        url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        return this;
    }

    private MainActivity initDialogComponents() {
        findMapViewById(ivDoubleTable1, R.id.ivDoubleTable1)
                .findMapViewById(ivDoubleTable2, R.id.ivDoubleTable2)
                .findMapViewById(ivDoubleTable3, R.id.ivDoubleTable3)
                .findMapViewById(ivDoubleTable4, R.id.ivDoubleTable4)
                .findMapViewById(ivDoubleTable5, R.id.ivDoubleTable5)
                .findMapViewById(ivDoubleTable6, R.id.ivDoubleTable6)
                .findMapViewById(ivDoubleTable7, R.id.ivDoubleTable7)
                .findMapViewById(ivDoubleTable8, R.id.ivDoubleTable8)
                .findMapViewById(ivQuadrupleTable1, R.id.ivQuadrupleTable1)
                .findMapViewById(ivQuadrupleTable2, R.id.ivQuadrupleTable2)
                .findMapViewById(ivQuadrupleTable3, R.id.ivQuadrupleTable3)
                .findMapViewById(ivQuadrupleTable4, R.id.ivQuadrupleTable4)
                .findMapViewById(ivOctaTable1, R.id.ivOctaTable1)
                .findMapViewById(ivOctaTable2, R.id.ivOctaTable2)
                .findMapViewById(ivOctaTable3, R.id.ivOctaTable3)
                .findMapViewById(ivBar1, R.id.ivBar1)
                .findMapViewById(ivBar2, R.id.ivBar2)
                .findMapViewById(ivBar3, R.id.ivBar3)
                .findMapViewById(ivBar4, R.id.ivBar4)
                .findMapViewById(ivBar5, R.id.ivBar5);

        return this;
    }

    private MainActivity initFabComponents() {
        ImageView ivFab = new ImageView(this);
        ivFab.setImageDrawable(getDrawable(R.drawable.ic_add_black_36dp));
        ImageView ivLogOut = new ImageView(this);
        ivLogOut.setImageDrawable(getDrawable(R.drawable.ic_exit_to_app_black_24dp));
        ImageView ivProfile = new ImageView(this);
        ivProfile.setImageDrawable(getDrawable(R.drawable.ic_profile_black_24dp));
        ImageView ivSettings = new ImageView(this);
        ivSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_black_24dp));
        fab = new FloatingActionButton.Builder(this)
                .setContentView(ivFab)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        btLogOut = itemBuilder.setContentView(ivLogOut).build();
        btProfile = itemBuilder.setContentView(ivProfile).build();
        btSettings = itemBuilder.setContentView(ivSettings).build();
        new FloatingActionMenu.Builder(this)
                .addSubActionView(btLogOut)
                .addSubActionView(btProfile)
                .addSubActionView(btSettings)
                .attachTo(fab)
                .build();
        return this;
    }

    public boolean isFree(long idTable) {
        boolean free = false;
        for (Mesa table : tableList) {
            if (table.getId() == idTable && table.getEstado() == FREE_TABLE) {
                current = table;
                free = true;
            }
        }
        return free;
    }

    private MainActivity setOnClickMessage(int state, long tableId) {
        switch (state) {
            case OCCUPIED_TABLE:
                Toast.makeText(this, getString(R.string.theTable) + " " + tableId + " " + getString(R.string.cccupiedTable), Toast.LENGTH_SHORT).show();
                break;
            case RESERVED_TABLE:
                Toast.makeText(this, getString(R.string.theTable) + " " + tableId + " " + getString(R.string.reservedTable), Toast.LENGTH_SHORT).show();
                break;
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private MainActivity setSavedInstanceValues() {
        if (savedInstanceState != null && savedInstanceState.getSerializable(KEY_ACTION_MODE) != null) {
            List<Factura> list = (List<Factura>) savedInstanceState.getSerializable(KEY_ACTION_MODE);
            adapter.setData(list);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelected()) {
                    enableActionMode(i);
                }
            }
        }
        return this;
    }

    public MainActivity setSelectedTableValues(ImageView view) {
        long tableId = Long.parseLong(view.getContentDescription().toString());
        for (Mesa table : tableList) {
            if (table.getId() == tableId) {
                int capacity = (int) table.getCapacidad();
                int state = (int) table.getEstado();
                setTableBg(state, view, capacity, tableId);
            }
        }
        return this;
    }

    private MainActivity setSupportActionBarValues() {
        setSupportActionBar(tb);
        getSupportActionBar().setTitle(getString(R.string.tbInvoicesTitle));
        return this;
    }

    public MainActivity setTableBg(int state, ImageView view, int capacity, long tableId) {
        TypedArray tablesBg = getSelectedTableBackgrounds(capacity, tableId);
        switch (state) {
            case OCCUPIED_TABLE:
                view.setImageDrawable(tablesBg.getDrawable(OCCUPIED_TABLE));
                break;
            case FREE_TABLE:
                view.setImageDrawable(tablesBg.getDrawable(FREE_TABLE));
                break;
            case RESERVED_TABLE:
                view.setImageDrawable(tablesBg.getDrawable(RESERVED_TABLE));
                break;
        }
        return this;
    }

    private MainActivity startActivity() {
        if (actionMode != null) {
            actionMode.finish();
        }
        Intent intent = new Intent(this, CommandActivity.class)
                .putExtra(KEY_INVOICE, (Serializable) invoice)
                .putExtra(KEY_TABLE, (Serializable) current);
        startActivity(intent);
        return this;
    }

    private MainActivity startActivityFromResult() {
        if (actionMode != null) {
            actionMode.finish();
        }
        Intent intent = new Intent(this, CommandActivity.class)
                .putExtra(KEY_INVOICE, (Serializable) invoice)
                .putExtra(KEY_TABLE, (Serializable) current);
        startActivityForResult(intent, KEY_MAIN_INTENT);
        return this;
    }

    private class ItemTouchHandler extends ItemTouchHelper.SimpleCallback {

        public ItemTouchHandler(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int from = viewHolder.getAdapterPosition();
            int to = target.getAdapterPosition();

            Collections.swap(adapter.getInvoices(), from, to);
            adapter.notifyItemMoved(from, to);

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final Factura current = adapter.getInvoices().get(viewHolder.getAdapterPosition());
            adapter.getInvoices().remove(viewHolder.getAdapterPosition());
            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Factura borrada", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter.getInvoices().add(current);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .show();
        }
    }
}
