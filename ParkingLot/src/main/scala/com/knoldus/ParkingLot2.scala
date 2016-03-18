package com.knoldus

import akka.actor.{Props, ActorSystem, Actor}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

/**
  * Created by knodus on 16/3/16.
  */

case class Message(str:String,id:Int)


object Parking{

  val parkingLot=scala.collection.mutable.Map.empty[Int,Int]

  val capacity=5
  def initialiseLot(): Unit ={
    for(i <- 0 to capacity-1 ){
      parkingLot += (i->0)
    }
  }

  def checkAvailability :Boolean={
    val list=parkingLot.values.toList
    list.contains(0)
  }

  def allocate(vehicleId:Int) {
    for ((k, v) <- parkingLot) {
      if (v == 0) {
        parkingLot.put(k, vehicleId)
        return
      }
    }
  }

    def deAllocate(vehicleId: Int) {
      for ((k, v) <- parkingLot) {
        if (v == vehicleId) parkingLot.put(k, 0)

      }
    }
  }

class Attendent extends Actor {
  def receive = {
    case Message(msg,id)=> {
      if (Parking.checkAvailability)
        send(Message(msg,id))
      else println("Slots not available")
    }
  }

  def send(msg: Message) = {
    ParkingLot.actor2 ! msg
  }
}


class Monitor extends Actor {

  def receive = {
    case Message("Add",id) => {
      Parking.allocate(id)
      println("Id Alloted")
    }
    case Message("Remove",id) => {
      Parking.deAllocate(id)
      println(" One More slot available ")
    }
  }
}


object ParkingLot {

  val system=ActorSystem("ParkingMain")
  val actor2= system.actorOf(Props[Monitor],"Monitor")
  val actor1= system.actorOf(Props[Attendent],"Attendent")

  implicit val timeout = Timeout(10 seconds)

  def send(msg:Message): Unit ={
    println(s"Add Me : $msg")
    (actor1 ! msg )
    Thread.sleep(1000)

  }


  def main(args: Array[String]): Unit ={
    Parking.initialiseLot()
    send(Message("Add",1))
    send(Message("Add",2))
    send(Message("Add",3))
    send(Message("Remove",2))

    send(Message("Add",4))
    send(Message("Add",5))
    send(Message("Add",6))
    send(Message("Add",7))


    Thread.sleep(6000)
    system.shutdown()
  }
}
