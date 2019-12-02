package org.izv.proyecto.model.data;

public class Producto {
    private long id;
    private String nombre, destino, categoria, subcategoria;
    private float precio;

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

    public String getDestino() {
        return destino;
    }

    public Producto setDestino(String destino) {
        this.destino = destino;
        return this;
    }

    public String getCategoria() {
        return categoria;
    }

    public Producto setCategoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public Producto setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
        return this;
    }

    public float getPrecio() {
        return precio;
    }

    public Producto setPrecio(float precio) {
        this.precio = precio;
        return this;
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
}
