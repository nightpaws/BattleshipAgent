package com.innovation.battleships.player;

public class Shot
{
private int x;
private int y;
private int possibleUsefullness;

public Shot(int x,int y){
this.x=x;
this.y=y;
this.possibleUsefullness=0;
}
public int getX(){
return x;
}

public int getY(){
return y;
}

public int getUsefullnness(){
return possibleUsefullness;
}

public void possibleUse(){
possibleUsefullness++;
}

public boolean isValid(){
	return (x<12&&x>-1&&y<12&&y>-1&&(!(x>5&&y<6)));
}


@Override
public boolean equals(Object o)
{
if(!(o instanceof Shot)){
return false;
}
Shot otherShot = (Shot) o;
return((this.getX()==otherShot.getX())&&(this.getY()==otherShot.getY()));
}

@Override
public int hashCode(){
	return (x*50)+y;
}
}


