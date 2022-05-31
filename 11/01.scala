import java.util.concurrent.{ExecutorService, Future}

/**
 * Par, Parser, Option, Stream, List에 대한 모나드 인스턴스 작성..
 * Parser는 뺍시다
 * Stream(deprecated) -> LazyList (2.13)
 */

object Monad {

  val parMonad: Monad[Par] = new Monad[Par] {
    override def unit[A](a: => A): Par[A] = ???

    override def flatMap[A, B](ma: Par[A])(f: A => Par[B]): Par[B] = ???
  }

  val optionMonad: Monad[Option] = new Monad[Option] {
    override def unit[A](a: => A): Option[A] = ???

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] = ???
  }

  val streamMonad: Monad[LazyList] = new Monad[LazyList] {
    override def unit[A](a: => A): LazyList[A] = ???

    override def flatMap[A, B](ma: LazyList[A])(f: A => LazyList[B]): LazyList[B] = ???
  }

  val listMonad: Monad[List] = new Monad[List] {
    override def unit[A](a: => A): List[A] = ???

    override def flatMap[A, B](ma: List[A])(f: A => List[B]): List[B] = ???
  }
}

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Monad[F[_]] extends Functor[F] {
  def unit[A](a: => A): F[A]

  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]

  override def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))

  def map2[A,B,C](fa: F[A],fb:F[B])(f: (A,B) => C):F[C] = flatMap(fa)(a=> map(fb)(b=>f(a,b)))
}

type Par[A] = ExecutorService => Future[A]

