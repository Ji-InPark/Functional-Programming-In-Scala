/**
 * 목록의 구조를 유지하면서 목록의 각 요소를 수정하는 작업을 일반화한 함수 map을 작성하라. 서명은 다음과 같다.
 */

def map[A, B](as: List[A])(f: A => B): List[B] = foldRight[A, List[B]](as, Nil)((a, b) => Cons(f(a), b))
