package org.izv.proyecto.model.data;

public class Comanda {
    private long id, idfactura, idproducto, idempleado, unidades, entregada;
    private float precio;

    public long getId() {
        return id;
    }

    public Comanda setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdfactura() {
        return idfactura;
    }

    public Comanda setIdfactura(long idfactura) {
        this.idfactura = idfactura;
        return this;
    }

    public long getIdproducto() {
        return idproducto;
    }

    public Comanda setIdproducto(long idproducto) {
        this.idproducto = idproducto;
        return this;
    }

    public long getIdempleado() {
        return idempleado;
    }

    public Comanda setIdempleado(long idempleado) {
        this.idempleado = idempleado;
        return this;
    }

    public long getUnidades() {
        return unidades;
    }

    public Comanda setUnidades(long unidades) {
        this.unidades = unidades;
        return this;
    }

    public long getEntregada() {
        return entregada;
    }

    public Comanda setEntregada(long entregada) {
        this.entregada = entregada;
        return this;
    }

    public float getPrecio() {
        return precio;
    }

    public Comanda setPrecio(float precio) {
        this.precio = precio;
        return this;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", idfactura=" + idfactura +
                ", idproducto=" + idproducto +
                ", idempleado=" + idempleado +
                ", unidades=" + unidades +
                ", entregada=" + entregada +
                ", precio=" + precio +
                '}';
    }
}
