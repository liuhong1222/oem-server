package com.credit.oem.admin.modules.agent;


/**
 * @author zhangx
 * @date 2018/5/18 17:29
 */
public interface BaseCreditMapper<T,U> {
    int deleteByPrimaryKey(U id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(U id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);


}
