/**
 * 이 문제도 어렵다 애송이드라
 * 목록들의 목록을 하나의 목록으로 연결하는 함수를 작성하라.
 * 실행 시간은 반드시 모든 목록의 전체 길이에 선형으로 비례해야 한다.
 * 이미 정의한 함수들을 활용하도록 노력할 것
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def appendAll[A](as: List[List[A]]): List[A] =
  foldRight(as, Nil: List[A])(append)

def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // 스칼라가 형식 추론을 할 수 있게 개별적 인수그룹에 넣어줌
  as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

def append[A](a1: List[A], a2: List[A]): List[A] =
  foldRight(a1, a2)((x, y) => Cons(x, y))