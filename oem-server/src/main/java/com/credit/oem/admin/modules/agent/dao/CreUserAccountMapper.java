package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.ApiAccountInfo;
import com.credit.oem.admin.modules.agent.entity.CreUserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface CreUserAccountMapper extends BaseCreditMapper<CreUserAccount, Integer> {

    /**
     * 获取空号文件检查剩余条数
     */
    BigDecimal sumEmptyFileBalance();

    List<CreUserAccount> selectByCreUserId(Integer userId);

    CreUserAccount selectNormalByCreUserId(Integer userId);

    int addNumber(CreUserAccount addAccount);
    
    int saveCreUserAccount(CreUserAccount creUserAccount);
    
    int saveApiAccountInfo(ApiAccountInfo apiAccountInfo);
}
