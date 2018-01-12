package com.actec.bsms.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


//import sun.misc.BASE64Encoder;

/**
 * 
 * <p> Title: 通用工具类</p>
 * 
 * <p> Description: </p>
 * 
 * <p> Copyright: Copyright (c) 2014 by ACTEC </p>
 * 
 * <p> Company: ACTEC </p>
 * 
 * @author: WDL & Lee.Jiang
 * @version: 1.0
 * @date: 2014年2月26日 上午10:01:02
 * 
 */
public class GlobalUtil {
    private static Logger logger = LoggerFactory.getLogger(GlobalUtil.class);

    public static String LOCAL_IP = "127.0.0.1";

    /**
     * 格式化日期
     * 
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat(Constant.FORMAT).format(date);
    }

    /**
     * 字符串日期转毫秒
     * @param str
     * @return
     */
    public static long formatDateStrToLong(String str){
    	long time = 0;
		try {
			time = new SimpleDateFormat(Constant.FORMAT).parse(str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
		
    }
    /**
     * 毫秒转字符串日期
     * @param arg
     * @return
     */
    public static String formatDateLongToStr(long arg) {
		DateFormat formatter = new SimpleDateFormat(Constant.FORMAT);
		return formatter.format(arg);
    }
    /**
     * 设置用户的IP地址
     * 
     * @param request
     * @return
     */
    public static void setLocalIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        String uriStr = request.getRequestURL().toString();
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            LOCAL_IP =  ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
// 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                LOCAL_IP =  ip.substring(0, index);
            } else {
                LOCAL_IP = ip;
            }
        } else if(StringUtils.isNotEmpty(uriStr)) {
            URI uri = URI.create(uriStr);
            LOCAL_IP = getIpFromUri(uri);
        } else {
            LOCAL_IP = request.getRemoteAddr();
        }
        logger.debug("Local_ip;"+LOCAL_IP);
        //开启心跳
//        new Thread(MainSxuFinder.getInstance()).start();
    }

    public static String getIpFromUri(URI uri) {
        URI effectiveURI;
        String ipAddr = "";
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }
        String host = effectiveURI.getHost();
        if(StringUtils.isIp(host)) {
            ipAddr =  host;
        } else {
            try {
                ipAddr = InetAddress.getByName(host).getHostAddress();
            } catch (UnknownHostException e) {
                logger.error(e.getMessage());
            }
        }
        return ipAddr;
    }

    /**
     * 返回本地IP地址
     * 
     * @return
     */
    public static String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
            return "";
        }

    }

    /**
     * 简单的MD5加密，平常使用
     * 
     * @param password
     * @return
     */
    public static String getMd5(String password) {
        String str = "";
        if (password != null && !password.equals("")) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                // BASE64Encoder base = new BASE64Encoder();
                // 加密后的字符串
                // str = base.encode(md.digest(password.getBytes("utf-8")));
                str = Encodes.encodeBase64(md.digest(password.getBytes("utf-8")));
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * MD5加密(32位)
     * 
     * @param instr
     *            要加密的字符串
     * @return 返回加密后的字符串
     */
    public final static String encoderByMd5With32Bit(String instr) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            if (instr != null && !"".equals(instr)) {
                byte[] strTemp = instr.getBytes();
                // MD5计算方法
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                mdTemp.update(strTemp);
                byte[] md = mdTemp.digest();
                int j = md.length;
                char str[] = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取随机的UUID字符串
     * 
     * @return String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 判断变量是否为空
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用来去掉List中空值和相同项的。
     * 
     * @param list
     * @return
     */
    public static List<String> removeSameItem(List<String> list) {
        List<String> difList = new ArrayList<String>();
        for (String t : list) {
            if (t != null && !difList.contains(t)) {
                difList.add(t);
            }
        }
        return difList;
    }

    /**
     * 字符在字符串中出现的次数
     * 
     * @param string
     * @param a
     * @return
     */
    public static int occurTimes(String string, String a) {
        int pos = -2;
        int n = 0;

        while (pos != -1) {
            if (pos == -2) {
                pos = -1;
            }
            pos = string.indexOf(a, pos + 1);
            if (pos != -1) {
                n++;
            }
        }
        return n;
    }

    //读文件，返回字符串
    public static String ReadFile(String path){
        File file = new File(path);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    logger.error(e1.toString());
                }
            }
        }
        return sb.toString();
    }

    //把json格式的字符串写到文件
    public static void writeFile(String filePath, String sets) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        out.write(sets);
        out.println();
        fw.close();
        out.close();
    }

    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        String result = defaultValue;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return result;
    }
    
    /**
     * 使用率计算
     * 
     * @return
     */
    public static String fromUsage(long free, long total) {
        Double d = new BigDecimal(free * 100 / total).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(d);
    }

    
    /*
	 * 格式化查询时间为当月或当天时，赋值正确结束时间
	 */
	public static String setEndTime(String startDate , String endDate){
		
		Calendar nowDaTe =  Calendar.getInstance();
		nowDaTe.setTime(new Date());
		
		Calendar startDateC =  Calendar.getInstance();
		startDateC.setTimeInMillis(GlobalUtil.formatDateStrToLong(startDate));
		
		Calendar endDateC =  Calendar.getInstance();
		endDateC.setTimeInMillis(GlobalUtil.formatDateStrToLong(endDate));
		
		
		if(nowDaTe.before(endDateC)){
			if(startDateC.get(Calendar.DAY_OF_MONTH) == endDateC.get(Calendar.DAY_OF_MONTH)){
				nowDaTe.set(nowDaTe.get(Calendar.YEAR), nowDaTe.get(Calendar.MONTH), nowDaTe.get(Calendar.DAY_OF_MONTH), nowDaTe.get(Calendar.HOUR_OF_DAY)-1, 59, 59);
			}else{
				nowDaTe.set(nowDaTe.get(Calendar.YEAR), nowDaTe.get(Calendar.MONTH), nowDaTe.get(Calendar.DAY_OF_MONTH)-1, 23, 59, 59);
			}
		
			endDate = GlobalUtil.formatDate(nowDaTe.getTime());
		}
		
		return endDate;
		
	}
	
	/**
	 * 格式化 double 类型保留4位小数
	 * @param d
	 * @return
	 */
	public static Double formatDuble4Point(double d){
		DecimalFormat df = new DecimalFormat("#.####");
		return Double.valueOf(df.format(d));
	}
	/**
	 * 格式化 double 类型保留2位小数
	 * @param d
	 * @return
	 */
	public static Double formatDuble2Point(double d){
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(d));
	}
}
