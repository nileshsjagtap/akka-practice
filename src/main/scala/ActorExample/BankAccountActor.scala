package ActorExample

import ActorExample.BankAccountActor._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class BankAccountActor(balance: Int) extends Actor{
  var amount = balance
  override def receive: Receive = {
    case Deposit(amt) => {
                            if(amt > 0){
                              amount+=amt
                              Success
                            }
                            else {
                              Failure
                            }

                          }
    case Withdraw(amt) => {
                            if(amt <= amount && amount > 0){
                              amount-=amt
                              Success
                            }
                            else {
                              Failure
                            }
                          }
    case Statement => println(s"Total amt in your account is ${amount}")
  }

}

case class Transaction(ref : ActorRef)


//class Person extends Actor{
//  override def receive: Receive = {
//    case
//  }
//
//
//}

object BankAccountActor{

  case class Deposit(amount:Int)

  case class Withdraw(amount:Int)


  case object Statement



  case object Success

  case object Failure

}

object BankAccount extends App{

  val system = ActorSystem("BankAccount")
  val bankAccountActor = system.actorOf(Props(new BankAccountActor(500)),"BankAccountActor")

  val nileshBankAct = system.actorOf(Props(new BankAccountActor(500)),"NileshBankAct")
  val radhikaBankAct = system.actorOf(Props(new BankAccountActor(500)),"RadhikaBankAct")

  bankAccountActor ! Deposit(100)
  bankAccountActor ! Deposit(100)
  bankAccountActor ! Deposit(100)
  bankAccountActor ! Withdraw(100)
  bankAccountActor ! Withdraw(100)
  bankAccountActor ! Statement


  //nileshBankAct




}
