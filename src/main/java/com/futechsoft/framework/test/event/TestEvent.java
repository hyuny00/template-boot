package com.futechsoft.framework.test.event;

import com.futechsoft.framework.util.FtMap;

public class TestEvent {

	private FtMap ftMap;
	private String data;


	public TestEvent( FtMap ftMap) {
		this(ftMap,"");
	}

	public TestEvent( FtMap ftMap,  String data) {

		this.ftMap=ftMap;
		this.data=data;
	}

	public FtMap getFtMap() {
		return ftMap;
	}
	public void setFtMap(FtMap ftMap) {
		this.ftMap = ftMap;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}




}
