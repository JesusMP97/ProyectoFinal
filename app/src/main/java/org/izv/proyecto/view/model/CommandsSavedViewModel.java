package org.izv.proyecto.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.izv.proyecto.model.data.Comanda;
import org.izv.proyecto.model.data.Producto;
import org.izv.proyecto.model.repository.CommandRepository;
import org.izv.proyecto.model.repository.ProductRepository;
import org.izv.proyecto.model.repository.Repository;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Settings;

import java.io.File;
import java.util.List;

public class CommandsSavedViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private CommandRepository commandRepository;

    public CommandsSavedViewModel(@NonNull Application application) {
        super(application);
        String url = IO.readPreferences(application.getApplicationContext(), Settings.FILE_SETTINGS, Settings.KEY_URL, Settings.KEY_DEFAULT_VALUE);
        commandRepository = new CommandRepository(url);
        productRepository = new ProductRepository(url);
    }

    public void setOnFailureListener(Repository.OnFailureListener onFailureListener) {
        productRepository.setOnFailureListener(onFailureListener);
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

}
