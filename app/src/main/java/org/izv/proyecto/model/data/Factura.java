package org.izv.proyecto.model.data;

import java.io.Serializable;

public class Factura implements Serializable {
    private String horainicio, horacierre;
    private long id, idmesa, idempleadoinicio, idempleadocierre;
    private float total;
    private boolean selected;

    public String getHorainicio() {
        return horainicio;
    }

    public Factura setHorainicio(String horainicio) {
        this.horainicio = horainicio;
        return this;
    }

    public String getHoracierre() {
        return horacierre;
    }

    public Factura setHoracierre(String horacierre) {
        this.horacierre = horacierre;
        return this;
    }

    public long getId() {
        return id;
    }

    public Factura setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdmesa() {
        return idmesa;
    }

    public Factura setIdmesa(long idmesa) {
        this.idmesa = idmesa;
        return this;
    }

    public long getIdempleadoinicio() {
        return idempleadoinicio;
    }

    public Factura setIdempleadoinicio(long idempleadoinicio) {
        this.idempleadoinicio = idempleadoinicio;
        return this;
    }

    public long getIdempleadocierre() {
        return idempleadocierre;
    }

    public Factura setIdempleadocierre(long idempleadocierre) {
        this.idempleadocierre = idempleadocierre;
        return this;
    }

    public float getTotal() {
        return total;
    }

    public Factura setTotal(float total) {
        this.total = total;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public Factura setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "horainicio='" + horainicio + '\'' +
                ", horacierre='" + horacierre + '\'' +
                ", id=" + id +
                ", idmesa=" + idmesa +
                ", idempleadoinicio=" + idempleadoinicio +
                ", idempleadocierre=" + idempleadocierre +
                ", total=" + total +
                ", selected=" + selected +
                '}';
    }
}
