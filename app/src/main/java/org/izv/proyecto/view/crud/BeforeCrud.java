package org.izv.proyecto.view.crud;

import org.izv.proyecto.model.data.Contenedor;

public interface BeforeCrud {
    void doIt(Contenedor.CommandDetail current);
}
