package com.hm.db;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hm.model.serverpublic.IpToDomain;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wyp
 * @description
 *          域名转换工具
 * @date 2020/12/24 16:22
 */
public class IpToDomainUtil {

    private static final String privateKeyBase64 ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIYUlZZAtb5oiBJhjUlp+Qm9NBeAyGqKwOeVgi8uQf6F387nsA5FTpTt1UI6fp43fRpxf51SO1sKa6U5xYgAKC/u7GgvG+IftbxNtbXOcjGqjFkiXooUJamEs8jTQwurHY5FLMF9pbEm6SUK3r+Rma3hgg+qTTquC2QonncrP0e/AgMBAAECgYAQCA4KtRoF9kGu0PMvv/gC7hh4ZvGPq599ESdLtOA3KpKkDpfCXQ8UR4ukK5JQIJpYuxFjIh3uuMQ+MTYpGLu1MOGgHCQJds8uLkL0f1+c6IgzGkeKqV3tHPRtwie92JtWIcWeCImXImwxegpbueR4eVB1Vg+TViwsCO8v2f0WYQJBAP+c3KHzVovi3xtGDE9/YYWRBYeKPnRll9jQbl2FTrXpQ8UXfz948M0+nZoDJc35+NRsCDiWVNUEjjd8ahyrH9UCQQCGSJY1mUTJgvykvj2cFhZQdi08IaBnq3iu1+VJhc64AgVVwFLmPX0uTX0sx4yOsUWGEHQ3qcjHqZbrJa8w6qdDAkEAhuwkpbONTl9dhsbtvpStRFRtR8MX/laV1VMHvoOcLXdIV2N0vms3KA9SHzZkIRo+VtoDaOMpOhuiefVJCYLmVQJAP55aE9CmZzX4jfBXbgCE19/1NFzWCueRCHzec0PCmSjT8DlqzQvqp6osmK99pwjTYF2hQdHdfsKY7pTu+CTj0wJBAOIrvKu2Ci4wdOvEfGWI9MC2MnZzG4QHPoRxdyEYyJ4BMyOOos5mEKme4P1S3W2EbhKTUTyhc2eoB+bXPkoZlL0=";
    private static final String publicKeyBase64 ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGFJWWQLW+aIgSYY1JafkJvTQXgMhqisDnlYIvLkH+hd/O57AORU6U7dVCOn6eN30acX+dUjtbCmulOcWIACgv7uxoLxviH7W8TbW1znIxqoxZIl6KFCWphLPI00MLqx2ORSzBfaWxJuklCt6/kZmt4YIPqk06rgtkKJ53Kz9HvwIDAQAB";
    /**
     * @description
     *          域名加密
     * @param domain
     * @return java.lang.String
     * @author wyp
     * @date 2020/12/24 17:52
     */
    public static String encryptDomain(String domain){
        if(StringUtils.isBlank(domain)){
            return null;
        }
        RSA rsa = new RSA(privateKeyBase64,publicKeyBase64);
        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(domain, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        String encode = Base64.encode(encrypt);
        return encode;
    }

    //-- 数据缓存 --//

    private static final IpToDomainUtil instance = new IpToDomainUtil();
    public static IpToDomainUtil getInstance() {
        return instance;
    }
    public final IpToDomain DefaultValue;
    private final LoadingCache<String, IpToDomain> cache;


    public IpToDomain getIpToDomain(String ip) {
        IpToDomain ipToDomain = cache.getUnchecked(ip);
        return ipToDomain.getId() == DefaultValue.getId()? null:ipToDomain;
    }

    /**
     * @description
     *          修改时删除缓存数据
     * @param ip
     * @return void
     * @author wyp
     * @date 2020/12/24 19:22
     */
    public void delCache(String ip){
        this.cache.invalidate(ip);
    }

    public void loadDate(Map<String,IpToDomain> map){
        this.cache.putAll(map);
    }

    private IpToDomainUtil() {
        DefaultValue = new IpToDomain();
        DefaultValue.setId(-1);
        cache = CacheBuilder.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(5, TimeUnit.MINUTES)//设置时间对象没有被读/写访问则对象从内存中删除
                //.recordStats()  //开启统计功能
                /*.removalListener(new RemovalListener<String, IpToDomain>() {   // 对象被移除时的监听事件
                    @Override
                    public void onRemoval(RemovalNotification<String, IpToDomain> notification) {
                        System.err.println("cache del IP"+notification.getKey());
                    }
                })*/
                .build(new CacheLoader<String, IpToDomain>() {
                    @Override
                    public IpToDomain load(String ip) {  // 数据load方法。元数据
                        List<IpToDomain> list = IpToDomain.loadByParam(ip);
                        IpToDomain ipToDomain = list.stream().filter(e -> StringUtils.isNotBlank(e.getDomain())).findFirst().orElse(null);
                        if(Objects.isNull(ipToDomain)){
                            return DefaultValue;
                        }
                        return ipToDomain;
                    }
                });
    }

    public static void main(String[] args) {
        // AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue() 可以设置加密规则
        RSA rsa = new RSA();
        System.out.println(rsa.getPrivateKeyBase64());
        System.out.println(rsa.getPublicKeyBase64());
        IpToDomain.loadData();
        IpToDomain ipToDomain = IpToDomainUtil.getInstance().getIpToDomain("127.1.1.2:82");
        System.out.println(ipToDomain);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IpToDomain ipToDomain2 = IpToDomainUtil.getInstance().getIpToDomain("127.1.1.2:82");
        System.out.println(ipToDomain2);
    }
}
