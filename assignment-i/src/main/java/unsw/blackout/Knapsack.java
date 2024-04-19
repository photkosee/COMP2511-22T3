package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

public class Knapsack {

    private int numItems;
    private int capacity;
    private File[] items;

    public Knapsack(int capacity, List<File> files) {
        this.numItems = files.size();
        this.capacity = capacity;
        File[] list = new File[numItems];
        int i = 0;
        for (File file : files) {
            list[i++] = file;
        }
        this.items = list;
    }

    public List<File> solution() {
        int[][] matrix = new int[numItems + 1][capacity + 1];

        for (int i = 0; i <= capacity; i++) {
            matrix[0][i] = 0;
        }
        for (int i = 1; i <= numItems; i++) {
            for (int j = 0; j <= capacity; j++) {
                if (items[i - 1].getSize() > j) {
                    matrix[i][j] = matrix[i - 1][j];
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - items[i - 1].getSize()]
                                    + items[i - 1].getTransferredBytes());
                }
            }
        }

        int res = matrix[numItems][capacity];
        int w = capacity;
        List<File> solutionList = new ArrayList<File>();
        for (int i = numItems; i > 0  &&  res > 0; i--) {
            if (res != matrix[i - 1][w]) {
                solutionList.add(items[i - 1]);
                res -= items[i - 1].getTransferredBytes();
                w -= items[i - 1].getSize();
            }
        }
        return solutionList;
    }
}
