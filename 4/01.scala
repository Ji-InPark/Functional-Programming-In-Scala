/*
 * 목록 4.2에 나온 Option에 대한 함수들을 모두 구현하라.
 * 각 함수를 구현할 때 그 함수가 어떤 일을 하고 어떤 상황에서 쓰일 것인지 생각해 볼 것.
 * 이 함수들 각각의 용도를 잠시 후에 보게 될 것이다.
 * 다음은 이 연습문제를 위한 몇 가지 힌트이다.
 * - 패턴 부합을 사용해도 좋으나, map과 getOrElse를 제외한 모든 함수는 패턴 부합 없이도 구현할 수 있어야한다.
 * - map과 flatMap의 형식 서명은 구현을 결정하기에 충분해야 한다.
 * - getOrElse는 Option의 Some 안의 결과를 돌려준다. 단, Option이 None이면 주어진 기본값을 돌려준다.
 * - orElse는 첫 Option이 정의되어 있으면 그것을 돌려주고 그렇지 않으면 둘째 Option을 돌려준다.
 */

sealed trait Option[+A] {
  def map[B](f: A => B): Option[B] = ???

  def flatMap[B](f: A => Option[B]): Option[B] = ???

  def getOrElse[B >: A](default: => B): B = ???

  def orElse[B >: A](ob: => Option[B]): Option[B] = ???

  def filter(f: A => Boolean): Option[A] = ???
}

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]
