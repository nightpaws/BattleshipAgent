package com.innovation.battleships.player;
import java.awt.Point;
import java.util.*;

public class HitSet extends HashSet<Point>
{
	public List<int[]> numberOfEachX(){
		Set<Integer> possibleXs = new HashSet<Integer>();
		for(Point current:this){
			possibleXs.add(current.x);
		}
		List<int[]> numberOfEachX = new ArrayList<int[]>();
		for(Integer nextX:possibleXs){
			numberOfEachX.add(new int[]{nextX,0});	
		}
		for(Point current:this){
			for(int[] numOfX:numberOfEachX){
				if(current.x==numOfX[0]){
					numOfX[1]++;
					break;
				}
			}
		}
		return numberOfEachX;
	}
	
	public List<int[]> numberOfEachY(){
		Set<Integer> possibleYs = new HashSet<Integer>();
		for(Point current:this){
			possibleYs.add(current.y);
		}
		List<int[]> numberOfEachY = new ArrayList<int[]>();
		for(Integer nextY:possibleYs){
			numberOfEachY.add(new int[]{nextY,0});	
		}
		for(Point current:this){
			for(int[] numOfY:numberOfEachY){
				if(current.y==numOfY[0]){
					numOfY[1]++;
					break;
				}
			}
		}
		return numberOfEachY;
	}
}
