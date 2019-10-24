package com.hari.learning.netty.first.tests

import com.hari.learning.netty.first.client.HeartbeatClient
import com.hari.learning.netty.first.server.HeartbeatServer
import org.testng.annotations.{ BeforeTest, AfterTest }
import io.netty.bootstrap.{ ServerBootstrap, Bootstrap }
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.{ ChannelInitializer, ChannelHandlerContext }
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import com.hari.learning.netty.first.server.handler.HeartbeatServerHandler

class TestHearbeatClientServer {

  var sockBs: Option[ServerBootstrap] = None
  var clientBs: Option[Bootstrap] = None
  val serverPort = 12345

  def initBootstraps: Unit = {
    val sockBs = Some(new ServerBootstrap().group(new NioEventLoopGroup()).localAddress(serverPort)
      .channel(classOf[NioServerSocketChannel]).handler(new ChannelInitializer[SocketChannel]() {
        override def initChannel(sock: SocketChannel) {
          sock.pipeline().addLast(HeartbeatServerHandler())
        }
      }))

  }

}