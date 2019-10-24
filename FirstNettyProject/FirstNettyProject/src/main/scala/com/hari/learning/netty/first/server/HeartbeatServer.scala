package com.hari.learning.netty.first.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.ChannelHandlerContext
import java.net.InetSocketAddress
import com.hari.learning.netty.first.server.handler.HeartbeatServerHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

/**
 * Receiver at the server side which listens for heartbeats
 * from client.
 *
 * @author harim
 */

class HeartbeatServer {

  def start(port: Int): Unit = {
    val bootstrap = new ServerBootstrap
    val eventLoopGrp = new NioEventLoopGroup
    try {
      bootstrap.group(eventLoopGrp).channel(classOf[NioServerSocketChannel])
        .localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline().addLast(HeartbeatServerHandler()) // adding the necesary handlers to the channel as part of init channel.
          }
        })
      val channelFut = bootstrap.bind().sync()
      channelFut.channel().closeFuture().sync() // this will not exit until the channel is closed.
    } finally {
      eventLoopGrp.shutdownGracefully().sync() // close all the resources gracefully.
    }
  }

}

object HeartbeatServer {

  def start(port: Int): Unit = { new HeartbeatServer().start(port) } // start a heartbeat server.
}

