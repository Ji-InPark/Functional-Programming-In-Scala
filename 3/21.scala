/**
 * flatMap을 이용해서 filter를 구현하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def flatMap[A,B](as: List[A])(f: A => List[B]): List[B] = as match {
  case Nil => Nil : List[B]
  case Cons(x,xs) => {
    def map(as: List[B]) : List[B] = as match {
      case Nil => flatMap(xs)(f)
      case Cons(x1,xs1) => Cons(x1,map(xs1))
    }
    map(f(x))
  }
}

def filter[A](as: List[A])(f: A=>Boolean) : List[A] = flatMap(as)(x =>
  if(f(x)) Nil : List[A]
  else Cons(x,Nil)
)