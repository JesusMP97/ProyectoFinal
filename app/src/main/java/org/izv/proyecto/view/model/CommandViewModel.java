package org.izv.proyecto.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Factura;
import org.izv.proyecto.model.data.Mesa;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.model.repository.CommandRepository;
import org.izv.proyecto.model.repository.InvoiceRepository;
import org.izv.proyecto.model.repository.ProductRepository;
import org.izv.proyecto.model.repository.Repository;
import org.izv.proyecto.model.repository.TableRepository;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Settings;

import java.io.File;
import java.util.List;

public class CommandViewModel extends AndroidViewModel {
    private ProductRepository productRepository;

    public void setOnFailureListener(Repository.OnFailureListener onFailureListener) {
        productRepository.setOnFailureListener(onFailureListener);
    }

    public LiveData<Long> getInvoiceId() {
        return invoiceRepository.getInvoiceId();
    }


    public ViewModel<Producto> productoViewModel = new ViewModel<Producto>() {
        @Override
        public void add(Producto object) {
            productRepository.add(object);
        }

        @Override
        public void delete(Producto object) {
            productRepository.delete(object);
        }

        @Override
        public void update(Producto object) {
            productRepository.update(object);
        }

        @Override
        public void upload(File file) {

        }

        @Override
        public LiveData<List<Producto>> getAll() {
            return productRepository.getAll();
        }

        @Override
        public void setUrl(String url) {
            productRepository.setUrl(url);
        }
    };

    private CommandRepository commandRepository;
    public ViewModel<Comanda> commandViewModel = new ViewModel<Comanda>() {
        @Override
        public void add(Comanda object) {
            commandRepository.add(object);
        }

        @Override
        public void delete(Comanda object) {
            commandRepository.delete(object);
        }

        @Override
        public void update(Comanda object) {
            commandRepository.update(object);
        }

        @Override
        public void upload(File file) {

        }

        @Override
        public LiveData<List<Comanda>> getAll() {
            return commandRepository.getAll();
        }

        @Override
        public void setUrl(String url) {
            commandRepository.setUrl(url);
        }
    };
    private InvoiceRepository invoiceRepository;
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
    private TableRepository tableRepository;
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

    public CommandViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), Settings.FILE_SETTINGS, Settings.KEY_URL, Settings.KEY_DEFAULT_VALUE);
        invoiceRepository = new InvoiceRepository(url);
        tableRepository = new TableRepository(url);
        commandRepository = new CommandRepository(url);
        productRepository = new ProductRepository(url);
    }
}
