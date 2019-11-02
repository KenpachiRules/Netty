package com.hari.learning.netty.myhandler

import io.netty.channel.{ ChannelInboundHandlerAdapter, ChannelHandlerContext }
import io.netty.buffer.ByteBuf
import com.hari.learning.utils.scala.MyLogger.{ logInfo, logSevere }

/***
 * Custom ChannelInboundHandlerAdapter
 * @author harikrishna 
 */

trait MyHandler[T <: ByteBuf] extends ChannelInboundHandlerAdapter {
  val info = logInfo("MyHandler")
  val severe = logSevere("MyHandler")

  override def channelRead(ctx: ChannelHandlerContext, obj: Object): Unit = {
    try{
      val msg:T = obj.asInstanceOf[T]
      ctx.fireChannelRead(transformInboundData(ctx,msg))      
    }
    catch {
      case cce:ClassCastException => {
        severe(" Not an instanceof expected type T")
        ctx.close.sync
        }
    }
   
  }

  def transformInboundData(ctx: ChannelHandlerContext, msg: T): T

}