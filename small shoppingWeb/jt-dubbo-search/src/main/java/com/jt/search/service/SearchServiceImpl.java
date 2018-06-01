package com.jt.search.service;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Item;
import com.jt.dubbo.service.DubboSearchService;

public class SearchServiceImpl implements DubboSearchService {
	
	@Autowired
	private HttpSolrServer httpSolrServer;
	
	
	@Override
	public List<Item> findItemListByKeyword(String keyword) {
		
		//创建请求对象
		SolrQuery query = new SolrQuery(keyword);
		query.setStart(0);	//分页操作
		query.setRows(20);
		
		try {
			QueryResponse queryResponse = httpSolrServer.query(query);
			
			List<Item> itemList = queryResponse.getBeans(Item.class);
			
			//由于查询的数据是document这样的对象其中包含属性值field,需要将
			//field中的属性值自动的为item对象赋值.
			return itemList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
}
