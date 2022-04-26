/*
 * Either에 대한 sequence와 traverse를 작성하라.
 * 이 두 함수는 발생한 첫 오류를 돌려주어야 한다(오류가 발생하였다면).
 */

def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = ???

def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = ???
