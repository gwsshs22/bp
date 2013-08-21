package club.sgen.entity;

public class Pop {
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