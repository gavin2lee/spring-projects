package com.anyi.report.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseMapVo extends BaseVo{
	@JsonProperty("data")
	private Map<String , Object> data;

	public Map<String , Object> getData() {
		return data;
	}

	public void setData(Map<String , Object> data) {
		this.data = data;
	}
	
	public void addData(String key,Object value){
		if(this.data==null){
			this.data = new HashMap<String,Object>();			
		}
		this.data.put(key, value);
	}
	
}
