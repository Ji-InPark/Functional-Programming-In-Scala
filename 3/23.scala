/**
 * 연습문제 3.22의 함수를 정수나 덧셈에 국한되지 않도록 일반화하라.
 * 함수의 이름은 zipWith로 할 것.
 */

def zipWith[A, B, C](a: List[A], b: List[B])(f: (A, B) => C): List[C] = a match {
  case Nil => Nil
  case Cons(ha, ta) => b match {
    case Nil => Nil
    case Cons(hb, tb) => Cons(f(ha, hb), zipWith(ta, tb)(f))
  }
}
