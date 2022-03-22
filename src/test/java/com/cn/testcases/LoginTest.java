package com.cn.testcases;

import com.cn.apidefinition.ApiCall;
import com.cn.pojo.CaseData;
import com.cn.util.ExcelUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author hanyan
 * @Description LoginTest
 * @date 2022-03-18 16:13
 */
public class LoginTest {

    @Test
    public void logintest(){
        String test="{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        ApiCall.login(test);

    }
    @DataProvider
    public Object[] datas(){
      return   ExcelUtil.readExcel(0).toArray();
    }
//
//    @Test(dataProvider ="datas" )
//    public void logindata(CaseData caseData){
////        String test=caseData.getInputParams();
////        ApiCall.login(test);
//
//    }



}
