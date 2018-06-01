package com.jt.web.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.jt.common.po.BasePojo;
public class ItemDesc extends BasePojo{
	private Long itemId;	//应该与Item表中的id一致 一对一
	private String itemDesc;	//商品的描述信息
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
}
