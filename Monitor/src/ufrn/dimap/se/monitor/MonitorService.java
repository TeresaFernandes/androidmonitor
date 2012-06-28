package ufrn.dimap.se.monitor;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class MonitorService extends Service {
	private final MonitorBinder binder = new MonitorBinder();
	private MonitorData data;
	private Intent battery;
	protected static long readInterval = 1000;

	public MonitorService() {
		System.out.println("construiu serviço");
	}

	public MonitorData getData(){
		return this.data;
	}
		
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		System.out.println("serviço criado");
		readThread.start();
		data = new MonitorData();
		data.changeWriting();
	}

	public class MonitorBinder extends Binder {
		MonitorService getService() {
			return MonitorService.this;
		}
	}

	@Override
	public void onDestroy() {
		readThread.interrupt();
		readThread = null;
		if (data!=null) data.exit();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		battery = registerReceiver(null, ifilter);
	}

	public void setInterval(int interval) {
		readInterval = interval;
	}

	private Runnable read = new Runnable() {
		public void run() {
			while (readThread == Thread.currentThread()) {
				try {
					// enviar dados para o intent
					// data = new MonitorData(CPU, batteryCurrent);
					data.update(battery);
					Thread.sleep(readInterval);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	};
	private Thread readThread = new Thread(read, "readThread");

}
