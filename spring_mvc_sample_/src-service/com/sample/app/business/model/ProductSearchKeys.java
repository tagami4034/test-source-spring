package com.sample.app.business.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.sampletool.common.model.BaseSearchKeys;

public class ProductSearchKeys extends BaseSearchKeys {
	//抽出順序--------
	static public final int ORDER_PROCD = 1;
	static public final int ORDER_NAME = 2;
	static public final int ORDER_TANKA = 32;

	//プロパティ---------
	@Size(max=20)
	private String procd;

	@Size(max=20)
	private String proClass;

	@Size(max=20)
	private String name;

	@Range(max=200000)
	private Integer tanka;
	///名前前方一致（BeginWith）
	@Size(max=20)
	private String nameBW;
	//------
	private int[] orderBy;

	@Range(min=-1, max=20000)
	private int tankaFrom = -1;
	@Range(min=-1, max=200000)
	private int tankaTo = -1;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameBW() {
		return nameBW;
	}
	public void setNameBW(String nameBW) {
		this.nameBW = nameBW;
	}
	public int[] getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int... orderBy) {
		this.orderBy = orderBy;
	}
	public String getProcd() {
		return procd;
	}

	public void setProcd(String procd) {
		this.procd = procd;
	}

	public void setLoginId(String procd) {
		this.procd = procd;
	}

	public Integer getTanka(){
		return tanka;
	}

	public void setTanka(Integer tanka){
		this.tanka = tanka;
	}

	public int getTankaFrom() {
		return tankaFrom;
	}
	public void setTankaFrom(int tankaFrom) {
		this.tankaFrom = tankaFrom;
	}

	public int getTankaTo() {
		return tankaTo;
	}
	public void setTankaTo(int tankaTo) {
		this.tankaTo = tankaTo;
	}

	public String getProClass(){
		return proClass;
	}

	public void setProClass(String proClass){
		this.proClass = proClass;
	}

}
