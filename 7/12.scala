
/**
 * 7.12
 * choiceN에도 여전히 자의적인 구석이 남아 있다. List를 사용하는 것은 필요 이상으로 구체적인 것 같다. 
 * 이 조합기는 컨테이너의 구체적인 종류에 구애받을 필요가 없다.
 * 예를 들어, 계산들의 목록 대신 계산들의 Map이 있다면 어떨까?
 *
 *  def choiceMap[K, V](Key: Par[K])(chocies: Map[K, Par[V]]): Par[V]
 *
 * 원한다면 책을 잠시 덮고, choice와 choiceN, choiceMap의 구현에 사용할 수 있는 좀 더 일반적인 새 조합기를 고안해 보기 바란다.
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

  def fork[A](a: => Par[A]): Par[A] = // This is the simplest and most natural implementation of `fork`, but there are some problems with it--for one, the outer `Callable` will block waiting for the "inner" task to complete. Since this blocking occupies a thread in our thread pool, or whatever resource backs the `ExecutorService`, this implies that we're losing out on some potential parallelism. Essentially, we're using two threads when one should suffice. This is a symptom of a more serious problem with the implementation, and we will discuss this later in the chapter.
    es => es.submit(new Callable[A] { def call = a(es).get })

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def chocieN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] = ???

object Main:
  def main(args: Array[String]): Unit = {
    
  }