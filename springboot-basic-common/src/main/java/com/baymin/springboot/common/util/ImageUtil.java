package com.baymin.springboot.common.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Created by baymin on 2016/8/4.
 */
public class ImageUtil {

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @throws
     * @Title: generateImage
     * @param: imgStr    base64编码
     * @param: imgFilePath    要生成图片的路径
     * @param: @return
     * @return: boolean
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) {
            return false;
        }
        imgStr = imgStr.replaceFirst("data:image/jpeg;base64,", "");
        try {
            // Base64解码
            byte[] bytes = Base64.getDecoder().decode(new String(imgStr).getBytes());
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
