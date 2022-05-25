/**
 * 인수의 형식과 반환값의 형식이 같은 함수를 자기함수(endofunction)라고 부른다. 자기함수들을 위한 모노이드를 작성하라.
 */

def endoMonoid[A]: Monoid[A => A] = ???

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}