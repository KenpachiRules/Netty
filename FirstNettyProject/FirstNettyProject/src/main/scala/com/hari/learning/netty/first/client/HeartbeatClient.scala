package com.hari.learning.netty.first.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.{ ChannelInitializer, ChannelHandlerContext }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import com.hari.learning.netty.first.client.handler.HeartbeatClientHandler
import java.util.UUID

/**
 * Client whichs send heart beats to HeartbeatServer to stay connected
 * with the server.
 *
 * @author harim
 */

class HeartbeatClient(clientId: String, host: String, port: Int, hbClientHandler: HeartbeatClientHandler) {

  def connect: Unit = {
    val boots = new Bootstrap
    val loopGrp = new NioEventLoopGroup()
    val test = boots.group(loopGrp).remoteAddress(host, port).handler(new ChannelInitializer[SocketChannel] {
      override def initChannel(channel: SocketChannel) = {
        channel.pipeline().addLast(hbClientHandler)
      }
    })
    val connectFut = boots.connect().sync()
    connectFut.channel().closeFuture().sync()
  }

}

object HeartbeatClient {

  def apply(id: String, host: String, port: Int): HeartbeatClient = new HeartbeatClient(id, host, port, HeartbeatClientHandler(id))
}


