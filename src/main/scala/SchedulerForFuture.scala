import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.ExecutionContext.Implicits.global
import java.time.LocalTime
import java.util.concurrent.{Executors, TimeUnit}

object SchedulerForFuture extends App {
  trait MyScheduler {
    def schedule(future: () => Future[Unit], duration: Duration): Unit
  }

  class MySchedulerImpl extends MyScheduler {
    private implicit val stp = Executors.newScheduledThreadPool(1)
    private implicit val ec = ExecutionContext.fromExecutor(stp)

    override def schedule(future: () => Future[Unit], duration: Duration): Unit = {
      stp.scheduleAtFixedRate(() => future(), 0, duration.toMillis, TimeUnit.MILLISECONDS)
    }
  }

  val schedulerForFuture = new MySchedulerImpl
  val future = (n: Int) => () => Future{ println(n, LocalTime.now()); Thread.sleep(2000); println(n, "end")}

  schedulerForFuture.schedule(future(1), 2.seconds)
  schedulerForFuture.schedule(future(2), 2.seconds)
  schedulerForFuture.schedule(future(3), 2.seconds)

}
