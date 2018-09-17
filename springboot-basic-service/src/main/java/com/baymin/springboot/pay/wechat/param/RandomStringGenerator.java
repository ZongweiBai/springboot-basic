package com.baymin.springboot.pay.wechat.param;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomStringGenerator {

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 商户订单号
     * 14位订单生成时间：20121225114609代表 2012-12-25 11:46:09
     * 8位随机数：yg3xhl5zu8tx系统自动生成
     */
    public static String getTradeNo(String channelCode) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer sb = new StringBuffer();
        sb.append(channelCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append(sdf.format(new Date()));
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String randomNumString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String tradeNo = getTradeNo("0000");
        System.out.println(tradeNo);
        System.out.println("tradeNo.length()=" + tradeNo.length());
        System.out.println(randomNumString(4));
    }

}