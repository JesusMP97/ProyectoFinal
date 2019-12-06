package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Mesa implements Parcelable {
    public static final Creator<Mesa> CREATOR = new Creator<Mesa>() {
        @Override
        public Mesa createFromParcel(Parcel in) {
            return new Mesa(in);
        }

        @Override
        public Mesa[] newArray(int size) {
            return new Mesa[size];
        }
    };
    private long id, numero, estado, capacidad;
    private String zona;

    public Mesa() {

    }

    protected Mesa(Parcel in) {
        id = in.readLong();
        numero = in.readLong();
        estado = in.readLong();
        capacidad = in.readLong();
        zona = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(numero);
        dest.writeLong(estado);
        dest.writeLong(capacidad);
        dest.writeString(zona);
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "id=" + id +
                ", numero=" + numero +
                ", estado=" + estado +
                ", capacidad=" + capacidad +
                ", zona='" + zona + '\'' +
                '}';
    }

    public long getCapacidad() {
        return capacidad;
    }

    public Mesa setCapacidad(long capacidad) {
        this.capacidad = capacidad;
        return this;
    }

    public long getEstado() {
        return estado;
    }

    public Mesa setEstado(long estado) {
        this.estado = estado;
        return this;
    }

    public long getId() {
        return id;
    }

    public Mesa setId(long id) {
        this.id = id;
        return this;
    }

    public long getNumero() {
        return numero;
    }

    public Mesa setNumero(long numero) {
        this.numero = numero;
        return this;
    }

    public String getZona() {
        return zona;
    }

    public Mesa setZona(String zona) {
        this.zona = zona;
        return this;
    }
}
