package com.hari.learning.handler.tests

import org.testng.annotations.{ BeforeTest, AfterTest, Test,Parameters}
import com.hari.learning.netty.client.MyClient._
import com.hari.learning.netty.server.MyServer
import com.hari.learning.netty.handler.client.{HelloHandler,ToUpperHandler}
import java.util.concurrent.{Executor,ExecutorService,Executors}
import io.netty.channel.{ChannelInboundHandler,ChannelHandlerContext,ChannelInboundHandlerAdapter}
import io.netty.util.CharsetUtil
import io.netty.buffer.Unpooled

class MyHandlerTests {

  var threadExecs:ExecutorService = Executors.newFixedThreadPool(2)
  var serverPropsInit:Option[(List[ChannelInboundHandler]) => Unit] = None
  
  @BeforeTest
  @Parameters(Array("server_host","server_port"))
  def initializeChannelPipeline(server_host:String,server_port:Int):Unit = {
    serverPropsInit = Some(initPipeline(server_host,server_port))
 }
  
  @BeforeTest
  @Parameters(Array("server_port"))
  def startServer(server_port:Int):Unit = {
    threadExecs.submit( new Runnable(){
      override def run:Unit = MyServer.startServer(server_port)
      })
  }
  
  @Test
  def testMultipleInboundHandlers(server_host:String,server_port:Int): Unit = {
     threadExecs.submit( new Runnable(){
       override def run:Unit = {
        val echoClienHandler = new ChannelInboundHandlerAdapter{
         override def channelActive(ctx:ChannelHandlerContext):Unit = {
           ctx.writeAndFlush(Unpooled.copiedBuffer("Hari",CharsetUtil.UTF_8))
         }
        }
        serverPropsInit.map( f => f(List(HelloHandler(),ToUpperHandler(),echoClienHandler)))
       }
     })
  }
   
}