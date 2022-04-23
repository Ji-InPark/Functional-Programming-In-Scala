/**
 * 목록에서 주어진 술어를 만족하지 않는 요소들을 제거하는 함수 filter를 작성하라.
 * 그리고 그 함수를 이용해서 List[Int]에서 모든 홀수를 제거하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List { // List 동반(Companion) 객체, 목록의 생성과 조작을 위한 함수들을 담는다
  def sum(ints: List[Int]): Int = ints match { // 패턴 부합을 이용해서 목록의 정수들을 합하는 함수
    case Nil => 0 // 빈 목록의 합은 0
    case Cons(x, xs) => x + sum(xs) // x로 시작하는 목록의 합은 x 더하기 목록 나머지 부분의 합
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
}

object temp{
  def main(args: Array[String]): Unit = {
    print(filter(List(1, 2, 3, 4, 5, 6))((a) => a % 2 == 0))
  }
  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // 스칼라가 형식 추론을 할 수 있게 개별적 인수그룹에 넣어줌
    as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def filter[A](as: List[A])(f: A => Boolean): List[A] =
    foldRight(as, Nil: List[A])((h, t) => if(f(h)) Cons(h, t) else t)
}

