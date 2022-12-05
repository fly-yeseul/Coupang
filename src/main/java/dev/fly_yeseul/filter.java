package dev.fly_yeseul;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class filter {
    public static List<Integer> filterOdds(List<Integer> nums) {
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            if (nums.get(i) > 0 && nums.get(i) % 2 != 0) {
                newList.add(nums.get(i));
            }
//            return newList;
        }
        return newList;
    };





    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>();
        for(int i = 0; i < nums.size(); i++) {
            nums.add(i);
        }
        System.out.println(filterOdds(nums));

    }





    public int getLengthOf(String string) {
        return string.length();
    }

}
