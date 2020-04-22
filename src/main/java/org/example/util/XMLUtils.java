package org.example.util;

import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @ClassName XMLUtils
 * @Description TODO
 * @Date 2019/10/22 19:47
 * @Author wangyong
 * @Version 1.0
 **/
public class XMLUtils {

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    public static String getRequestXml(TreeMap<String, String> parameters)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("mch_id".equalsIgnoreCase(k) || "nonce_str".equalsIgnoreCase(k)
                    || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static String Progress_resultParseXml(String xml) {
        String publicKey = null;
        try {
            StringReader read = new StringReader(xml);
            InputSource source = new InputSource(read);
            SAXBuilder sb = new SAXBuilder();
            org.jdom.Document doc;
            doc = sb.build(source);
            org.jdom.Element root = doc.getRootElement();
            List<org.jdom.Element> list = root.getChildren();
            if (list != null && list.size() > 0) {
                for (org.jdom.Element element : list) {
                    if ("pub_key".equals(element.getName())) {
                        publicKey = element.getText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }
}
