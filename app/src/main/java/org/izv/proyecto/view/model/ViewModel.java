package org.izv.proyecto.view.model;

import androidx.lifecycle.LiveData;

import java.io.File;
import java.util.List;

public interface ViewModel<T> {
    void add(T object);

    void delete(T object);

    void update(T object);

    void upload(File file);

    LiveData<List<T>> getAll();

    void setUrl(String url);
}
