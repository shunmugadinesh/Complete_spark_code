package org.inceptez.streaming
import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.streaming._;

object filestream {
  def main(args:Array[String])=
  {
    val spark=SparkSession.builder().appName("spark streaming").master("local[*]")
    .getOrCreate()
        
    val sc=spark.sparkContext
    val sqlc=spark.sqlContext
    sc.setLogLevel("Error")
    
    val ssc=new StreamingContext(sc,Seconds(10));
    //8.10.00 -> 8.10.10
    //8.10.11 -> 8.10.20
    //dstream -> discretized stream -> micro batch rdds of x seconds
    //val linesrdd=sc.textFile("file:///home/hduser/sparkdata/streaming/")
   
    val lines = ssc.textFileStream("file:///home/hduser/sparkdata/streaming/");
    //val lines =ssc.socketTextStream("localhost",9999)
   //dstream -> Sequence of RDD (1 RDD, multiple RDDs)

    import spark.implicits._;

    lines.foreachRDD(x=>
        {
         val  courses = x.flatMap(_.split(" "))
         val df1=courses.toDF("coursename")
         df1.createOrReplaceTempView("fileview")
         spark.sql("select coursename,count(1) from fileview group by coursename").show(50,false)
         // rdd1 -> hadoop spark
         //hadoop,infa
         // rdd2 -> spark ds
         //ds ds
         //courses ->  
         //hadoop
         //spark
         //hadoop
         //infa
         //spark
         //ds
         //ds
         //ds
         
        }    
    )
    
/*   val dstream1=Array("rdd1","rdd2")
   val rdd1=dstream1.foreach(x => {
     val elements=x.contains("rdd")
     if(elements)
     {
       println("i have an rdd")
     }
   })*/
   

//import spark.implicits._;
//val df1=lines.toDF();
    
    ssc.start();
    ssc.awaitTermination();
    // 9am -> 11 am, 2pm -> 4pm, 8PM -> 10pm
    
    
  }
}