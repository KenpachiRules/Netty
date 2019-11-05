package com.hari.learning.netty.handler.tests

import org.testng.annotations.{ BeforeTest, AfterTest, Test, Parameters }
import com.hari.learning.netty.client.MyClient._
import com.hari.learning.netty.server.MyServer
import com.hari.learning.utils.scala.MyLogger.{ logSevere, logInfo }
import com.hari.learning.netty.handler.client.{ HelloHandler, ToUpperHandler }
import java.util.concurrent.{ Executor, ExecutorService, Executors, Future }
import io.netty.channel.{ ChannelInboundHandler, ChannelHandlerContext, ChannelInboundHandlerAdapter }
import io.netty.util.{ CharsetUtil, ReferenceCountUtil }
import io.netty.buffer.Unpooled

class MyHandlerTests {

  var threadExecs: ExecutorService = Executors.newFixedThreadPool(2)
  var serverPropsInit: Option[(List[ChannelInboundHandler]) => Unit] = None
  val info = logInfo(this.getClass.getCanonicalName)
  val error = logSevere(this.getClass.getCanonicalName)
  var serverCloseFut: Option[Future[_]] = None

  @BeforeTest
  @Parameters(Array("server_host", "server_port"))
  def initializeChannelPipeline(server_host: String, server_port: Int): Unit = {
    info("Inside init channel pipeline")
    serverPropsInit = Some(initPipeline(server_host, server_port))
    serverCloseFut = Some(threadExecs.submit(new Runnable() {
      override def run: Unit = MyServer.startServer(server_port)
    }))
  }

  @Test
  def testMultipleInboundHandlers: Unit = {
    val runnableFut = threadExecs.submit(new Runnable() {
      override def run: Unit = {
        val printClientHandler = new ChannelInboundHandlerAdapter {
          import io.netty.buffer.ByteBuf
          val iter: Iterator[String] = List("Hari", "Gayathri", "Kunji", "Shan").iterator
          override def channelActive(ctx: ChannelHandlerContext): Unit = {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Aadhi Bhagawan", CharsetUtil.UTF_8))
          }
          override def channelRead(ctx: ChannelHandlerContext, obj: Object): Unit = {
            println(s" Message -> ${obj.asInstanceOf[ByteBuf].toString(CharsetUtil.UTF_8)} ")
            Thread.sleep(1000)
            if (iter.hasNext) {
              ctx.writeAndFlush(Unpooled.copiedBuffer(iter.next, CharsetUtil.UTF_8))
            } else
              ctx.close
            ReferenceCountUtil.release(obj)
          }

        }
        serverPropsInit.map(f => f(List(HelloHandler(), ToUpperHandler(), printClientHandler)))
      }
    })
    runnableFut.get
  }

  @AfterTest
  def de_init_thread_pool: Unit = {
    // At the end of tests de-init the allocated resources.
    serverCloseFut.map(fut => if (fut.isDone()) info("Server shutdown successfully"))
    threadExecs.shutdown()
  }

}