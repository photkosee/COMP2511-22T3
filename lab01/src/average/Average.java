package average;

/**
 * Average
 * @author Phot Koseekrainiramon (z5387411)
 *
 */
public class Average {
    /**
     * Returns the average of an array of numbers
     * 
     * @param the array of integer numbers
     * @return the average of the numbers
     */
    public float computeAverage(int[] nums) {
        float result = 0;
        float length = nums.length;
        for (int i = 0; i < length; i++) {
            result += nums[i];
        }
        result /= length;
        return result;
    }

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6};
        Average average = new Average();
        System.out.println("The average is " + average.computeAverage(numbers));
    }
}
