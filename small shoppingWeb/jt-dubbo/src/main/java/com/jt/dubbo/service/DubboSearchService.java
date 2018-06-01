package com.jt.dubbo.service;

import java.util.List;

import com.jt.dubbo.pojo.Item;

public interface DubboSearchService {
	//根据关键字查询数据信息
	List<Item> findItemListByKeyword(String keyword);
}
