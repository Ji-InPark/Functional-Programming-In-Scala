/**
 * 같은 맥락에서 List의 첫 요소를 다른 값으로 대체하는 함수 setHead를 구현하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def setHead[A](head: A, list: List[A]): List[A] = list match {
    case Cons(_, t) => Cons(head, t)
    case _ => list
}

def test(): Unit = {
    println(setHead(5, Nil))
    println(setHead(5, Cons(2, Nil)))
    println(setHead(5, Cons(3, Cons(2, Nil))))
}
