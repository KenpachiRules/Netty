package com.hari.learning.nio.netty.server
import io.netty.channel.{ ChannelHandlerContext, ChannelInboundHandlerAdapter }
import io.netty.buffer.{ ByteBuf, ByteBufUtil, Unpooled }
import com.hari.learning.utils.scala.MyLogger.{ logInfo, logSevere }
import com.hari.learning.nio.netty.server.NioServerHandler.{ info, severe }
import io.netty.util.CharsetUtil

class NioServerHandler extends ChannelInboundHandlerAdapter {

  override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {
    require(msg.isInstanceOf[ByteBuf])
    val strVal = new String(ByteBufUtil.getBytes(msg.asInstanceOf[ByteBuf]))
    println(" value --> "+strVal)
    info(s"Received string value is ${strVal}")
    ctx.write(Unpooled.copiedBuffer("ServerAck", CharsetUtil.UTF_8))
  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) // push out all buffered values.
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    severe(s" Channel handler encountered an exception ${cause}  ")
    ctx.close
  }

}

object NioServerHandler {
  val info = logInfo(this.getClass.getCanonicalName)
  val severe = logSevere(this.getClass.getCanonicalName)
}