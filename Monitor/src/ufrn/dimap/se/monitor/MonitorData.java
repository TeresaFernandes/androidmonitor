package ufrn.dimap.se.monitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

import android.content.ComponentName;
import android.os.IBinder;

public class MonitorData {
	private int batteryConsumeValue;
	private int batteryScale;
	private BufferedReader readStream;
	private String x;
	private String[] a;
	Vector<Float> cPUTotalP, cPUAMP, cPURestP;
	private long workT, totalT;
	private long total, totalBefore, work, workBefore;


	public MonitorData() {
		// TODO Auto-generated constructor stub
	}

	public long getBatteryCurrent() {
		return batteryConsumeValue;
	}

	public void setBatteryScale(int intExtra) {
		batteryScale = intExtra;
	}

	public int getBatteryScale() {
		return batteryScale;
	}

	public void updateBattery(Data currentBData) {
		update();
		int consume = currentBData.getTotalConsume();
		this.batteryConsumeValue = consume;
		this.batteryScale = currentBData.getScale();
	}

	public void update() {
		try {
			readStream = new BufferedReader(new FileReader("/proc/stat"));
			a = readStream.readLine().split("[ ]+", 9);
			work = Long.parseLong(a[1]) + Long.parseLong(a[2])
					+ Long.parseLong(a[3]);
			total = work + Long.parseLong(a[4]) + Long.parseLong(a[5])
					+ Long.parseLong(a[6]) + Long.parseLong(a[7]);

			if (totalBefore != 0) {
				workT = work - workBefore;
				totalT = total - totalBefore;
				cPUTotalP.add(0, workT * 100 / (float) totalT);
				cPURestP.add(0, (workT) * 100 / (float) totalT);
			}
			workBefore = work;
			totalBefore = total;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
