package com.sbkj.car.common;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 14:10
 */
public class SecuritySHATool {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";


    /**
     * MD5加密字节
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();

    }

    /**
     * SHA加密字节
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }


    /**
     * SHA加密
     *
     * @param inputStr
     * @return
     */
    public static String shaEncrypt(String inputStr) {
        byte[] inputData = inputStr.getBytes();
        String returnString = "";
        try {
            inputData = encryptSHA(inputData);
            for (int i = 0; i < inputData.length; i++) {
                returnString += byteToHexString(inputData[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }


    private static String byteToHexString(byte ib) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = digit[(ib >>> 4) & 0X0F];
        ob[1] = digit[ib & 0X0F];

        String s = new String(ob);

        return s;
    }

    /**
     * MD5加密
     *
     * @param inputStr
     * @return
     */
    public static String md5Encrypt(String inputStr) {
        byte[] inputData = inputStr.getBytes();
        String returnString = "";
        try {
            BigInteger md5 = new BigInteger(encryptMD5(inputData));
            returnString = md5.toString(16);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnString;
    }

    public static String dataDecrypt(Map<String, Object> serviceParams) {
        StringBuilder sb = new StringBuilder();
        Object[] keys = serviceParams.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            sb.append(key).append(serviceParams.get(key));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(shaEncrypt("username111password111"));
        System.out.println(md5Encrypt("111"));
    }
}