package com.cvte.cheetah.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @Author mengxp
 * @Date 2020/5/12 5:19 下午
 * @Version 1.0
 */
@SpringBootApplication
@EnableScheduling
class SpringBootScalaIntegration

object SpringBootScalaIntegration extends App {
  SpringApplication.run(classOf[SpringBootScalaIntegration])
}
