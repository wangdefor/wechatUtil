package org.example.util;

/**
 * @ClassName SignUtils
 * @Description SignUtils
 * @Date 2019/10/22 19:55
 * @Author wangyong
 * @Version 1.0
 **/

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class SignUtils {

    /**
     * @param key 签名key值  apiKey
     */
    public static String creatSign(SortedMap<Object, Object> parameters,String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MD5.MD5Encode(sb.toString()).toUpperCase();
        System.out.println(sign);
        return sign;
    }
}
