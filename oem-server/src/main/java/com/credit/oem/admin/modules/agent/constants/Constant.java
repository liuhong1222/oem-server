package com.credit.oem.admin.modules.agent.constants;

/**
 * @author chenzj
 * @since 2018/8/9
 */
public class Constant {
    public static final Integer MAX_PAGE_SIZE = 1048570;

    //代理商能否升级登记
    public static final String AGENT_LEVEL_CAN_UPGRADE_NAME="能";
    public static final String AGENT_LEVEL_CAN_NOT_UPGRADE_NAME="否";

    //代理商状态
    public static final String AGENT_STATUS_ENABLE="使用中";
    public static final String AGENT_STATUS_DISABLE="已禁用";

    //通用状态值
    public static final Integer COMMON_NORMAL_STATUS_VALUE = 1;

    //代理商禁用状态
    public static final Integer COMMON_DISABLED_STATUS_VALUE = 0;

    //代理商删除状态
    public static final Integer COMMON_DELETED_STATUS_VALUE = 2;

    //默认空号预警条数
    public static final Long DEFAULT_AGENT_EMPTY_WARN_NUMBER=2000L;

    //管理员角色
    public static final Long ADMIN_ROLE_ID=1L;
    //代理商角色
    public static final Long AGENT_ROLE_ID=2L;


    //代理商主账号角色
    public static final Integer MASTER_SYS_USER_FLAG=0;
    //代理商副账号角色
    public static final Integer SLAVER_SYS_USER_FLAG=1;

    //代理商充值  充值
    public static final Integer AGENT_ORDER_TYPE_RECHARGE=1;
    //代理商充值  退款
    public static final Integer AGENT_ORDER_TYPE_REFUND=2;
    //代理商给用户充值
    public static final Integer AGENT_ORDER_TYPE_RECHARGE_CUST=3;

    //代理商充值状态  待处理
    public static final Integer AGENT_ORDER_STATUS_TODO=0;
    //代理商充值状态  成功
    public static final Integer AGENT_ORDER_STATUS_SUCCESS=1;
    public static final Integer AGENT_ORDER_STATUS_CHECK=3;
    public static final Integer AGENT_ORDER_STATUS_RESUFED=4;
    //代理商充值状态  失败
    public static final Integer AGENT_ORDER_STATUS_FAILURE=2;

    //代理商充值操作的角色  代理商
    public static final Integer AGENT_ORDER_ROLE_TYPE_AGENT=0;
    //代理商充值操作的角色  管理员
    public static final Integer AGENT_ORDER_ROLE_TYPE_ADMIN=1;


    //空号检查服务成功码
    public static final String HTTP_UNN_RESULT_SUCCESS="000000";
    
    //空号检查服务成功码
    public static final String HTTP_WS_RESULT_SUCCESS="200000";
    
    public static final String CHECK_RESULT_PASS="1";
    public static final String CHECK_RESULT_REFUSED="0";

}
