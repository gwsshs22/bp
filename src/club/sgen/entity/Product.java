package club.sgen.entity;

import java.util.Date;

public class Product {
	public static enum TYPE {
		P, M;

		public String toString() {
			if (this == P)
				return "P";
			else
				return "M";
		}
	};

	private TYPE type;

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public int getProduct_key() {
		return product_key;
	}

	public void setProduct_key(int product_key) {
		this.product_key = product_key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	private int price;
	private int product_key;
	private String name;
	private Date term_start;
	private Date term_end;
	private String description;
	private String image;

}