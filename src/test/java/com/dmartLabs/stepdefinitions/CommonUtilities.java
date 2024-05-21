package com.dmartLabs.stepdefinitions;

import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;

import com.dmartLabs.pojo.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommonUtilities implements ConStants {
    RequestGenerator requestGenerator = new RequestGenerator();
    public static String accessToken;
    Response response;

    public static String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }

    static ThreadLocal<Response> response2 = new ThreadLocal<>();

    static ThreadLocal<Response> threadSafeConfirmDeliveryResponse = new ThreadLocal<>();
    static ThreadLocal<Response> threadSafeGetDeliveryResponse = new ThreadLocal<>();
    static ThreadLocal<Map> requestPayload = new ThreadLocal<>();
    static ThreadLocal<String> token = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeDlvNumber = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeTruckType = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeClientId = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeSiteId = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeWhNumber = new ThreadLocal<>();
    static ThreadLocal<List<String>> threadSafeTxfOrdItemSeq = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeDstSiteId = new ThreadLocal<>();

    static ThreadLocal<String> threadSafeSrcSiteId = new ThreadLocal<>();

    static int expectedTotalBoxes=0;

    static  ThreadLocal<String>threadSafetxfOrdNumber=new ThreadLocal<>();
    static ThreadLocal<Integer>threadSafeTotalBoxes=new ThreadLocal<>();
    static  ThreadLocal<Integer>threadSafeTransferOrderItemsCount=new ThreadLocal<>();

    //    =====================================================
    public static int gettotalboxescount(){

        return threadSafeTotalBoxes.get();
    }

    public static void settotalboxescount(int totalboxescount){
        CommonUtilities.threadSafeTotalBoxes.set(totalboxescount);
    }

    //====================================================
    public  static  void settxfOrdNumber(String txfOrdNumber)
    {
        CommonUtilities.threadSafetxfOrdNumber.set(txfOrdNumber);
    }

    public  static  String gettxfOrdNumber(String txfOrdNumber)
    {
        return threadSafetxfOrdNumber.get();
    }

    public static int getTransferOrderItemsCount(){

        return threadSafeTransferOrderItemsCount.get();
    }

    public static void setTransferOrderItemsCount(int TransferOrderItemsCount){
        CommonUtilities.threadSafeTransferOrderItemsCount.set(TransferOrderItemsCount);
    }


    //making the ConfirmDeliveryPOJO class Object ThreadSafe for parallel execution

    //making the ConfirmDelivery Response Object ThreadSafe for parallel execution
    public static void setConfirmDeliveryResponse(Response response){
        CommonUtilities.threadSafeConfirmDeliveryResponse.set(response);
    }

    //getting the ThreadSafe ConfirmDelivery Response Object for parallel execution
    public static Response getConfirmDeliveryResponse(){
        return threadSafeConfirmDeliveryResponse.get();
    }

    //making the ConfirmDelivery Response Object ThreadSafe for parallel execution
    public static void setGetConfirmDeliveryResponse(Response response){
        CommonUtilities.threadSafeGetDeliveryResponse.set(response);
    }

    //getting the ThreadSafe ConfirmDelivery Response Object for parallel execution
    public static Response getGetConfirmDeliveryResponse(){
        return threadSafeGetDeliveryResponse.get();
    }

    public static String getDeliveryNumber(){
        return threadSafeDlvNumber.get();
    }

    public static void setDeliveryNumber(String deliveryNumber){
        CommonUtilities.threadSafeDlvNumber.set(deliveryNumber);
    }

    public static String getTruckType(){
        return threadSafeTruckType.get();
    }

    public static void setTruckType(String truckType){
        threadSafeTruckType.set(truckType);
    }

    public static String getAcessToken() {
        return token.get();
    }

    public static void setToken(String setToken) {
        token.set((String) setToken);
    }

    public static void setClientId(String clientId){
        threadSafeClientId.set(clientId);
    }

    public static String getClientId(){
        return threadSafeClientId.get();
    }

    public static void setSiteId(String siteId){
        threadSafeSiteId.set(siteId);
    }

    public static String getSiteId(){
        return threadSafeSiteId.get();
    }

    public static Response getResponseInstance() {
        return response2.get();
    }

    public  static String gettxfOrdNumber()
    {
        return  threadSafetxfOrdNumber.get();
    }

    public static String getWhNumber(){
        return threadSafeWhNumber.get();
    }

    public static void setWhNumber(String whNumber){
        threadSafeWhNumber.set(whNumber);
    }

    public static List<String> getTxfOrdItemSeq(){
        return threadSafeTxfOrdItemSeq.get();
    }

    public static void setTxfOrdItemSeq(List<String> txfOrdItemSeq){
        threadSafeTxfOrdItemSeq.set(txfOrdItemSeq);
    }
//====================================================================================
    public static String getDstSiteId(){

        return threadSafeDstSiteId.get();
    }

    public static void setDstSiteID(String dstSiteID){
        threadSafeDstSiteId.set(dstSiteID);
    }

    public static String getSrcSiteId(){

        return threadSafeSrcSiteId.get();
    }

    public static void setSrcSiteID(String SrcSiteId){
        threadSafeSrcSiteId.set(SrcSiteId);
    }


    //==================================================================================
    public static void setResponseInstance(Response setInstanceResponse) {
        response2.set((Response) setInstanceResponse);
    }

    public static Map getMapRequestPayload() {
        return requestPayload.get();
    }

    public static void setMapRequestPayload(Map mapRequestPayload) {
        requestPayload.set(mapRequestPayload);
    }

    public static Map<String,String> genericHeader() {
        Map<String,String> header = new HashMap<>();
        GenerateAccessTokenSteps generateAccessTokenSteps = new GenerateAccessTokenSteps();
        accessToken = generateAccessTokenSteps.sendUsernameAndPasswordToGetAccessToken();
        CommonUtilities.setToken(accessToken);
        header.put("Authorization", CommonUtilities.getAcessToken());
        return header;
    }

    public static void validateTruckType(List<Map<String,Object>> actualVehicles, String expectedTruckType) {
        for (int i=0;i< actualVehicles.size();i++) {
            String actualVehicleType = (String)actualVehicles.get(i).get("vehicleType");
            if (actualVehicleType.equals(expectedTruckType)) {
                ExtentReportManager.logPassDetails("truckType/vehicleType field is Passed");
                ExtentReportManager.logInfoDetails("Expected truckType/vehicleType is " +expectedTruckType + " and the Actual priceBoard is " +actualVehicleType);
            }
            else{
                ExtentReportManager.logFailureDetails("truckType/vehicleType field is Failed");
                ExtentReportManager.logInfoDetails("Expected truckType/vehicleType is " +expectedTruckType + " and the Actual priceBoard is " +actualVehicleType);
            }
            Assert.assertEquals(actualVehicleType, expectedTruckType);
            break;
        }
    }

    public static void validateDlvItems(List<Map<String, String>> actualDlvItems, List<DeliveryItemsPOJO> expectedDlvItems) {
        for (int i=0;i< actualDlvItems.size();i++) {
            String actualDlvItemNum = actualDlvItems.get(i).get("dlvItemNum");
            String actualPlannedDlvBoxQty = actualDlvItems.get(i).get("plannedDlvBoxQty");
            String actualMinimumBoxes = actualDlvItems.get(i).get("minimumBoxes");
            String expectedDlvItemNum = expectedDlvItems.get(i).getDlvItemNum();
            String expectedPlannedDlvBoxQty = expectedDlvItems.get(i).getPlannedDlvBoxQty();
            String expectedMinimumBoxes = expectedDlvItems.get(i).getMinimumBoxes();

            if (actualDlvItemNum.equals(expectedDlvItemNum) &&
                    actualPlannedDlvBoxQty.equals(expectedPlannedDlvBoxQty) &&
                                actualMinimumBoxes.equals(expectedMinimumBoxes)) {
                ExtentReportManager.logPassDetails("dlvItems field is Passed");
                ExtentReportManager.logInfoDetails("Expected dlvItemNum is " +expectedDlvItemNum + " and the Actual dlvItemNum is " +actualDlvItemNum);
                ExtentReportManager.logInfoDetails("Expected plannedDlvBoxQty is " +expectedPlannedDlvBoxQty + " and the Actual plannedDlvBoxQty is " +actualPlannedDlvBoxQty);
                ExtentReportManager.logInfoDetails("Expected minimumBoxes is " +expectedMinimumBoxes + " and the Actual minimumBoxes is " +actualMinimumBoxes);
            }
            else{
                ExtentReportManager.logPassDetails("dlvItems field is Failed");
                ExtentReportManager.logInfoDetails("Expected dlvItemNum is " +expectedDlvItemNum + " and the Actual dlvItemNum is " +actualDlvItemNum);
                ExtentReportManager.logInfoDetails("Expected plannedDlvBoxQty is " +expectedPlannedDlvBoxQty + " and the Actual plannedDlvBoxQty is " +actualPlannedDlvBoxQty);
                ExtentReportManager.logInfoDetails("Expected minimumBoxes is " +expectedMinimumBoxes + " and the Actual minimumBoxes is " +actualMinimumBoxes);
            }
            Assert.assertEquals(actualDlvItemNum, expectedDlvItemNum);
            Assert.assertEquals(actualPlannedDlvBoxQty, expectedPlannedDlvBoxQty);
            Assert.assertEquals(actualMinimumBoxes, expectedMinimumBoxes);
        }

    }

    public static void validateDlvItemsUsingListMap(List<Map<String, String>> actualDlvItems, List<Map<String, String>> expectedDlvItems) {
        for (int i=0;i< actualDlvItems.size();i++) {
            String actualDlvItemNum = actualDlvItems.get(i).get("dlvItemNum");
            String actualPlannedDlvBoxQty = actualDlvItems.get(i).get("plannedDlvBoxQty");
            String actualMinimumBoxes = actualDlvItems.get(i).get("minimumBoxes");
            String expectedDlvItemNum = expectedDlvItems.get(i).get("dlvItemNum");
            String expectedPlannedDlvBoxQty = expectedDlvItems.get(i).get("plannedDlvBoxQty");
            String expectedMinimumBoxes = expectedDlvItems.get(i).get("minimumBoxes");

            if (actualDlvItemNum.equals(expectedDlvItemNum) &&
                    actualPlannedDlvBoxQty.equals(expectedPlannedDlvBoxQty) &&
                    actualMinimumBoxes.equals(expectedMinimumBoxes)) {
                ExtentReportManager.logPassDetails("dlvItems field is Passed");
                ExtentReportManager.logInfoDetails("Expected dlvItemNum is " +expectedDlvItemNum + " and the Actual dlvItemNum is " +actualDlvItemNum);
                ExtentReportManager.logInfoDetails("Expected plannedDlvBoxQty is " +expectedPlannedDlvBoxQty + " and the Actual plannedDlvBoxQty is " +actualPlannedDlvBoxQty);
                ExtentReportManager.logInfoDetails("Expected minimumBoxes is " +expectedMinimumBoxes + " and the Actual minimumBoxes is " +actualMinimumBoxes);
            }
            else{
                ExtentReportManager.logPassDetails("dlvItems field is Failed");
                ExtentReportManager.logInfoDetails("Expected dlvItemNum is " +expectedDlvItemNum + " and the Actual dlvItemNum is " +actualDlvItemNum);
                ExtentReportManager.logInfoDetails("Expected plannedDlvBoxQty is " +expectedPlannedDlvBoxQty + " and the Actual plannedDlvBoxQty is " +actualPlannedDlvBoxQty);
                ExtentReportManager.logInfoDetails("Expected minimumBoxes is " +expectedMinimumBoxes + " and the Actual minimumBoxes is " +actualMinimumBoxes);
            }
            Assert.assertEquals(actualDlvItemNum, expectedDlvItemNum);
            Assert.assertEquals(actualPlannedDlvBoxQty, expectedPlannedDlvBoxQty);
            Assert.assertEquals(actualMinimumBoxes, expectedMinimumBoxes);
        }

    }


    public static String getAccessTokenFromRequestSpecificaton(RequestSpecification requestSpecification){
        QueryableRequestSpecification header = SpecificationQuerier.query(requestSpecification);
        return header.getHeaders().get("Authorization").toString();
    }


}
