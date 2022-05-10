
/**
 * 7.3
 * 어려움: Future의 만료 시간 계약을 존중하도록 map2의 구현을 개선하라
 */
import java.util.concurrent.*

object Par:
  opaque type Par[A] = ExecutorService => Future[A]

  extension [A](pa: Par[A]) def run(s: ExecutorService): Future[A] = pa(s)

  def unit[A](a: A): Par[A] =
    es => UnitFuture(a) // `unit` is represented as a function that returns a `UnitFuture`, which is a simple implementation of `Future` that just wraps a constant value. It doesn't use the `ExecutorService` at all. It's always done and can't be cancelled. Its `get` method simply returns the value that we gave it.

  private case class UnitFuture[A](get: A) extends Future[A]:
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false


object Main:
  def main(args: Array[String]): Unit = {
    
  }