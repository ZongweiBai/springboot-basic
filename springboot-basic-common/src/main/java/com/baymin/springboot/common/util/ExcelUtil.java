package com.baymin.springboot.common.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by jonez on 2017/1/11.
 */
public class ExcelUtil {

    /**
     * 写入EXCEL
     *
     * @param titleList 数据头
     * @param rslist    数据结果集
     * @param outpath   导出文件路径
     * @throws Exception
     */
    public static void writeExcel(List<String> titleList, List<List<String>> rslist, String outpath) {
        SXSSFWorkbook book = null;
        Sheet sheet = null;
        // 限制内存中保留对象数量，防止大数据量内存溢出
        int cacheRows = 1000;
        try {
            book = new SXSSFWorkbook(cacheRows);
            sheet = book.createSheet();
            // 标题
            if (titleList != null && titleList.size() > 0) {
                Row row = sheet.createRow(0);
                for (int i = 0; i < titleList.size(); i++) {
                    row.createCell(i).setCellValue(titleList.get(i));
                }
            }
            if (rslist != null && rslist.size() > 0) {
                for (int i = 0; i < rslist.size(); i++) {
                    List<String> list = rslist.get(i);
                    if (list != null && list.size() > 0) {
                        Row row = sheet.createRow(1 + i);
                        for (int j = 0; j < list.size(); j++) {
                            row.createCell(j).setCellValue(list.get(j));
                        }
                    }
                }
            }
            FileOutputStream out = new FileOutputStream(outpath);
            book.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeComplicatedHeaderExcel(String[] excelHeader0, String[] headnum0, String[] excelHeader1, List<List<String>> rslist, String outpath) {
        SXSSFWorkbook book = null;
        Sheet sheet = null;
        // 限制内存中保留对象数量，防止大数据量内存溢出
        int cacheRows = 1000;
        try {
            book = new SXSSFWorkbook(cacheRows);
            sheet = book.createSheet();
            // 标题
            if (excelHeader0 != null && excelHeader0.length > 0) {
                Row row = sheet.createRow(0);
                for (int i = 0; i < excelHeader0.length; i++) {
                    row.createCell(i).setCellValue(excelHeader0[i]);
                }
            }


            // 动态合并单元格
            for (int i = 0; i < headnum0.length; i++) {
                String[] temp = headnum0[i].split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
            }

            // 第二行表头
            if (excelHeader1 != null && excelHeader1.length > 0) {
                Row row = sheet.createRow(1);
                for (int i = 0; i < excelHeader1.length; i++) {
                    row.createCell(i+4).setCellValue(excelHeader1[i]);
                }
            }

            if (rslist != null && rslist.size() > 0) {
                for (int i = 0; i < rslist.size(); i++) {
                    List<String> list = rslist.get(i);
                    if (list != null && list.size() > 0) {
                        Row row = sheet.createRow(2 + i);
                        for (int j = 0; j < list.size(); j++) {
                            row.createCell(j).setCellValue(list.get(j));
                        }
                    }
                }
            }
            FileOutputStream out = new FileOutputStream(outpath);
            book.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @throws Exception
     */
    public static void downLoadFile(String downPath, HttpServletResponse response) throws Exception {
        String url = downPath;
        File downFile = new File(url);
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/x-msdownload");
        setFileDownloadHeader(response, downFile.getName());
//        response.setHeader("Content-Disposition", "attachment;filename=" + downFile.getName());
//        response.setHeader("Content-Disposition",
//                "attachment; filename="
//                        + new String(downFile.getName().getBytes("gb2312"),
//                        "utf8") + "");
        javax.servlet.ServletOutputStream ou = null;
        java.io.FileInputStream fileInputStream = null;
        try {
            ou = response.getOutputStream();
            fileInputStream = new java.io.FileInputStream(downFile);
            if (fileInputStream != null) {
                int filelen = fileInputStream.available();
                // 文件太大时内存不能一次读出,要循环
                byte a[] = new byte[filelen];
                fileInputStream.read(a);
                ou.write(a);
            }
            fileInputStream.close();
            ou.close();
        } catch (RuntimeException e) {
            response.getOutputStream().close();
            response.getWriter().close();
        }
    }

    /**
     * <pre>
     * 浏览器下载文件时需要在服务端给出下载的文件名，当文件名是ASCII字符时没有问题
     * 当文件名有非ASCII字符时就有可能出现乱码
     *
     * 这里的实现方式参考这篇文章
     * http://blog.robotshell.org/2012/deal-with-http-header-encoding-for-file-download/
     *
     * 最终设置的response header是这样:
     *
     * Content-Disposition: attachment;
     *                      filename="encoded_text";
     *                      filename*=utf-8''encoded_text
     *
     * 其中encoded_text是经过RFC 3986的“百分号URL编码”规则处理过的文件名
     * </pre>
     *
     * @param response
     * @param filename
     * @return
     */
    private static void setFileDownloadHeader(HttpServletResponse response, String filename) {
        String headerValue = "attachment;";
        headerValue += " filename=\"" + encodeURIComponent(filename) + "\";";
        headerValue += " filename*=utf-8''" + encodeURIComponent(filename);
        response.setHeader("Content-Disposition", headerValue);
    }

    /**
     * <pre>
     * 符合 RFC 3986 标准的“百分号URL编码”
     * 在这个方法里，空格会被编码成%20，而不是+
     * 和浏览器的encodeURIComponent行为一致
     * </pre>
     *
     * @param value
     * @return
     */
    public static String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
