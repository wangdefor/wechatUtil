package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @ClassName HttpClientCustomSSL
 * @Description wangyong
 * @Date 2019/10/22 19:45
 * @Author wangyong
 * @Version 1.0
 **/
@Slf4j
public class HttpClientCustomSSL {

    /**
     * httpClient 请求获取公钥
     *
     * @param
     * @return
     * @throws Exception
     */
    public static String httpClientResultGetPublicKey(String xml,String path,String merchantPassword,String merchantId) throws Exception {
        StringBuffer reultBuffer = new StringBuffer();
		/*
			注意这里的readCustomerSSL是另一个方法，在下面贴出来\
			读取证书的类
		*/
        SSLConnectionSocketFactory sslsf = ReadSSl.getInstance().readCustomSSL(path,merchantPassword,merchantId);

        HttpPost httpPost = new HttpPost("https://fraud.mch.weixin.qq.com/risk/getpublickey");
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        StringEntity myEntity = new org.apache.http.entity.StringEntity(xml, "UTF-8");
        myEntity.setContentType("text/xml;charset=UTF-8");
        myEntity.setContentEncoding("UTF-8");
        httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
        httpPost.setEntity(myEntity);

        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inputStream = entity.getContent();
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    reultBuffer.append(str);
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            httpclient.close();
            response.close();
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
        }

        return reultBuffer.toString();
    }

    public static  String getPublicKey(String serectKey,String path,String merchantPassword,String merchantId) throws Exception {
        //1.0 拼凑所需要传递的参数 map集合
        SortedMap<Object, Object> parameters = new TreeMap<>();
        String nonce_str = UUID.randomUUID().toString().replace("-", "");
        parameters.put("mch_id", merchantId);
        parameters.put("nonce_str", nonce_str);
        parameters.put("sign_type", "MD5");
        //2.0 根据要传递的参数生成自己的签名~~注意creatSign是自己封装的一个类。大家可以在下面自主下载
        String sign = SignUtils.creatSign( parameters,serectKey);
        //3.0 把签名放到map集合中【因为签名也要传递过去】
        parameters.put("sign", sign);
        //4.0将当前的map结合转化成xml格式
        String reuqestXml = WXPayUtil.getRequestXml(parameters);
        log.info("当前的请求的参数xml为" + reuqestXml);
        log.info("===================================================================================================");
        //5.0 发送请求到微信请求公钥Api。发送请求是一个方法来的~~注意需要带着证书哦
        String xml1 = HttpClientCustomSSL.httpClientResultGetPublicKey(reuqestXml,path,merchantPassword,merchantId);
        log.info("当前返回的xml格式为 " + xml1);
        log.info("===================================================================================================");
        //6.0 解析返回的xml数据===》map集合
        String publicKey = XMLUtils.Progress_resultParseXml(xml1);
        return publicKey;
    }

    public static String convertPubToPub8(String publicKey) throws Exception{
        RSAPublicKey rsaPublicKey = RSAPublicKey.getInstance(org.bouncycastle.util.encoders.Base64.decode(publicKey));
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(rsaPublicKey.getModulus(),rsaPublicKey.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        String public_key_pkcs8 = org.bouncycastle.util.encoders.Base64.toBase64String(keyFactory.generatePublic(rsaPublicKeySpec).getEncoded());
        log.info("转化成功，转化之后的public_key_pkcs8 为" + public_key_pkcs8);
        log.info("===================================================================================================");
        return public_key_pkcs8;
    }

    public static void main(String[] args) throws Exception {
        RSAPublicKey rsaPublicKey = RSAPublicKey.getInstance(org.bouncycastle.util.encoders.Base64.decode("MIIBCgKCAQEA1FHV2fFTOHL0FMdz7vu33rm+byyUhoHEU9Dj3ctsW9HP596nRm58KNXiFKgD796i0UQEhhQ7ao7IqRtJyWs8w/QlBuX69f2S64/oImCXM/8bEHhFkSKUkJzh5dA2Dur3mxgVPfM01+N5mi31gmg6YZ3lfuuUMYty5THcARrxhs+7odV8+7RTHgtrJ66bih+xKhOvnSuZEtCi7fxoTbOaYENqILTS+POhCHhA76dhdoP/hv+EnczuJjdWU1gniDsli/EDvCou+Mxu8vVZQ/EV/ZYCPBW7gubbtW5xLDgYK9YGsQOYuNLo46Fe1ouSn//quhUZLsJw374BmqlhPo4HkQIDAQAB"));
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(rsaPublicKey.getModulus(),rsaPublicKey.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
        System.out.println(publicKey.getAlgorithm());
        System.out.println(publicKey.getFormat());
        String s = org.bouncycastle.util.encoders.Base64.toBase64String(publicKey.getEncoded());
        System.out.println(s);
    }
}
