package com.favorite.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtils {

	private static Logger log = LoggerFactory.getLogger(XmlUtils.class);

	/**
	 * xml转对象
	 * 
	 * @param xml
	 * @param cls
	 * @return
	 */
	public static <T> T xml2Object(String xml, Class<T> cls) {

		if (StringUtils.isBlank(xml)) {
			log.error("process xml to {}  Object error;", cls.getName());
			return null;
		}
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(cls);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xml);
			return u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())), cls).getValue();

		} catch (JAXBException e) {
			e.printStackTrace();
			log.error("process xml to " + cls.getName() + " Object error;", e);
		}
		return null;
	}

	/**
	 * 对象转xml字符串(添加了头部信息)
	 *
	 * @param o
	 * @return
	 */
	public static String object2Xml(Object o) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(o.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// 省略xml头信息

			String xmlHeard = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
			StringWriter sw = new StringWriter();
			marshaller.marshal(o, sw);
			log.debug("parse object " + o.getClass().getName() + " to xml string:" + xmlHeard + sw.toString());
			return xmlHeard + sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
			log.error("parse object " + o.getClass().getName() + " to xml string error;" + e.toString());
			return "";
		}
	}

}
