package org.izv.proyecto.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.Repository;
import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.utils.IO;

import java.util.List;

public class CommandViewModel extends AndroidViewModel {

    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final String KEY_DEFAULT_VALUE = "0";
    private static final String KEY_URL = "url";
    private Repository repository;

    public CommandViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        repository = new Repository(url);
    }

    public void add(Factura invoice) {
        repository.add(invoice);
    }

    public void add(Comanda command) {
        repository.add(command);
    }

    public LiveData<List<Producto>> getLiveProductsList() {
        return repository.getLiveProductList();
    }

    public void setUrl(String url) {
        repository.setUrl(url);
    }

    public void update(Mesa mesa) {
        repository.update(mesa);
    }
}
