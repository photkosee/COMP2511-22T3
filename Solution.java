import java.util.ArrayList;
import java.util.List;

class Solution {
    public int singleNumber(int[] nums) {
        List<Integer> list = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!list.contains(nums[i])) {
                list.add(nums[i]);
                result.add(nums[i]);
            } else {
                result.remove(nums[i]);
            }
        }
        return result.get(0);
    }
}