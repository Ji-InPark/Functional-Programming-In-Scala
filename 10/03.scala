/**
 * 인수의 형식과 반환값의 형식이 같은 함수를 자기함수(endofunction)라고 부른다. 자기함수들을 위한 모노이드를 작성하라.
 */

def endoMonoid[A]: Monoid[A => A] = ???

object test{
  def addThree(a: Int): Int = a + 3
  def multiplyThree(a: Int): Int = a * 3

  def preString(a: String): String = "아주 " + a
  def postString(a: String): String = a + " 함수형 프로그래밍"

  def main(args: Array[String]): Unit = {
    println(endoMonoid.op(addThree, multiplyThree)(3))
    println(endoMonoid.op(addThree, multiplyThree)(6))

    println(endoMonoid.op(multiplyThree, addThree)(3))
    println(endoMonoid.op(multiplyThree, addThree)(6))

    println(endoMonoid.op(preString, postString)("즐거운"))
    println(endoMonoid.op(preString, postString)("개같은"))

    /**
     12
     21
     18
     27
     아주 즐거운 함수형 프로그래밍
     아주 개같은 함수형 프로그래밍
     */
  }
}

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}