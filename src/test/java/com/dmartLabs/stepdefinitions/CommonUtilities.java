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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CommonUtilities implements ConStants {
    RequestGenerator requestGenerator = new RequestGenerator();
    public static String accessToken;
    Response response;
    static ThreadLocal<Response> response2 = new ThreadLocal<>();
    static ThreadLocal<Response>ThreadGetpoResponse=new ThreadLocal<>();

    static ThreadLocal< List<LinkedHashMap<String,Object>>> threadSafeResponseItemsList = new ThreadLocal<>();
    static ThreadLocal< LinkedHashMap<String,Object>> threadSafeCreatePoMAp = new ThreadLocal<>();


    static ThreadLocal<Response> threadSafeGetDeliveryResponse = new ThreadLocal<>();
    static ThreadLocal<Map> requestPayload = new ThreadLocal<>();
    static ThreadLocal<String> token = new ThreadLocal<>();
    static ThreadLocal<String> threadsafeSuppliercode = new ThreadLocal<>();

    static ThreadLocal<String> threadSafeDlvNumber = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeTruckType = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeClientId = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeSiteId = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeWhNumber = new ThreadLocal<>();
    static ThreadLocal<List<String>> threadSafeTxfOrdItemSeq = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeDstSiteId = new ThreadLocal<>();
    static ThreadLocal<String> threadSafeSrcSiteId = new ThreadLocal<>();
    static  ThreadLocal<List<String>>threadSafeCalenderResponse=new ThreadLocal<>();
    static  ThreadLocal<LinkedHashMap<String,List<LinkedHashMap<String,Object>>>>threadPonumbersBody=new ThreadLocal<>();

    //   public static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> ponumbersbody = new LinkedHashMap<>();


    static int expectedTotalBoxes=0;

    static  ThreadLocal<String>threadSafePONumber=new ThreadLocal<>();

    static ThreadLocal<Integer>threadSafeTotalBoxes=new ThreadLocal<>();
    static  ThreadLocal<Integer>threadSafeTransferOrderItemsCount=new ThreadLocal<>();

    //    =====================================================

    public static String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath js = new JsonPath(resp);
        return js.get(key).toString();
    }


    public static int gettotalboxescount(){

        return threadSafeTotalBoxes.get();
    }

    public static void settotalboxescount(int totalboxescount){
        CommonUtilities.threadSafeTotalBoxes.set(totalboxescount);
    }

    //====================================================

    public static int getTransferOrderItemsCount(){

        return threadSafeTransferOrderItemsCount.get();
    }

    public static void setTransferOrderItemsCount(int TransferOrderItemsCount){
        CommonUtilities.threadSafeTransferOrderItemsCount.set(TransferOrderItemsCount);
    }


    //making the ConfirmDeliveryPOJO class Object ThreadSafe for parallel execution

    //making the ConfirmDelivery Response Object ThreadSafe for parallel execution
//    public static void setConfirmDeliveryResponse(Response response){
//        CommonUtilities.threadSafeConfirmDeliveryResponse.set(response);
//    }
//
//    //getting the ThreadSafe ConfirmDelivery Response Object for parallel execution
//    public static Response getConfirmDeliveryResponse(){
//        return threadSafeConfirmDeliveryResponse.get();
//    }
//
//    //making the ConfirmDelivery Response Object ThreadSafe for parallel execution
//    public static void setGetConfirmDeliveryResponse(Response response){
//        CommonUtilities.threadSafeGetDeliveryResponse.set(response);
//    }

    //getting the ThreadSafe ConfirmDelivery Response Object for parallel execution
    public static Response getGetConfirmDeliveryResponse(){
        return threadSafeGetDeliveryResponse.get();
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



    public static Response getResponseInstance() {
        return response2.get();
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


    public  static LinkedHashMap<String,List<LinkedHashMap<String,Object>>>getPonumbersBody()
    {
        return  threadPonumbersBody.get();
    }
    public static  void setPonumbersBody(LinkedHashMap<String,List<LinkedHashMap<String,Object>>>PonumbersBody)
    {
        threadPonumbersBody.set(PonumbersBody);
    }

    //threadPonumbersBody

//==================================================================================
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

    public static void setpoResponse(Response GetpoResponse) {
        ThreadGetpoResponse.set((Response) GetpoResponse);
    }
    public  static  Response getpoResponse()
    {

        return ThreadGetpoResponse.get();
    }

    //======================================================================
public static void setcalenderResponseList(List<String> CalenderResponse){

    threadSafeCalenderResponse.set(CalenderResponse);
}

    public static List<String> getcalenderResponseList(){

        return threadSafeCalenderResponse.get();
    }


    //=======================================================================
    public static void setConfirmmAppointmentResponseInstance(Response setInstanceResponse) {
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
    public static String getAccessTokenFromRequestSpecificaton(RequestSpecification requestSpecification){
        QueryableRequestSpecification header = SpecificationQuerier.query(requestSpecification);
        return header.getHeaders().get("Authorization").toString();
    }

    //===================================================================================================
    //PO
    public  static  void setPONumber(String PONumber)
    {
        CommonUtilities.threadSafePONumber.set(PONumber);
    }

    public  static  String getPONumber()
    {

        return threadSafePONumber.get();
    }
//====================================================
   // threadsafeSuppliercode

    public  static  void setSuppliercode(String Suppliercode)
    {
        CommonUtilities.threadsafeSuppliercode.set(Suppliercode);
    }

    public  static  String getSuppliercode()
    {

        return threadsafeSuppliercode.get();
    }



    public static void setSiteId(String siteId){
        threadSafeSiteId.set(siteId);
    }

    public static String getSiteId(){
        return threadSafeSiteId.get();
    }


    public static String getDeliveryNumber(){
        return threadSafeDlvNumber.get();
    }

    public static void setDeliveryNumber(String deliveryNumber){
        CommonUtilities.threadSafeDlvNumber.set(deliveryNumber);
    }
//    List<LinkedHashMap<String,Object>>ResponseItemsList= (List<LinkedHashMap<String, Object>>) GetporesponseValidate.get("items");

    public static List<LinkedHashMap<String,Object>>  getResponseItemsList(){

        return threadSafeResponseItemsList.get();
    }

    //making the ConfirmDelivery Response Object ThreadSafe for parallel execution
    public static void setResponseItemsList(List<LinkedHashMap<String,Object>> ResponseItemsList){
        CommonUtilities.threadSafeResponseItemsList.set(ResponseItemsList);
    }


    public static LinkedHashMap<String,Object> getCreatePoMAp(){

        return threadSafeCreatePoMAp.get();
    }

    //making the ConfirmDelivery Response Object ThreadSafe for parallel execution
    public static void setPOResponseMap(LinkedHashMap<String,Object>CreatePoMAp){
        CommonUtilities.threadSafeCreatePoMAp.set(CreatePoMAp);
    }
}
