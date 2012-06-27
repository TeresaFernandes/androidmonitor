package ufrn.dimap.se.monitor;

public class Data {
	private int value;
	private int scale;
	
	public void setBatteryCurrentValue(int intExtra) {
		value = intExtra;
	}

	public void setBatteryScale(int intExtra) {
		scale = intExtra;
	}

	public int getValue() {
		return value;
	}
	
	public int getScale(){
		return scale;
	}

}
