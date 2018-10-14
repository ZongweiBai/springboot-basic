package com.baymin.springboot.pay.wechat.param;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Signature {

    public static String getSign(Map<String, Object> map, String key) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                if ("packageInfo".equals(entry.getKey())) {
                    list.add("package=" + entry.getValue() + "&");
                } else {
                    list.add(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        result = MD5.MD5Encode(result).toUpperCase();

        return result;
    }

    /**
     * 从API返回的XML数据里面重新计算一次签名
     *
     * @param responseString API返回的XML数据
     * @return 新鲜出炉的签名
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static String getSignFromResponseString(String responseString)
            throws IOException, SAXException, ParserConfigurationException {
//		Map<String, Object> map = XMLParser.getMapFromXML(responseString);
//		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
//		map.put("sign", "");
//		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
//		return Signature.getSign(map);
        return null;
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     *
     * @param responseString API返回的XML数据字符串
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean checkIsSignValidFromResponseString(
            String responseString) throws ParserConfigurationException,
            IOException, SAXException {

//		Map<String, Object> map = XMLParser.getMapFromXML(responseString);
//		Util.log(map.toString());
//
//		String signFromAPIResponse = map.get("sign").toString();
//		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
//			Util.log("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
//			return false;
//		}
//		Util.log("服务器回包里面的签名是:" + signFromAPIResponse);
//		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
//		map.put("sign", "");
//		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
//		String signForAPIResponse = Signature.getSign(map);
//
//		if (!signForAPIResponse.equals(signFromAPIResponse)) {
//			// 签名验不过，表示这个API返回的数据有可能已经被篡改了
//			Util.log("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
//			return false;
//		}
//		Util.log("API返回的数据签名验证通过!!!");
        return true;
    }

    public static HashMap<String, String> sortAsc(Map<String, String> map) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }


    public static String mapJoin(Map<String, String> map, boolean valueUrlEncode) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            if (map.get(key) != null && !"".equals(map.get(key))) {
                try {
                    String temp = (key.endsWith("_") && key.length() > 1) ? key.substring(0, key.length() - 1) : key;
                    sb.append(temp);
                    sb.append("=");
                    //获取到map的值
                    String value = map.get(key);
                    //判断是否需要url编码
                    if (valueUrlEncode) {
                        value = URLEncoder.encode(map.get(key), "utf-8").replace("+", "%20");
                    }
                    sb.append(value);
                    sb.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}