package club.sgen.entity;

import java.util.Date;

import club.sgen.network.DataRequester;

import android.content.Intent;

public class Pop {
	public static Pop getPopByIntent(Intent intent) {
		User u = new User();
		Product p = new Product();
		Betting b = new Betting();

		u.setEmail(intent.getStringExtra("userEmail"));
		u.setId(intent.getStringExtra("userID"));
		u.setImage(intent.getStringExtra("userImage"));
		u.setName(intent.getStringExtra("userName"));
		u.setPoint(intent.getIntExtra("userPoint", 0));
		u.setUser_key(intent.getIntExtra("userKey", 0));

		p.setDescription(intent.getStringExtra("p_desc"));
		p.setImage(intent.getStringExtra("p_image"));
		p.setName(intent.getStringExtra("p_name"));
		p.setPrice(intent.getIntExtra("p_price", 0));
		p.setProduct_key(intent.getIntExtra("p_product", 0));
		p.setTerm_end((Date) DataRequester.parseDateString(intent
				.getStringExtra("p_term_end")));
		p.setTerm_start((Date) DataRequester.parseDateString(intent
				.getStringExtra("p_term_start")));
		p.setType((Product.TYPE) intent.getSerializableExtra("p_t"));

		b.setBetting_key(intent.getIntExtra("b_key", 0));
		b.setDescription(intent.getStringExtra("b_desc"));
		b.setGoal(intent.getStringExtra("b_g"));
		b.setIs_end(intent.getStringExtra("b_is_end"));
		b.setMax_number(intent.getIntExtra("b_m", 0));
		b.setName(intent.getStringExtra("b_n"));
		b.setProduct_key(intent.getIntExtra("b_pk", 0));
		b.setResultImage(intent.getStringExtra("b_ri"));
		b.setUserId(intent.getStringExtra("b_ui"));
		b.setTerm_end((Date) DataRequester.parseDateString(intent
				.getStringExtra("b_te")));
		b.setTerm_start((Date) DataRequester.parseDateString(intent
				.getStringExtra("b_ts")));
		b.setType((Betting.TYPE) intent.getSerializableExtra("b_t"));

		Pop pop = new Pop();
		pop.setUser(u);
		pop.setBetting(b);
		pop.setProduct(p);
		pop.setAgree(intent.getIntExtra("agree", 0));
		pop.setDisagree(intent.getIntExtra("disagree", 0));
		return pop;
	}

	public static void putPopToIntent(Intent intent, Pop pop) {
		intent.putExtra("agree", pop.getAgree());
		intent.putExtra("disagree", pop.getDisagree());

		User user = pop.getUser();
		Product product = pop.getProduct();
		Betting betting = pop.getBetting();
		intent.putExtra("userEmail", user.getEmail());
		intent.putExtra("userID", user.getId());
		intent.putExtra("userImage", user.getImage());
		intent.putExtra("userName", user.getName());
		intent.putExtra("userPoint", user.getPoint());
		intent.putExtra("userKey", user.getUser_key());

		intent.putExtra("p_desc", product.getDescription());
		intent.putExtra("p_image", product.getImage());
		intent.putExtra("p_name", product.getName());
		intent.putExtra("p_price", product.getPrice());
		intent.putExtra("p_product", product.getProduct_key());
		intent.putExtra("p_term_end",
				DataRequester.formatDateString(product.getTerm_end()));
		intent.putExtra("p_term_start",
				DataRequester.formatDateString(product.getTerm_start()));
		intent.putExtra("p_t", product.getType());

		intent.putExtra("b_key", betting.getBetting_key());
		intent.putExtra("b_desc", betting.getDescription());
		intent.putExtra("b_g", betting.getGoal());
		intent.putExtra("b_is_end", betting.getIs_end());
		intent.putExtra("b_m", betting.getMax_number());
		intent.putExtra("b_n", betting.getName());
		intent.putExtra("b_pk", betting.getProduct_key());
		intent.putExtra("b_ri", betting.getResultImage());
		intent.putExtra("b_ui", betting.getUserId());
		intent.putExtra("b_te",
				DataRequester.formatDateString(betting.getTerm_end()));
		intent.putExtra("b_ts",
				DataRequester.formatDateString(betting.getTerm_start()));
		intent.putExtra("b_t", betting.getType());
	}

	private Betting betting;
	private User user;
	private Product product;
	private int agree;
	private int disagree;

	public Betting getBetting() {
		return betting;
	}

	public void setBetting(Betting betting) {
		this.betting = betting;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getAgree() {
		return agree;
	}

	public void setAgree(int agree) {
		this.agree = agree;
	}

	public int getDisagree() {
		return disagree;
	}

	public void setDisagree(int disagree) {
		this.disagree = disagree;
	}
}