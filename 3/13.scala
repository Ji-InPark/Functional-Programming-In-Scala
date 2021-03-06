/**
 * 이 문제는 어렵다 애송이드라
 * foldLeft를 foldRight를 이용해서 구현할 수 있을까?
 * 그 반대 방향은 어떨까? foldLeft를 이용하면 foldRight를 꼬리 재귀적으로 구현할 수 있으므로 긴 목록에 대해서도 스택이 넘치지 않는다는 장점이 생긴다.
 */

def foldLeft2[A, B](as: List[A], z: B)(F: (B, A) => B): B =
  foldRight(as, (t: B) => t)((a, b) => (t: B) => b(F(t, a)))(z)

def foldRight2[A, B](as: List[A], z: B)(F: (A, B) => B): B =
  foldLeft(as, (t: B) => t)((a, b) => (t: B) => a(F(b, t)))(z)
