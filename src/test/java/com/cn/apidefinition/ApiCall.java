package com.cn.apidefinition;


import com.cn.common.GlobalConfig;
import com.cn.util.Environment;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @author hanyan
 * @Description ApiCall
 * @date 2022-03-18 15:35
 */
public class ApiCall {
    //封装统一方法
   public static Response request(String method, String url, Map<String,Object> headersMap, String inputParams){
       //每个接口请求的日志单独的保存到本地的每一个文件中(重定向)
       String logFilePath=null;
       if(!GlobalConfig.IS_DEBUG) {
           PrintStream fileOutPutStream = null;
           //设置日志文件的地址
           String logFileDir = "target/log/";
           File file = new File(logFileDir);
           if (!file.exists()) {
               file.mkdirs();
           }
           logFilePath = logFileDir + "test_" + System.currentTimeMillis() + ".log";
           try {
               fileOutPutStream = new PrintStream(new File(logFilePath));
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
           RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
       }
       //参数化替换
       //1、接口入参做参数化替换
       inputParams = Environment.replaceParams(inputParams);
       //2、接口请求头参数化替换
       headersMap = Environment.replaceParams(headersMap);
       //3、接口请求地址参数化替换
       url = Environment.replaceParams(url);
       Response res=null;
       if("get".equalsIgnoreCase(method)){
           res = given().log().all().headers(headersMap).when().get(url+"?"+inputParams).then().log().all().extract().response();
       }else if("post".equalsIgnoreCase(method)){
           res = given().log().all().headers(headersMap).body(inputParams).when().post(url).then().log().all().extract().response();
       }else if("put".equalsIgnoreCase(method)){
           res = given().log().all().headers(headersMap).body(inputParams).when().put(url).then().log().all().extract().response();
       }else if("delete".equalsIgnoreCase(method)){
           //TODO
       }else {
           System.out.println("接口请求方法非法，请检查你的请求方法");
       }

       //添加日志信息到Allure报表中
       if(!GlobalConfig.IS_DEBUG) {
           try {
               Allure.addAttachment("接口的请求/响应信息", new FileInputStream(logFilePath));
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
       }
       return res;


   }

   //登录方法
  public static Response login(String test){
       Map<String,Object> headr=new HashMap<>();
       headr.put("application/json","charset=UTF-8");
      Response post = request("post", "http://mall.lemonban.com:8107/login", headr, test);
      return post;
  }

 //搜素方法
    public static Response searchProdPage(String test){
        Map<String,Object> headr=new HashMap<>();
        headr.put("application/json","charset=UTF-8");
        Response get = request("get", "http://mall.lemonban.com:8107/search/searchProdPage", headr, test);
        return get;
    }
//选择商品
    public static Response prodId(int  test){
       Map<String ,Object> heard=new HashMap<>();
       heard.put("application/json","charset=UTF-8");
        Response get = request("get", "http://mall.lemonban.com:8107/prod/prodInfo", heard, "prodId="+test);
        return get;

    }
    //添加购物侧
    public static  Response shopCart(String test,String token){
        Map<String ,Object> heard=new HashMap<>();
        heard.put("application/json","charset=UTF-8");
        heard.put("Authorization","bearer"+token);
        Response post = request("post", "http://mall.lemonban.com:8107/p/shopCart/changeItem", heard, test);
        return post;
    }

   //注册验证码发送
    public static Response sendRegisterSms(String test){
        Map<String ,Object> heard=new HashMap<>();
        heard.put("application/json","charset=UTF-8");
        Response put = request("put", "http://mall.lemonban.com:8107/user/sendRegisterSms", heard, test);
        return put;
    }
    //注册验证码请求
    public static Response checkRegisterSms(String test){
        Map<String ,Object> heard=new HashMap<>();
        heard.put("application/json","charset=UTF-8");
        Response put = request("put", "http://mall.lemonban.com:8107/user/checkRegisterSms", heard, test);
        return put;
    }
    //注册请求接口
    public static Response registerOrBindUser(String test){
        Map<String ,Object> heard=new HashMap<>();
        heard.put("application/json","charset=UTF-8");
        Response put = request("put", "http://mall.lemonban.com:8107/user/registerOrBindUser", heard, test);
        return put;
    }
    /**
     * 确认订单接口定义
     * @param inputParams 接口入参
     * {"addrId":0,"orderItem":{"prodId":412,"skuId":766,"prodCount":1,"shopId":1},"couponIds":[],"isScorePay":0,"userChangeCoupon":0,"userUseScore":0,"uuid":"c3b16d57-6683-4ad2-8bc6-7aeee5e79936"}
     * @param token
     * @return
     */
    public static Response confirmOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/confirm",headMap,inputParams);
    }

    /**
     * 提交订单接口定义
     * @param inputParams 接口入参
     * {"orderShopParam":[{"remarks":"","shopId":1}],"uuid":"c3b16d57-6683-4ad2-8bc6-7aeee5e79936"}
     * @param token
     * @return
     */
    public static Response submitOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/submit",headMap,inputParams);
    }

    /**
     * 支付下单接口定义
     * @param inputParams 接口入参
     * {"payType":3,"orderNumbers":"1481249684885606400"}
     * @param token
     * @return
     */
    public static Response placeOrder(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/p/order/pay",headMap,inputParams);
    }

    /**
     * 模拟支付回调的接口
     * @param inputParams 接口入参
     *{
     *    "payNo":1470015941797744640, #商城支付订单号
     *    "bizPayNo":XXXX, #微信方的订单号
     *    "isPaySuccess":true,#true成功，false失败
     * }
     * @param token
     * @return
     */
    public static Response mockPay(String inputParams,String token){
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","bearer"+token);
        return request("POST","/notice/pay/3",headMap,inputParams);
    }


}
