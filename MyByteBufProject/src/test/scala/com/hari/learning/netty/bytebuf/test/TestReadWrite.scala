package com.hari.learning.netty.bytebuf.test

import org.testng.annotations.{ BeforeTest, AfterTest, BeforeSuite, AfterSuite, Test }
import io.netty.buffer.{ ByteBuf, Unpooled, UnpooledByteBufAllocator }
import io.netty.util.CharsetUtil

class TestReadWrite {

  val HELLO_WORLD = "Hello World!"
  val I_AM_HARI = "I am Hari."
  val helloWorld: ByteBuf = Unpooled.copiedBuffer(HELLO_WORLD, CharsetUtil.UTF_8)
  val iamHari: ByteBuf = Unpooled.copiedBuffer(I_AM_HARI, CharsetUtil.UTF_8)

  @Test
  def simpleReadHW: Unit = {
    assert(helloWorld.isReadable())
    assert(helloWorld.readerIndex() == 0)
    assert(helloWorld.writerIndex() == HELLO_WORLD.length, s"Length of the stored data does not match ${HELLO_WORLD.length()}")
    val destBuf = Unpooled.buffer(11)
    val strVal = destBuf.writeBytes(helloWorld)
    assert(destBuf.readableBytes() == HELLO_WORLD.length)
    assert(
      helloWorld.readerIndex() == HELLO_WORLD.length(),
      s" ReaderIndex --> ${helloWorld.readerIndex} and HelloWorld length is ${HELLO_WORLD.length()} ")
    assert(destBuf.toString(CharsetUtil.UTF_8).equals(HELLO_WORLD))
  }

  @Test
  def testReaderIndex: Unit = {
    // test reader index value.
    try {
      val hwBytes = Unpooled.buffer(HELLO_WORLD.length)
      // if the code executes normally beyond this it means readerIndex has not been moved.
      helloWorld.readBytes(hwBytes)
      new AssertionError("ReaderIndex has not exceeded writerIndex value, this is wrong.")
    } catch {
      case aix: IndexOutOfBoundsException =>
        println(s"As excepted , as no more bytes to be read ${aix.getLocalizedMessage}")
    }
  }

  @Test
  def writeMoreBytesToHW: Unit = {
    helloWorld.writeBytes(iamHari)
    println(s"Printing the value of copied bytebuf => ${helloWorld.toString(CharsetUtil.UTF_8)}")
    assert(I_AM_HARI.equals(helloWorld.toString(CharsetUtil.UTF_8)))
  }

}