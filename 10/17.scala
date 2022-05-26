/**
 * 결과가 모노이드인 함수들에 대한 모노이드 인스턴스를 작성하라.
 */

def functionMonoid[A,B](B: Monoid[B]): Monoid[A => B] = ???

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}