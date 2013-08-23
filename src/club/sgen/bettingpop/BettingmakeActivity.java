package club.sgen.bettingpop;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import club.sgen.entity.Betting;
import club.sgen.entity.User;
import club.sgen.network.AsyncCallback;
import club.sgen.network.BettingpopApplication;
import club.sgen.network.DataRequester;
import club.sgen.network.R;

public class BettingmakeActivity extends Activity implements
		AsyncCallback<HashMap<String, Object>> {

	private ImageView closebutton;
	private RadioButton bettingradio;
	private RadioButton donationradio;
	private EditText title;
	private EditText year;
	private EditText month;
	private EditText day;
	private EditText hour;
	private RadioButton buyproduct;
	private RadioButton writeproduct;
	private EditText writeproductname;
	private RadioGroup typeGroup;
	private RadioGroup numberGroup;
	private RadioButton number2;
	private RadioButton number4;
	private RadioButton number6;
	private RadioButton number8;
	private ImageView upload;
	private int productKey;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_make);
		user = BettingpopApplication.getUser();
		closebutton = (ImageView) findViewById(R.id.bettingmake_closebutton);
		typeGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		bettingradio = (RadioButton) findViewById(R.id.bettingmake_bettingicon);
		donationradio = (RadioButton) findViewById(R.id.bettingmake_donationicon);
		title = (EditText) findViewById(R.id.bettingmake_title);
		year = (EditText) findViewById(R.id.bettingmake_year);
		month = (EditText) findViewById(R.id.bettingmake_month);
		day = (EditText) findViewById(R.id.bettingmake_day);
		hour = (EditText) findViewById(R.id.bettingmake_hour);
		buyproduct = (RadioButton) findViewById(R.id.bettingmake_findgift);
		writeproduct = (RadioButton) findViewById(R.id.bettingmake_inputgift);
		writeproductname = (EditText) findViewById(R.id.bettingmake_writeproduct);
		number2 = (RadioButton) findViewById(R.id.bettingmake_people2);
		number4 = (RadioButton) findViewById(R.id.bettingmake_people4);
		number6 = (RadioButton) findViewById(R.id.bettingmake_people6);
		number8 = (RadioButton) findViewById(R.id.bettingmake_people8);
		numberGroup = (RadioGroup) findViewById(R.id.radioGroup3);
		upload = (ImageView) findViewById(R.id.bettingmake_ok);

		buyproduct.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					writeproductname.setEnabled(false);
					Intent intent = new Intent(BettingmakeActivity.this,
							ProductListActivity.class);
					intent.putExtra(ProductListActivity.REQUEST_PRODUCT_TYPE,
							ProductListActivity.GET_MY_PRODUCTS);
					startActivityForResult(intent,
							ProductListActivity.REQUEST_PRODUCT_KEY);
				} else {
					writeproductname.setEnabled(true);
				}
			}
		});

		closebutton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		upload.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEmpty(title) || isEmpty(year) || isEmpty(month)
						|| isEmpty(day) || isEmpty(hour)) {
					Toast.makeText(BettingmakeActivity.this,
							"모든 항목을 입력해 주십시오.", Toast.LENGTH_LONG).show();
					return;
				}
				Betting.TYPE type = Betting.TYPE.B;
				if (typeGroup.getCheckedRadioButtonId() == donationradio
						.getId())
					type = Betting.TYPE.D;
				int max_number;
				if (numberGroup.getCheckedRadioButtonId() == number2.getId())
					max_number = 2;
				if (numberGroup.getCheckedRadioButtonId() == number4.getId())
					max_number = 4;
				if (numberGroup.getCheckedRadioButtonId() == number6.getId())
					max_number = 6;
				else
					max_number = 8;
				String t, y, m, d, h, pro;
				if (type == Betting.TYPE.B) {
					if (isEmpty(writeproductname)) {
						Toast.makeText(BettingmakeActivity.this,
								"배팅 상품을 직접 입력해주십시오.", Toast.LENGTH_LONG).show();
						return;
					}
				}
				pro = writeproductname.getText().toString();
				t = title.getText().toString();
				y = year.getText().toString();
				m = month.getText().toString();
				d = day.getText().toString();
				h = hour.getText().toString();
				Betting betting = new Betting();
				betting.setGoal(pro);
				betting.setDescription(t);
				betting.setMax_number(max_number);
				betting.setType(type);
				betting.setProduct_key(productKey);
				betting.setTerm_start(DataRequester.getCurrentDate());
				betting.setTerm_end(DataRequester.toDate(Integer.parseInt(y),
						Integer.parseInt(m), Integer.parseInt(d),
						Integer.parseInt(h), 0, 0));
				betting.setUserId(user.getId());
				DataRequester.registerBetting(betting, productKey,
						BettingmakeActivity.this);
			}
		});

	}

	private boolean isEmpty(EditText editText) {
		String str = editText.getText().toString();
		return str.equals("");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ProductListActivity.REQUEST_PRODUCT_KEY) {
			productKey = resultCode;
			if (resultCode == 0) {
				writeproduct.setChecked(true);
				writeproductname.setEnabled(true);
			}
		}
	}

	@Override
	public void onResult(HashMap<String, Object> result) {
		String type = (String) result.get("type");
		Boolean errorOccured = (Boolean) result.get("error_occured");
		if (type.equals("registerBetting")) {
			if (!errorOccured) {
				boolean success = (Boolean) result.get("success");
				if (success) {
					Toast.makeText(BettingmakeActivity.this, "배팅이 등록되었습니다.",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(BettingmakeActivity.this, "배팅 등록이 실패했습니다.",
							Toast.LENGTH_LONG).show();
				}
				finish();
			} else {

			}
		}
	}

	@Override
	public void exceptionOccured(Exception e) {
		Toast.makeText(BettingmakeActivity.this, "배팅 등록이 실패했습니다.",
				Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void cancelled() {

	}

}
