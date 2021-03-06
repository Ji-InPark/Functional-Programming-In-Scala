import scala.annotation.tailrec

/**
 * n번째 피보나치 수를 돌려주는 재귀 함수를 작성하라.
 * 처음 두 피보나치 수는 0과 1이다. n번째 피보나치 수는 항상 이전 두 수의 합이다.
 * 즉, 피보나치수열은 0, 1, 1, 2, 3, 5로 시작한다.
 * 반드시 지역 꼬리 재귀 함수를 사용해서 작성할 것.
 */

  def fib(n: Int): Int = {
    @tailrec
    def fibAdd(n: Int, first : Int, second : Int) : Int = {
      if(n <= 1) first
      else if(n == 2) second
      else fibAdd(n-1, second, first + second)
    }
    fibAdd(n, 0, 1)
  }
