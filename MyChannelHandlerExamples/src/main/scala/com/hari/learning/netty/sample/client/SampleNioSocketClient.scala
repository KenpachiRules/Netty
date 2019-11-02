package com.hari.learning.netty.sample.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.ChannelInitializer
import com.hari.learning.netty.channel.client.handler.{HelloHandler,UpperCaseHandler}

class SampleNioSocketClient {

  def start(host: String, port: Int): Unit = {
    val bs = new Bootstrap()
    val eventLoopGrp = new NioEventLoopGroup()
    // initialize clients.
    try {
      bs.group(eventLoopGrp).remoteAddress(host, port).channel(classOf[NioSocketChannel])
        .handler(new ChannelInitializer[NioSocketChannel]() {
          def initChannel(ch: NioSocketChannel): Unit = {
             ch.pipeline().addFirst(new HelloHandler)
             ch.pipeline().addLast(new UpperCaseHandler)
          }
        })
      val channelFut = bs.connect.sync
      channelFut.channel.closeFuture.sync
    } finally {
      eventLoopGrp.shutdownGracefully()
    }
  }

}