/**
 * sum, product와 목록의 길이를 계산하는 함수를 foldLeft를 이용해서 작성하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def sum[Int](ns : List[Int]) : Int = foldLeft(ns,0)(_ + _)
def product[Double](ns : List[Double]) : Double = foldLeft(ns,1.0)(_ * _)
def size[A](ns: List[A]) : Int = foldLeft(ns,0) ((_,y)=>y+1)