package com.sample.app.business.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.DateTime;

import com.sampletool.common.model.BaseObject;

public class Product extends BaseObject {

	@NotNull
	@Size(max=30)
	private String name;

	@NotNull
	private int tanka;

	@Size(min=1, max=4)
	private String procd;

	private DateTime upDate; //更新日

	private String opapi; //更新者

	private Integer kazu; //在庫

	@NotNull
	@Size(min=1, max=2)
	private String proClass; //商品種別

	//@NotNull
	//@Kanji(type=CheckType.HANKAKU)
	//@Pattern(regexp="ROLE_(ADMIN|UPDATE|READ)")
	//private String role;

	public String getName() {
	  return name;
	}

	public void setName(String name) {
	  this.name = name;
	}

	public DateTime getUpDate() {
	 return upDate;
	}

	public void setUpDate(DateTime upDate) {
	 this.upDate = upDate;
	}

	public String getProcd(){
		return procd;
	}

	public void setProcd(String procd){
		this.procd = procd;
	}

	public int getTanka(){
		return tanka;
	}


	public void setTanka(int tanka) {
		this.tanka = tanka;
	}


	public void setOpapi(String opapi){
		this.opapi = opapi;
	}

	public String getOpapi(){
		return opapi;
	}

	public String getProClass() {
		return proClass;
	}

	public void setProClass(String proClass) {
		this.proClass = proClass;
	}

	public Integer getKazu() {
		return kazu;
	}

	public void setKazu(Integer kazu) {
		this.kazu = kazu;
	}


/*
	 public String getRole() {
		return role;
	}
	 public void setRole(String role) {
		this.role = role;
	}
*/
}
