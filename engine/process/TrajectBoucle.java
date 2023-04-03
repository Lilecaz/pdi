package engine.process;

import engine.map.Block;
import engine.map.TestMap;

public class TrajectBoucle {

    private Block currentpos;
    private TestMap map;
    private int iter;

    public TrajectBoucle(Block currentpos, TestMap map ,int iter) {
        this.currentpos = currentpos;
        this.map = map;
        this.iter=iter;
    }

    public Block T1(){
        int x= currentpos.x;
        int y=currentpos.y;
        if(x< map.getHeight() && iter>=0 && iter <30){
            x=x+1;
        }
        if(y< map.getHeight() && iter>=30 && iter <60){
            y=y+1;
        }
        if(x>0 && iter>=60 && iter <90){
            x=x-1;
        }
        if(y>0 && iter>=90 && iter <120){
            y=y-1;
        }
        Block newPosition = map.getBlock(x, y);
        return newPosition;
    }
    public Block T2(){
        int x= currentpos.x;
        int y=currentpos.y;
        if(x>0 && iter>=0 && iter <30){
            x=x-1;
        }
        if(y>0 && iter>=30 && iter <60){
            y=y-1;
        }
        if(x< map.getHeight() && iter>=60 && iter <90){
            x=x+1;
        }
        if(y< map.getHeight() && iter>=90 && iter <120){
            y=y+1;
        }
        Block newPosition = map.getBlock(x, y);
        return newPosition;
    }
    public Block T3(){
        int x= currentpos.x;
        int y=currentpos.y;
        if(x< map.getHeight() && iter>=0 && iter <30){
            x=x+1;
        }
        if(y>0 && iter>=30 && iter <60){
            y=y-1;
        }
        if(x>0 && iter>=60 && iter <90){
            x=x-1;
        }
        if(y< map.getHeight() && iter>=90 && iter <120){
            y=y+1;
        }
        Block newPosition = map.getBlock(x, y);
        return newPosition;
    }
    public Block T4(){
        int x= currentpos.x;
        int y=currentpos.y;
        if(x>0 && iter>=0 && iter <30){
            x=x-1;
        }
        if(y< map.getHeight() && iter>=30 && iter <60){
            y=y+1;
        }
        if(x< map.getHeight() && iter>=60 && iter <90){
            x=x+1;
        }
        if(y>0 && iter>=90 && iter <120){
            y=y-1;
        }
        Block newPosition = map.getBlock(x, y);
        return newPosition;
    }
}
