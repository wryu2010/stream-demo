package com.wry;

import com.wry.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>Title: Demo</p>
 * <p>Description: </p>
 *
 * @author wenrongyu
 * @date 2020/10/25
 */
public class Demo {


    public static void main(String[] args) {
        //testConversion();
        //testMap();
        //testFilter();
        //testForEach();
        //testReduce();
        //testLimitAndSkip();
        //testSorted();
        //testMin();
        //testMax();
        testMatch();
    }

    private static void testConversion() {
        // 将多个值转换成Stream
        IntStream stream = IntStream.of(1, 2, 3);
        System.out.println("将多个值转换成Stream");
        stream.forEach(System.out::println);

        // 将数组转换成Stream
        String[] arrays = new String[]{"a", "b", "c"};
        Stream arraysStream = Arrays.stream(arrays);

        // 将Stream转换成数组
        String[] strArray1 = (String[]) arraysStream.toArray(String[]::new);
        System.out.println("将Stream转换成数组");
        for (String str : strArray1) {
            System.out.println(str);
        }

        // 将List集合构造成Stream
        List<String> list = new ArrayList<>();
        list.add("list1");
        list.add("list2");
        list.add("list3");
        Stream<String> listStream = list.stream();

        // 将Stream转换成集合，转换之后Stream就失效了，不能重复调用collect方法
        System.out.println("将Stream转换成List");
        List<String> newList = listStream.collect(Collectors.toList());
        newList.forEach(System.out::println);

        //将set集合构造成Stream
        Set<String> set = new HashSet<>();
        set.add("set1");
        set.add("set2");
        set.add("set3");
        Stream<String> setStream = set.stream();

        Set<String> newSet = setStream.collect(Collectors.toSet());
        //Set<String> set1 = setStream.collect(Collectors.toCollection(HashSet::new));
        System.out.println("将Stream转换成Set");
        newSet.forEach(System.out::println);
    }

    private static void testMap() {
        // 将List集合构造成Stream
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Stream<String> listStream = list.stream();
        // map方法把listStream的每一个元素，映射成另外一个元素，并返回一个新的Stream
        List<String> newList = listStream.map(String::toUpperCase).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("调用map方法之后的List");
        newList.forEach(System.out::println);

        // flatMap方法将listStream1的元素抽出来放到一起，并返回一个新的Stream，可以用来实现一对多的需求
        Stream<List<Integer>> listStream1 = Stream.of(Arrays.asList(3, 4), Arrays.asList(1, 2), Arrays.asList(5, 6));
        List<Integer> newList1 = listStream1.flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println("调用flatMap方法之后的List");
        newList1.forEach(System.out::println);

    }

    private static void testFilter() {
        String line = "abc,d,ef,g,hijk,,lmnopqrst,uvwxyz";
        Stream<String> stream = Arrays.stream(line.split(","));
        // 过滤出符合条件的元素组成新的Stream并返回
        stream.filter(s -> s.length() > 0).forEach(System.out::println);
    }

    private static void testForEach() {
        Stream<String> stream1 = Stream.of("a", "c", "d");
        // forEach方法接收一个Lambda表达式，然后在Stream的每一个元素上执行该表达式，无返回值，
        // 注意不能对同一Stream连续调用多次forEach方法，不能修改自己包含的值，也不能用break/return之类的关键字提前结束循环
        System.out.println("forEach遍历Stream");
        stream1.forEach(System.out::println);
        //stream1.forEach(System.out::println);//这里会报stream has already been operated upon or closed

        Stream<String> stream2 = Stream.of("d","e");
        // peek方法接收一个Lambda表达式，然后在Stream的每一个元素上执行该表达式，返回一个新的Stream实例，
        // 可以对同一Stream连续调用多次peek方法
        System.out.println("peek遍历Stream");
        stream2.peek(System.out::println)
                .map(String::toUpperCase)
                .peek(System.out::println)
        .collect(Collectors.toList());
    }

    private static void testReduce() {
        // reduce方法会把Stream元素通过指定的运算方式组合起来，如果有起始值，返回组合后的数据类型的值，否则返回Optional实例

        // 字符串连接
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println("字符串连接："+concat);

        //求最小值，起始值为Double.MAX_VALUE
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        System.out.println("求最小值:"+minValue);

        // 求和，初始值为0
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println("求和，有初始值：" + sumValue);

        // 求和，无初始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        System.out.println("求和，无初始值：" + sumValue);
    }

    private static void testLimitAndSkip() {
        Stream<String> stream = Stream.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        // limit方法返回Stream的前面N个元素，skip方法则是扔掉前N个元素
        stream.limit(10).skip(3).forEach(System.out::println);
    }

    private static void testSorted() {
        Stream<Integer> stream = Stream.of(2,1, 1, 5, 6, 8, 10, 9, 3, 4, 7, 3);

        // sorted方法可以对Stream里的元素进行排序，可以先对Stream进行map、filter、limit、skip、distinct减少元素数量后，再排序
        stream.limit(5).distinct().sorted(Integer::compareTo).forEach(System.out::println);

    }

    private static void testMin() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 78);
        // Min方法获取Stream中最小的元素
        Integer min = stream.min(Integer::compareTo).get();
        System.out.println("最小值："+min);
    }

    private static void testMax() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 78);
        // Max方法获取Stream中最大的元素
        Integer max = stream.max(Integer::compareTo).get();
        System.out.println("最大值："+max);
    }

    private static void testMatch() {
        List<User> users = new ArrayList<>();
        users.add(new User("name1", 10));
        users.add(new User("name2", 20));
        users.add(new User("name3", 30));
        users.add(new User("name4", 40));

        // allMatch方法：Stream中全部元素符合传入的predicate，返回true
        Boolean result = users.stream().allMatch(u -> u.getAge() > 20);
        System.out.println("是否全部的user的age都大于20：" + result);

        // anyMatch方法：Stream中只要有一个元素符合传入的predicate，返回true
        result = users.stream().anyMatch(u -> u.getAge() > 20);
        System.out.println("是否存在user的age大于20：" + result);

        // noneMatch方法：Stream中没有一个元素符合传入的predicate，返回true
        result = users.stream().noneMatch(u -> u.getAge() > 20);
        System.out.println("是否不存在user的age大于20：" + result);
    }
}
