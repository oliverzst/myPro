package com.actec.bsms.entity;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 基站信息
 */
public class RcuInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;//多个,号分隔
	private String domain_name;//域名
	private int  rcu_lai;//系统号
	private int attr;//属性
	private String alias;//别名
	private float gn;//经度
	private float ge;//纬度
	private String ip_addr;//ip地址
	private int ip_port;//ip端口
	private int sig_ch;//信道号
	private String name;
	private User operate_by;
	private String manufacturer;
	private int ant_altitude;
	private int ant_num;
	private String ant_type;
	private double band_width;
	private int link_type;
	private String contact;
	private String contact_phone;
	private String photo;
	//动态属性
	//控制信道频点号
	private String cchFreq;
	//逻辑信道个数
	private int tchCount;
	//连接状态
	private int rcuStatus;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomain_name() {
		return domain_name;
	}

	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}

	public int getRcu_lai() {
		return rcu_lai;
	}

	public void setRcu_lai(int rcu_lai) {
		this.rcu_lai = rcu_lai;
	}

	public int getAttr() {
		return attr;
	}

	public void setAttr(int attr) {
		this.attr = attr;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public float getGn() {
		return gn;
	}

	public void setGn(float gn) {
		this.gn = gn;
	}

	public float getGe() {
		return ge;
	}

	public void setGe(float ge) {
		this.ge = ge;
	}

	public String getIp_addr() {
		return ip_addr;
	}

	public void setIp_addr(String ip_addr) {
		this.ip_addr = ip_addr;
	}

	public int getIp_port() {
		return ip_port;
	}

	public void setIp_port(int ip_port) {
		this.ip_port = ip_port;
	}

	public int getSig_ch() {
		return sig_ch;
	}

	public void setSig_ch(int sig_ch) {
		this.sig_ch = sig_ch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOperate_by() {
		return operate_by;
	}

	public void setOperate_by(User operate_by) {
		this.operate_by = operate_by;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getAnt_altitude() {
		return ant_altitude;
	}

	public void setAnt_altitude(int ant_altitude) {
		this.ant_altitude = ant_altitude;
	}

	public int getAnt_num() {
		return ant_num;
	}

	public void setAnt_num(int ant_num) {
		this.ant_num = ant_num;
	}

	public String getAnt_type() {
		return ant_type;
	}

	public void setAnt_type(String ant_type) {
		this.ant_type = ant_type;
	}

	public double getBand_width() {
		return band_width;
	}

	public void setBand_width(double band_width) {
		this.band_width = band_width;
	}

	public int getLink_type() {
		return link_type;
	}

	public void setLink_type(int link_type) {
		this.link_type = link_type;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCchFreq() {
		return cchFreq;
	}

	public void setCchFreq(String cchFreq) {
		this.cchFreq = cchFreq;
	}

	public int getTchCount() {
		return tchCount;
	}

	public void setTchCount(int tchCount) {
		this.tchCount = tchCount;
	}

	public int getRcuStatus() {
		return rcuStatus;
	}

	public void setRcuStatus(int rcuStatus) {
		this.rcuStatus = rcuStatus;
	}

	@Override
	public String toString() {
		return "RcuInfo{" +
				"id='" + id + '\'' +
				", domain_name='" + domain_name + '\'' +
				", rcu_lai=" + rcu_lai +
				", attr=" + attr +
				", alias='" + alias + '\'' +
				", gn=" + gn +
				", ge=" + ge +
				", ip_addr='" + ip_addr + '\'' +
				", ip_port=" + ip_port +
				", sig_ch=" + sig_ch +
				", name='" + name + '\'' +
				", operate_by=" + operate_by +
				", manufacturer='" + manufacturer + '\'' +
				", ant_altitude=" + ant_altitude +
				", ant_num=" + ant_num +
				", ant_type='" + ant_type + '\'' +
				", band_width=" + band_width +
				", link_type=" + link_type +
				", contact='" + contact + '\'' +
				", contact_phone='" + contact_phone + '\'' +
				", photo='" + photo + '\'' +
				", cchFreq='" + cchFreq + '\'' +
				", tchCount=" + tchCount +
				", rcuStatus=" + rcuStatus +
				'}';
	}

	public static String listToString(List<RcuInfo> rcuInfoList) {
		if(CollectionUtils.isEmpty(rcuInfoList))
			return "";
		StringBuffer sb = new StringBuffer();
		for (RcuInfo rcuInfo : rcuInfoList) {
			sb.append(rcuInfo.toString()).append('\\');
		}
		return sb.toString();
	}
}
