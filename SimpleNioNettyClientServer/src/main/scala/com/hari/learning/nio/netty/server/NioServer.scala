package com.hari.learning.nio.netty.server
import io.netty.channel.ChannelHandlerContext
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import java.net.InetSocketAddress

class NioServer(port: Int) {

  def start: Unit = {
    val serverBs = new ServerBootstrap
    val eventLoopGrp = new NioEventLoopGroup
    try {
      serverBs.group(eventLoopGrp).localAddress(new InetSocketAddress(port)).channel(classOf[NioServerSocketChannel])
        .childHandler(new NioServerHandler())
      val channelFut = serverBs.bind().sync()
      channelFut.channel().closeFuture().sync()
    } finally {
      eventLoopGrp.shutdownGracefully()
    }
  }

}

object NioServer {
  def start(port: Int): Unit = new NioServer(port).start
}