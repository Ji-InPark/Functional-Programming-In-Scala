/**
 * List[double]의 각 값을 String으로 변환하는 함수를 작성하라.
 * d: Double을 String으로 변환할 때에는 d.toString이라는 표현식을 사용하면 된다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def doubleToString(as : List[Double]) : List[String] = as match {
  case Nil => Nil : List[String]
  case Cons(x,xs) => Cons(x.toString,doubleToString(xs))
}