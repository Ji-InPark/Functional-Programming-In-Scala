/**
 * WC를 위한 모노이드 인스턴스를 작성하고, 그것이 모노이드 법칙을 지키는지 확인하라.
 *
 * 힌트: zero에 대한건 테스트 결과를 참고하세요!
 */

val wcMonoid: Monoid[WC] = ???

object test{
  def main(args: Array[String]): Unit = {
    println(wcMonoid.zero)
    println(wcMonoid.op(Stub(""), Part("123", 3, "")))
    println(wcMonoid.op(Part("123", 1, "456"), Part("123", 3, "456")))
    println(wcMonoid.op(Part("", 1, ""), Part("123", 6, "")))

    /**
    Stub()
    Part(123,3,)
    Part(123,5,456)
    Part(,8,)
     */
  }
}

sealed trait WC
case class Stub(chars: String) extends WC
case class Part(lStub: String, words: Int, rStub: String) extends WC

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}