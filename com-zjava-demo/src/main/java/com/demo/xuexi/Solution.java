package com.demo.xuexi;

/**
 给定 nums = [2, 7, 11, 15], target = 9
 因为 nums[0] + nums[1] = 2 + 7 = 9
 所以返回 [0, 1]
 * @param nums
 * @param target
 * @return
 */
public class Solution
{

    public static void main(String[] args)
    {
        int[] nums = {2, 7, 11, 15};
        int target = 22;
        int[] ss = twoSum(nums, target);
        for (int i = 0; i < ss.length; i++)
        {
            System.out.println(ss[i]);
        }
    }



    public static int[] twoSum(int[] nums, int target)
    {
        int[] result = null;
        for (int j = 0; j < nums.length; j++)
        {
            int tmp = nums[j];

            for (int k = j + 1; k < nums.length; k++)
            {
                if (tmp + nums[k] == target)
                {
                    result = new int[]{tmp, nums[k]};
                }
            }
        }
        return result;
    }
}
