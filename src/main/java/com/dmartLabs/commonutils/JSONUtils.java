package com.dmartLabs.commonutils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

import static com.dmartLabs.config.ConStants.JSON_FILE_PATH;

public class JSONUtils {

    public static File getRequestPayloadAsJsonFile(String filePath) {
        File jsonFile = new File(filePath);

        return jsonFile;
    }

//    public static JSONObject getRequestPayloadAsObjectFromJsonFile(String filePath){
//        FileInputStream jsonfile = null;
//        try {
//            jsonfile = new FileInputStream(filePath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = new JSONObject(jsonfile);
//        return jsonObject;
//    }

    public static JSONObject getRequestPayloadAsObjectFromJsonFile(String jsonFileName){
        File jsonFile = new File(JSON_FILE_PATH +jsonFileName);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(jsonFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONTokener jsonTokener = new JSONTokener(fileReader);

        JSONObject jsonObject = new JSONObject(jsonTokener);
        return jsonObject;
    }

    public static JSONArray getRequestPayloadAsObjectFromJsonFileArray(String jsonFileName) throws FileNotFoundException {
        File jsonFile = new File(JSON_FILE_PATH + jsonFileName);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(jsonFile);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JSONTokener jsonTokener = new JSONTokener(fileReader);
        //  org.json.JSONObject jsonObject = new org.json.JSONObject(jsonTokener);//json objecr coming from org.json
        JSONArray jsonArray = new JSONArray(jsonTokener);
        return jsonArray ;
    }




}
