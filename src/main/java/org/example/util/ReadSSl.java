package org.example.util;


import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * @ClassName ReadSSl
 * @Description ReadSSl
 * @Date 2019/10/22 19:46
 * @Author wangyong
 * @Version 1.0
 **/
public class ReadSSl {

    private volatile static ReadSSl readSSL = null;

    private ReadSSl() {

    }

    /**
     * double check的单例模式
     * @return
     */
    public static ReadSSl getInstance() {
        if (readSSL == null) {
            synchronized (ReadSSl.class){
                if(readSSL == null){
                    readSSL = new ReadSSl();
                }
            }
        }
        return readSSL;
    }

    /**
     * 读取证书
     *
     * @param path apiclient_cert.p12 证书路径
     * @param merchantId 商户号 appId
     * @param merchantPassword 商户号密码
     * @return
     * @throws Exception
     */
    public SSLConnectionSocketFactory readCustomSSL(String path,String merchantPassword,String merchantId) throws Exception {
        /**
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
         */
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(path));
        try {
            keyStore.load(instream, merchantPassword.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, merchantId.toCharArray()).build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        return sslsf;
    }
}
