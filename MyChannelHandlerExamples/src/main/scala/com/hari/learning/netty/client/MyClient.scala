package com.hari.learning.netty.client

import io.netty.channel.{ ChannelHandlerContext, ChannelInitializer, ChannelInboundHandler, ChannelInboundHandlerAdapter }
import io.netty.bootstrap.Bootstrap
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import com.hari.learning.netty.handler.client.{ HelloHandler, ToUpperHandler }
import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.util.CharsetUtil

object MyClient {

  def createPipeline(remoteHost: String, port: Int)(handlers: List[ChannelInboundHandler]): Unit = {
    val bs = new Bootstrap
    val nioEventLoopGrp = new NioEventLoopGroup()
    try {
      bs.group(nioEventLoopGrp).remoteAddress(remoteHost, port).channel(classOf[NioSocketChannel]).handler(
        new ChannelInitializer[SocketChannel]() {
          override def initChannel(sc: SocketChannel): Unit = {
            handlers.foreach(handler => sc.pipeline.addLast(handler))
          }
        })
      val channelFut = bs.connect.sync
      channelFut.channel().closeFuture.sync
    } finally {
      nioEventLoopGrp.shutdownGracefully().sync
    }
  }

  def initPipeline(host: String, port: Int): (List[ChannelInboundHandler]) => Unit = createPipeline(host, port)(_)

}
