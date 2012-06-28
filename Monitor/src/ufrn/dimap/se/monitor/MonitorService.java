package ufrn.dimap.se.monitor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class MonitorService extends Service {
	private final MonitorBinder binder = new MonitorBinder();
	private Data currentBData;
	private Data lastBData;
	private int currentBatteryConsume;
	private int currentBatteryAcc;
	private MonitorData data;
	protected long readInterval;

	public MonitorService() {
		currentBatteryAcc = 0;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		System.out.println("weeee");
	}

	public class MonitorBinder extends Binder {
		MonitorService getService() {
			return MonitorService.this;
		}
	}

	@Override
	public void onDestroy() {
		// code to execute when the service is shutting down
	}

	@Override
	public void onStart(Intent intent, int startid) {
		// code to execute when the service is starting up
	}

	public void setInterval(int interval) {
		this.readInterval = interval;
	}

	public void startBatteryMananger() {
		BroadcastReceiver br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent in) {
				lastBData = currentBData;
				currentBData = new Data();
				currentBData
						.setBatteryCurrentValue(in.getIntExtra("level", -1));
				currentBData.setBatteryScale(in.getIntExtra("scale", -1));
				currentBData.setAcc(currentBatteryAcc);
				if (lastBData != null) {
					// Atualizar o consumo de bateria
					currentBatteryConsume = lastBData.getValue()
							- currentBData.getValue();
					currentBatteryAcc += currentBatteryConsume;
				}
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(br, batteryLevelFilter);
	}

	private Runnable readThread = new Runnable() {
		public void run() {
			try {
				// enviar dados para o intent
				//data = new MonitorData(CPU, batteryCurrent);
				data.updateBattery(currentBData);
				Thread.sleep(readInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
