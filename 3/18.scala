/**
 * 목록의 구조를 유지하면서 목록의 각 요소를 수정하는 작업을 일반화한 함수 map을 작성하라. 서명은 다음과 같다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def map[A, B](as: List[A])(f: A => B): List[B] = as match {
    case Cons(head, tail) => Cons(f(head), map(tail)(f))
    case _ => Nil
}

def test(): Unit = {
    println(map(Cons(3.1, Nil))(_ + 1.0))
    println(map(Cons(1.2, Cons(2.4, Cons(3.6, Nil))))(_ + 1.0))
}

