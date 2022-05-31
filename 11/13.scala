/**
 * flatMap이나 compose를 join과 map을 이용해서 구현하라
 */

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
trait Monad[F[_]] extends Functor[F] {
  def join[A](mma: F[F[A]]): F[A] = ???

  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B] = ???

  def compose[A,B,C](f:A=>F[B],g:B =>F[C]):A => F[C] = ???

  def unit[A](a: => A): F[A]

  override def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))

  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = flatMap(fa)(a => map(fb)(b => f(a, b)))

  def product[A, B](ma: F[A], mb: F[B]): F[(A, B)] = map2(ma, mb)((_, _))
}