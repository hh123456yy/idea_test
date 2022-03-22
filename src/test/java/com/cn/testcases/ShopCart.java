package com.cn.testcases;

import com.cn.apidefinition.ApiCall;
import com.cn.common.BaseTest;
import com.cn.pojo.CaseData;
import com.cn.service.LoginAndSearchAndProd;
import com.cn.util.Environment;
import com.cn.util.ExcelUtil;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author hanyan
 * @Description ShopCart
 * @date 2022-03-18 16:36
 */
public class ShopCart extends BaseTest {


    //不用数据源
    @Test
    public void sopcart(CaseData caseData){


        Response loginsuccseprod = LoginAndSearchAndProd.loginsuccseprod();

        int skuId = loginsuccseprod.jsonPath().get("skuList[0].skuId");
        Environment.saveToEnvironment("skuId",skuId);
        String shopCartData="{\"basketId\":0,\"count\":1,\"prodId\":\"#prodId#\",\"shopId\":1,\"skuId\":#skuId#}";
        ApiCall.shopCart(shopCartData,"#token#");

    }
    //用数据源
    @Test(dataProvider = "shopCart")
    public void shopcart(CaseData caseData){
        String test="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        Response login = ApiCall.login(test);
        String token=login.jsonPath().get("");
        String inputParams = caseData.getInputParams();
        Response response = ApiCall.shopCart(inputParams, token);
        assertResponse(caseData.getAssertResponse(),response);
        //数据库断言
        assertDB(caseData.getAssertDB());

    }


    @DataProvider
    public Object[] shopCart(){
        List<CaseData> caseData = ExcelUtil.readExcel(2);
        return caseData.toArray();
    }
}
