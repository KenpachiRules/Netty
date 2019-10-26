package com.hari.learning.nio.netty.client.test

import org.testng.annotations.{ BeforeTest, AfterTest, BeforeSuite, AfterSuite, Parameters }
import io.netty.bootstrap.Bootstrap
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import java.util.concurrent.ExecutorService
import io.netty.bootstrap.ServerBootstrap
import org.testng.annotations.Test
import java.util.concurrent.Future
import com.hari.learning.utils.scala.MyLogger.{ logInfo, logSevere }
import com.hari.learning.nio.netty.server.NioServer
import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.util.CharsetUtil
import io.netty.channel.socket.SocketChannel

class RunHelloWorldTests {

  var sockBs: Option[ServerBootstrap] = None
  var clientBs: Option[Bootstrap] = None
  val info = logInfo(this.getClass.getCanonicalName)
  val error = logSevere(this.getClass.getCanonicalName)
  val serverPort = 12345
  val remoteAddress = "192.168.0.108" // my ipv4 - subject to change.
  val executor: ExecutorService = Executors.newFixedThreadPool(1)
  var serverRunnableFut: Option[Future[_]] = None
  val messages: Iterator[ByteBuf] = Range(1, 10).map(i => Unpooled.copiedBuffer(i.toString, CharsetUtil.UTF_8)).iterator

  @BeforeSuite
  def initServer: Unit = {
    serverRunnableFut = Some(executor.submit(new Runnable() {
      override def run: Unit = {
        NioServer.start(serverPort)
      }
    }))
  }

  @Test
  def initiaiteClientServerInteraction: Unit = {
    // start server first.
    var clientLoop = new NioEventLoopGroup()
    try {
      val clientBs = new Bootstrap().group(clientLoop)
        .remoteAddress(new InetSocketAddress(remoteAddress, serverPort))
        .channel(classOf[NioSocketChannel])
        .handler(new ChannelInitializer[SocketChannel]() {
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline().addLast(TestNettyClientHandler(messages))
          }
        })
      val clientBind = clientBs.connect().sync
      clientBind.channel.closeFuture.sync
    } finally {
      clientLoop.shutdownGracefully().sync
    }
  }

  @AfterSuite
  def de_initServer: Unit = {
    serverRunnableFut.map(fut => if (fut.isDone()) info("Server shutdown gracefully") else error("Server shutdown failed."))
    executor.shutdown
  }

}