package ninja.PanicHelper.adapter;

/**
 *  An item in the Navigation Bar
 **/
public class NavDrawerItem {
	private String title;
	private int icon;
	private String count = "0";
	/* Boolean to set visiblity of the counter  */
	private boolean isCounterVisible = false;

	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
}