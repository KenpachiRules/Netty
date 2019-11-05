package com.hari.learning.netty.server

import io.netty.channel.{ Channel, ChannelHandlerContext, ChannelInitializer }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.{ NioServerSocketChannel, NioSocketChannel }
import io.netty.bootstrap.ServerBootstrap
import com.hari.learning.netty.handler.server.EchoHandler
import io.netty.util.concurrent.{ Future, GenericFutureListener }

class MyServer(port: Int) {

  import com.hari.learning.utils.scala.MyLogger.{ logInfo }
  private val info = logInfo(this.getClass.getCanonicalName)

  def start: Unit = {
    info("----- Starting server -----")
    val serverBs = new ServerBootstrap()
    val eventLoopGrp = new NioEventLoopGroup()
    try {
      serverBs.group(eventLoopGrp).localAddress(port).channel(classOf[NioServerSocketChannel]).childHandler(
        new ChannelInitializer[NioSocketChannel]() {
          override def initChannel(ch: NioSocketChannel): Unit = {
            ch.pipeline().addLast(new EchoHandler())
          }
        })
      val channelFut = serverBs.bind.sync
      channelFut.channel().closeFuture.sync
    } finally {
      eventLoopGrp.shutdownGracefully()
    }
  }

}

object MyServer {

  def startServer(port: Int): Unit = new MyServer(port).start

}