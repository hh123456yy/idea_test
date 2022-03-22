package com.cn.testcases;

import com.cn.apidefinition.ApiCall;
import com.cn.common.BaseTest;
import com.cn.pojo.CaseData;
import com.cn.util.ExcelUtil;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author hanyan
 * @Description SearchTest
 * @date 2022-03-18 16:25
 */
public class SearchTest extends BaseTest {

    @Test(dataProvider = "datas")
    public void searchgoods(CaseData caseData){
        String test="prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response response = ApiCall.searchProdPage(test);

        assertResponse(caseData.getAssertResponse(),response);
    }
    @DataProvider
    public Object[] datas(){
        List<CaseData> caseData = ExcelUtil.readExcel(0);
        return caseData.toArray();
    }

}
