
/**
 * 7.5
 * 어려움: 그러한 수단인 함수 sequence를 작성하라. 다른 기본수단은 필요하지 않다. run은 호출하지 말아야 한다.
 */
import java.util.concurrent._
import language.implicitConversions

object Par {
  type Par[A] = ExecutorService => Future[A]
  
  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a) // `unit` is represented as a function that returns a `UnitFuture`, which is a simple implementation of `Future` that just wraps a constant value. It doesn't use the `ExecutorService` at all. It's always done and can't be cancelled. Its `get` method simply returns the value that we gave it.
  
  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true 
    def get(timeout: Long, units: TimeUnit) = get 
    def isCancelled = false 
    def cancel(evenIfRunning: Boolean): Boolean = false 
  }

  def sequence[A](ps: List[Par[A]]): Par[List[A]] = ???
}

object Main {
  def main(args: Array[String]): Unit = {
    
  }
}
