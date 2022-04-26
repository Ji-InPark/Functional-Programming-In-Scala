/**
 * sum, product와 목록의 길이를 계산하는 함수를 foldLeft를 이용해서 작성하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def sum(ns: List[int]) =
  foldLeft(ns, 0)(_+_)

def product(ns: List[Double]) =
  foldLeft(ns, 1.0)(_*_)

def length[A](as: List[A]): Int =
  foldLeft(as, 0)((x,_) => x+1)