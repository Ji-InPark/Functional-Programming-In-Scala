/**
 * flatMap을 compose를 이용해서 구현하라.
 * 이 구현이 가능 -> compose, unit은 모나드의 또다를 최소집합.
 */

trait Monad[F[_]] {
  def compose[A,B,C](f:A=>F[B],g:B =>F[C]):A => F[C] = ???

  def unit[A](a: => A): F[A]

  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]
}