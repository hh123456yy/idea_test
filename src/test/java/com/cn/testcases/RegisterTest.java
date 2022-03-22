package com.cn.testcases;

import com.cn.apidefinition.ApiCall;
import com.cn.common.BaseTest;
import com.cn.util.Environment;
import com.cn.util.JDBCUtils;
import com.cn.util.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * @author hanyan
 * @Description RegisterTests
 * @date 2022-03-18 23:05
 */
public class RegisterTest extends BaseTest {

    public void register(){
        //获取随机的数据
        String phonenum= RandomDataUtil.getUnregisterPhone();
        String name=RandomDataUtil.getUnregisterName();
        Environment.saveToEnvironment("phone",phonenum);
        Environment.saveToEnvironment("name",name);
        String test="{\"mobile\":\"#phone#\"}";
        Response response = ApiCall.sendRegisterSms(test);
        String sql="SELECT mobile_code from tz_sms_log where id = (SELECT MAX(id) FROM tz_sms_log);";
        String code = (String) JDBCUtils.querySingleData(sql);

        Environment.saveToEnvironment("code",code);
        String check="{\"mobile\":\"#phone#\",\"validCode\":\"#code#\"}";
        Response response1 = ApiCall.checkRegisterSms(check);
//        拿到接口响应纯文本类型的数据
        String checkSms = response1.body().asString();
        //将验证码校验字符串保存到环境变量中
        Environment.saveToEnvironment("checkSms",checkSms);
        String data03 = "{\"appType\":3,\"checkRegisterSmsFlag\":\"#checkSms#\"," +
                "\"mobile\":\"#phone#\",\"userName\":\"#name#\"," +
                "\"password\":\"123456\",\"registerOrBind\":1,\"validateType\":1}";
        Response response2 = ApiCall.registerOrBindUser(data03);
        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.jsonPath().get("name"),"");
        //数据库断言

        String assertSql = "SELECT COUNT(*) FROM tz_user WHERE user_mobile='#randomPhone#';";
        long o = (long)JDBCUtils.querySingleData(assertSql);
        Assert.assertEquals(0,1);


    }

}
