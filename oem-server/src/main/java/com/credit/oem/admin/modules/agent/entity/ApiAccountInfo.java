package com.credit.oem.admin.modules.agent.entity;

import java.io.Serializable;
import java.util.Date;

public class ApiAccountInfo implements Serializable{

	private static final long serialVersionUID = 6885630934292305018L;

	private Integer id;
	
	private Integer creUserId;
	
	private String userName;
	
	private String password;
	  	
	private String bdIp;
		
	private String resultPwd;
	
	private String salt;
	
	private Integer flag;
	
	private Integer version;
	
	private Date createTime;
	
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreUserId() {
		return creUserId;
	}

	public void setCreUserId(Integer creUserId) {
		this.creUserId = creUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBdIp() {
		return bdIp;
	}

	public void setBdIp(String bdIp) {
		this.bdIp = bdIp;
	}

	public String getResultPwd() {
		return resultPwd;
	}

	public void setResultPwd(String resultPwd) {
		this.resultPwd = resultPwd;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
