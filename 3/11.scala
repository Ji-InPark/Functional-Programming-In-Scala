/**
 * sum, product와 목록의 길이를 계산하는 함수를 foldLeft를 이용해서 작성하라
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

def sum(ints: List[Int]): Int = foldLeft(ints, 0)(_ + _)

def product(ds: List[Double]): Double = foldLeft(ds, 1.0)(_ * _)

def length[A](as: List[A]): Int = foldLeft(as, 0)((b, _) => b + 1)

def test(): Unit = {
    println(sum(Nil))
    println(sum(Cons(3, Nil)))
    println(sum(Cons(3, Cons(3, Cons(3, Nil)))))
    println(product(Nil))
    println(product(Cons(3.0, Nil)))
    println(product(Cons(3.0, Cons(3.0, Cons(3.0, Nil)))))
    println(length(Nil))
    println(length(Cons(3, Nil)))
    println(length(Cons(3, Cons(3, Cons(3, Nil)))))
}
