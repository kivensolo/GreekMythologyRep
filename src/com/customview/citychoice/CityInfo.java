package com.customview.citychoice;
import java.io.Serializable;

public class CityInfo implements Serializable {
	/*id="8601" pid="86" name="上海" pinyin_pre="SH" type="province*/

	private static final long serialVersionUID = 1L;

	/**
	 * 城市id
	 */
	private String id;

	/**
	 * 地区
	 */
	private String pid;

	/**
	 * 城市名称
	 */
	private String name;

	/**
	 * 拼音前缀
	 */
	private String pinyin_pre;

	/**
	 * 城市类型 [province , city , district , 区县]
	 */
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin_pre() {
		return pinyin_pre;
	}

	public void setPinyin_pre(String pinyin_pre) {
		this.pinyin_pre = pinyin_pre;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CityInfo [id=" + id + ", pid=" + pid + ", name=" + name
				+ ", pinyin_pre=" + pinyin_pre + ", type=" + type + "]";
	}


}
