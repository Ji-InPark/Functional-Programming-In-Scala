/**
 * 어려움
 * foldMap을 이용한 주어진 IndexedSeq[Int]가 정렬되어 있는지 점검하라. 독창적인 Monoid를 고안해야 할 것이다.
 *
 * 힌트:
 * 2번 문제인 optionMonoid와
 * 7번 문제인 foldMapV를 활용해보세요
 */

def ordered(ints: IndexedSeq[Int]): Boolean = ???

object test{
  def main(args: Array[String]): Unit = {
    val l1 = IndexedSeq(1, 2, 3, 4, 5, 6, 7, 8)
    val l2 = IndexedSeq(1, 2, 3, 4, 6, 5, 7, 8)
    val l3 = IndexedSeq(1)
    val l4 = IndexedSeq()
    val l5 = IndexedSeq(5, 4, 3, 2, 1)
    val l6 = IndexedSeq(1, 1, 1, 1, 1, 1)

    println(ordered(l1))
    println(ordered(l2))
    println(ordered(l3))
    println(ordered(l4))
    println(ordered(l5))
    println(ordered(l6))

    /**
    true
    false
    true
    true
    false
    true
     */
  }
}

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}