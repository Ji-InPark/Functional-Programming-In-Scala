/**
 * flatMap을 이용해서 filter를 구현하라
 */

def filter[A](as: List[A])(f: A => Boolean) =
  flatMap(as)((a) => if(f(a)) Cons(a, Nil) else Nil)
