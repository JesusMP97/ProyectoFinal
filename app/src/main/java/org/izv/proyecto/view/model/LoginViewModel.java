package org.izv.proyecto.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.repository.EmployeeRepository;
import org.izv.proyecto.model.repository.Repository;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Settings;

import java.io.File;
import java.util.List;

public class LoginViewModel extends AndroidViewModel implements ViewModel<Empleado> {
    private EmployeeRepository employeeRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), Settings.FILE_SETTINGS, Settings.KEY_URL, Settings.KEY_DEFAULT_VALUE);
        employeeRepository = new EmployeeRepository(url);
    }

    public void setOnFailureListener(Repository.OnFailureListener onFailureListener) {
        employeeRepository.setOnFailureListener(onFailureListener);
    }

    @Override
    public void add(Empleado object) {
        employeeRepository.add(object);
    }

    @Override
    public void delete(Empleado object) {
        employeeRepository.delete(object);
    }

    @Override
    public void update(Empleado object) {
        employeeRepository.update(object);
    }

    @Override
    public void upload(File file) {
    }

    @Override
    public LiveData<List<Empleado>> getAll() {
        return employeeRepository.getAll();
    }

    @Override
    public void setUrl(String url) {
        employeeRepository.setUrl(url);
    }
}
