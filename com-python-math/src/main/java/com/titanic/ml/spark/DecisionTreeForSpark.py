from pyspark.sql import SparkSession
from pyspark.sql import SQLContext

spark = SparkSession.builder\
        .master("spark://binend01:7077")\
        .appName("DT")\
        .config("spark.some.config.option", "some-value") \
        .getOrCreate()

sc =  spark.sparkContext;
sc.setLogLevel("INFO")
sqlContext=SQLContext(sc)

data = sqlContext.read\
        .format('com.databricks.spark.csv')\
        .option('header', 'true') \
        .option('delimiter', ',')\
        .option("inferschema",'true') \
        .load("hdfs://binend01:8020/dt/xg.csv")

print(data.select("*").show)
# data.registerTempTable('data')
# sqlContext.sql("select * from data" ).show



spark.stop



