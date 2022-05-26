/**
 * '자루(bag)'라는 자료구조는 집합처럼 각 요소를 하나씩만 담되, 그요소의 출현 회수도 기억한다.
 * 구체적으로, 자루는 각 요소가 키이고 그 요소의 출현 횟수가 값인 Map으로 표현된다.
 * 다음이 자루의 예이다.
 * scala> bag(Vector("a", "rose", "is", "a", "rose"))
 * Map[String, Int] = Map(a -> 2, rose -> 2, is -> 1)
 *
 * 모노이드를 이용해서 IndexedSeq로부터 '자루'를 계산하는 함수를 작성하라.
 */

def bag[A](as: IndexedSeq[A]): Map[A, Int] = ???

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}