package com.udcm.constants;



/******
 * 定时器类型
 * ********/
public  enum SchedulerJobType {
	
	CRON(1),
	SIMPLE(2),
	CALENDAR(3);
	
	private int value;
	private SchedulerJobType(int value){
		this.value=value;
	}
	public int getValue(){
		return value;
	}
	
}
