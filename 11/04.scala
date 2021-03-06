/**
 * replicateM 구현하라
 */

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
trait Monad[F[_]] extends Functor[F] {
  def replicateM[A](n:Int,ma:F[A]):F[List[A]] = ???

  def unit[A](a: => A): F[A]

  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]

  override def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))

  def map2[A,B,C](fa: F[A],fb:F[B])(f: (A,B) => C):F[C] = flatMap(fa)(a=> map(fb)(b=>f(a,b)))
}