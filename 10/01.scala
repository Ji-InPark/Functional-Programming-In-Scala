/**
 * 정수 덧셈과 곱셈에 대한 Monoid 인스턴스들과 해당 부울 연산자들을 제시하라.
 */

val intAddition: Monoid[Int] = ???
val intMultiplication: Monoid[Int] = ???
val booleanOr: Monoid[Boolean] = ???
val booleanAnd: Monoid[Boolean] = ???

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}

object test{
  def main(args: Array[String]): Unit = {
    if(intAddition.op(3, intAddition.zero) == 3) print("테스트 1 통과") else print("테스트 1 실패")
    if(intAddition.op(3, 10) == 13) print("테스트 2 통과") else print("테스트 2 실패")

    if(intMultiplication.op(3, intMultiplication.zero) == 3) print("테스트 3 통과") else print("테스트 3 실패")
    if(intMultiplication.op(3, 10) == 30) print("테스트 4 통과") else print("테스트 4 실패")

    if(booleanOr.op(false, booleanOr.zero) == false) print("테스트 5 통과") else print("테스트 5 실패")
    if(booleanOr.op(false, true) == true) print("테스트 6 통과") else print("테스트 6 실패")

    if(booleanAnd.op(true, booleanAnd.zero) == true) print("테스트 7 통과") else print("테스트 7 실패")
    if(booleanAnd.op(false, true) == false) print("테스트 8 통과") else print("테스트 8 실패")
  }
}
