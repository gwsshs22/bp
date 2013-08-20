package club.sgen.network;

import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

public class DataHandler extends Handler {
	private DataListener listener;
	public DataHandler(DataListener listener){
		this.listener = listener;
	}
	@Override
	public void handleMessage(Message message){
		HashMap<String, Object> data = new HashMap<String,Object>();
		
		listener.onDataReceived(data);
	}
}
