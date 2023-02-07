package com.credit.oem.admin.common.utils;

import java.util.UUID;

/**
 * @author songb
 * @since  2018年6月4日
 *
 */
public class Locker {

	/**
	 * key name
	 */
	private String name;
	
	/*
	 * thread id
	 */
	private String value;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	
	public Locker(String name) {
		this.name = name;
		this.value = UUID.randomUUID().toString();
	}
	
	
}
