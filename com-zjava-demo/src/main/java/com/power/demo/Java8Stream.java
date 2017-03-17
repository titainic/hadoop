//package com.power.demo;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * java8操作集合，类似spark rdd并行操作
// */
//public class Java8Stream
//{
//    public static void main(String[] args)
//    {
//        try
//        {
//            String contexts = new String(Files.readAllBytes(Paths.get("/home/titanic/soft/hadoop/hadoop-2.6.4/README.txt")), "UTF-8");
//            List<String> words = Arrays.asList(contexts.split("/n"));
//
//            //并行化的流
//            Stream wStream = words.parallelStream();
//            Stream<Map<String, Integer>> mapStream = wStream.flatMap(new Function()
//            {
//                public Map<String, Integer> apply(Object o)
//                {
//                    String[] arrsString = ((String) o).split(" ");
//                    Map<String, Integer> m = new HashMap<String, Integer>();
//                    for (String s : arrsString)
//                    {
//                        m.put(s, 1);
//                    }
//                    return m;
//                }
//            });
//
//            final int[] tmp_count = {0};
//            Files.readAllLines(Paths.get("D:tmpCDMI.txt"))
//                    .stream()
//                    .flatMap(x -> Stream.of(x.trim().split("")))
//                    .sorted()
//                    .reduce((x, y) ->
//                    {
//                        if (x.equals(y))
//                        {
//                            tmp_count[0] += 1;
//                        } else
//                        {
//                            System.out.println(x + "," + tmp_count[0]);
//                            tmp_count[0] = 1;
//                        }
//                        return y;
//                    });
//
//
//            for (Map<String, Integer> m : list)
//            {
//                for (Map.Entry<String, Integer> entry : m.entrySet())
//                {
//                    System.out.println("key:" + entry.getKey());
//                    System.out.println("key:" + entry.getValue());
//                }
//            }
//
//
////            List<Object> list =mapStream.collect(Collectors.toList());
////
////            for (Object o : list)
////            {
////                Map<String,Integer> xmap = (Map<String,Integer>)o;
////
////
////            }
//
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
