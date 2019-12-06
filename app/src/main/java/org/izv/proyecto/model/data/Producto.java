package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable {
    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };
    private long id;
    private String nombre, destino, categoria, subcategoria;
    private float precio;

    public Producto() {

    }

    protected Producto(Parcel in) {
        id = in.readLong();
        nombre = in.readString();
        destino = in.readString();
        categoria = in.readString();
        subcategoria = in.readString();
        precio = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nombre);
        dest.writeString(destino);
        dest.writeString(categoria);
        dest.writeString(subcategoria);
        dest.writeFloat(precio);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", destino='" + destino + '\'' +
                ", categoria='" + categoria + '\'' +
                ", subcategoria='" + subcategoria + '\'' +
                ", precio=" + precio +
                '}';
    }

    public String getCategoria() {
        return categoria;
    }

    public Producto setCategoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public String getDestino() {
        return destino;
    }

    public Producto setDestino(String destino) {
        this.destino = destino;
        return this;
    }

    public long getId() {
        return id;
    }

    public Producto setId(long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Producto setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public float getPrecio() {
        return precio;
    }

    public Producto setPrecio(float precio) {
        this.precio = precio;
        return this;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public Producto setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
        return this;
    }
}
