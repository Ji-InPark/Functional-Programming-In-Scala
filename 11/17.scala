/**
 * map과 flatMap을 이 클래스의 메서드로 구현하고, Monad[Id]의 구현을 제시하라.
 */

case class Id[A](value: A)

trait Monad[F[_]] {
  //  def unit[A](a: => A): F[A]
//  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B] = ???

//  def unit[A](a: => A): F[A]
//  def compose[A,B,C](f:A=>F[B],g:B =>F[C]):A => F[C] = ???

//  def unit[A](a: => A): F[A]
//  def join[A](mma: F[F[A]]): F[A] = ???
//  def map[A, B](fa: F[A])(f: A => B): F[B] = ???
}

object Id {
 val idMonad = new Monad[Id] {

 }
}