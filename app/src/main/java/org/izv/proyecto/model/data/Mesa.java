package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;


public class Mesa implements Parcelable {

    private long id, estado, capacidad, mesaprincipal;
    private String zona;

    public Mesa() {

    }

    protected Mesa(Parcel in) {
        id = in.readLong();
        estado = in.readLong();
        capacidad = in.readLong();
        mesaprincipal = in.readLong();
        zona = in.readString();
    }

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

    @Override
    public String toString() {
        return "Mesa{" +
                "id=" + id +
                ", estado=" + estado +
                ", capacidad=" + capacidad +
                ", mesaprincipal=" + mesaprincipal +
                ", zona='" + zona + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(estado);
        dest.writeLong(capacidad);
        dest.writeLong(mesaprincipal);
        dest.writeString(zona);
    }

    public long getMesaprincipal() {
        return mesaprincipal;
    }

    public Mesa setMesaprincipal(long mesaprincipal) {
        this.mesaprincipal = mesaprincipal;
        return this;
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

    public String getZona() {
        return zona;
    }

    public Mesa setZona(String zona) {
        this.zona = zona;
        return this;
    }
}
