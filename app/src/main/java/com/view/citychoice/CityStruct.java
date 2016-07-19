package com.view.citychoice;

import java.io.Serializable;
import java.util.ArrayList;

public class CityStruct implements Serializable {

	private static final long serialVersionUID = 1L;
	private int count;
	private String city_type;
	private ArrayList<CityInfo> city_item;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<CityInfo> getCity_item() {
		return city_item;
	}
	public void setCity_item(ArrayList<CityInfo> city_item) {
		this.city_item = city_item;
	}
	@Override
	public String toString() {
		return "CityStruct [count=" + count + ",  city_type=" +  city_type + ", city_item="
				+ city_item.toString() + "]";
	}
	public String getCity_type() {
		return city_type;
	}
	public void setCity_type(String city_type) {
		this.city_type = city_type;
	}


}
