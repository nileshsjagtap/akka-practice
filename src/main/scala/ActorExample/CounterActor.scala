package ActorExample

import akka.actor.{Actor, ActorSystem, Props}



object Counter extends App {

  class CounterActor extends Actor {
    var count = 0
    override def receive(): Receive = {
      case "incr" => count+=1
      case "decr" => count-=1
      case "print" => println(s"counter value is $count")
    }
  }

  val system = ActorSystem("ActorSystem")
  val counterActor = system.actorOf(Props[CounterActor], "CounterActor")
  val anotherCounterActor = system.actorOf(Props[CounterActor], "AnotherCounterActor")

  counterActor ! "incr"
  counterActor ! "incr"
  counterActor ! "incr"
  counterActor ! "decr"
  counterActor ! "print"
  anotherCounterActor ! "print"
  anotherCounterActor ! "incr"
  anotherCounterActor ! "incr"
  anotherCounterActor ! "print"



}
