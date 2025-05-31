package com.thehxlab.adventureengine.core;

public class Lock {
	private boolean unlocked;
	private String desc;
	private String requiredItem;
	
	public Lock(String desc, String reqItem) {
		this.unlocked = false;
		this.desc = desc;
		this.requiredItem = reqItem;
	}
	
	public boolean isUnlocked() {
		return this.unlocked;
	}
	
	public String getDescription() {
		return this.desc;
	}
	
	public String getRequiredItem() {
		return this.requiredItem;
	}
	
	public boolean unlock(String i) {
		if (this.requiredItem.equalsIgnoreCase(i)) {
			return unlocked = true;
		}
		return false;
	}

}
