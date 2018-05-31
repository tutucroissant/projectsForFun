package com.wy.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
 *	该类放一些与服务器相关的参数
 *	200,404,500；s
 *	端口号port，线程池含量MAX_Thread
 *	解析XML文件，并存放在中间
 */
public class CommonChar {
	static public String htmlUrl;
	static public int MAX_Thread;
	static public int PORT;
	static public String NowProtocol;
	public final static int StATUS_OK = 200;
	public final static int StATUS_NOT_FOUND = 404;
	public final static int StATUS_SERVER_ERROR = 500;
	public static Map<String, String> typesMap;
	private static List<Element> typemappingsList;
	static{
		typesMap = new HashMap<String, String>();
		init();
	}
	private static void init() {//读取XML（protocol）文件，并输入到map中
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read("config/config.xml");
			Element serverRoot = doc.getRootElement();
			//解析connector标签
			Element connector = 
					serverRoot.element("service").element("connector");
			NowProtocol = connector.attributeValue("protpcol");
			PORT = Integer.parseInt(
					connector.attributeValue("port"));
			MAX_Thread = Integer.parseInt(
					connector.attributeValue("MaxThread"));
			htmlUrl = serverRoot.element("service").elementText("webroot");
			System.out.println("NowProtocol = "+NowProtocol+";PORT = "+PORT+
					";MAX_Thread = "+MAX_Thread+";htmlUrl = "+htmlUrl);
			typemappingsList = serverRoot.element(
					"type-mappings").elements("type-mapping");
			for(Element element : typemappingsList)
			{
				String suffix = element.attributeValue("suffix");
				String type = element.attributeValue("type");
				typesMap.put(suffix, type);
				System.out.println(suffix+"--"+type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
