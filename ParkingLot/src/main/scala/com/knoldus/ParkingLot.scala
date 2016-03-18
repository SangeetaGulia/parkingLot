//package com.knoldus
//
//import akka.actor.{Props, ActorSystem, Actor}
//import akka.pattern.ask
//import akka.util.Timeout
//import scala.concurrent.{Future, Await}
//import scala.concurrent.duration._
//
///**
//  * Created by knodus on 16/3/16.
//  */
//
//
//object Parking{
//  var capacity=5
//
//  def allot:Int={
//    capacity -=1
//    capacity
//  }
//}
//
//class Attendent extends Actor{
//  def receive={
//    case x:Int => if(x > -1) sender ! "Alloted" else sender ! "Seats Full"
//  }
//}
//
//
//class Monitor extends Actor {
//
//  def send(msg:Int) = {
//    (ParkingLot.actor2 ! msg)
//  }
//
//  def receive = {
//    case "Get Ticket" => send(Parking.allot)
//    case "Alloted" => println("Alloted")
//    case "Seats Full" => println("Full ")
//
//  }
//}
//
//
//object ParkingLot {
//
//  val system=ActorSystem("Parking")
//  val actor1= system.actorOf(Props[Monitor],"Monitor")
//  val actor2= system.actorOf(Props[Attendent],"Attendent")
//
//  implicit val timeout = Timeout(10 seconds)
//  def send(msg:String): Unit ={
//    println(s"Me : $msg")
//    val res:Future[Int]= (actor1 ? msg ).mapTo[Int]
//    Thread.sleep(1000)
//    // Ask
//   // println(Await.result(res,10 seconds))
//  }
//
//  def main(args: Array[String]): Unit ={
//    send("Get Ticket")
//    send("Get Ticket")
//    send("Get Ticket")
//    send("Get Ticket")
//    send("Get Ticket")
//    send("Get Ticket")
//
//    Thread.sleep(6000)
//    system.shutdown()
//  }
//}
