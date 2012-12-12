package com.zachtib.thefuckingweather;

public class TheFuckingWeather {
	private String temperature;
	private String remark;
	private String flavor;

	public TheFuckingWeather(String temperature, String remark, String flavor) {
		super();
		this.temperature = temperature;
		this.remark = remark;
		this.flavor = flavor;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

}
