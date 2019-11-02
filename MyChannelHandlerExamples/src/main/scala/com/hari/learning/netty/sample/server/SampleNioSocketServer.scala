package com.hari.learning.netty.sample.server

import io.netty.channel.nio.NioEventLoopGroup
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.ChannelInitializer
import com.hari.learning.netty.channel.server.handler.EchoServerHandler

class SampleNioSocketServer {

  def start(port: Int): Unit = {
    val serverBs = new ServerBootstrap
    val eventLoopGrp = new NioEventLoopGroup
    try {
      serverBs.group(eventLoopGrp).localAddress(port).channel(classOf[ServerSocketChannel]).childHandler(new ChannelInitializer[ServerSocketChannel]() {
        override def initChannel(ch: ServerSocketChannel): Unit = {
           ch.pipeline().addLast(new EchoServerHandler())
        }
      })
      val chFuture = serverBs.bind.sync
      chFuture.channel.closeFuture.sync()
    } finally {
      eventLoopGrp.shutdownGracefully()
    }
  }

}