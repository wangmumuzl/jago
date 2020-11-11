package g12;

import java.util.Comparator;

public class Point implements Comparable<Point>{

    public Point() {
        // TODO Auto-generated constructor stub
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Point(int pos, int score) {
        super();
        this.pos = pos;
        this.score = score;
    }

    public Point(int row,int col,int score){
        super();
        this.row = row;
        this.col = col;
        this.score = score;

    }
    public void draw() {
        System.out.println("(" + pos + ", " + score + ")");
    }
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int pos = 0;
    private int score = 0;
    private int row = 0;
    private int col = 0;
    @Override
    public int compareTo(Point arg0) {
        // TODO Auto-generated method stub
        return this.score - arg0.score;
    }

    public static Comparator<Point> scoreComparator = new Comparator<Point>(){
        public int compare(Point p1, Point p2) {
            return p2.score - p1.score;
        }
    };

}
