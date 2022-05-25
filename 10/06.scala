/**
 *어려움
 * foldmap 함수를 foldLeft나 foldRight를 이용해서 구현할 수 있다.
 * 그런데 foldLeft와 foldRight를 foldMap을 이용해서 구현할 수도 있다. 시도해보라.
 *
 * hint
 * 3번문제인 endoMonoid와
 * 2번문제에 나오는 dual을 활용해보세요
 */

// 구현한 것 복붙
def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B = ???

def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B = ???

def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B = ???


object test{
  def main(args: Array[String]): Unit = {
    val ls = List(1, 2, 3, 4, 5, 6, 7)

    println(foldRight(ls)(Nil: List[Int])((head, tail) => head + 3 :: tail))
    println(foldLeft(ls)(Nil: List[Int])((tail, head) => head * 3 :: tail))

    /**
    List(4, 5, 6, 7, 8, 9, 10)
    List(21, 18, 15, 12, 9, 6, 3)
     */
  }
}

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}