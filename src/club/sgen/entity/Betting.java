package club.sgen.entity;

import java.util.Date;

public class Betting {
	private int betting_key;
	private String name;
	private String goal;
	private String type;
	private Date term_start;
	private Date term_end;
	private String description;
	private int max_number;
	private int product_key;
	public int getBetting_key() {
		return betting_key;
	}
	public void setBetting_key(int betting_key) {
		this.betting_key = betting_key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getTerm_start() {
		return term_start;
	}
	public void setTerm_start(Date term_start) {
		this.term_start = term_start;
	}
	public Date getTerm_end() {
		return term_end;
	}
	public void setTerm_end(Date term_end) {
		this.term_end = term_end;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMax_number() {
		return max_number;
	}
	public void setMax_number(int max_number) {
		this.max_number = max_number;
	}
	public int getProduct_key() {
		return product_key;
	}
	public void setProduct_key(int product_key) {
		this.product_key = product_key;
	}
}