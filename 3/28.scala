/**
 * List에 대한 함수 map과 비슷하게 트리의 각 요소를 주어진 함수로 수정하는 함수 map을 작성하라.
 */

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](Left: Tree[A], right: Tree[A]) extends Tree[A]

def map[A, B](t: Tree[A])(f: A => B): Tree[B] =
  t match {
    case Leaf(x) => Leaf(f(x))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }