/**
 * 목록의 역을 돌려주는(이를 테면 List(1, 2, 3)에 대해 List(3, 2, 1)을 돌려주는) 함수를 작성하라. 접기(fold) 함수를 이용해서 작성할 수 있는지 시도해 볼 것
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
    print(reverse(List(1, 2, 3, 4, 5)))
  }

  @annotation.tailrec
  def foldLeft[A, B](as: List[A], z: B)(F: (B, A) => B): B =
    as match {
      case Nil => z
      case Cons(h, t) => foldLeft(t, F(z, h))(F)
    }

  def reverse[A](as: List[A]): List[A] = foldLeft(as, List[A]())((x, y) => Cons(y, x))
}

