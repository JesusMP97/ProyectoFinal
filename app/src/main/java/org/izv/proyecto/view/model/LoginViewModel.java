package org.izv.proyecto.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.izv.proyecto.model.Repository;
import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.view.utils.IO;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final String KEY_DEFAULT_VALUE = "0";
    private static final String KEY_URL = "url";
    private Repository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        repository = new Repository(url);
    }

    public LiveData<List<Empleado>> getLiveEmployeeList() {
        return repository.getLiveEmployeeList();
    }

    public void setUrl(String url) {
        repository.setUrl(url);
    }

}
