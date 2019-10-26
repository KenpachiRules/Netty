package com.hari.learning.nio.netty.client

import io.netty.channel.{ ChannelHandlerContext, ChannelHandler }
import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import java.net.InetSocketAddress
import io.netty.channel.socket.nio.NioSocketChannel

class NioClient(server: String, port: Int) {

  def connect(channelHandler: ChannelHandler = NioClientHandler()): Unit = {
    val bs = new Bootstrap
    val eventLoopGrp = new NioEventLoopGroup
    try {
      bs.group(eventLoopGrp).remoteAddress(new InetSocketAddress(server, port))
        .channel(classOf[NioSocketChannel])
        .handler(channelHandler)
      val channelFut = bs.connect.sync
      channelFut.channel.closeFuture().sync
    } finally {
      eventLoopGrp.shutdownGracefully()
    }
  }
}

object NioClient {
  def connectToServer(server: String, port: Int): Unit = new NioClient(server, port).connect()
}