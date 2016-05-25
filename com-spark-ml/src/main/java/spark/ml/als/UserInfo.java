package spark.ml.als;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;


/**
 *用户信息统计
 */
public class UserInfo
{
    public static void main(String[] args)
    {
//        init();
    }

    private static void init()
    {
        SparkConf conf = new SparkConf();
        conf.setAppName("ALS");
        conf.setMaster("spark://localhost:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.addJar("/home/titanic/soft/Work_Intellij/20151106/com-spark-ml/target/com-spark-ml-0.0.1-SNAPSHOT.jar");

        //resources目录下数据文件
        JavaRDD<String> userDataRDD = jsc.textFile("/home/titanic/soft/Work_Intellij/20151106/com-spark-ml/src/main/resources/u.user");
        String userDataStr = userDataRDD.first();
        System.out.println("初始化用户数据如下");
        System.out.println("id|年龄|性别|职业|邮政编码");
        System.out.println(userDataStr);

        //过滤数据格式
        JavaRDD<String[]> userFields = userDataRDD.map(new Function<String, String[]>()
        {
            public String[] call(String s) throws Exception
            {
                return s.split("\\|");
            }
        });
        String[] userFieldsStr = userFields.first();
        System.out.println("过滤之后的数据如下");
       for(String s : userFieldsStr)
       {
           System.out.print(s + ",");
       }
        //count有多少个用户
        JavaRDD<String> countUserRDD  = userFields.map(new Function<String[], String>()
        {
            public String call(String[] strings) throws Exception
            {
                return strings[0];
            }
        });
        long countUser = countUserRDD.count();


        //性别有多少
        JavaRDD<String> numGendersRDD = userFields.map(new Function<String[], String>()
        {
            public String call(String[] strings) throws Exception
            {
                return strings[2];
            }
        });

        long numGenders = numGendersRDD.distinct().count();

        //有多少职业
        JavaRDD<String> numOccuptionsRDD = userFields.map(new Function<String[], String>()
        {
            public String call(String[] strings) throws Exception
            {
                return strings[3];
            }
        });
        long numOccuptions = numOccuptionsRDD.distinct().count();

        //有多少邮政编码
        JavaRDD<String> numZIPCodeRDD = userFields.map(new Function<String[], String>()
        {
            public String call(String[] strings) throws Exception
            {
                return strings[4];
            }
        });
        long numZIPCode = numZIPCodeRDD.distinct().count();

        System.out.println("用户有：" + countUser+", 性别有： " +numGenders +", 职业有: "+numOccuptions+", 邮政编码有: "+numZIPCode);







    }
}
