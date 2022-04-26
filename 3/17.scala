/**
 * List[double]의 각 값을 String으로 변환하는 함수를 작성하라.
 * d: Double을 String으로 변환할 때에는 d.toString이라는 표현식을 사용하면 된다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def format(l: List[Double]): List[String] = l match {
    case Cons(head, tail) => Cons(head.toString, format(tail))
    case _ => Nil
}

def test(): Unit = {
    println(format(Nil))
    println(format(Cons(3.1, Nil)))
    println(format(Cons(1.2, Cons(2.4, Cons(3.6, Nil)))))
}
