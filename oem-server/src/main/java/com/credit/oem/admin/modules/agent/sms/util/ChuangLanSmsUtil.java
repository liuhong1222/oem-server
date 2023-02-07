//package com.credit.oem.admin.modules.agent.sms.util;
//
//import com.alibaba.fastjson.JSON;
//import com.credit.oem.admin.modules.agent.sms.request.SmsSendRequest;
//import com.credit.oem.admin.modules.agent.sms.response.SmsSendResponse;
//
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//
///**
// *
// * @author tianyh
// * @Description:HTTP 请求
// */
//public class ChuangLanSmsUtil {
//
//	private static ChuangLanSmsUtil chuangLanSmsUtil;
//
//	public static ChuangLanSmsUtil getInstance() {
//        if (chuangLanSmsUtil == null) {
//            synchronized (ChuangLanSmsUtil.class) {
//               if (chuangLanSmsUtil == null) {
//            	   chuangLanSmsUtil = new ChuangLanSmsUtil();
//               }
//            }
//        }
//        return chuangLanSmsUtil;
//    }
//
//	// 用户平台API账号(非登录账号,示例:N1234567)
//	public static String ACCOUNT = "N9229578";
//		// 用户平台API密码(非登录密码)
//	public static String PSWD = "74kGi3wge";
//
//
//
//	/**
//	 *
//	 * @author tianyh @Description @param path @param postContent @return
//	 * String @throws
//	 */
//	public String sendSmsByPost(String path, String postContent) {
//		URL url = null;
//		try {
//			url = new URL(path);
//			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//			httpURLConnection.setRequestMethod("POST");// 提交模式
//			httpURLConnection.setConnectTimeout(10000);// 连接超时 单位毫秒
//			httpURLConnection.setReadTimeout(10000);// 读取超时 单位毫秒
//			// 发送POST请求必须设置如下两行
//			httpURLConnection.setDoOutput(true);
//			httpURLConnection.setDoInput(true);
//			httpURLConnection.setRequestProperty("Charset", "UTF-8");
//			httpURLConnection.setRequestProperty("Content-Type", "application/json");
//
//			httpURLConnection.connect();
//			OutputStream os = httpURLConnection.getOutputStream();
//			os.write(postContent.getBytes("UTF-8"));
//			os.flush();
//
//			StringBuilder sb = new StringBuilder();
//			int httpRspCode = httpURLConnection.getResponseCode();
//			if (httpRspCode == HttpURLConnection.HTTP_OK) {
//				// 开始获取数据
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
//				String line = null;
//				while ((line = br.readLine()) != null) {
//					sb.append(line);
//				}
//				br.close();
//				return sb.toString();
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 用户余额不足时发送短信提醒
//	 */
//	public Boolean sendSmsByAccountRemain(String mobile,int productCount, int warningCount,String name) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "【创蓝数据应用中心】尊敬的客户您好：截止到您的" + name + "余额为：" + productCount + "条,不足" + warningCount + "条,为了不影响业务，请及时充值！";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JPC");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 注册验证码发送短信
//	 * @param mobile
//	 * @param code
//	 * @return
//	 */
//	public Boolean sendSmsByMobile(String mobile,String code) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "【创蓝数据应用中心】您的验证码是：" + code;
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("DL");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 代理商注册验证码发送短信
//	 * @param mobile
//	 * @param code
//	 * @return
//	 */
//	public Boolean sendSmsByMobileToDls(String mobile,String code,String sign) {
//		if(sign!=null && !"".equals(sign)){
//			sign = "【" + sign + "】";
//		}
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = sign + "您的验证码是：" + code;
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("DL");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * API充值成功发送短信
//	 * @param mobile
//	 * @param number
//	 * @return
//	 */
//	public Boolean sendSmsByApiRecharge(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "尊敬的客户您好：您本次充值的Api商品次数己到账，请登陆data.253.com查看";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 充值成功发送短信
//	 * @param mobile
//	 * @param number
//	 * @return
//	 */
//	public Boolean sendSmsByMobileForRecharge(String mobile,Integer number) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "尊敬的客户您好：您本次成功冲值" + number + "条己到账，请登陆data.253.com查看";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("CZ");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 检测成功发送短信
//	 */
//	public Boolean sendSmsByMobileForTest(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "尊敬的客户您好：您的实号检测报告己完成，请登陆data.253.com查收";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JPC");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 检测成功发送短信
//	 */
//	public Boolean sendSmsByMobileForZZTTest(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		//
//		String msg = "【创蓝数据应用中心】尊敬的客户您好：您的实号检测报告己完成，请登陆zz.253.com查收";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JZT");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 代理商检测成功发送短信
//	 */
//	public Boolean sendSmsByMobileForDlsTest(String mobile,String sign,String url) {
//		if(sign!=null && !"".equals(sign)){
//			sign = "【" + sign + "】";
//		}
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = sign +"尊敬的客户您好：您的实号检测报告己完成，请登陆" + url + "查收";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JZT");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 二次检测剩余条数不足
//	 */
//	public Boolean sendSmsByMobileForTwoTest(String mobile,Integer number) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "尊敬的客户您好：您的账户二次清洗服务剩佘条数己低于"+number+"条，请注意冲值";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
//		smsSingleRequest.setExtend("RTS");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 检测异常发送短信
//	 */
//	public Boolean sendSmsByMobileForTestEx(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "尊敬的用户您好，非常抱歉的通知您，由于系统原因，您本次实号检测业务未能正常进行，请登陆data.253.com 重新检测，本次检测不扣费，如有疑问请咨询页面客服";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JYC");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 检测失败发送短信
//	 */
//	public Boolean sendSmsByMobileForTestZZtEx(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "【创蓝数据应用中心】尊敬的用户您好，非常抱歉的通知您，由于系统原因，您本次实号检测业务未能正常进行，请登陆zz.253.com 重新检测，本次检测不扣费，如有疑问请咨询页面客服";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JYC");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 代理商检测失败发送短信
//	 */
//	public Boolean sendSmsByMobileForTestDlsEx(String mobile,String sign,String url) {
//		if(sign!=null && !"".equals(sign)){
//			sign = "【" + sign + "】";
//		}
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = sign + "尊敬的用户您好，非常抱歉的通知您，由于系统原因，您本次实号检测业务未能正常进行，请登陆" + url + "重新检测，本次检测不扣费，如有疑问请咨询页面客服";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PSWD, msg, phone, report);
////		smsSingleRequest.setExtend("JYC");
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 账户二次清洗 验证手机号码是否为真实手机号码
//	 */
//	public Boolean sendSmsByMobileForTwoTest(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest("M0066451", "cmVAf5eSNFda60", msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//	/**
//	 * 账户二次清洗发营销短信
//	 * @param mobile
//	 * @param code
//	 * @return
//	 */
//	public Boolean sendYxByMobile(String mobile) {
//
//		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
//		// 短信内容
//		String msg = "【太子龙】冬款男装特价促销，原价1260元，现二件298元，今晚20:00开抢。点 http://1-tmall.com/goods/dk5?hmsr=ad_konghao&hmpl=253&hmcu= 抢购买一送二货到付款.退订TD";
//		// 手机号码
//		String phone = mobile;
//		// 状态报告
//		String report = "true";
//
//		SmsSendRequest smsSingleRequest = new SmsSendRequest("M0066451", "cmVAf5eSNFda60", msg, phone, report);
//
//		String requestJson = JSON.toJSONString(smsSingleRequest);
//
//		String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//
//		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//
//		return smsSingleResponse.getCode().equals("0") ? true : false;
//	}
//
//
//
////	账号：M0066451
////	密码：cmVAf5eSNFda60
//
//
//
//	public static void main(String[] args) {
//		ChuangLanSmsUtil.getInstance().sendYxByMobile("13817367247");
//	}
//
//}
