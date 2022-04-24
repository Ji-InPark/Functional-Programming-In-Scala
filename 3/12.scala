/**
 * 목록의 역을 돌려주는(이를 테면 List(1, 2, 3)에 대해 List(3, 2, 1)을 돌려주는) 함수를 작성하라. 접기(fold) 함수를 이용해서 작성할 수 있는지 시도해 볼 것
 */

def reverse[A](as: List[A]) = foldLeft[A, List[A]](as, Nil)((acc, e) => Cons(e, acc))
