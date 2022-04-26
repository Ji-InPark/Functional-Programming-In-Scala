/**
 * append를 foldLeft나 foldRight를 이용해서 구현하라
 */

def append[A](a1: List[A], a2: List[A]) = foldRight(a1, a2)(Cons(_, _))
