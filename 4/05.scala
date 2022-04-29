/*
 * 아래의 함수를 구현하라.
 * map과 sequence를 사용하면 더 간단하겟지만, 목록을 단 한번만 훑는 좀 더 효율적인 구현을 시도해 볼 것.
 * 더 나아가서, sequence를 이 traverse로 구현해 보라.
 */

def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = a.foldRight(Some(Nil): Option[List[B]])((e, acc) => for (
  accv <- acc;
  bv <- f(e)
) yield bv :: accv)

def sequence[A](a: List[Option[A]]): Option[List[A]] = traverse(a)((e) => e)
