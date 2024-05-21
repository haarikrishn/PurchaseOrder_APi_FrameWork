package com.dmartLabs.commonutils;

import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import rst.pdfbox.layout.text.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.LongStream;

public class GenricUtils extends Constants {
    static Workbook workbook;
    /**
     * random number generator upto 4 digit
     * @return
     */
    public static long getRendomMobileNumber() {
        Random random = new Random();
        // Generate a random integer between 1000 and 9999 (inclusive)
        String num = "5555555"+(random.nextInt(1000));
        return Long.parseLong(num);
    }

    public static String genrateStateRegNumber(){
        String num = ""+GenricUtils.getRendomMobileNumber();
        String randNum = "35060211"+num;
        return randNum;
    }

    public static String encode(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()));
    }

    public static String getRandomDeliveryNumber() {
        Random random = new Random();
        String num = "9"+(random.nextInt(1000000000));
        return num;
    }

    public static String getPONumber() {
        Random random = new Random();
        String poNumber = "500"+(random.nextInt(1000000));
        return poNumber;
    }

    public static  String getDate(String dateFormat){
        Date dt = new Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
        return inputFormat.format(dt);
    }
    //time format
    public static String  getTime(String timeformat) {
        Date dt = new Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat("HHmmss");
        return inputFormat.format(dt);
    }

    public static JWTClaimsSet decodeAccessToken(String accessToken) {
        String[] accessTokenArray = accessToken.split(" ");
        String formattedAccessToken = accessTokenArray[1];
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(formattedAccessToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JWTClaimsSet claims = null;
        try {
            claims = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public static List<String> getRolesFromAccessToken(String accessToken) {
        String[] accessTokenArray = accessToken.split(" ");
        String formattedAccessToken = accessTokenArray[1];
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(formattedAccessToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JWTClaimsSet claims = null;
        try {
            claims = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> roles = null;
        try {
//            roles = claims.getStringArrayClaim("roles");
            roles = claims.getStringListClaim("roles");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public static String getSiteID_FromAccessToken(String accessToken) {
        String[] accessTokenArray = accessToken.split(" ");
        String formattedAccessToken = accessTokenArray[1];
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(formattedAccessToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JWTClaimsSet claims = null;
        try {
            claims = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String siteID = null;
        try {
            siteID = claims.getStringClaim("site");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return siteID;
    }

    public static String getClientID_FromAccessToken(String accessToken) {
        String[] accessTokenArray = accessToken.split(" ");
        String formattedAccessToken = accessTokenArray[1];
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(formattedAccessToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JWTClaimsSet claims = null;
        try {
            claims = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String clientID = null;
        try {
            clientID = claims.getStringClaim("sub");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return clientID;
    }

    public static String getRequester_FromAccessToken(String accessToken) {
        String[] accessTokenArray = accessToken.split(" ");
        String formattedAccessToken = accessTokenArray[1];
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(formattedAccessToken);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JWTClaimsSet claims = null;
        try {
            claims = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String requester=null;
        try {
            requester = claims.getStringClaim("sub");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return requester;
    }

    public static boolean convertStringToBoolean(String value){
        boolean booleanValue = false;
        if (value.equals("true") || value.equals("false")){
            booleanValue = Boolean.parseBoolean(value);
        }
        return booleanValue;
    }

    public static String getFormattedDateTime(String dateFormat){
       return new SimpleDateFormat(dateFormat).format(new Date());
    }

    public static String getUTC_Format_CurrentDateTime(){
        String currentDateTime = new DateTime().toDateTime(DateTimeZone.UTC).toString();
        if (currentDateTime.contains("0Z")){
            String[] dateTime = currentDateTime.split("0Z");
            currentDateTime = dateTime[0]+"Z";
        }
        return currentDateTime;
    }

    public static String getPO_DueDate(){
        String currentDateTime = new DateTime().toDateTime(DateTimeZone.UTC).toString();
        if (currentDateTime.contains("0Z")){
            String[] dateTime = currentDateTime.split("0Z");
            currentDateTime = dateTime[0]+"Z";
        }
        String[] splitedDate = currentDateTime.split("-");
        String date = splitedDate[2];
        String[] dateAndTime = date.split("T");
        int finalDate = Integer.parseInt(dateAndTime[0]) + 10;
        if (splitedDate[1]=="02" && finalDate>28){
            finalDate=finalDate-28;
            splitedDate[1]="03";
        } else if (splitedDate[1]=="04" || splitedDate[1]=="06" || splitedDate[1]=="09" || splitedDate[1]=="11"){
            if (finalDate>30){
                finalDate=finalDate-30;
                int month = Integer.parseInt(splitedDate[1]) + 1;
                if (month<10) {
                    splitedDate[1] = "0"+String.valueOf(month);
                }
                else {
                    splitedDate[1] = String.valueOf(month);
                }
            }
        } else{
            if (finalDate>31){
                finalDate = finalDate-31;
                int month = Integer.parseInt(splitedDate[1]) + 1;
                if (month>12){
                    splitedDate[0] = String.valueOf(Integer.parseInt(splitedDate[0]+1));
                    splitedDate[1] = "01";
                } else{
                    if (month<10) {
                        splitedDate[1] = "0"+String.valueOf(month);
                    }
                    else {
                        splitedDate[1] = String.valueOf(month);
                    }
                }
            }
        }

        String poDueDate = splitedDate[0] + "-" + splitedDate[1] + "-" + String.valueOf(finalDate) + "T" + dateAndTime[1];
        return poDueDate;
    }

    public static String getDeviceSerialNumber(){
        return String.valueOf(Math.abs(new Random().nextLong()%10000000000l));
    }


    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static String getRandomAlphabets(int length){
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String getRandomAlphabets(int min_length, int max_length){
        return RandomStringUtils.randomAlphabetic(min_length, max_length);
    }

    public static String gettxfOrdNumber() {
        Random random = new Random();
        String num = "00"+"9"+(random.nextInt(10000000));
        return num;
    }


    public static void main(String[] args) throws ParseException {
//        long num = Math.abs(new Random().nextLong()%10000000000l);
//        System.out.println(num);
//        List<String> roles = GenricUtils.getRolesFromAccessToken("Bearer eyJ0eXBlIjoiYXQiLCJhbGciOiJSUzI1NiIsImtpZCI6IjhhZWIwM2RlLWU1MGMtNDEyZC04MTllLWZiMzNhZWI5ODlkZiJ9.eyJzdWIiOiI2NTc3Zjk4MTMwZTMzZDMzOTAzZTc4NzYiLCJzaXRlIjoiMTAxNCIsInJvbGVzIjpbIkRFTElWRVJZIFBMQU5ORVIiLCJESVNQQVRDSEVSIFBMQU5ORVIiLCJQSUNLRVIiLCJESVNQQVRDSEVSIiwiTURNIEFETUlOIiwiUElDSyBQTEFOTkVSIiwiQURNSU4iLCJQSUNLRVIgUk8iLCJNRE0gRURJVE9SIiwiR0FURUtFRVBFUiIsIlBJQ0sgUExBTk5FUiBSTyIsIkRFTElWRVJZIFBMQU5ORVIgUk8iLCJDSEVDS0lOIE9GRklDRVIgUk8iLCJDSEVDS0lOIE9GRklDRVIiXSwiaXNzIjoiaHR0cHM6XC9cL3d3dy5tZXJhZG1hcnQuY29tIiwiZXhwIjoxNzA1Mzc4NDg3LCJpYXQiOjE3MDUyOTIwODcsInVzZXIiOiIxMDEyMDEiLCJqdGkiOiIyZjYwZjBkMi0zYTNhLTQzZDktYjlkMS03ZWU5MzYxYmQ3YzAifQ.RsxiIAL_IJG7M0CWUDxcGG56CupTq6tQBRgIaUC8W4KMO0rYJ5lycBHc_J1-rG7m_10ldlykHQG_TFyv9Y1rC3paCQZTtx9Bv6WnFaGmcJNVki_6JftSXm6Hxbj8MTObjjVLF-mUxnbWIOJM7rGLxBWVBxMWk84O5UjgVGc9-stKsdVzLePwqvJ4apJEG6toywEz7nboqzg2Go3JyBsIqt44Ths8677gKhFqRKUzpJ0US7p61VTjTdJvvcIZPiLIiiF4DIL6vqjujSwa1UV1mRSMV2fcCAen0vbK-TDpsxr3-7t8Ka5y8lraOYphnPXhoLoMtIdKThrnIekXI3heCQ");
//        System.out.println(roles);
//        //System.out.println(GenricUtils.getSiteID_FromAccessToken("Bearer eyJ0eXBlIjoiYXQiLCJhbGciOiJSUzI1NiIsImtpZCI6IjhhZWIwM2RlLWU1MGMtNDEyZC04MTllLWZiMzNhZWI5ODlkZiJ9.eyJzdWIiOiI2NTc3Zjk4MTMwZTMzZDMzOTAzZTc4NzYiLCJzaXRlIjoiMTAxNCIsInJvbGVzIjpbIkRFTElWRVJZIFBMQU5ORVIiLCJESVNQQVRDSEVSIFBMQU5ORVIiLCJQSUNLRVIiLCJESVNQQVRDSEVSIiwiTURNIEFETUlOIiwiUElDSyBQTEFOTkVSIiwiQURNSU4iLCJQSUNLRVIgUk8iLCJNRE0gRURJVE9SIiwiR0FURUtFRVBFUiIsIlBJQ0sgUExBTk5FUiBSTyIsIkRFTElWRVJZIFBMQU5ORVIgUk8iLCJDSEVDS0lOIE9GRklDRVIgUk8iLCJDSEVDS0lOIE9GRklDRVIiXSwiaXNzIjoiaHR0cHM6XC9cL3d3dy5tZXJhZG1hcnQuY29tIiwiZXhwIjoxNzA0Nzk2NDM4LCJpYXQiOjE3MDQ3MTAwMzgsInVzZXIiOiIxMDEyMDEiLCJqdGkiOiI0YjM0NjdmNi03NTI0LTQ5OTEtYmVmZC02MmEwOGJjNWNjMTgifQ.xxpM6LShqaZ-iUa7s8ZP_tjtcpFFDy8cvoktNYaARoJgsP6LU2ktMXAKXS_2VraO0xypDzYNqVLggmYdWcupym5XTXzT63AuYqfrB33p2jDuRjh0nmpkr-T0hHSfFhD6nCOKEi9P0QMnpwo4i1jiGwxdtf0qRQrZSYoWUY2nsQQpnXTb3JTTtPNcIh-6Gwf3i5oT6MQ4mVudafumcUGB_CWWuHGxzLHesPhzM_5sofaiQt6BirA-_nh7xTW7DzCV2wYsoyFu7BEglBSroz_VE7odNHxB80s0qB9okeU-3-XBUwD8p6_knHql8rnA_5Czf6Q_yXFM5VZjy9F_E4oHGg"));
//
//        if (roles.size()==1 && roles.get(0).equals("DELIVERY PLANNER")){
//
//        }
        String utcDate = getUTC_Format_CurrentDateTime();
        System.out.println("CreationDate "+utcDate);
        String[] splitedDate = utcDate.split("-");
        String date = splitedDate[2];
        String[] dateAndTime = date.split("T");
        int finalDate = Integer.parseInt(dateAndTime[0]) + 10;
        if (splitedDate[1]=="02" && finalDate>28){
            finalDate=finalDate-28;
            splitedDate[1]="03";
        } else if (splitedDate[1]=="04" || splitedDate[1]=="06" || splitedDate[1]=="09" || splitedDate[1]=="11"){
            if (finalDate>30){
                finalDate=finalDate-30;
                int month = Integer.parseInt(splitedDate[1]) + 1;
                if (month<10) {
                    splitedDate[1] = "0"+String.valueOf(month);
                }
                else {
                    splitedDate[1] = String.valueOf(month);
                }
            }
        } else{
            if (finalDate>31){
                finalDate = finalDate-31;
                int month = Integer.parseInt(splitedDate[1]) + 1;
                if (month>12){
                    splitedDate[0] = String.valueOf(Integer.parseInt(splitedDate[0]+1));
                    splitedDate[1] = "01";
                } else{
                    if (month<10) {
                        splitedDate[1] = "0"+String.valueOf(month);
                    }
                    else {
                        splitedDate[1] = String.valueOf(month);
                    }
                }
            }
        }

        String poDueDate = splitedDate[0] + "-" + splitedDate[1] + "-" + String.valueOf(finalDate) + "T" + dateAndTime[1];
        System.out.println("Due date "+poDueDate);

    }

}
