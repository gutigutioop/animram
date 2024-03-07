package com.example.animram.constants;

public enum AnimalType {
	Smile(1),Angry(2), Sad(3);
	
	private int n;
	
	private AnimalType(int n) {
		this.n = n;
		
	}
	public int getN() {
	    return this.n;
	  }

	
}


