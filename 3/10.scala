/**
 * 이번 절의 foldRight 구현은 꼬리 재귀가 아니므로 긴 목록에 대해서는 StackOverflowError 오류가 발생한다. (이를 "스택에 안전[stack-safe]하지 않다"라고 말한다).
 * 실제로 그런지 실험해 보고, 꼬리 재귀적인 또 다른 일반적 목록 재귀 함수 foldLeft를 이전 장에서 논의한 기법들을 이용해서 작성하라.
 * 서명은 다음과 같다.
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

def test(): Unit = {
    println(foldLeft(Cons(1, Cons(2, Cons(3, Nil))), 4)(_ + _))
}
