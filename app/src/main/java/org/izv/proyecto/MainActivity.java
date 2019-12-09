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
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.print.PrintManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import org.izv.circularfloatingbutton.FloatingActionButton;
import org.izv.circularfloatingbutton.FloatingActionMenu;
import org.izv.circularfloatingbutton.SubActionButton;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.model.repository.Repository;
import org.izv.proyecto.view.adapter.MainViewAdapter;
import org.izv.proyecto.view.model.MainViewModel;
import org.izv.proyecto.view.operations.BeforePayment;
import org.izv.proyecto.view.print.PrintDocument;
import org.izv.proyecto.view.splash.OnSplash;
import org.izv.proyecto.view.splash.Splash;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Time;
import org.izv.proyecto.view.utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String FILE_LOGIN = "login";
    private static final String KEY_LOGIN_ID = "id";
    private static final int ASC = 1;
    private static final int DESC = -1;
    private static final int DOUBLE_TABLE = 2;
    private static final int FIRST_ELEMENT = 0;
    private static final long EMPTY = 0;
    private static final String FILE_INVOICE = "invoice";
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final int FREE_TABLE = 1;
    private static final long HORIZONTAL_TABLE_ID = 7;
    private static final String KEY_ACTION_MODE = "actionmode";
    private static final String KEY_DEFAULT_VALUE = "0";
    private static final String KEY_HISTORY = "history";
    private static final String KEY_INVOICE = "invoice";
    private static final String KEY_INVOICES_FILTERED = "invoicesFiltered";
    private static final String KEY_INVOICE_ID = "invoiceId";
    private static final int KEY_MAIN_INTENT = 1;
    private static final String KEY_TABLE = "table";
    private static final String KEY_URL = "url";
    private static final long MAIN_DEFAULT_VALUE = 0;
    private static final long NO_EMPTY = 1;
    private static final int OCCUPIED_TABLE = 0;
    private static final int OCTA_TABLE = 8;
    private static final int QUADRUPLE_TABLE = 4;
    private static final int RESERVED_TABLE = 2;
    private static final String KEY_MAP_DIALOG = "showed";
    private static final String KEY_SEARCH = "search";
    private static final int SINGLE_TABLE = 1;
    private static final String TAG = "xyz";
    private static final String INTENT_TYPE = "application/pdf";
    private ActionMode actionMode;
    private MainViewAdapter adapter;
    private boolean loading = true, commandsArrived, productsArrived;
    private AlertDialog mapDialog, loadingDialog;
    private SubActionButton btLogOut, btHistory, btSettings;
    private Mesa current;
    private FloatingActionButton fab;
    private Factura invoice;
    private ImageView ivDoubleTable1, ivDoubleTable2, ivDoubleTable3, ivDoubleTable4, ivDoubleTable5, ivDoubleTable6, ivDoubleTable7, ivDoubleTable8, ivQuadrupleTable1, ivQuadrupleTable2, ivQuadrupleTable3, ivQuadrupleTable4, ivOctaTable1, ivOctaTable2, ivOctaTable3, ivBar1, ivBar2, ivBar3, ivBar4, ivBar5;
    private RecyclerView rvList;
    private SearchView svSearch;
    private List<ImageView> tables;
    private Toolbar tb;
    private String url, search;
    private MainViewModel viewModel;
    private List<Mesa> tableList;
    private List<Factura> invoiceList;
    private Bundle savedInstanceState;
    private ImageView ivLoading;
    private Splash splash;
    private TypedArray loadingBg;
    private Factura currentInvoice;
    private List<Comanda> commands;
    private List<Producto> products;
    private static final String DEFAULT_VALUE = "0";
    private List<Long> deletedIds = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_MAIN_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (mapDialog != null) {
                    mapDialog.cancel();
                }
                recreate();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        if (!this.url.equalsIgnoreCase(url)) {
            viewModel.invoiceViewModel.setUrl(url);
            viewModel.tableViewModel.setUrl(url);
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
                .setSavedInstanceValues()
                .initLoadingAlertDialogComponents();

    }

    @Override
    protected void onStop() {
        splash.setLoading(false);
        super.onStop();
    }

    private MainActivity initLoadingAlertDialogComponents() {
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

    private MainActivity afterSplash() {
        if (!this.isDestroyed()) {
            loadingDialog.cancel();
            if (savedInstanceState != null && savedInstanceState.getBoolean(KEY_MAP_DIALOG)) {
                createDialog()
                        .initDialogComponents()
                        .assignDialogEvents();
            }
        }
        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<Factura> list = adapter.getInvoices();
        if (actionMode != null) {
            outState.putParcelableArrayList(KEY_ACTION_MODE, (ArrayList<? extends Parcelable>) list);
        }
        if (mapDialog != null && mapDialog.isShowing()) {
            outState.putBoolean(KEY_MAP_DIALOG, true);
        }
        String search = svSearch.getQuery().toString();
        outState.putString(KEY_SEARCH, search);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.itSearch);
        svSearch = (SearchView) menuItem.getActionView();
        if (search != null && !search.isEmpty()) {
            menuItem.expandActionView();
            svSearch.setQuery(search, true);
            svSearch.clearFocus();
            adapter.getFilter().filter(search);
        }
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

    private MainActivity sortInvoicesByPrice(final int critery) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invoiceList.sort(new Comparator<Factura>() {
                @Override
                public int compare(Factura f1, Factura f2) {
                    return Float.compare(f1.getTotal(), f2.getTotal()) * critery;
                }
            });
        }
        return this;
    }

    private MainActivity sortInvoicesByTime(final int critery) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invoiceList.sort(new Comparator<Factura>() {
                @Override
                public int compare(Factura f1, Factura f2) {
                    return f1.getHorainicio().compareTo(f2.getHorainicio()) * critery;
                }
            });
        }
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subItPrice:
                sortInvoicesByPrice(ASC);
                adapter.setData(invoiceList);
                break;

            case R.id.subItPriceDes:
                sortInvoicesByPrice(DESC);
                adapter.setData(invoiceList);
                break;

            case R.id.subItTime:
                sortInvoicesByTime(ASC);
                adapter.setTables(tableList);
                break;

            case R.id.subItTimeDes:
                sortInvoicesByTime(DESC);
                adapter.setData(invoiceList);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Comanda> getFilteredCommands() {
        List<Comanda> filtered = new ArrayList<>();
        for (Comanda command : commands) {
            if (command.getIdfactura() == currentInvoice.getId()) {
                filtered.add(command);
            }
        }
        return filtered;
    }

    private List<Comanda> getFilteredCommands(List<Factura> invoices, Factura main) {
        List<Comanda> filtered = new ArrayList<>();
        for (Factura invoice : invoices) {
            if (invoice.getId() != main.getId()) {
                for (Comanda command : commands) {
                    if (command.getIdfactura() == invoice.getId()) {
                        command.setIdfactura(main.getId());
                        filtered.add(command);
                    }
                }
            }
        }
        return filtered;
    }

    private File getInvoicePdf() {
        File file = null;
        if (productsArrived && commandsArrived) {
            List<Comanda> commands = getFilteredCommands();
            PrintDocument printDocument = new PrintDocument(this, products, commands);
            file = printDocument.createPdf();
        }
        return file;
    }

    private MainActivity print() {
        if (productsArrived && commandsArrived) {
            List<Comanda> commands = getFilteredCommands();
            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
            String jobName = this.getString(R.string.app_name) + " " + getString(R.string.document);
            PrintDocument printDocument = new PrintDocument(this, products, commands);
            printManager.print(jobName, printDocument, null);
        }
        return this;
    }

    private MainActivity sendPdf() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(INTENT_TYPE);
        Uri uri = Uri.parse(getInvoicePdf().getAbsolutePath());
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.shareError), Toast.LENGTH_SHORT).show();
        }
        return this;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itAdd:
                startActivityFromResult();
                break;
            case R.id.itSee:
                Intent intent = new Intent(this, SeeCommandActivity.class);
                startActivity(intent);
                break;
            case R.id.itSend:
                createDialog(getString(R.string.send), getString(R.string.send2), new BeforePayment() {
                    @Override
                    public void doIt() {
                        sendPdf();
                    }
                }, null);
                break;
            case R.id.itPayment:
                createDialog(getString(R.string.print), getString(R.string.print2), new BeforePayment() {
                    @Override
                    public void doIt() {
                        print()
                                .deleteCommands()
                                .updateTableState()
                                .finishInvoice();
                    }
                }, new BeforePayment() {
                    @Override
                    public void doIt() {
                        deleteCommands()
                                .updateTableState()
                                .finishInvoice();
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    private MainActivity deleteCommands() {
        List<Comanda> commands = getFilteredCommands();
        for (Comanda command : commands) {
            viewModel.commandViewModel.delete(command);
        }
        return this;
    }

    private MainActivity updateTableState() {
        Mesa mainTable = new Mesa();
        for (Mesa table : tableList) {
            if (table.getId() == currentInvoice.getIdmesa()) {
                if (table.getMesaprincipal() == table.getId()) {
                    mainTable = table;
                } else {
                    table.setEstado(FREE_TABLE);
                    viewModel.tableViewModel.update(table);
                }
            }
        }
        for (Mesa table : tableList) {
            if (table.getMesaprincipal() == mainTable.getId()) {
                table.setEstado(FREE_TABLE);
                table.setMesaprincipal(MAIN_DEFAULT_VALUE);
                viewModel.tableViewModel.update(table);
            }
        }
        return this;
    }

    private MainActivity finishInvoice() {
        long idEmp = Long.parseLong(IO.readPreferences(this, FILE_LOGIN, KEY_LOGIN_ID, DEFAULT_VALUE));
        currentInvoice.setIdempleadocierre(idEmp);
        currentInvoice.setHoracierre(Time.getCurrentTime());
        viewModel.invoiceViewModel.update(currentInvoice);
        return this;
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

    private MainActivity createDialog(String message, String
            title, final BeforePayment beforePayment, final BeforePayment pay) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                beforePayment.doIt();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pay.doIt();
                dialog.cancel();
            }
        });
        builder.create().show();
        return this;
    }

    private List<Factura> getFilteredInvoices(List<Factura> facturas) {
        List<Factura> filtered = new ArrayList<>();
        for (Factura invoice : facturas) {
            if (invoice.getHoracierre().trim().isEmpty()) {
                filtered.add(invoice);
            }
        }
        return filtered;
    }

    private MainActivity showConexionError() {
        Toast.makeText(this, getString(R.string.conexionError), Toast.LENGTH_SHORT).show();
        return this;
    }

    private MainActivity assignEvents() {
        viewModel.tableViewModel.getAll().observe(this, new Observer<List<Mesa>>() {
            @Override
            public void onChanged(List<Mesa> mesas) {
                adapter.setTables(mesas);
                tableList = mesas;
                fab.setEnabled(true);
                splash.setLoading2(false);

            }
        });

        viewModel.productViewModel.getAll().observe(this, new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> productos) {
                productsArrived = true;
                products = productos;
            }
        });

        viewModel.commandViewModel.getAll().observe(this, new Observer<List<Comanda>>() {
            @Override
            public void onChanged(List<Comanda> comandas) {
                commandsArrived = true;
                commands = comandas;
            }
        });
        viewModel.invoiceViewModel.getAll().observe(this, new Observer<List<Factura>>() {
            @Override
            public void onChanged(List<Factura> facturas) {
                List<Factura> filtered = getFilteredInvoices(facturas);
                invoiceList = filtered;
                if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(KEY_ACTION_MODE) == null) {
                    adapter.setData(filtered);
                }
                if (savedInstanceState == null) {
                    adapter.setData(filtered);
                }
                splash.setLoading(false);
            }
        });
        Repository.OnFailureListener onInvoiceFailure = new Repository.OnFailureListener() {
            @Override
            public void onConnectionFailure() {
                splash.setLoading(false);
                showConexionError();
            }
        };
        Repository.OnFailureListener onTableFailure = new Repository.OnFailureListener() {
            @Override
            public void onConnectionFailure() {
                splash.setLoading(false);
                showConexionError();
            }
        };
        viewModel.setOnFailureListener(onInvoiceFailure, onTableFailure);
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
        btHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
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
                currentInvoice = invoice;
                menu.setHeaderTitle(getString(R.string.select));
                getMenuInflater().inflate(R.menu.invoice_menu, menu);
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

    private MainActivity checkTableState(ImageView iv) {
        long tableId = Long.parseLong(iv.getContentDescription().toString());
        if (isFree(tableId)) {
            startActivityFromResult();
//                    .startActivity();
            Toast.makeText(this, getString(R.string.table) + " " + current.getNumero() + " " + getString(R.string.tableAdded), Toast.LENGTH_SHORT).show();
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

    private MainActivity orderTablesById(List<Mesa> tableList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tableList.sort(new Comparator<Mesa>() {
                @Override
                public int compare(Mesa o1, Mesa o2) {
                    return Long.compare(o1.getId(), o2.getId());
                }
            });
        }
        return this;
    }

    private List<Factura> getInvoicesFiltered() {
        List<Factura> invoices = adapter.getInvoices();
        List<Factura> filtered = new ArrayList<>();
        for (Factura invoice : invoices) {
            if (invoice.isSelected())
                filtered.add(invoice);
        }
        return filtered;
    }

    private List<Mesa> getTablesFiltered(List<Factura> filtered) {
        List<Mesa> tableFiltered = new ArrayList<>();
        for (Mesa table : tableList) {
            for (Factura invoice : filtered) {
                if (table.getId() == invoice.getIdmesa()) {
                    tableFiltered.add(table);
                }
            }
        }
        orderTablesById(tableFiltered);
        return tableFiltered;
    }

    private Factura getMainInvoice(List<Factura> filtered, Mesa tableMain) {
        Factura main = new Factura();
        for (Factura invoice : filtered) {
            if (invoice.getIdmesa() == tableMain.getId()) {
                main = invoice;
            }
        }
        return main;
    }

    private MainActivity updateTables(List<Mesa> filtered, Mesa tableMain) {
        for (Mesa table : filtered) {
            table.setMesaprincipal(tableMain.getId());
            viewModel.tableViewModel.update(table);
        }
        return this;
    }

    private MainActivity updateInvoice(List<Factura> filtered, Factura main) {
        for (Factura invoice : filtered) {
            if (invoice != main) {
                long idEmp = Long.parseLong(IO.readPreferences(MainActivity.this, FILE_LOGIN, KEY_LOGIN_ID, DEFAULT_VALUE));
                invoice.setIdempleadocierre(idEmp);
                invoice.setHoracierre(Time.getCurrentTime());
                viewModel.invoiceViewModel.update(invoice);
            }
        }
        return this;
    }

    private MainActivity updateCommands(List<Factura> filtered, Factura main) {
        List<Comanda> filteredCommands = getFilteredCommands(filtered, main);
        for (Comanda command : filteredCommands) {
            viewModel.commandViewModel.update(command);
        }
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
                    if (item.getItemId() == R.id.link) {
                        List<Factura> invoicesFiltered = getInvoicesFiltered();
                        List<Mesa> tableFiltered = getTablesFiltered(invoicesFiltered);
                        Mesa tableMain = tableFiltered.get(FIRST_ELEMENT);
                        Factura main = getMainInvoice(invoicesFiltered, tableMain);
                        updateCommands(invoicesFiltered, main)
                                .updateTables(tableFiltered, tableMain)
                                .updateInvoice(invoicesFiltered, main);

                        actionMode.finish();
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
                    Tools.setSystemBarColor(MainActivity.this, R.color.colorPrimaryDark);
                }
            });

        adapter.toggleSelection(position);
        final int size = adapter.getSelectedItems().size();
        if (size == EMPTY) {
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

    private List<Factura> filterFacturas(String date) {
        List<Factura> filtered = new ArrayList<>();
        for (int i = 0; i < invoiceList.size(); i++) {
            if (invoiceList.get(i).getHorainicio().contains(date)) {
                filtered.add(invoiceList.get(i));
            }
        }
        return filtered;
    }

    private void sendToHistory(String date) {
        Intent intent = new Intent(this, HistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_HISTORY, (ArrayList<? extends Parcelable>) filterFacturas(date));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private MainActivity showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                final String selectedDate = day + getString(R.string.dash) + (month + 1) + getString(R.string.dash) + year;
                sendToHistory(selectedDate);
            }
        });
        newFragment.show(this.getSupportFragmentManager(), null);
        return this;
    }

    private MainActivity initFabComponents() {
        ImageView ivFab = new ImageView(this);
        ivFab.setImageDrawable(getDrawable(R.drawable.ic_add_black_36dp));
        ImageView ivLogOut = new ImageView(this);
        ivLogOut.setImageDrawable(getDrawable(R.drawable.ic_exit_to_app_black_24dp));
        ImageView ivHistory = new ImageView(this);
        ivHistory.setImageDrawable(getDrawable(R.drawable.ic_history_black_24dp));
        ImageView ivSettings = new ImageView(this);
        ivSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_black_24dp));
        fab = new FloatingActionButton.Builder(this)
                .setContentView(ivFab)
                .build();
        fab.setEnabled(false);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        btLogOut = itemBuilder.setContentView(ivLogOut).build();
        btHistory = itemBuilder.setContentView(ivHistory).build();
        btSettings = itemBuilder.setContentView(ivSettings).build();
        new FloatingActionMenu.Builder(this)
                .addSubActionView(btLogOut)
                .addSubActionView(btHistory)
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
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList(KEY_ACTION_MODE) != null) {
                List<Factura> list = savedInstanceState.getParcelableArrayList(KEY_ACTION_MODE);
                adapter.setData(list);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        enableActionMode(i);
                    }
                }
            }
            search = savedInstanceState.getString(KEY_SEARCH);
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

    private MainActivity startActivityFromResult() {
        if (actionMode != null) {
            actionMode.finish();
        }
        Intent intent = new Intent(this, CommandActivity.class)
                .putExtra(KEY_INVOICE, currentInvoice)
                .putExtra(KEY_TABLE, current);
        startActivityForResult(intent, KEY_MAIN_INTENT);
        return this;
    }

    public Mesa getCurrentTable(Factura invoice) {
        Mesa currentTable = new Mesa();
        for (Mesa table : tableList) {
            if (table.getId() == invoice.getIdmesa()) {
                currentTable = table;
            }
        }
        return currentTable;
    }

    private class ItemTouchHandler extends ItemTouchHelper.SimpleCallback {

        public ItemTouchHandler(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        private void updateTableState(Mesa table, List<Mesa> tables, int state, long idTable) {
            List<Long> ids = deletedIds;
            deletedIds = new ArrayList<>();
            int cont = 0;
            if (table.getMesaprincipal() > EMPTY) {
                for (Mesa current : tables) {
                    if (current.getMesaprincipal() == table.getMesaprincipal()) {
                        deletedIds.add(current.getMesaprincipal());
                        current.setEstado(state);
                        if (idTable > EMPTY) {
                            current.setMesaprincipal(ids.get(cont));
                        } else {
                            current.setMesaprincipal(idTable);
                        }
                        viewModel.tableViewModel.update(current);
                        cont++;
                    }
                }
            } else {
                table.setEstado(state);
                viewModel.tableViewModel.update(table);
            }
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final Factura current = adapter.getInvoices().get(viewHolder.getAdapterPosition());
            current.setHoracierre(null);
            final Mesa currentTable = getCurrentTable(current);
            updateTableState(currentTable, tableList, FREE_TABLE, EMPTY);
            viewModel.invoiceViewModel.delete(current);
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, getString(R.string.invoiceDeleted), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.des), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateTableState(currentTable, tableList, OCCUPIED_TABLE, NO_EMPTY);
                            viewModel.invoiceViewModel.add(current);
                        }
                    })
                    .show();
        }
    }
}
