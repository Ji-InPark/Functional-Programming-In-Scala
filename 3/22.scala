/**
 * 목록 두 개를 받아서 대응되는 요소들을 더한 값들로 이루어진 새 목록을 구축하는 함수를 작성하라.
 * 예를 들어 List(1, 2, 3)과 List(4, 5, 6)은 List(5, 7, 9)가 되어야 한다.
 */

def zipPlus(a1: List[Int], a2: List[Int]): List[Int] = a1 match {
  case Nil => Nil
  case Cons(h1, t1) => a2 match {
    case Nil => Nil
    case Cons(h2, t2) => Cons(h1 + h2, zipPlus(t1, t2))
  }
}
