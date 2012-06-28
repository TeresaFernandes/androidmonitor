package ufrn.dimap.se.monitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import android.content.Intent;
import android.os.BatteryManager;

public class MonitorData {
	private BufferedReader readStream;
	private String[] a;
	Vector<Float> cPUTotalP;
	private long workT, totalT;
	private long total, totalBefore, work, workBefore;
	private int batteryLastLevel;
	private int batteryLevel;
	private int batteryScale;

	public MonitorData() {
		// TODO Auto-generated constructor stub
		cPUTotalP = new Vector<Float>(15);
	}

	public long getBatteryConsume() {
		return batteryLastLevel - batteryLevel;
	}

	public void setBatteryScale(int intExtra) {
		batteryScale = intExtra;
	}

	public int getBatteryScale() {
		return batteryScale;
	}

	public void update(Intent battery) {
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
			}
			workBefore = work;
			totalBefore = total;
			System.out.println(cPUTotalP.lastElement() + "%");
			batteryLastLevel = batteryLevel;
			batteryLevel = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			batteryScale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			System.out.println("Bateria: "+batteryLevel + " / " + batteryScale+ " = " + (long)batteryLevel/batteryScale);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
