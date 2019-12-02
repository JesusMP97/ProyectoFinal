package org.izv.proyecto.view.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.izv.proyecto.model.Repository;
import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.utils.IO;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Repository repository;
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final String KEY_URL = "url";
    private static final String KEY_DEFAULT_VALUE = "0";

    public MainViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        repository = new Repository(url);
    }

    public MutableLiveData<List<Factura>> getLiveInvoices() {
        return repository.getLiveInvoices();
    }

    public LiveData<List<Mesa>> getLiveTables() {
        return repository.getLiveTableList();
    }

    public void setUrl(String url) {
        repository.setUrl(url);
    }
}
