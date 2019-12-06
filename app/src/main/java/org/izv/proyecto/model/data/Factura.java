package org.izv.proyecto.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Factura implements Parcelable {
    public static final Creator<Factura> CREATOR = new Creator<Factura>() {
        @Override
        public Factura createFromParcel(Parcel in) {
            return new Factura(in);
        }

        @Override
        public Factura[] newArray(int size) {
            return new Factura[size];
        }
    };
    private String horainicio, horacierre;
    private long id, idmesa, idempleadoinicio, idempleadocierre;
    private boolean selected;
    private float total;

    public Factura() {

    }

    protected Factura(Parcel in) {
        horainicio = in.readString();
        horacierre = in.readString();
        id = in.readLong();
        idmesa = in.readLong();
        idempleadoinicio = in.readLong();
        idempleadocierre = in.readLong();
        total = in.readFloat();
        selected = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(horainicio);
        dest.writeString(horacierre);
        dest.writeLong(id);
        dest.writeLong(idmesa);
        dest.writeLong(idempleadoinicio);
        dest.writeLong(idempleadocierre);
        dest.writeFloat(total);
        dest.writeByte((byte) (selected ? 1 : 0));
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

    public String getHoracierre() {
        return horacierre;
    }

    public Factura setHoracierre(String horacierre) {
        this.horacierre = horacierre;
        return this;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public Factura setHorainicio(String horainicio) {
        this.horainicio = horainicio;
        return this;
    }

    public long getId() {
        return id;
    }

    public Factura setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdempleadocierre() {
        return idempleadocierre;
    }

    public Factura setIdempleadocierre(long idempleadocierre) {
        this.idempleadocierre = idempleadocierre;
        return this;
    }

    public long getIdempleadoinicio() {
        return idempleadoinicio;
    }

    public Factura setIdempleadoinicio(long idempleadoinicio) {
        this.idempleadoinicio = idempleadoinicio;
        return this;
    }

    public long getIdmesa() {
        return idmesa;
    }

    public Factura setIdmesa(long idmesa) {
        this.idmesa = idmesa;
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
}
