/**
 * 제 3장의 이진 Tree 자료 형식을 기억할 것이다.
 * 그 자료구조를 위한 Foldable 인스턴스를 구현하라.
 *
 * 이 부분은 테스트가 없습니다...
 */

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object TreeFoldable extends Foldable[Tree] {
  override def foldMap[A, B](as: Tree[A])(f: A => B)(mb: Monoid[B]): B = ???
  override def foldLeft[A, B](as: Tree[A])(z: B)(f: (B, A) => B) = ???
  override def foldRight[A, B](as: Tree[A])(z: B)(f: (A, B) => B) = ???
}

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}