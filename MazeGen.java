import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

class cell{
    public int size = 5;
    public int x, y;

    // N, E, S, W
    public boolean[] walls = {true, true, true, true};
    public boolean visited = false;

    public cell[] adj = {null, null, null, null};
    public cell last = null;

    cell(int x, int y, int size){
        this.x = x;
        this.y = y;
        this.size = size;
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
            g.drawLine(this.x, this.y, this.x + size, this.y);
        }
        // East
        if (walls[1]){
            g.drawLine(this.x + size, this.y, this.x + size, this.y + size);
        }
        // South
        if (walls[2]){
            g.drawLine(this.x, this.y + size, this.x + size, this.y + size);
        }
        // West
        if (walls[3]){
            g.drawLine(this.x, this.y, this.x, this.y + size);
        }
    }
}


public class MazeGen extends JPanel {
    public static int size = 5;
    public static int d = 5;
    public cell[][] cells = new cell[d][d];


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < d; row++){
            for (int col = 0; col < d; col++){
                cells[row][col].draw(g);
            }
        }
    }


    public void createMazeRecursive(cell c){
        c.visited = true;

        int [] adj = c.getValidAdj();

        if (adj.length == 0){
            if (c.last == null){
                return;
            }else{
                cell last = c.last;
                c.last = null;
                createMazeRecursive(last);
            }
        }else{
            int next = new Random().nextInt(adj.length);
            c.walls[adj[next]] = false;
            c.adj[adj[next]].walls[(adj[next] + 2) % 4] = false;
            c.adj[adj[next]].last = c;
            createMazeRecursive(c.adj[adj[next]]);
        }
    }


    public void createMazeIterative(cell c){    
        do{
            c.visited = true;

            int [] adj = c.getValidAdj();

            if (adj.length == 0){
                cell temp = c;
                c = c.last;
                temp.last = null;
            }else{
                int next = new Random().nextInt(adj.length);
                c.walls[adj[next]] = false;
                c.adj[adj[next]].walls[(adj[next] + 2) % 4] = false;
                c.adj[adj[next]].last = c;

                c = c.adj[adj[next]];
            }
        }while (c.last != null);
    }


    public static void main(String[] args) {
        int method = 0;
        if (args.length == 0){
            d = 2;
        }else{
            d = Integer.parseInt(args[0]);
            method = Integer.parseInt(args[1]);
        }
        
        MazeGen mazeGen = new MazeGen();

        for (int x = 0; x < d; x++){
            for (int y = 0; y < d; y++){
                mazeGen.cells[x][y] = new cell((x * size) + 10, (y * size) + 10, size);
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

        if (method == 0){
            mazeGen.createMazeRecursive(mazeGen.cells[0][0]);
        }else{
            mazeGen.createMazeIterative(mazeGen.cells[0][0]);
        }

        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mazeGen);
        frame.setSize(d * size + 20, d * size + 40);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}