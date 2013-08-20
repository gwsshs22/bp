package club.sgen.network;

import java.util.HashMap;

public interface DataListener {
	public void onDataReceived(HashMap<String,Object> data);
}
