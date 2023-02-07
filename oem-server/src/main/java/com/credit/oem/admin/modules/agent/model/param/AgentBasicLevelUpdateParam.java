package com.credit.oem.admin.modules.agent.model.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author chenzj
 * @since 2018/8/11
 */
public class AgentBasicLevelUpdateParam extends AgentBasicLevelSaveParam {

    @ApiModelProperty("级别id")
    @NotNull(message = "级别id不能为空")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
