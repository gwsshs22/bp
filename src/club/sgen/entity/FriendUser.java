package club.sgen.entity;

public class FriendUser {
	private User user;
	private boolean isRequest=false;
	
	public FriendUser(){
		
	}
	
	public FriendUser(User user){
		this.user = user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public void setIsRequestTrue(){
		this.isRequest = true;
	}
	
	public User getUser(){
		return user;
	}
	
	public boolean getIsRequest(){
		return isRequest;
	}
} 
