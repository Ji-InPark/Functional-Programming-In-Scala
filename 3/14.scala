/**
 * append를 foldLeft나 foldRight를 이용해서 구현하라
 */

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
    case Cons(head, tail) => f(head, foldRight(tail, z)(f))
    case _ => z
}

def append[A](as: List[A], z: A) = foldRight[A, List[A]](as, Cons(z, Nil))(Cons(_, _))

def test(): Unit = {
    println(append(Nil, 4))
    println(append(Cons(3, Nil), 4))
    println(append(Cons(1, Cons(2, Cons(3, Nil))), 4))
}
