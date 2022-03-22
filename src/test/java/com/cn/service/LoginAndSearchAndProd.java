package com.cn.service;


import com.cn.apidefinition.ApiCall;
import com.cn.util.Environment;
import io.restassured.response.Response;

/**
 * @author hanyan
 * @Description LoginAndSearchAndProd
 * @date 2022-03-18 16:41
 */
public class LoginAndSearchAndProd {
    public static Response loginsuccseprod(){
        String test="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        Response login = ApiCall.login(test);
        String  token = login.jsonPath().get("s.d");
        Environment.saveToEnvironment("token",token);
        String test1="prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response response = ApiCall.searchProdPage(test1);
        int prodId = response.jsonPath().get("records[0].prodId");
        Environment.saveToEnvironment("prodId",prodId);
        //商品信息
        Response response1 = ApiCall.prodId(prodId);
        return response1;

    }



}
