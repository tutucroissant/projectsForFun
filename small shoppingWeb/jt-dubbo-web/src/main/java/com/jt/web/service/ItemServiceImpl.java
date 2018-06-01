package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;

@Service
public class ItemServiceImpl implements ItemService{
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private HttpClientService httpClient;

	/*需要查询后台数据 manage.jt.com
	 * 1.定义远程的uri
	 * 2.检测是否含有参数.如果有参数准备Map集合封装
	 * 3.发起http请求.获取返回值json数据
	 * 4.将json串转化为对象
	 */
	@Override
	public Item findItemById(Long itemId) {
		//1.定义uri 使用restFul结构传参
		String uri = 
	"http://manage.jt.com/web/item/findItemById/"+itemId;
		String json = httpClient.doGet(uri); 
		Item item = null;
		try {
			item = objectMapper.readValue(json, Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String uri = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
		String itemDescJSON = httpClient.doGet(uri);
		ItemDesc itemDesc = null;
		try {
			itemDesc = objectMapper.readValue(itemDescJSON,ItemDesc.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
	
	
	
	
	
	
	
	
}
