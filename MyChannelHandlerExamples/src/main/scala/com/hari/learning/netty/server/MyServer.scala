package com.hari.learning.netty.server

import io.netty.channel.{ ChannelHandlerContext, ChannelInitializer }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.ServerSocketChannel
import io.netty.bootstrap.ServerBootstrap
import com.hari.learning.netty.handler.server.EchoHandler

class MyServer(port: Int) {

  def start: Unit = {
    val serverBs = new ServerBootstrap()
    val eventLoopGrp = new NioEventLoopGroup()
    try {
      serverBs.group(eventLoopGrp).localAddress(port).channel(classOf[NioServerSocketChannel]).handler(
        new ChannelInitializer[ServerSocketChannel]() {
          override def initChannel(ch: ServerSocketChannel): Unit = {
            ch.pipeline().addLast(new EchoHandler())
          }
        })
      val serverChannelFuture = serverBs.bind.sync
      serverChannelFuture.channel.closeFuture.sync
    } finally {
      eventLoopGrp.shutdownGracefully()
    }
  }

}

object MyServer {

  def startServer(port: Int): Unit = new MyServer(port).start

}