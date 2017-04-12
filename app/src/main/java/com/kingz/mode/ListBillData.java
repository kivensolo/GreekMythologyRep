package com.kingz.mode;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

/**
 * @author: KingZ
 * @Description: Bean封装
 */
public class ListBillData {

	private String itemName;
	private Intent mIntent = null;
	private Context context;

	public ListBillData(Context context, String itemName,Intent mIntent) {

		this.context = context;
		this.itemName = itemName;
		this.mIntent = mIntent;
	}

	public String getUserName() {
		return itemName;
	}

	public Intent getmIntent() {
		return mIntent;
	}
	private Context getContext() {
		return context;
	}

	public void setUserName(String userName) {
		this.itemName = userName;
	}

	public void setmIntent(Intent mIntent) {
		this.mIntent = mIntent;
	}

	public void startActivity(ActivityOptions opts){
		if(opts == null){
			getContext().startActivity(getmIntent());
		}else{
			getContext().startActivity(getmIntent(),opts.toBundle());
		}
	}

	@Override
	public String toString() {
		return getUserName();
	}

}
