package ActorExample

package export

import java.util.concurrent.atomic.AtomicBoolean
import akka.actor.{Actor, Props}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class OfflineExport() extends Actor{

  import OfflineExport._

  private val filePresent: AtomicBoolean = new AtomicBoolean(false)

  override def receive: Receive = {
    case CheckForFile(filePath: String) =>
      try{
        //check(filePath: String)
      } catch {
        case _: Exception => filePresent.set(false)
      }
      schedule(filePath)
    case GetStatus => sender ! fileStatus
  }

//  def check(filePath: String) = {
//    offlineExportService.isPresent(filePath)
//  }

  def fileStatus = {
    if(filePresent.get()) Present else Absent
  }

  def schedule(p: String) = {
    context.system.scheduler.scheduleOnce(DurationInt(5).seconds, self, CheckForFile(p))
  }

}
object OfflineExport {

  case class CheckForFile(filePath: String)
  case object GetStatus

  case object Present
  case object Absent
}

