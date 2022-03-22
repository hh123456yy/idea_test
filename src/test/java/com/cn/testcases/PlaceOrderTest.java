package com.cn.testcases;

import com.cn.apidefinition.ApiCall;
import com.cn.common.BaseTest;
import com.cn.service.LoginAndSearchAndProd;
import com.cn.util.Environment;
import com.cn.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * @author hanyan
 * @Description PlaceOrderTest
 * @date 2022-03-18 23:32
 */
public class PlaceOrderTest extends BaseTest {
    public void test_place_roder_success(){
        Response loginsuccseprod = LoginAndSearchAndProd.loginsuccseprod();
        String o = loginsuccseprod.jsonPath().get("skuList[0].skuId");

        //1、确认订单
        String confirmDatas = "{\"addrId\":0,\"orderItem\":{\"prodId\":#prodId#,\"skuId\":#skuId#," +
                "\"prodCount\":1,\"shopId\":1},\"couponIds\":[],\"isScorePay\":0," +
                "\"userChangeCoupon\":0,\"userUseScore\":0," +
                "\"uuid\":\"c3b16d57-6683-4ad2-8bc6-7aeee5e79936\"}";
        Response response = ApiCall.confirmOrder(confirmDatas, "#token#");
        //获取订单号
        String orderNumbers = response.jsonPath().get("orderNumbers");
        Environment.saveToEnvironment("orderNumbers",orderNumbers);

        //下单
        String placeOrderData = "{\"payType\":3,\"orderNumbers\":\"#orderNumbers#\"}";
        Response placeOrderRes = ApiCall.placeOrder(placeOrderData,"#token#");
        //4、模拟回调 --模拟真实的支付流程  (目前20220112有bug，明天会修复)
        String mockPayData="{\n" +
                "    \"payNo\":#orderNumbers#, \n" +
                "    \"bizPayNo\":\"XXXX\",\n" +
                "    \"isPaySuccess\":true\n" +
                "}";
        Response mockPayRes = ApiCall.mockPay(mockPayData,"#token#");
        //1、响应断言
        //提取纯文本响应体数据
        String actual = mockPayRes.body().asString();
        Assert.assertEquals(actual,"success");
        //2、数据库断言
        String sql = "SELECT status FROM tz_order WHERE order_number=#orderNumbers#;";
        Object actualDB = JDBCUtils.querySingleData(sql);
        System.out.println(actualDB.getClass());
        Assert.assertEquals(actualDB,2);
    }

}
