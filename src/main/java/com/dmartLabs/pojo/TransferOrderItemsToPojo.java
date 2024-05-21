package com.dmartLabs.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferOrderItemsToPojo {


    public  String txfOrdItemSeq;
    public  String matNumber;
    public  String matDesc;
    public  String ean;
    public  String batchNum;
    public  String srcStorageType;
    public  String srcStorageBin;
    public  String srcQty;
    public  String caselot;
    public  String uom;
    public  String mrp;
    public  String itemWeight;
    public  String itemVolume;
    public  String itemType;
    public  String binSeq;
    public  String bbDate;
    public  String storageLoc;
    public  String destStorageType;
    public  String palletQty;
    public  String availableBinQty;
}
