import org.json.simple.*;

import java.io.PrintStream;

import java.lang.Math;

public class AIClient
{
    public static void main(String[] argv)
    {
        JSONObject obj = (JSONObject)JSONValue.parse(argv[0]);
        run(obj, System.out);
        System.out.flush();
    }

    public static void run(JSONObject jsonObj, PrintStream out)
    {
        Board board = Board.initializeBoardFromJSON(jsonObj);

        // the following "AI" moves a piece as far left as possible
        while (board._block.checkedLeft(board)) {
            out.println("left");
            out.println("Score: " + scoreBoard(board));
        }
    }

    public static double scoreBoard(Board board) {
        int holes = getHoles(board);
        int lines = getCompleteLines(board);
        int bumpiness = getBumpiness(board);
        int aggreg = getAggregateHeight(board);
        
        double score = (-0.510066 * (double)aggreg) + (0.760666 * (double)lines) + (-0.35663 * (double)holes) + (-0.184483 * (double)bumpiness);
        return score;
    }    

    public static int getHeight(int[][] bitmap, int j) {
        int highestBlockIndex = 0;
        for(int i = 0; i < 33; i++) {
            if(bitmap[i][j] != 0) {
                return 32 - i;
            }
        }

        return highestBlockIndex;
    }

    public static void printBitmap(int[][] bitmap)
    {
        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 12; j++) {
                System.out.printf("%5d ", bitmap[i][j]);
            }
            System.out.println();
        }
    }

    public static int getAggregateHeight(Board board) {
        int total = 0;
        for(int j = 0; j < 12; j++) {
            System.out.println(getHeight(board._bitmap, j));
            total += getHeight(board._bitmap, j);
        }

        return total;
    }

    public static int getHoles(Board board) {
        int holes = 0;
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 32; j++) {
                if (board._bitmap[j][i] == 0) {
                    if (board._bitmap[j][i+1] != 0 && board._bitmap[j][i-1] != 0) {
                        while(board._bitmap[j][i] == 0) {
                            j++;
                            holes++;
                        }
                    } 
                }   
            }
        }
        for (int j = 1; j < 32; j++) {
            for (int i = 1; i < 11; i++) {
                if (board._bitmap[j][i] == 0) {
                    if (board._bitmap[j+1][i] != 0 && board._bitmap[j-1][i] != 0) {
                        int temp_holes = 0;
                        while(board._bitmap[j][i] == 0) {
                            i++;
                            temp_holes++;
                        }
                    } 
                }   
            }
        }
        
        return holes;
    }

    public static int getCompleteLines(Board board) {
        int lines = 0;
         for (int j = 0; j < 33; j++) {
            if (board._bitmap[j][0] != 0) {
                int i = 0;
                while (i <= 12) {
                    if (i == 12) {
                        lines++;
                        break;
                    } else if (board._bitmap[j][i] != 0) {
                        break;
                    }
                    i++;
                }    
            }
        }  
        return lines; 
    }
    public static int getBumpiness(Board board) {
        int[] columns = new int[12];
        for (int i = 0; i < 12; i++) {
            int j = 0;
            while (j <= 32) {
                if (j == 32) {
                    columns[i] = 32;
                    break;
                }
                if (board._bitmap[j][i] != 0) {
                    columns[i] = j;
                    break;
                }
                j++;
            }
        }
        int total = 0;
        for (int i = 0; i < 11; i++) {
            total = Math.abs(columns[i] - columns[i+1]) + total;
        }
        return total;
    }
}
