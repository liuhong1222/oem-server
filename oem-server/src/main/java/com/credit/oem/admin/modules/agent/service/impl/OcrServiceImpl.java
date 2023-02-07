package com.credit.oem.admin.modules.agent.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.Base64Img;
import com.credit.oem.admin.common.utils.HttpUtils;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.service.OcrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzj
 * @since 2018/8/31
 */
@Service
public class OcrServiceImpl implements OcrService {

    @Value("${wanshuUrl}")
    private String wanshuUrl;

    @Value("${wanshuAppId}")
    private String wanshuAppId;

    @Value("${wanshuAppKey}")
    private String wanshuAppKey;

    /**
     * 识别代理商营业执照URL
     */
    private static final String OCR_AGENT_BIZ_LICENSE_URL = "/qtsb/bus_license";


    /**
     * 识别代理商营业执照
     */
    @Override
    public Map<String, String> agentBizLicenseByFilePath(String filePath, String orderNo) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> param = new HashMap<String, String>();
        String imgBase64Str = Base64Img.GetImageStrFromPath(filePath);
        param.put("image", imgBase64Str);
        param.put("appId", wanshuAppId);
        param.put("appKey", wanshuAppKey);
        param.put("fixMode", "1");
        String url = wanshuUrl + OCR_AGENT_BIZ_LICENSE_URL;
        String result = HttpUtils.post(url, param);
        JSONObject json = JSONObject.parseObject(result);
        if (json != null) {
            if (Constant.HTTP_WS_RESULT_SUCCESS.equals(json.getString("code"))) {
                resultMap = handleInvokeResult(json.getString("data"));
            } else {
                throw new RRException(json.getString("message"));
            }
        } else {
            throw new RRException("服务异常");
        }
        return resultMap;
    }

    private Map<String, String> handleInvokeResult(String dataJsonStr){
    	if(StringUtils.isBlank(dataJsonStr)) {
    		return null;
    	}
    	
    	JSONObject json = JSONObject.parseObject(dataJsonStr);
    	if(!"SUCCESS".equals(json.getString("msg"))) {
    		return null;
    	}
    	
    	JSONObject dataJson = JSONObject.parseObject(json.getString("data"));
    	if(!"normal".equals(dataJson.getString("image_status"))) {
    		return null;
    	}
    	
    	
    	JSONObject temp = JSONObject.parseObject(dataJson.getString("words_result"));
    	Map<String, String> map = new HashMap<>();
    	map.put("name", JSONObject.parseObject(temp.getString("单位名称")).getString("words"));
        map.put("address", JSONObject.parseObject(temp.getString("地址")).getString("words"));
        map.put("person", JSONObject.parseObject(temp.getString("法人")).getString("words"));
        map.put("regNum", JSONObject.parseObject(temp.getString("社会信用代码")).getString("words"));
        map.put("establishDate", JSONObject.parseObject(temp.getString("成立日期")).getString("words").replace("年", "0").replace("月", "0").replace("00", "0").replace("日", ""));
        map.put("validPeriod", JSONObject.parseObject(temp.getString("有效期")).getString("words").replace("年", "0").replace("月", "0").replace("00", "0").replace("日", ""));
        return map;
    }
}
