import scala.annotation.tailrec

/**
 * Array[A]가 주어진 비교 함수에 의거하여 정렬되어 있는지 점검하는 isSorted 함수를 구현하라.
 */
def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
  @tailrec
  def iterate(index: Int): Boolean = 
    if (index >= as.length - 1) true
    else if (ordered(as(index), as(index + 1))) iterate(index + 1)
    else false

  iterate(0)
}
