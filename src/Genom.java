import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Genom {
    // values for match, mismatch, gap open, and gap extend
    public static final double GAP_OPEN = -1;
    public static final double GAP_EXT = -0.5;
    public static final double MISMATCH = -1;
    public static final double MATCH = 2;

    public static void main(String[] args) {

        String inputFiles[] = new String[]{"test1.seq", "test2.seq", "test3.seq", "test4.seq", "test5.seq"};
        int j;
        String x = null,y = null;
        for (int i = 0; i <5 ; i++) {
            j=1;
            try {
                File myObj = new File(inputFiles[i]);

                Scanner myReader = new Scanner(myObj);

                System.out.println("\n\n"+inputFiles[i]);
                while (myReader.hasNextLine()) {
                    if(j==1){
                        x = myReader.nextLine();
                    }else if(j==2){
                        y = myReader.nextLine();
                    }
                    j++;
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            Node[][] table = fill_table(x,y);
            //printMatrix(table);
            backtrack(table, x, y);
        }



    }

    //function to fill the table
    public static Node[][] fill_table(String x, String y) {
        int m = x.length(); int n = y.length();
        Node[][] table = new Node[m+1][n+1];

        table[0][0] = new Node();
        table[0][0].value = 0; // set the value of initial node
        for (int i = 0; i < m; i++) { // set the values of nodes at initial row
            if (i == 0) {  // in this for loop current node is i+1,j+1 in the table
                table[i+1][0] = new Node();
                table[i+1][0].value = GAP_OPEN;
                table[i+1][0].up = true;
                continue;
            }
            table[i+1][0] = new Node();
            table[i+1][0].value = table[i][0].value + GAP_EXT;
            table[i+1][0].up = true;
        }
        for (int i = 0; i < n; i++) { // set the values of nodes at initial column
            if (i == 0) { // in this for loop current node is i+1,j+1 in the table
                table[0][i+1] = new Node();
                table[0][i+1].value = GAP_OPEN;
                table[0][i+1].left = true;
                continue;
            }
            table[0][i+1] = new Node();
            table[0][i+1].value = table[0][i].value + GAP_EXT;
            table[0][i+1].left = true;
        }

        double up, left, cross; // values from the nodes up, left, cross
        for (int i = 1; i < m+1; i++) { // fill the rest table
            for (int j = 1; j < n+1; j++) {
                table[i][j] = new Node();
                if(table[i][j-1].left)
                    left = table[i][j-1].value + GAP_EXT; // if the upper node comes from up then this node is gap extension
                else
                    left = table[i][j-1].value + GAP_OPEN; // else this node is gap open

                if(table[i-1][j].up)
                    up = table[i-1][j].value + GAP_EXT; // if the left node comes from left then this node is gap extension
                else
                    up = table[i-1][j].value + GAP_OPEN; // else this node is gap open

                if(x.charAt(i-1) == y.charAt(j-1)) // if the
                    cross = table[i-1][j-1].value + MATCH;
                else
                    cross = table[i-1][j-1].value + MISMATCH;

                if (up >= cross && up >= left)
                    table[i][j].up = true;
                if (left >= cross && left >= up)
                    table[i][j].left = true;
                if (cross >= up && cross >= left)
                    table[i][j].cross = true;
                table[i][j].value = Math.max(up,Math.max(left,cross)); // set the current nodes value
            }
        }
        System.out.println(table[m][n].value);
        return table;
    }

    public static void backtrack(Node[][] table, String x, String y) {
        String res1 = "", res2 = "";
        int i = x.length(), j = y.length();

        while (true) {
            if(i == 0 && j == 0 ){ // control statemnt if we reached the start
                break;
            }
            if(table[i][j].cross) {
                res1 += x.charAt(i-1);
                res2 += y.charAt(j-1);
                i--;j--;
            } else if (table[i][j].left) {
                res1 += "-";
                res2 += y.charAt(j-1);
                j--;
            } else if (table[i][j].up) {
                res1 += x.charAt(i-1);
                res2 += "-";
                i--;
            }


        }
        StringBuilder sbr = new StringBuilder(res1);
        res1 = sbr.reverse().toString();
        sbr = new StringBuilder(res2);
        res2 = sbr.reverse().toString();
        System.out.println(res1);
        System.out.println(res2);
    }

    static class Node {
         double value;
         boolean up = false, left = false, cross = false;
    }

    public static void printMatrix(Node[][] table){
        for (int i = 0; i <table.length ; i++) {
            for (int j = 0; j <table[0].length ; j++) {
                System.out.printf("%8.1f |",table[i][j].value);
            }
            System.out.println();
        }
    }

}
