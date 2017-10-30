package com.spark.sql2;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.current_date;

/**
 * 资金额度异常比对
 */
public class FinancialContrastSpark
{
    public static String user = "root";
    public static String pwd = "TYKYadmin@258";
    public static String ip = "10.22.55.30:3306";
    public static String driver = "com.mysql.jdbc.Driver";
    public static String db = "hnrescat";
    public static String url = "jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pwd + "&useUnicode=true&characterEncoding=UTF8";

    public static void main(String[] args)
    {

        SparkSession spark = SparkSession.builder().
                master("spark://tyky28:7077").
                appName("FinancialContrastSpark").
                getOrCreate();

//        provinceToCity(spark);
//        cityToXian(spark);
        shengToXian(spark);
//        countBiuErr(spark);
//        projectErr(spark);


        spark.stop();
    }


    /**
     * 项目受益异常
     * @param spark
     */
    private static void projectErr(SparkSession spark)
    {
        Map<String, String> itemParam = new HashMap<String, String>();
        itemParam.put("url", url);
        itemParam.put("driver", driver);
        itemParam.put("dbtable", "funds_item");

        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("url", url);
        sysParam.put("driver", driver);
        sysParam.put("dbtable", "sys_funds_detailed");

        Map<String, String> fundsdetail = new HashMap<String, String>();
        fundsdetail.put("url", url);
        fundsdetail.put("driver", driver);
        fundsdetail.put("dbtable", "funds_item_detail");

        //乘以10000
        Dataset<Row> itemDS = spark.read().format("jdbc").options(itemParam).load().select(col("funds_code"), col("money").multiply(10000));
        //乘以后会有别名，as重新命名money
        Dataset<Row> itemDSmoney =  itemDS.select(col("funds_code"),col("(money * 10000)").as("money"));
        //统计money
        Dataset<Row> itemGroupDS = itemDSmoney.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        //sum函数之后重新命名
        Dataset<Row> itemAsDS = itemGroupDS.select(col("funds_code"), col("sum(money)").as("money"));

        Dataset<Row> sysDS = spark.read().format("jdbc").options(sysParam).load().select(col("funds_code"), col("center_money").as("money"));
        Dataset<Row> sysTmp =  sysDS.select(col("funds_code"),col("money").multiply(10000));
        Dataset<Row> sysDSmoney = sysTmp.select(col("funds_code"),col("(money * 10000)").as("money"));
        Dataset<Row> sysGroupDS = sysDSmoney.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> sysAsDs = sysGroupDS.select(col("funds_code"), col("sum(money)").as("money"));

        Dataset<Row> fundsDS = spark.read().format("jdbc").options(fundsdetail).load().select(col("funds_code"), col("money"));
        Dataset<Row> fundsGroupDS =fundsDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> fundsAsDs = fundsGroupDS.select(col("funds_code"), col("sum(money)").as("money"));

        Dataset<Row> unDS =  sysAsDs.union(fundsAsDs);

        Dataset<Row> unGroupDS =unDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> unAsDs = unGroupDS.select(col("funds_code"), col("sum(money)").as("money"));

        Dataset<Row> resultDS = itemAsDS.except(unAsDs);

        Dataset<Row> data = resultDS.select(col("funds_code"), col("money"), current_date()).withColumnRenamed("current_date()", "result_date");
        data.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "r_project_money_err")
                .mode(SaveMode.Append)
                .save();

    }

    /**
     * 资金发放总额异常
     */
    private static void countBiuErr(SparkSession spark)
    {
        //总额
        Map<String, String> countParam = new HashMap<String, String>();
        countParam.put("url", url);
        countParam.put("driver", driver);
        countParam.put("dbtable", "funds_gone");

        //个人
        Map<String, String> peopleParam = new HashMap<String, String>();
        peopleParam.put("url", url);
        peopleParam.put("driver", driver);
        peopleParam.put("dbtable", "funds_people");

        //项目
        Map<String, String> itemParam = new HashMap<String, String>();
        itemParam.put("url", url);
        itemParam.put("driver", driver);
        itemParam.put("dbtable", "funds_item");

        Dataset<Row> countDS = spark.read().format("jdbc").options(countParam).load().select(col("funds_code"), col("issued_money").as("money"));
        Dataset<Row> countGroupDS = countDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> countAsDS =  countGroupDS.withColumnRenamed("sum(money)", "money");

        Dataset<Row> peopleDS = spark.read().format("jdbc").options(peopleParam).load().select(col("funds_code"), col("money")).repartition(64);
        Dataset<Row> peopleGroupDS = peopleDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> peopleAsDS = peopleGroupDS.withColumnRenamed("sum(money)", "money");

        Dataset<Row> itemDS = spark.read().format("jdbc").options(itemParam).load().select(col("funds_code"), col("money"));
        Dataset<Row> itemGroupDS = itemDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> itemAsDS = itemGroupDS.withColumnRenamed("sum(money)", "money");

        //合并总额和个人
        Dataset<Row> unDS =  peopleAsDS.union(itemAsDS);// 7505

        //合并之后在把funds_code相同的金额相加
        Dataset<Row> unGroupDS = unDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("money"));
        Dataset<Row> unAsDS = unGroupDS.withColumnRenamed("sum(money)", "money");

        unAsDS.createOrReplaceTempView("unAsDS");
        countAsDS.createOrReplaceTempView("countAsDS");
        Dataset<Row> moneyDS =  countAsDS.sqlContext().sql("SELECT t1.funds_code AS g_funds_code,t1.money AS g_money,t2.funds_code AS s_funds_code,t2.money AS s_money FROM unAsDS t1,countAsDS t2 WHERE t1.funds_code = t2.funds_code");


        //取差集
        Dataset<Row> countResultDS =  countAsDS.except(unAsDS);
        Dataset<Row> resulrDS =  countResultDS.select(col("funds_code"),current_date()).withColumnRenamed("current_date()", "result_date");
        resulrDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "r_count_money_err")
                .mode(SaveMode.Append)
                .save();

        moneyDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "r_count_money_result")
                .mode(SaveMode.Append)
                .save();

    }


    /**
     * 省资金拨付县总额异常
     * @param spark
     */
    private static void shengToXian(SparkSession spark)
    {
        Map<String, String> xzqhMap = new HashMap<String, String>();
        xzqhMap.put("url", url);
        xzqhMap.put("driver", driver);
        xzqhMap.put("dbtable", "sys_xzqh");

        Map<String, String> shengParam = new HashMap<String, String>();
        shengParam.put("url", url);
        shengParam.put("driver", driver);
        shengParam.put("dbtable", "funds_source");

        Map<String, String> xianParam = new HashMap<String, String>();
        xianParam.put("url", url);
        xianParam.put("driver", driver);
        xianParam.put("dbtable", "funds_gone");

        Dataset<Row> xzqhDS = spark.read().format("jdbc").options(xzqhMap).load().select(col("code").as("pxzqh_code")).where(col("level_type").isin("3"));

        List<Row> list = xzqhDS.collectAsList();
        List<String> listStr = new ArrayList<String>();
        for (Row r : list)
        {
            listStr.add(r.mkString());
        }

        //县的来源
        Dataset<Row> shengDS = spark.read().format("jdbc").options(shengParam).load().select(col("p_funds_code").as("funds_code"), col("source_money"))
                .where(col("pxzqh_code").isin(listStr.toArray()).and(col("p_funds_code").substr(3, 2).isin("00")));
        System.out.println(shengDS.count());
        //省的去向
        Dataset<Row> xianDS = spark.read().format("jdbc").options(xianParam).load().select(col("funds_code"), col("issued_money").as("source_money"))
                .where(col("xzqh_code").isin(listStr.toArray()).and( col("funds_code").substr(3, 2).isin("00")));
        System.out.println(xianDS.count());
        Dataset<Row> shengGroupDS = shengDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("source_money"));
        Dataset<Row> xianGroupDS = xianDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("source_money"));


        Dataset<Row> shengResultDS = shengGroupDS.except(xianGroupDS);


        Dataset<Row> result = shengResultDS.select(col("funds_code"), col("sum(source_money)"), current_date()).withColumnRenamed("sum(source_money)", "source_money").withColumnRenamed("current_date()", "result_date");
        result.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "r_province_xian")
                .mode(SaveMode.Append)
                .save();
    }


    /**
     * 市资金拨付县总额异常
     *
     * @param spark
     */
    private static void cityToXian(SparkSession spark)
    {
        Map<String, String> xzqhMap = new HashMap<String, String>();
        xzqhMap.put("url", url);
        xzqhMap.put("driver", driver);
        xzqhMap.put("dbtable", "sys_xzqh");

        //来源表
        Map<String, String> cityParam = new HashMap<String, String>();
        cityParam.put("url", url);
        cityParam.put("driver", driver);
        cityParam.put("dbtable", "funds_source");

        Map<String, String> xianParam = new HashMap<String, String>();
        xianParam.put("url", url);
        xianParam.put("driver", driver);
        xianParam.put("dbtable", "funds_gone");

        Dataset<Row> xzqhDS = spark.read().format("jdbc").options(xzqhMap).load().select(col("code").as("pxzqh_code")).where(col("level_type").isin("3"));

        List<Row> list = xzqhDS.collectAsList();
        List<String> listStr = new ArrayList<String>();
        for (Row r : list)
        {
            listStr.add(r.mkString());
        }

        //县的来源
        Dataset<Row> cityDS = spark.read().format("jdbc").options(cityParam).load().select(col("p_funds_code").as("funds_code"), col("source_money"))
                .where(col("pxzqh_code").isin(listStr.toArray()).and(col("p_funds_code").substr(5, 3).isin("000")).and(col("p_funds_code").substr(3, 2).notEqual("00")));

        Dataset<Row> cityGroup = cityDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("source_money"));

        //市的去向
        Dataset<Row> xianDS = spark.read().format("jdbc").options(xianParam).load().select(col("funds_code"), col("issued_money").as("source_money"))
                .where(col("xzqh_code").isin(listStr.toArray()).and(col("funds_code").substr(5, 3).isin("000")).and(col("funds_code").substr(3, 2).notEqual("00")));

        Dataset<Row> xianGroup = xianDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("source_money"));

        //取差集
        Dataset<Row> xianResultDS = cityGroup.except(xianGroup);

        Dataset<Row>  resultDS = xianResultDS.select(col("funds_code"), col("sum(source_money)"), current_date()).withColumnRenamed("sum(source_money)", "source_money").withColumnRenamed("current_date()", "result_date");
        resultDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "r_city_xian")
                .mode(SaveMode.Append)
                .save();

    }

    /**
     * 省发放到市资金异常对比
     */
    private static void provinceToCity(SparkSession spark)
    {
        //来源表
        Map<String, String> provinceParam = new HashMap<String, String>();
        provinceParam.put("url", url);
        provinceParam.put("driver", driver);
        provinceParam.put("dbtable", "funds_source");

        Map<String, String> cityParam = new HashMap<String, String>();
        cityParam.put("url", url);
        cityParam.put("driver", driver);
        cityParam.put("dbtable", "funds_gone");

        //获取省数据
        Dataset<Row> provinceDS = spark.read().format("jdbc").options(provinceParam).load().select(col("p_funds_code").as("funds_code"), col("source_money")).where(col("pxzqh_code").like("%00000000").and(col("p_funds_code").like("4300%")));

        //获取市数据
        Dataset<Row> cityDS = spark.read().format("jdbc").options(cityParam).load().select(col("funds_code"), col("issued_money").as("source_money")).where(col("xzqh_code").like("%00000000"));

        //统计funds_code相同，的source_money总额
        Dataset<Row> groupProvinceDS = provinceDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("source_money"));
        Dataset<Row> groupCityDS = cityDS.groupBy("funds_code").agg(org.apache.spark.sql.functions.sum("source_money"));

        //市的去向（取两个数据集中，不同的数据）
        Dataset<Row> cityResultDS = groupProvinceDS.except(groupCityDS);

        //添加计算时间
        Dataset<Row> addTimecityDS =  cityResultDS.select(col("funds_code"), col("sum(source_money)"), current_date());

        Dataset<Row> cityAddTimeDS = addTimecityDS.withColumnRenamed("sum(source_money)", "source_money").withColumnRenamed("current_date()", "result_date");
        cityAddTimeDS.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "r_province_city")
                .mode(SaveMode.Append)
                .save();

    }

}
