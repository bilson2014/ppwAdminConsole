package com.panfeng.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
public class CsvWriter {  
	  
    /** CSV文件列分隔符 */  
    private static final String CSV_COLUMN_SEPARATOR = ",";  
  
    /** CSV文件列分隔符 */  
    private static final String CSV_RN = "\r\n";  
  
    /** 
     *  
     * 将检索数据输出的对应的csv列中 
     * */  
    public static String formatCsvData(List<Map<String, Object>> data,  
            String displayColNames, String matchColNames) {  
  
        StringBuffer buf = new StringBuffer();  
  
        String[] displayColNamesArr = null;  
        String[] matchColNamesMapArr = null;  
  
        displayColNamesArr = displayColNames.split(",");  
        matchColNamesMapArr = matchColNames.split(",");  
  
        // 输出列头  
        for (int i = 0; i < displayColNamesArr.length; i++) {  
            buf.append(displayColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);  
        }  
        buf.append(CSV_RN);  
  
        if (null != data) {  
            // 输出数据  
            for (int i = 0; i < data.size(); i++) {  
  
                for (int j = 0; j < matchColNamesMapArr.length; j++) {
                	String column = null == data.get(i).get(matchColNamesMapArr[j])
                			?"": data.get(i).get(matchColNamesMapArr[j]).toString();
                	column = column.replaceAll(",", "，");//英文逗号会被换行
                	buf.append(column).append(  
                            CSV_COLUMN_SEPARATOR);  
                }  
                buf.append(CSV_RN);  
            }  
        }  
        return buf.toString();  
    }  
    public static void exportCsv(String fileName, String content,  
            HttpServletResponse response) throws IOException {  
  
        // 设置文件后缀  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh24mmss");  
        String fn = URLEncoder.encode(fileName.concat(sdf.format(new Date()).toString() + ".csv"), "UTF-8");
        // 设置响应  
        response.setCharacterEncoding("UTF-8");  
        response.setContentType("text/csv; charset=UTF-8");  
        response.setHeader("Pragma", "public");  
        response.setHeader("Cache-Control", "max-age=30");  
        response.setHeader("Content-Disposition", "attachment; filename=" + fn);  
  
        // 写出响应  
        OutputStream os = response.getOutputStream();  
        //加上UTF-8文件的标识字符
        os.write(new   byte []{( byte ) 0xEF ,( byte ) 0xBB ,( byte ) 0xBF }); 
        os.write(content.getBytes("UTF-8"));  
        os.flush();  
        os.close();  
    }  
  
}  
