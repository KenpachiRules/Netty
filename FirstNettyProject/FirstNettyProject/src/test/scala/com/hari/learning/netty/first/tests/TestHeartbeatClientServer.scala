package com.hari.learning.netty.first.tests

import com.hari.learning.netty.first.client.HeartbeatClient
import com.hari.learning.netty.first.server.HeartbeatServer
import com.hari.learning.utils.scala.MyLogger.{ logInfo, logSevere }
import org.testng.annotations.{ BeforeTest, AfterTest, BeforeSuite, Test }
import io.netty.bootstrap.{ ServerBootstrap, Bootstrap }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.{ ChannelInitializer, ChannelHandlerContext, ChannelFuture }
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import com.hari.learning.netty.first.server.handler.HeartbeatServerHandler
import io.netty.util.concurrent.GenericFutureListener
import java.util.concurrent.{ ExecutorService, Executors, Future }
import java.lang.Runnable
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

/**
 * Test client and server hearbeat
 *
 * @author harim
 */

class TestHeartbeatClientServer {

  var sockBs: Option[ServerBootstrap] = None
  var clientBs: Option[Bootstrap] = None
  val info = logInfo(this.getClass.getCanonicalName)
  val error = logSevere(this.getClass.getCanonicalName)
  val serverPort = 12345
  val remoteAddress = "10.80.22.121" // my ipv4 - subject to change.
  val executor: ExecutorService = Executors.newFixedThreadPool(1)
  var serverRunnableFut: Option[Future[_]] = None

  @BeforeSuite
  def initServer: Unit = {
    serverRunnableFut = Some(executor.submit(new Runnable() {
      override def run: Unit = {
        HeartbeatServer.start(serverPort)
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
            ch.pipeline().addLast(TestHeartbeatClientHandler.newInstance())
          }
        })
      val clientBind = clientBs.connect().sync
      clientBind.channel.closeFuture.sync
    } finally {
      clientLoop.shutdownGracefully().sync
    }
  }

  def de_initServer: Unit = {
    serverRunnableFut.map(fut => if (fut.isDone()) info("Server shutdown gracefully") else error("Server shutdown failed."))
    executor.shutdown
  }

}