package org.izv.proyecto.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.repository.InvoiceRepository;
import org.izv.proyecto.model.repository.TableRepository;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Settings;

import java.io.File;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private InvoiceRepository invoiceRepository;
    private TableRepository tableRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), Settings.FILE_SETTINGS, Settings.KEY_URL, Settings.KEY_DEFAULT_VALUE);
        invoiceRepository = new InvoiceRepository(url);
        tableRepository = new TableRepository(url);

    }

    public ViewModel<Factura> invoiceViewModel = new ViewModel<Factura>() {
        @Override
        public void add(Factura object) {
            invoiceRepository.add(object);
        }

        @Override
        public void delete(Factura object) {
            invoiceRepository.delete(object);
        }

        @Override
        public void update(Factura object) {
            invoiceRepository.update(object);
        }

        @Override
        public void upload(File file) {

        }

        @Override
        public LiveData<List<Factura>> getAll() {
            return invoiceRepository.getAll();
        }

        @Override
        public void setUrl(String url) {
            invoiceRepository.setUrl(url);
        }
    };
    public ViewModel<Mesa> tableViewModel = new ViewModel<Mesa>() {
        @Override
        public void add(Mesa object) {
            tableRepository.add(object);
        }

        @Override
        public void delete(Mesa object) {
            tableRepository.delete(object);
        }

        @Override
        public void update(Mesa object) {
            tableRepository.update(object);
        }

        @Override
        public void upload(File file) {

        }

        @Override
        public LiveData<List<Mesa>> getAll() {
            return tableRepository.getAll();
        }

        @Override
        public void setUrl(String url) {
            tableRepository.setUrl(url);
        }
    };
}
