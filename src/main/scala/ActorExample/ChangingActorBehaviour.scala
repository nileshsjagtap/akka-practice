package ActorExample

import ActorExample.Mom.{Ask, StartMom}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}



object Child{

  case object Accept
  case object Reject
  val HAPPY = "happy"
  val SAD = "sad"
}

class Child extends Actor{
  import Child._
  import Mom._
  var state = HAPPY
  override def receive: Receive = {
    case Food(VEGETABLE) => state = SAD
    case Food(CHOCOLATE) => state = HAPPY
    case Ask(_) => if(state == HAPPY) sender() ! Accept
                  else sender() ! Reject
  }
}

object Mom{

  case class StartMom(kidRef: ActorRef)
  case class Food(food:String)
  case class Ask(msg: String)
  val VEGETABLE = "vegetable"
  val CHOCOLATE = "Chocolate"

}

class Mom extends Actor{
  import Mom._
  import Child._
  override def receive: Receive = {
    case StartMom(kidRef) =>
      kidRef ! Food(CHOCOLATE)
      kidRef ! Ask("do you want to play")

    case Accept => println("yay my kid is happy")
    case Reject => println("my kid is not happy, but he is healthy")
  }
}

object ChangingActorBehaviour extends App{

  val system = ActorSystem("ChangingActorBehaviour")
  val kid = system.actorOf(Props[Child], "kid")
  val mom = system.actorOf(Props[Mom], "mom")

  mom ! StartMom(kid)

}



