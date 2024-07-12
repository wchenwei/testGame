package com.hm.model.battle.xiaochu;

import com.hm.util.CellUtils;
import lombok.Data;

/**
 * 单元格
 *
 * @author
 */
@Data
public class Cell {
	/**
	 * x坐标
	 */
	public transient int X;
	/**
	 * y坐标
	 */
	public transient int Y;
 
	public int colorId;
	public int score;
	public int id;
	private int npcId;

	public Cell() {
		super();
	}

	public Cell(int id) {
		super();
		this.id = id;
	}

	public Cell(int x, int y) {
		super();
		X = x;
		Y = y;
		this.id = x * CellUtils.maxIndex + y;
	}
	public Cell(int x, int y,int colorId,int id) {
		super();
		X = x;
		Y = y;
		this.colorId = colorId;
		this.id = id;
	}
 
	public int getX() {
		return X;
	}
 
	public void setX(int x) {
		X = x;
	}
 
	public int getY() {
		return Y;
	}
 
	public void setY(int y) {
		Y = y;
	}
 

	public int getColorId(){
		return colorId;
	}
 	public int getId(){
		return id;
	}

	public boolean nearCell(Cell cell) {
		if (cell != null) {
			if (this.X == cell.X && this.Y == (cell.Y + 1)) {
				return true;
			} else if (this.X == cell.X && this.Y == (cell.Y - 1)) {
				return true;
			} else if (this.X == (cell.X + 1) && this.Y == cell.Y) {
				return true;
			} else if (this.X == (cell.X - 1) && this.Y == cell.Y) {
				return true;
			}
		}
		return false;
	}
    
	@Override
	public String toString() {
		return this.X+"_"+this.Y+":"+this.colorId;
				
	}
 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + X;
		result = prime * result + Y;
		return result;
	}
 
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (X != other.X)
			return false;
		if (Y != other.Y)
			return false;
		return true;
	}
	
	public Cell clone(){
		Cell cell=new Cell();
		cell.setX(this.X);
		cell.setY(this.Y);
		cell.setColorId(this.colorId);
		cell.setScore(this.score);
		cell.setId(this.id);
		cell.setNpcId(this.npcId);
		return cell;
	}


}