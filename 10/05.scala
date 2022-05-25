/**
 * !! 1번 문제 풀어야지 테스트가 가능합니다
 *
 * foldMap을 구현하라
 */

// 구현한 것 복붙!
val intAddition: Monoid[Int] = ???

val intMultiplication: Monoid[Int] = ???

def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B = ???

object test{
  def main(args: Array[String]): Unit = {
    val ls = List("30", "2", "10", "43", "5")

    println(foldMap(ls, intAddition)((a: String) => a.toInt))
    println(foldMap(ls, intMultiplication)((a: String) => a.toInt))

    /**
    90
    129000
     */
  }
}

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}