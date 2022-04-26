/**
 * 목록의 역을 돌려주는(이를 테면 List(1, 2, 3)에 대해 List(3, 2, 1)을 돌려주는) 함수를 작성하라. 접기(fold) 함수를 이용해서 작성할 수 있는지 시도해 볼 것
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
    @annotation.tailrec
    def g(l: List[A], b: B): B = l match {
        case Cons(head, tail) => g(tail, f(b, head))
        case _ => b
    }
    return g(as, z)
}

def reverse[A](xs: List[A]): List[A] = foldLeft[A, List[A]](xs, Nil)((b, a) => Cons(a, b))

def test(): Unit = {
    println(reverse(Nil))
    println(reverse(Cons(3, Nil)))
    println(reverse(Cons(1, Cons(3, Cons(5, Nil)))))
}
