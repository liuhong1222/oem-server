package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.CreUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@Mapper
public interface CreUserMapper extends BaseCreditMapper<CreUser, Integer> {

    /**
     * 获取新注册用户数
     */
    int countNewUser(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<CreUser> selectSelective(CreUser user);

    List<Map> selectMobileListByMobileSet(@Param("userIdSet") Set userIdSet);

    CreUser selectByUserPhone(@Param("userPhone") String userPhone);

    List<Integer> selectUserIdListByAgentId(Map map);

    int selectUserCountByAgentId(@Param("agentId") Long agentId);

    List<Map> findUserIdMobileByMobileAndAgentId(@Param("userPhone") String userPhone, @Param("agentId") Long agentId);

    int getCreUserByUserName(@Param("userName") String userName);
    
    int saveCreUser(CreUser user);
}
