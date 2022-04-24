/**
 * tail을 일반화해서, 목록에서 처음 n개의 요소를 제거하는 함수 drop을 구현하라.
 * 이 함수의 실행 시간은 제거되는 원소의 개수에만 비례함을 주의할 것.
 * List 전체의 복사본을 만들 필요는 없다
 */

def drop[A](l: List[A], n: Int): List[A] =
  if (n == 0) l
  else l match {
    case Cons(_, xs) => drop(xs, n - 1)
    case Nil => Nil
  }
