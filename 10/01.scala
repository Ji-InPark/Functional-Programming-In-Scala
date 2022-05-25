/**
 * 정수 덧셈과 곱셈에 대한 Monoid 인스턴스들과 해당 부울 연산자들을 제시하라.
 */

val intAddition: Monoid[Int] = ???
val intMultiplication: Monoid[Int] = ???
val booleanOr: Monoid[Boolean] = ???
val booleanAnd: Monoid[Boolean] = ???

object test{
  def main(args: Array[String]): Unit = {
    if(intAddition.op(3, intAddition.zero) == 3) println("테스트 1 통과") else println("테스트 1 실패")
    if(intAddition.op(3, 10) == 13) println("테스트 2 통과") else println("테스트 2 실패")

    if(intMultiplication.op(3, intMultiplication.zero) == 3) println("테스트 3 통과") else println("테스트 3 실패")
    if(intMultiplication.op(3, 10) == 30) println("테스트 4 통과") else println("테스트 4 실패")

    if(booleanOr.op(false, booleanOr.zero) == false) println("테스트 5 통과") else println("테스트 5 실패")
    if(booleanOr.op(false, true) == true) println("테스트 6 통과") else println("테스트 6 실패")

    if(booleanAnd.op(true, booleanAnd.zero) == true) println("테스트 7 통과") else println("테스트 7 실패")
    if(booleanAnd.op(false, true) == false) println("테스트 8 통과") else println("테스트 8 실패")
  }
}


trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}
