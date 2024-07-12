package com.hm.libcore.util.rsa;

import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import com.hm.libcore.util.base64.Base64;


/**
 * Title: RSAUtil.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014-7-24
 * @version 1.0
 */
public class RSAUtil {
	   // 豌豆荚公钥
    public final static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUXjRmSCQLqSEMP9rUdK6c28qdSrOaUXmMUdb9cLuwcqkPmakLeeNxERqVPAcPDpgyC33mm/Xg4FPMkpmISfEKY5lscYtfvA/Ku6JwF1wblt7a/sDvPx0e6o1nxWAx7zn+ULWotdgeJfq56OZ2OoyJuZFOXBIOn9OXhTUsWKwONwIDAQAB";

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    
    public static boolean doCheck(String content, String inSign) {
        try {
        	String sign =  URLDecoder.decode(URLDecoder.decode(inSign.replaceAll("hammer", "="), "utf-8"), "utf-8");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(PUBLICKEY);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));

            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
	
}
