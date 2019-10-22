package com.hari.learning.netty.first

import io.netty.channel.{ ChannelInboundHandlerAdapter, ChannelHandlerContext }

class HeartbeatHandler extends ChannelInboundHandlerAdapter {

  override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {

  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {

  }
}