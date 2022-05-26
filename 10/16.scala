/**
 * 다음을 증명하라.
 * A.op와 B.op가 둘 다 결합적이면, 다음 함수에 대한 op의 구현은 자명하게 결합적이다.
 *
 * 정답에서는 해당 함수 구현만 있고 증명은 없네요;;
 */

def productMonoid[A, B](A: Monoid[A], B: Monoid[B]): Monoid[(A, B)] = ???

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}