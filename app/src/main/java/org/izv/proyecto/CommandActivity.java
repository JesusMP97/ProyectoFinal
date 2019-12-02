package org.izv.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.izv.circularfloatingbutton.FloatingActionButton;
import org.izv.circularfloatingbutton.FloatingActionMenu;
import org.izv.circularfloatingbutton.SubActionButton;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Contenedor;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.adapter.CommandViewAdapter;
import org.izv.proyecto.view.adapter.ProductViewAdapter;
import org.izv.proyecto.view.delay.AfterDelay;
import org.izv.proyecto.view.model.CommandViewModel;
import org.izv.proyecto.view.utils.IO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommandActivity extends AppCompatActivity {

    public static final int OCCUPIED_TABLE = 0;
    private static final String FILE_INVOICE = "invoice";
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final float GUIDE_DEFAULT_VALUE = 0.6f;
    private static final float GUIDE_MAX_VALUE = 1.0f;
    private static final String KEY_DEFAULT_VALUE = "0";
    private static final String KEY_INVOICE = "invoice";
    private static final String KEY_INVOICE_ID = "invoiceId";
    private static final String KEY_TABLE = "table";
    private static final String KEY_URL = "url";
    private static final long POST_CLOSE_SEARCH_VIEW = 300;
    private static final String PRICE_FORMAT = "%.2f";
    private SubActionButton btLogOut, btProfile, btSettings;
    private HashMap<String, List<String>> categoriesMap;
    private ConstraintLayout clContainter;
    private CommandViewAdapter commandAdapter;
    private FloatingActionButton fab;
    private Guideline glX;
    private MenuItem itSearch;
    private ProductViewAdapter productAdapter;
    private List<Producto> products, currentProducts;
    private RecyclerView rvProductList, rvCommandList;
    private SearchView svSearch;
    private Toolbar tb;
    private TextView tvCommandTotal;
    private String url;
    private CommandViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        initComponents()
                .initFabComponents()
                .setSupportActionBarValues()
                .assignEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_command, menu);
        itSearch = menu.findItem(R.id.itSearch);
        svSearch = (SearchView) itSearch.getActionView();
        assignMenuEvents();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.subItName:
                sortProductsByName();
                productAdapter.setData(currentProducts);
                break;
            case R.id.subItPrice:
                sortProductsByPrice();
                productAdapter.setData(currentProducts);
                break;
            case R.id.itSearch:
                glX.setGuidelinePercent(GUIDE_MAX_VALUE);
                clContainter.setVisibility(View.GONE);
                break;
        }
        setCorrectData(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        if (!this.url.equalsIgnoreCase(url)) {
            viewModel.setUrl(url);
        }
    }

    public CommandActivity addCommands(Producto product) {
        long idInvoice = Long.parseLong(IO.readPreferences(CommandActivity.this, FILE_INVOICE, KEY_INVOICE_ID, "0"));
        long idEmp = Long.parseLong(IO.readPreferences(CommandActivity.this, Login.FILE_LOGIN, Login.KEY_LOGIN_ID, "0"));
        Contenedor.CommandDetail commandDetail = new Contenedor.CommandDetail();
        commandDetail.setProduct(product);
        Comanda command = new Comanda();
        long units = 1;
        boolean duplicate = false;
        if (!commandAdapter.getCommands().isEmpty()) {
            for (Contenedor.CommandDetail cc : commandAdapter.getCommands()) {
                if (cc.getProduct().getNombre().equalsIgnoreCase(product.getNombre())) {
                    units = cc.getCommand().getUnidades();
                    units++;
                    float totalPrice = cc.getProduct().getPrecio() * units;
                    cc.getCommand().setUnidades(units)
                            .setPrecio(totalPrice);
                    duplicate = true;
                    commandAdapter.notifyDataSetChanged();
                }
            }
        }
        if (!duplicate) {
            command.setIdfactura(idInvoice)
                    .setIdempleado(idEmp)
                    .setUnidades(units)
                    .setPrecio(product.getPrecio())
                    .setEntregada(units);
            commandDetail.setCommand(command);
            commandAdapter.addCommand(commandDetail);
        }
        Toast.makeText(CommandActivity.this, product.getNombre() + " " + getString(R.string.productAdded), Toast.LENGTH_SHORT).show();
        return this;
    }

    private CommandActivity adjustComponents() {
        glX.setGuidelinePercent(GUIDE_DEFAULT_VALUE);
        AfterDelay afterDelay = new AfterDelay() {
            @Override
            public void doIt() {
                clContainter.post(new Runnable() {
                    @Override
                    public void run() {
                        clContainter.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        Delay delay = new Delay(POST_CLOSE_SEARCH_VIEW, afterDelay);
        delay.start();
        return this;
    }

    private CommandActivity assignEvents() {
        viewModel.getLiveProductsList().observe(this, new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> products) {
                productAdapter.setData(products);
                setProducts(products).
                        fetchCategoriesMap();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                List<Contenedor.CommandDetail> commandDetail = commandAdapter.getCommands();
                if (commandDetail != null && !commandDetail.isEmpty()) {
                    Intent intent = getIntent();
                    Factura invoice = (Factura) intent.getSerializableExtra(KEY_INVOICE);
                    Mesa table = (Mesa) intent.getSerializableExtra(KEY_TABLE);
                    table.setEstado(OCCUPIED_TABLE);
                    viewModel.update(table);
                    viewModel.add(invoice);
                    for (Contenedor.CommandDetail command : commandDetail) {
                        viewModel.add(command.getCommand());
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(CommandActivity.this, getString(R.string.unselectedProduct), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        productAdapter.setOnClickListener(new ProductViewAdapter.OnClickListener() {
            @Override
            public void onItemLongClick(Producto product) {
                addCommands(product)
                        .setCommandTotalValue();
            }
        });
        return this;
    }

    private CommandActivity assignMenuEvents() {
        itSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adjustComponents();
                return true;
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return this;
    }

    private CommandActivity fetchCategoriesMap() {
        sortProductsByCategory()
                .filterCategories();
        return this;
    }

    private CommandActivity filterCategories() {
        categoriesMap = new HashMap<>();
        List<String> subCategories = new ArrayList<>();
        for (Producto product : products) {
            if (!categoriesMap.containsKey(product.getCategoria())) {
                categoriesMap.put(product.getCategoria(), null);
                subCategories = new ArrayList<>();
            }
            if (!subCategories.contains(product.getSubcategoria())) {
                subCategories.add(product.getSubcategoria());
                categoriesMap.put(product.getCategoria(), subCategories);
            }
        }
        return this;
    }

    private List<Producto> filteredProducts(MenuItem item) {
        List<Producto> filteredProducts = new ArrayList<>();
        if (item.getTitle() != null) {
            for (Producto product : products) {
                if (product.getCategoria().equalsIgnoreCase(item.getTitle().toString()) || product.getSubcategoria().equalsIgnoreCase(item.getTitle().toString())) {
                    filteredProducts.add(product);
                }
            }
        }
        return filteredProducts;
    }

    public String getTotalPriceOfCommands() {
        float total = 0;
        for (Contenedor.CommandDetail com : commandAdapter.getCommands()) {
            total += com.getCommand().getPrecio();
        }
        return String.format(String.format(Locale.GERMAN, PRICE_FORMAT, total));
    }

    private CommandActivity initComponents() {
        tb = findViewById(R.id.tb);
        clContainter = findViewById(R.id.clContainter);
        glX = findViewById(R.id.glX);
        rvProductList = findViewById(R.id.rvProductList);
        rvCommandList = findViewById(R.id.rvProductValueList);
        tvCommandTotal = findViewById(R.id.tvCommandTotal);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvCommandList.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductViewAdapter(this);
        commandAdapter = new CommandViewAdapter(this);
        viewModel = ViewModelProviders.of(this).get(CommandViewModel.class);
        rvProductList.setAdapter(productAdapter);
        rvCommandList.setAdapter(commandAdapter);
        url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        return this;
    }

    private CommandActivity initFabComponents() {
        ImageView ivFab = new ImageView(this);
        ivFab.setImageDrawable(getDrawable(R.drawable.ic_done_black_36dp));
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

    public CommandActivity setCommandTotalValue() {
        tvCommandTotal.setText(getTotalPriceOfCommands());
        return this;
    }

    private void setCorrectData(MenuItem item) {
        List<Producto> filteredProducts = filteredProducts(item);
        if (item.getTitle() != null) {
            for (Map.Entry<String, List<String>> entry : categoriesMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(item.getTitle().toString()) && entry.getValue().get(0).isEmpty()) {
                    productAdapter.setData(filteredProducts);
                    currentProducts = filteredProducts;
                }
                if (entry.getValue().toString().toLowerCase().contains(item.getTitle().toString().toLowerCase())) {
                    productAdapter.setData(filteredProducts);
                    currentProducts = filteredProducts;
                }
            }
        }
    }

    private CommandActivity setProducts(List<Producto> products) {
        currentProducts = products;
        this.products = products;
        return this;
    }

    private CommandActivity setSupportActionBarValues() {
        setSupportActionBar(tb);
        getSupportActionBar().setTitle(getString(R.string.tbMainTitle));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return this;
    }

    private CommandActivity sortProductsByCategory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            products.sort(new Comparator<Producto>() {
                @Override
                public int compare(Producto o1, Producto o2) {
                    return o1.getCategoria().compareTo(o2.getCategoria());
                }
            });
        }
        return this;
    }

    private CommandActivity sortProductsByName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentProducts.sort(new Comparator<Producto>() {
                @Override
                public int compare(Producto o1, Producto o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
        }
        return this;
    }

    private CommandActivity sortProductsByPrice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentProducts.sort(new Comparator<Producto>() {
                @Override
                public int compare(Producto o1, Producto o2) {
                    return Float.compare(o1.getPrecio(), o2.getPrecio());
                }
            });
        }
        return this;
    }

    public class Delay extends Thread {
        private volatile AfterDelay afterDelay;
        private volatile long delay;

        public Delay(long delay, AfterDelay afterDelay) {
            this.delay = delay;
            this.afterDelay = afterDelay;
        }

        @Override
        public synchronized void run() {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            afterDelay.doIt();
        }
    }
}
