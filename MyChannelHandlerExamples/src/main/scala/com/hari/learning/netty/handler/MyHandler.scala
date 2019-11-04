package com.hari.learning.netty.handler

import io.netty.channel.{ ChannelHandlerContext, ChannelInboundHandlerAdapter }
import io.netty.buffer.ByteBuf
import io.netty.util.ReferenceCountUtil
import com.hari.learning.utils.scala.MyLogger.{ logSevere, logInfo }

trait MyHandler[T <: ByteBuf] extends ChannelInboundHandlerAdapter {

  val error = logSevere(this.getClass.getCanonicalName)
  val info = logInfo(this.getClass.getCanonicalName)

  override def channelRead(ctx: ChannelHandlerContext, obj: Object): Unit = {
    try {
      val transformedMsg = transformInboundData(obj.asInstanceOf[T])
      ReferenceCountUtil.release(obj)
      ctx.fireChannelRead(transformedMsg)
    } catch {
      case cce: ClassCastException =>
        error(s"Received message not of desired type ${cce.getLocalizedMessage}")
        ctx.close
      case t: Throwable =>
        error(s"Failed while transforming message object ${t.getLocalizedMessage}")
        ctx.close
    }
  }

  def transformInboundData(msg: T): T

}