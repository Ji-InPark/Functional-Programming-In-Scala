/**
 * 이 문제는 어렵다 애송이드라
 * foldLeft를 foldRight를 이용해서 구현할 수 있을까?
 * 그 반대 방향은 어떨까? foldLeft를 이용하면 foldRight를 꼬리 재귀적으로 구현할 수 있으므로 긴 목록에 대해서도 스택이 넘치지 않는다는 장점이 생긴다.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

@annotation.tailrec
def foldLeft[A, B](as: List[A], z: B)(F: (B, A) => B): B =
  as match {
    case Nil => z
    case Cons(h, t) => foldLeft(t, F(z, h))(F)
  }

def reverse[A](as: List[A]): List[A] = foldLeft(as, List[A]())((x, y) => Cons(y, x))

def foldRightWithFoldLeft[A, B](as: List[A], z: B)(f: (A, B) => B): B =
  foldLeft(reverse(as), z)((x, y) => f(y, x))