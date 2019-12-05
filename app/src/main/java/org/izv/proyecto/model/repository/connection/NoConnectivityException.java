package org.izv.proyecto.model.repository.connection;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet Connection";
    }
}