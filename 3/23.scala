/**
 * 연습문제 3.22의 함수를 정수나 덧셈에 국한되지 않도록 일반화하라.
 * 함수의 이름은 zipWith로 할 것.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]