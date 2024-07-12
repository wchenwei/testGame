package com.hm.config.excel.temlate;



public class PrizeTemplate {
	private int pos; 
	private int type; 
	private int id; 
	private int num; 
	private int weight;
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	} 
	
	public PrizeTemplate clone(){
		PrizeTemplate prize = new PrizeTemplate(); 
		prize.setId(this.id);
		prize.setNum(this.num);
		prize.setPos(this.pos);
		prize.setType(this.type);
		prize.setWeight(this.weight);
		return prize; 
	}
	
}
	
