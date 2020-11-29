package com.gtech.fishbangla.DATABASE.TABLE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "config_info")
public class CONFIG_TABLE {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "delivery_charge")
    String delivery_charge;

    @NonNull
    @ColumnInfo(name = "service_charge")
    String service_charge;

    @NonNull
    @ColumnInfo(name = "vat_charge")
    String vat_charge;

    @NonNull
    @ColumnInfo(name = "frozen_type")
    String frozen_type;

    @NonNull
    @ColumnInfo(name = "referrer")
    String referrer;

    @NonNull
    @ColumnInfo(name = "fishbangla_address")
    String fishbangla_address;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDelivery_charge() {
        return delivery_charge;
    }

    public CONFIG_TABLE() {
    }

    public void setDelivery_charge(@NonNull String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    @NonNull
    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(@NonNull String service_charge) {
        this.service_charge = service_charge;
    }

    @NonNull
    public String getVat_charge() {
        return vat_charge;
    }

    public void setVat_charge(@NonNull String vat_charge) {
        this.vat_charge = vat_charge;
    }

    @NonNull
    public String getFrozen_type() {
        return frozen_type;
    }

    public void setFrozen_type(@NonNull String frozen_type) {
        this.frozen_type = frozen_type;
    }

    @NonNull
    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(@NonNull String referrer) {
        this.referrer = referrer;
    }

    @NonNull
    public String getFishbangla_address() {
        return fishbangla_address;
    }

    public void setFishbangla_address(@NonNull String fishbangla_address) {
        this.fishbangla_address = fishbangla_address;
    }

    @Override
    public String toString() {
        return "CONFIG_TABLE{" +
                "delivery_charge='" + delivery_charge + '\'' +
                ", service_charge='" + service_charge + '\'' +
                ", vat_charge='" + vat_charge + '\'' +
                ", frozen_type='" + frozen_type + '\'' +
                ", referrer='" + referrer + '\'' +
                ", fishbangla_address='" + fishbangla_address + '\'' +
                '}';
    }
}
