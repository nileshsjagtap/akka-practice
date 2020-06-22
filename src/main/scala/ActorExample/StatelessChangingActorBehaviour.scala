package ActorExample

import ActorExample.Mom1.{Ask, StartMom}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}



object Child1{

  case object Accept
  case object Reject
  val HAPPY = "happy"
  val SAD = "sad"
}

class Child1 extends Actor{
  import Child1._
  import Mom1._

  override def receive : Receive = happyReceive(HAPPY)

  def happyReceive(state: String): Receive = {
    case Food(VEGETABLE) => context.become(sadReceive(SAD)) //context.become has one or two parameters second parameter is true or false, true means discard the old message handler or fully
    case Food(CHOCOLATE) => HAPPY                           //replace old msg handler with thew new msg handler. when we provide false to contecxt.become it will push the msg handler into the
    case Ask(_) =>  sender() ! Accept                       //stack.
  }

  def sadReceive(state: String): Receive = {
    case Food(VEGETABLE) => SAD
    case Food(CHOCOLATE) => context.become(happyReceive(HAPPY))
    case Ask(_) => sender() ! Reject
  }
}

object Mom1{

  case class StartMom(kidRef: ActorRef)
  case class Food(food:String)
  case class Ask(msg: String)
  val VEGETABLE = "vegetable"
  val CHOCOLATE = "Chocolate"

}

class Mom1 extends Actor{
  import Mom1._
  import Child1._
  override def receive: Receive = {
    case StartMom(kidRef) =>
      kidRef ! Food(VEGETABLE)
      kidRef ! Food(VEGETABLE)
      kidRef ! Food(VEGETABLE)
      kidRef ! Food(CHOCOLATE)
      kidRef ! Ask("do you want to play")

    case Accept => println("yay my kid is happy")
    case Reject => println("my kid is not happy, but he is healthy")
  }
}

object StatelessChangingActorBehaviour extends App{

  import Mom1._

  val system = ActorSystem("ChangingActorBehaviour")
  val kid = system.actorOf(Props[Child1], "kid")
  val mom = system.actorOf(Props[Mom1], "mom")

  mom ! StartMom(kid)

}



