package org.izv.proyecto.view.utils;

import org.izv.proyecto.model.data.Contenedor;

public interface BeforeCrud {
    void doIt(Contenedor.CommandDetail current);
}
