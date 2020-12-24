package com.cvte.cheetah.server.controller

import java.util

import org.apache.zeppelin.client.websocket.SimpleMessageHandler
import org.apache.zeppelin.client.{ClientConfig, ZSession}
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, ResponseBody, RestController}

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
 * @Author mengxp 
 * @Date 2020/5/12 5:25 下午 
 * @Version 1.0 
 */

@RestController
class ConfigController {

  @RequestMapping(Array("/config"))
  @ResponseBody
  def hello():String={
    var zession:ZSession=null
    try {
      val clientConfig = new ClientConfig("http://psd-hadoop036:8083")
      //设置flink.conf的配置
      val initProperties=new mutable.HashMap[String,String]()
     /* initProperties.put("flink.yarn.appName","zpclientSession")
      initProperties.put("flink.execution.mode","yarn")*/
      zession = ZSession.builder().
        setClientConfig(clientConfig)
        .setInterpreter("flink")
        .setIntpProperties(initProperties.asJava)
        .build()
      //zession.getZeppelinClient.login("admin","mengxp")
      zession.getZeppelinClient.login("admin","mengxp")
      zession.start(new SimpleMessageHandler())
      System.out.println("Flink Web UI: " + zession.getWeburl)


      val sql0=
        """
          |DROP TABLE IF EXISTS `datagen`
          |""".stripMargin

      val sql1=
        """
          |CREATE TABLE datagen (
          | f_sequence INT,
          | f_random INT,
          | f_random_str STRING,
          | ts AS localtimestamp,
          | WATERMARK FOR ts AS ts
          |) WITH (
          | 'connector' = 'datagen',
          | -- optional options --
          | 'rows-per-second'='50',
          | 'fields.f_sequence.kind'='sequence',
          | 'fields.f_sequence.start'='1',
          | 'fields.f_sequence.end'='300',
          | 'fields.f_random.min'='1',
          | 'fields.f_random.max'='100000',
          | 'fields.f_random_str.length'='10'
          |)
          |""".stripMargin

      val sql2=
        """
          |select
          |  f_sequence,
          |  f_random,
          |  f_random_str
          |from
          |datagen
          |""".stripMargin
      zession.execute("ssql",sql0)
      zession.execute("ssql",sql1)
      val localProperties: util.Map[String, String] = new util.HashMap[String, String]
      localProperties.put("type", "update")
      zession.execute("ssql",localProperties,sql2).getResults.asScala.foreach(
        rs=>{
          println(rs.getData+"  "+rs.getType)
        }
      )
      println("Flink Web UI: "+zession.getWeburl)
    } catch {
      case e:Throwable => println(e.getMessage)
    }finally {
      if (zession != null) {
        try {
          zession.stop()
        } catch {
          case e:Exception => println(e.getMessage)
        }
      }
    }
    List("hello","world").mkString("=")
  }

}
