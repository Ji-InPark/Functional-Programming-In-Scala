/**
 * curry의 변환을 역으로 수행하는 고차함수 uncury를 구현하라.
 * =>는 오른쪽으로 묶이므로, A => (B => C)를 A => B => C라고 표기할 수 있음을 주의할 것.
 */
def uncurry[A, B, C](f: A => B => C): (A, B) => C=
  (a: A, b: B) => f(a)(b)