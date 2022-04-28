/*
 * 두 Option 값을이항 함수(binary function)을 이용해서 결합하는 일반적 함수 map2를 작성하라.
 * 두 Option 값 중 하나라도 None이면 map2의 결과 역시 None이여야 한다.
 */

def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
  a.flatMap((av) => b.map((bv) => f(av, bv)))
