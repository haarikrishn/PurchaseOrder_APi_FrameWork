package com.dmartLabs.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateToMainPojo {

    public  String whNumber;
    public  String srcPlant;
    public  String destPlant;
    public  String grpDlv;
    public  String txfOrdNumber;
    public  String movementType;
    public  String creationDate;
    public  String creationTime;
    public List<String> dlvNumbers;
    public List<TransferOrderItemsToPojo>transferOrderItems;
}
