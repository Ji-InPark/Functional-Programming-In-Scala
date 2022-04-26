/**
 * map과 비슷하되 하나의 요소가 아니라 목록의 최종 결과 목록에 삽입하는 함수 flatMap을 작성하라.
 * 서명은 다음과 같다.
 *
 * 예를 들어 flatMap(List(1, 2, 3))(i => List(i, i))는 List(1, 1, 2, 2, 3, 3)이 되어야 한다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // 스칼라가 형식 추론을 할 수 있게 개별적 인수그룹에 넣어줌
  as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

def append[A](a1: List[A], a2: List[A]): List[A] =
  foldRight(a1, a2)((x, y) => Cons(x, y))

def appendAll[A](as: List[List[A]]): List[A] =
  foldRight(as, Nil: List[A])(append)

def map[A, B](as: List[A])(f: A => B): List[B] =
  foldRight(as, Nil: List[B])((x, y) => Cons(f(x), y))

def flatMap[A,B](as: List[A])(f: A => List[B]): List[B] =
  appendAll(map(as)(f))

