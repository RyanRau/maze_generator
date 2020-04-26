import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

class cell{
    public int x, y;
    public int xGrid, yGrid;
    public boolean[] walls = {true, true, true, true};
    public boolean visited = false;

    public cell[] adj = {null, null, null, null};

    cell(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int[] getValidAdj(){
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for (int i = 0; i < adj.length; i++){
            if (adj[i] != null){
                if(adj[i].visited == false)
                    tmp.add(i);
            }
        }

        int[] results = new int[tmp.size()];
        for (int i = 0; i < tmp.size(); i++){
            results[i] = tmp.get(i);
        }

        return results;
    }

    public void draw(Graphics g){
        // North
        if (walls[0]){
            g.drawLine(this.x, this.y, this.x + 50, this.y);
        }
        // East
        if (walls[1]){
            g.drawLine(this.x + 50, this.y, this.x + 50, this.y + 50);
        }
        // South
        if (walls[2]){
            g.drawLine(this.x, this.y + 50, this.x + 50, this.y + 50);
        }
        // West
        if (walls[3]){
            g.drawLine(this.x, this.y, this.x, this.y + 50);
        }
        g.drawString(xGrid + ", " + yGrid, x + 10, y + 10);
    }
}


public class MazeGen extends JPanel {
    public static final int size = 50;
    public static int d = 10;
    public cell[][] cells = new cell[d][d];
    public LinkedList<cell> stack = new LinkedList<cell>();
    public int globalCount = 0;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < d; row++){
            for (int col = 0; col < d; col++){
                cells[row][col].draw(g);
            }
        }
    }


    public void createMaze(int x, int y){
        cells[x][y].visited = true;
        
        int[] adj = cells[x][y].getValidAdj();

        if (adj.length == 0){
            if (stack.size() <= 0){
                return;
            }else{
                cell last = stack.removeLast();
                createMaze(last.xGrid, last.yGrid);
            }
        }else{
            stack.add(cells[x][y]);
            int next = new Random().nextInt(adj.length);
            cells[x][y].walls[adj[next]] = false;
            cells[x][y].adj[adj[next]].walls[(adj[next] + 2) % 4] = false;
            createMaze(cells[x][y].adj[adj[next]].xGrid, cells[x][y].adj[adj[next]].yGrid);
        }
    }

    public static void main(String[] args) {
        d = Integer.parseInt(args[0]);
        MazeGen mazeGen = new MazeGen();

        for (int x = 0; x < d; x++){
            for (int y = 0; y < d; y++){
                mazeGen.cells[x][y] = new cell((x * size) + 10, (y * size) + 10);  
                mazeGen.cells[x][y].xGrid = x;
                mazeGen.cells[x][y].yGrid = y;
            }
        }

        for (int x = 0; x < d; x++){
            for (int y = 0; y < d; y++){
                if (x == 0){
                    mazeGen.cells[x][y].adj[3] = null; 
                    mazeGen.cells[x][y].adj[1] = mazeGen.cells[x + 1][y];
                }else if (x == d - 1){
                    mazeGen.cells[x][y].adj[1] = null; 
                    mazeGen.cells[x][y].adj[3] = mazeGen.cells[x - 1][y];
                }else {
                    mazeGen.cells[x][y].adj[3] = mazeGen.cells[x - 1][y];
                    mazeGen.cells[x][y].adj[1] = mazeGen.cells[x + 1][y]; 
                }

                if (y == 0){
                    mazeGen.cells[x][y].adj[0] = null; 
                    mazeGen.cells[x][y].adj[2] = mazeGen.cells[x][y + 1];
                }else if (y == d - 1){
                    mazeGen.cells[x][y].adj[2] = null; 
                    mazeGen.cells[x][y].adj[0] = mazeGen.cells[x][y - 1];
                }else {
                    mazeGen.cells[x][y].adj[0] = mazeGen.cells[x][y - 1];
                    mazeGen.cells[x][y].adj[2] = mazeGen.cells[x][y + 1]; 
                } 
            }
        }

        mazeGen.createMaze(0, 0);

        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mazeGen);
        frame.setSize(d * 50 + 20, d * 50 + 40);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}