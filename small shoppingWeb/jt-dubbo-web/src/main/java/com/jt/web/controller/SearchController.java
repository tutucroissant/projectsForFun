package com.jt.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jt.dubbo.pojo.Item;
import com.jt.dubbo.service.DubboSearchService;

@Controller
public class SearchController {
	
	
	@Autowired
	private DubboSearchService searchService;
	
	//http://www.jt.com/search.html?q=%E5%8D%8E%E4%B8%BA%E6%89%8B%E6%9C%BA
	@RequestMapping("/search")
	public String search(@RequestParam("q")String keyword,Model model) throws UnsupportedEncodingException{
		
		//处理get乱码
		keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
		List<Item>  itemList = searchService.findItemListByKeyword(keyword);		
		model.addAttribute("query", keyword);
		model.addAttribute("itemList", itemList);
		//返回全文检索页面
		return "search";
	}
}
