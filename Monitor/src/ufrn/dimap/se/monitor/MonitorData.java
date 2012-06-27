package ufrn.dimap.se.monitor;

public class MonitorData {
	private long CPU_use;
	private int batteryCurrentValue;
	private int batteryScale;
	
	public MonitorData(long CPU, int batteryCurrent) {
		CPU_use = CPU;
		batteryCurrentValue = batteryCurrent;
	}
	
	public MonitorData() {
		// TODO Auto-generated constructor stub
	}

	public long getCPUUse() {
		return CPU_use;
	}
	
	public long getBatteryCurrent() {
		return batteryCurrentValue;
	}
	
	public void setBatteryCurrentValue(int x) {
		this.batteryCurrentValue = x;
	}

	public void setBatteryScale(int intExtra) {
		batteryScale = intExtra;
	}
	
	public int getBatteryScale() {
		return batteryScale;
	}
}
