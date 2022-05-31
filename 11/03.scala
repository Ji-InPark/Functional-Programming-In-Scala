/**
 * sequence, traverse 모나드에 대해서 한번만 작성
 */

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

trait Monad[F[_]] extends Functor[F] {
  def sequence[A](lma: List[F[A]]): F[List[A]] = ???

  def traverse[A, B](la: List[A])(f: A => F[B]): F[List[B]] = ???

  def unit[A](a: => A): F[A]

  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]

  override def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))

  def map2[A,B,C](fa: F[A],fb:F[B])(f: (A,B) => C):F[C] = flatMap(fa)(a=> map(fb)(b=>f(a,b)))
}