package g12;

import java.util.Comparator;

public class MyMove implements Comparable<MyMove>{
    private int row0,col0,row1,col1;
    private int score;

    public MyMove(int row0,int col0,int row1,int col1){
        this.row0 = row0;
        this.col0 = col0;
        this.row1 = row1;
        this.col1 =col1;
    }
    public MyMove(){

    }

    public int getRow0() {
        return row0;
    }

    public void setRow0(int row0) {
        this.row0 = row0;
    }

    public int getCol0() {
        return col0;
    }

    public void setCol0(int col0) {
        this.col0 = col0;
    }

    public int getRow1() {
        return row1;
    }

    public void setRow1(int row1) {
        this.row1 = row1;
    }

    public int getCol1() {
        return col1;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(MyMove mymove) {
        return this.score - mymove.score;
    }
    public static Comparator<MyMove> scoreComparatorDesc = new Comparator<MyMove>(){
        public int compare(MyMove m1, MyMove m2) {
            return m2.score - m1.score;
        }
    };
}
