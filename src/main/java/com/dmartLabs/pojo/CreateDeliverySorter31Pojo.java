package com.dmartLabs.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
//    ObjectMapper om = new ObjectMapper();
//    Root[] root = om.readValue(myJsonString, Root[].class); */
public class CreateDeliverySorter31Pojo {
    public String dlvNumber;
    public String proposedDlvDate;
    public String srcSiteId;
    public String dstSiteId;
    public String whNumber;
    public String createdBy;
    public String creationDate;
    public String creationTime;
    public String totalGdsMvtStat;
    ArrayList<DlvIemsSorterMain31Pojo> dlvItems ;

}