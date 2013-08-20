package club.sgen.bettingpop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import club.sgen.network.R;

public class BannerFragment extends Fragment{
	private View view;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup contaioner,
			Bundle savedInstanceState){
		
		view = inflater.inflate(R.layout.fragment_banner, null);
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
}
