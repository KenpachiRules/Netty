package com.hari.learning.netty.model

import java.util.UUID.randomUUID

/**
 * Serializable object used to pass message between client and server.
 * Contains 3 attributes
 *
 * id - a unique id to represent an instance of Heartbeat.
 * ts - long value representing the timestamp of creation of the object.
 * clientId - unique id representing a client.
 *
 * @author harim
 *
 */

case class Heartbeat(id: String, ts: Long, clientId: String, continue: Boolean = true) {
  require(id != null && !id.isEmpty() && ts > 0)

  override def toString: String = s"['id'$id ,'timestamp':$ts , 'clientId':$clientId]"

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[Heartbeat])
      false
    val beat = obj.asInstanceOf[Heartbeat]
    beat.clientId.equals(clientId) && beat.ts == ts && beat.continue == continue
  }

  override def hashCode: Int = clientId.hashCode() + clientId.hashCode() // I need to understand how to write hashCodes.

}

// Companion object.

object Heartbeat {

  /**
   * Generates a new Heartbeat a new random UUID and the time of invocation of this method in millis
   * and the clientId which represents the client which is talking to the server
   *
   * @param clientId
   * @return new instance of Heartbeat.
   */
  def beat(clientId: String): Heartbeat = new Heartbeat(randomUUID().toString, System.currentTimeMillis(), clientId)

  /**
   * Generates an abort Heartbeat, which signals the end of beat transfer.
   * @param clientId
   * @return new instance of Heartbeat.
   */

  def abortBeat(clientId: String): Heartbeat = new Heartbeat(randomUUID.toString(), System.currentTimeMillis(), clientId, false)

}