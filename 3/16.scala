/**
 * 정수 목록의 각 요소에 1을 더해서 목록을 변환하는 함수를 작성하라.
 * (주의: 새 List를 돌려주는 순수 함수이어야 한다.)
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def increase(l: List[Int]): List[Int] = l match {
    case Cons(head, tail) => Cons(head + 1, increase(tail))
    case _ => Nil
}

def test(): Unit = {
    println(increase(Nil))
    println(increase(Cons(3, Nil)))
    println(increase(Cons(1, Cons(2, Cons(3, Nil)))))
}
