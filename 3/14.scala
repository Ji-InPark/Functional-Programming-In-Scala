/**
 * append를 foldLeft나 foldRight를 이용해서 구현하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]