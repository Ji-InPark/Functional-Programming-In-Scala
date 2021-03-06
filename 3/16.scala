/**
 * 정수 목록의 각 요소에 1을 더해서 목록을 변환하는 함수를 작성하라.
 * (주의: 새 List를 돌려주는 순수 함수이어야 한다.)
 */

def addOne(as: List[Int]) = foldRight[Int, List[Int]](as, Nil)((a, b) => Cons(a + 1, b))
