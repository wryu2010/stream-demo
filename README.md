[TOC]
# 为什么需要Stream
Java 8提供的Stream与 java.io包里的InputStream和OutputStream是完全不同的概念，是对集合对象功能的增强。

# 如何使用Stream
## 转换
### 将多个值转换成Stream

```java
IntStream stream = IntStream.of(1, 2, 3);
stream.forEach(System.out::println);
```
### 将数组转换成Stream
```java
String[] arrays = new String[]{"a", "b", "c"};
Stream arraysStream = Arrays.stream(arrays);
```

### 将Stream转换成数组
```java
String[] strArray1 = (String[]) arraysStream.toArray(String[]::new);
```

### 将List集合转换成Stream
```java
List<String> list = new ArrayList<>();
list.add("list1");
list.add("list2");
list.add("list3");
Stream<String> listStream = list.stream();
```

### 将Stream转换成List
转换之后Stream就失效了，不能重复调用collect方法。
```java
List<String> newList = listStream.collect(Collectors.toList());
//等同 List<String> newList = listStream.collect(Collectors.toCollection(ArrayList::new));
```

### 将set集合构造成Stream
```java
Set<String> set = new HashSet<>();
set.add("set1");
set.add("set2");
set.add("set3");
Stream<String> setStream = set.stream();
```

### 将Stream转换成Set
转换之后Stream就失效了，不能重复调用collect方法。
```java
Set<String> newSet = setStream.collect(Collectors.toSet());
//等同 Set<String> newSet = setStream.collect(Collectors.toCollection(HashSet::new));
```

## 映射
### map
map方法把listStream的每一个元素，映射成另外一个元素，并返回一个新的Stream。下面的例子将Stream中的元素都转成大写。
```java
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
list.add("c");
Stream<String> listStream = list.stream();

List<String> newList = listStream.map(String::toUpperCase).collect(Collectors.toCollection(ArrayList::new));//"A","B","C"
```

### flatMap
flatMap方法将listStream1的元素执行stream方法后抽出来放到一起，并返回一个新的Stream，可以用来实现一对多转换的需求。
```java
Stream<List<Integer>> listStream1 = Stream.of(Arrays.asList(3, 4), Arrays.asList(1, 2), Arrays.asList(5, 6));
List<Integer> newList1 = listStream1.flatMap(Collection::stream).collect(Collectors.toList());//3,4,1,2,5,6
```

## 过滤
过滤出符合条件的元素组成新的Stream并返回这个Stream。
```java
String line = "abc,d,ef,g,hijk,,lmnopqrst,uvwxyz";
Stream<String> stream = Arrays.stream(line.split(","));
stream.filter(s -> s.length() > 0).forEach(System.out::println);//"abc","d","ef","g","hijk","lmnopqrst","uvwxyz"
```

## 遍历
### forEach
forEach方法接收一个Lambda表达式，然后在Stream的每一个元素上执行该表达式，无返回值。注意不能对同一Stream连续调用多次forEach方法，不能修改自己包含的值，也不能用break/return之类的关键字提前结束循环。
```java
Stream<String> stream1 = Stream.of("a", "c", "d");
stream1.forEach(System.out::println);//"a", "c", "d"
```
### peek
peek方法接收一个Lambda表达式，然后在Stream的每一个元素上执行该表达式，返回一个新的Stream实例。可以对同一Stream连续调用多次peek方法。
```java
Stream<String> stream2 = Stream.of("d","e");
stream2.peek(System.out::println)
                .map(String::toUpperCase)
                .peek(System.out::println)
        .collect(Collectors.toList());//"d","D","e","E"
```

## 运算
reduce方法会把Stream元素通过指定的运算方式组合起来，如果有起始值，返回组合后的数据类型的值，否则返回Optional实例。
```java
// 字符串连接
String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);//ABCD

//求最小值，起始值为Double.MAX_VALUE
double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);//-3.0

// 求和，初始值为0
int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);//10

// 求和，无初始值
sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();//10
```

## 限制和忽略
limit方法返回Stream的前面N个元素，skip方法则是扔掉前N个元素
```java
Stream<String> stream = Stream.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
stream.limit(10).skip(3).forEach(System.out::println);// "4", "5", "6", "7", "8", "9", "10"
```

## 排序和去重
sorted方法可以对Stream里的元素进行排序。可以先对Stream进行map、filter、limit、skip、distinct减少元素数量后再进行排序。
```java
Stream<Integer> stream = Stream.of(2,1, 1, 5, 6, 8, 10, 9, 3, 4, 7, 3);
stream.limit(5).distinct().sorted(Integer::compareTo).forEach(System.out::println);//1,2,5,6
```

## 最小值
 Min方法获取Stream中最小的元素，返回Optional实例。
```java
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 78);
Integer min = stream.min(Integer::compareTo).get();
```
## 最大值
Max方法获取Stream中最大的元素，返回Optional实例。
```java
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 78);
Integer max = stream.max(Integer::compareTo).get();
```

## 判断匹配
### allMatch
如果Stream中全部元素符合传入的predicate，allMatch方法返回true。
```java
List<User> users = new ArrayList<>();
users.add(new User("name1", 10));
users.add(new User("name2", 20));
users.add(new User("name3", 30));
users.add(new User("name4", 40));

Boolean result = users.stream().allMatch(u -> u.getAge() > 20);//true
```

### anyMatch
如果Stream中只要有一个元素符合传入的predicate，anyMatch方法返回true。
```java
result = users.stream().anyMatch(u -> u.getAge() > 20);//false
```
### noneMatch
如果Stream中没有一个元素符合传入的predicate，noneMatch方法返回true
```java
result = users.stream().noneMatch(u -> u.getAge() > 20);
```
