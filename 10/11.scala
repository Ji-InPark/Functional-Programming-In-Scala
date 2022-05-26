/**
 * WC 모노이드를 이용해서 String의 단어 개수를 세는 함수를 구현하라.
 * 구현은 주어진 무자열을 부분 문자열들로 분할하고 각 부분 문자열의 단어 개수를 세는 과정을 재귀적으로 반복해서 전체 단어 개수를 구해야한다.
 *
 * 힌트: foldMapV를 활용해보세요
 */

val wcMonoid: Monoid[WC] = ???

def count(s: String): Int = ???

object test{
  def main(args: Array[String]): Unit = {
    println(count("12 dfsa fdas dif dkfa fdia 123"))
    println(count("fjdkasl;fjdklas;fjkdlsa;"))
    println(count("12 d f s a f d a s d i f d k f a f d i a 123"))
    println(count(""))

    /**
    7
    1
    21
    0
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