/**
 * IndexedSeq에 대한 foldMap을 구현하라.
 * 구현은 반드시 순차열을 둘로 분할해서 재귀적으로 각 절반을 처리하고 그 결과들을 모노이드를 이용해서 결합해야 한다.
 */

def foldMapV[A, B](v: IndexedSeq[A], m: Monoid[B])(f: A => B): B =
  if (v.length == 0)
    m.zero
  else if (v.length == 1)
    f(v(0))
  else {
    val (l, r) = v.splitAt(v.length / 2)
    m.op(foldMapV(l, m)(f), foldMapV(r, m)(f))
  }

object test{
  val addWithPrintMonoid: Monoid[Int] = new Monoid[Int]{
    def op(x: Int, y: Int) = {
      println("x: " + x + " y: " + y)
      x + y
    }
    val zero = 0
  }

  def main(args: Array[String]): Unit = {
    val ls = IndexedSeq(1, 2, 3, 4, 5, 6, 7, 8)

    println("result: " + foldMapV(ls, addWithPrintMonoid)(a => a))

    /**
    x: 1 y: 2
    x: 3 y: 4
    x: 3 y: 7
    x: 5 y: 6
    x: 7 y: 8
    x: 11 y: 15
    x: 10 y: 26
    result: 36
     */
  }
}


trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}