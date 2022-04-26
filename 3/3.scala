/**
 * 같은 맥락에서 List의 첫 요소를 다른 값으로 대체하는 함수 setHead를 구현하라.
 */

def setHead[A](list: List[A], head: A) = list match {
  case Cons(_, xs) => Cons(head, xs)
  case Nil => Cons(head, Nil)
}
