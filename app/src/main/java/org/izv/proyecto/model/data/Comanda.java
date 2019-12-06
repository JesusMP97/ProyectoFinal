package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Comanda implements Parcelable {
    public static final Creator<Comanda> CREATOR = new Creator<Comanda>() {
        @Override
        public Comanda createFromParcel(Parcel in) {
            return new Comanda(in);
        }

        @Override
        public Comanda[] newArray(int size) {
            return new Comanda[size];
        }
    };
    private long id, idfactura, idproducto, idempleado, unidades, entregada;
    private float precio;

    public Comanda() {

    }

    protected Comanda(Parcel in) {
        id = in.readLong();
        idfactura = in.readLong();
        idproducto = in.readLong();
        idempleado = in.readLong();
        unidades = in.readLong();
        entregada = in.readLong();
        precio = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(idfactura);
        dest.writeLong(idproducto);
        dest.writeLong(idempleado);
        dest.writeLong(unidades);
        dest.writeLong(entregada);
        dest.writeFloat(precio);
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

    public long getEntregada() {
        return entregada;
    }

    public Comanda setEntregada(long entregada) {
        this.entregada = entregada;
        return this;
    }

    public long getId() {
        return id;
    }

    public Comanda setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdempleado() {
        return idempleado;
    }

    public Comanda setIdempleado(long idempleado) {
        this.idempleado = idempleado;
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

    public float getPrecio() {
        return precio;
    }

    public Comanda setPrecio(float precio) {
        this.precio = precio;
        return this;
    }

    public long getUnidades() {
        return unidades;
    }

    public Comanda setUnidades(long unidades) {
        this.unidades = unidades;
        return this;
    }
}
