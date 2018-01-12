package com.actec.bsms.utils;

import jersey.repackaged.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 全局配置类
 * 
 * @author Freelance
 * @version 2014-06-25
 */
public class Global {
	private static Logger logger = LoggerFactory.getLogger(Global.class);

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();

	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * 保存国际化值
	 */
	private static Map<String, String> mapI8n = Maps.newHashMap();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("application.properties");

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}
	
	/**
     * 属性文件加载对象
     */
    private static PropertiesLoader propertiesLoader = new PropertiesLoader("application.properties");

	/**
	 * 国际化
	 */
	private static String defaultLocale = getConfig("defaultLocale");
	
	/**
	 * 获取配置
	 * 
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null) {
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

	/**
	 * 获取国际化信息
	 *
	 */
	public static String getI18n(String key) {
		String value = mapI8n.get(key);
		if (StringUtils.isEmpty(value)) {
			Locale locale= Locale.getDefault();
			if (defaultLocale.equals("en_US")) {
				locale= Locale.US;
			} else if (defaultLocale.equals("zh_CN")) {
				locale= Locale.CHINA;
			}
			ResourceBundle rb= ResourceBundle.getBundle("i18n/messages", locale);
			try {
				String s = rb.getString(key);
//				value = s;
				value = new String(s.getBytes("ISO-8859-1"), "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			mapI8n.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

//	public static String getFileConfig(String filePath, String key) {
//		String value = null;//map.get(key);
//		if (value == null) {
//			String path = getUserfilesBaseDir();
//			path=path.substring(0,path.length()-1);
//			path +="/WEB-INF/classes/"+filePath;
////			System.out.print(path);
//			value = readFileProperty(path, key);
//			map.put(key, value != null ? value : StringUtils.EMPTY);
//		}
//		return value;
//
//
//	}

	private static String readFileProperty(String filePath, String key) {
		Properties prop = new Properties();
		//读取属性文件a.properties
		InputStream in = null;
		String value = "";
		try{
			in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in);     ///加载属性列表
			value = prop.getProperty(key);
		}
		catch(Exception e){
			logger.error(e.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return value;
	}

	/**
	 * 获取上传文件的根目录
	 *
	 * @return
	 */
	public static String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (StringUtils.isBlank(dir)) {
			try {
//				dir = ServletContextFactory.getServletContext()
//						.getRealPath("/");
			} catch (Exception e) {
				return "";
			}
		}
		if (!dir.endsWith("/")) {
			dir += "/";
		}
		// System.out.println("userfiles.basedir: " + dir);
		return dir;
	}

	public static void writeProperties(String filePath, String key, String value) {
		Properties prop = new Properties();
		FileOutputStream fos = null;
		InputStream in = null;
		Global global = new Global();
		try{
			in = global.getClass().getResourceAsStream(filePath);
			prop.load(in);
			fos = new FileOutputStream(filePath, false);//true表示追加打开
			prop.setProperty(key, value);
			prop.store(fos, "");
		}
		catch(Exception e){
			logger.error(e.getMessage());
		} finally {
			try {
				in.close();
				in = null;
				fos.close();
				fos = null;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
//	/**
//	 * 修改配置
//	 */
//	public static void setConfig(String filePath, String key, String value) {
//		String path = getUserfilesBaseDir();
//		path=path.substring(0,path.length()-1);
//		path +="/WEB-INF/classes/"+filePath;
//		propertiesLoader.setProperty(path, key, value);
//		map.remove(key);
//		map.put(key, value);
//	}
//	/**
//	 * 修改多个参数配置
//	 */
//	public static void setConfigs(String filePsath, Map<String,String> maps) throws IOException {
//		String path = getUserfilesBaseDir();
//		path=path.substring(0,path.length()-1);
//		path +="/WEB-INF/classes/"+filePsath;
//		propertiesLoader.writeProperties(path, maps);
//	}

	public static void setFileConfig(String filePath, String key, String value) {
		writeProperties(filePath, key, value);
		map.remove(key);
		map.put(key, value);
	}


	
	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}

	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 页面获取常量
	 * 
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

//	/**
//	 * 获取上传文件的根目录
//	 *
//	 * @return
//	 */
//	public static String getUserfilesBaseDir() {
//		String dir = getConfig("userfiles.basedir");
//		if (StringUtils.isBlank(dir)) {
//			try {
//				dir = ServletContextFactory.getServletContext()
//						.getRealPath("/");
//			} catch (Exception e) {
//				return "";
//			}
//		}
//		if (!dir.endsWith("/")) {
//			dir += "/";
//		}
//		// System.out.println("userfiles.basedir: " + dir);
//		return dir;
//	}

	/**
	 * 获取工程路径
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)) {
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				while (true) {
					File f = new File(file.getPath() + File.separator + "src"
							+ File.separator + "main");
					if (f == null || f.exists()) {
						break;
					}
					if (file.getParentFile() != null) {
						file = file.getParentFile();
					} else {
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
	}

}
