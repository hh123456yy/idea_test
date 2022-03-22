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
 * @Description ProdTest
 * @date 2022-03-18 16:30
 */
public class ProdTest extends BaseTest {
    @DataProvider
    public Object[] datas(){
        List<CaseData> caseData = ExcelUtil.readExcel(1);
        return caseData.toArray();
    }

    @Test(dataProvider = "datas")
    public void prodId(CaseData caseData){
//        Response response = ApiCall.prodId(caseData.getInputParams());
//        assertResponse(caseData.getInputParams(),response);
    }


}
