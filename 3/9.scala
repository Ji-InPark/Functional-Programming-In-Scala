/**
 * foldRight를 이용해서 목록의 길이를 계산하라.
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
    case Cons(head, tail) => f(head, foldRight(tail, z)(f))
    case _ => z
}

def length[A](as: List[A]): Int = foldRight(as, 0)((_, b) => b + 1)

def test(): Unit = {
    println(length(Nil))
    println(length(Cons(3, Nil)))
    println(length(Cons(3, Cons(3, Cons(3, Nil)))))
}
