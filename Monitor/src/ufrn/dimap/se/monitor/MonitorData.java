package ufrn.dimap.se.monitor;

import java.io.BufferedReader;
import java.io.FileReader;

import android.content.Intent;
import android.os.BatteryManager;

public class MonitorData {
	private BufferedReader readStream;
	private String[] a;
	private float cPUTotalP;
	private long workT, totalT;
	private long total, totalBefore, work, workBefore;
	private int batteryLastLevel;
	private int batteryLevel;
	private int batteryScale;
	private int memTotal;
	private int memFree;

	public MonitorData() {
		// TODO Auto-generated constructor stub
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
				cPUTotalP = workT * 100 / (float) totalT;
			}
			readStream.close();
			workBefore = work;
			totalBefore = total;
			System.out.println(cPUTotalP + "%");
			batteryLastLevel = batteryLevel;
			batteryLevel = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			batteryScale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			System.out
					.println("Bateria: " + batteryLevel + " / " + batteryScale
							+ " = " + (long) batteryLevel / batteryScale);

			readStream = new BufferedReader(new FileReader("/proc/meminfo"));
			String x = readStream.readLine();
			while (x != null) {
				if (x.startsWith("MemTotal:"))
					memTotal = Integer.parseInt(x.split("[ ]+", 3)[1]);
				if (x.startsWith("MemFree:"))
					memFree = Integer.parseInt(x.split("[ ]+", 3)[1]);
				x = readStream.readLine();
			}
			readStream.close();
			System.out.println("Memoria: (Total): " + memTotal + " (Usada):"
					+ (memTotal - memFree));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
