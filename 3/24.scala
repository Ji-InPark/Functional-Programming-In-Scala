import scala.annotation.tailrec

/**
 * 이번 문제 또한 어렵다 이 애송이드라
 * 효율성 손실의 한 예로, List가 또 다른 List를 부분 순차열로서 담고 있는지 젘검하는 hasSubsequence 함수를 구현하라.
 * 예를 들어 List(1, 2)나 List(2, 3), List(4)는 List(1, 2, 3, 4)의 부분 순차열이다.
 * 간결하고 순수 함수로만 이루어진, 그러면서도 효율적인 구현을 고안하기가 어려울 수 있겠지만, 그래도 개의치 말고 일단은 가장 자연스러운 방식으로 함수를 구현할 것.
 * 나중에 제 5장에서 이 함수를 좀 더 개선해 볼 것이다.
 * 참고: 스칼라에서 임의의 두 값 x와 y의 상등(equality)을 비교하는 표현식은 x == y이다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = {
  @tailrec
  def iterate(as:List[A]) : Boolean = as match {
    case Nil => false
    case Cons(_,xs) => if(xs == sub) true else iterate(xs)
  }

  @tailrec
  def iterate2(as:List[A]) : Boolean = as match {
    case Nil => false
    case Cons(_,xs) => if(iterate(xs)) true else iterate2(xs)
  }
  if(sup == sub) true
  else iterate2(sup)
}