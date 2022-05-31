/**
 * 각 형식에 대한 모나드 인스턴스를 제시하고 그 의미를 설명하라.
 * 각 인스턴스의 기본수단 연산들은 무엇인가?
 * flatMap동작은 무엇인가?
 * sequence나 join, replicateM같은 모나드적 함수에 주는 의미는 무엇인가?
 * 모나드 법칙들에 주는 의미는 무엇인가?
 */

case class Reader[R,A](run:R => A)
object Reader {
  def readerMonad[R] = new Monad[({type f[x] = Reader[R,x]})#f] {
    override def unit[A](a: => A): Reader[R, A] = ???

    override def flatMap[A, B](ma: Reader[R, A])(f: A => Reader[R, B]): Reader[R, B] = ???
  }
}

trait Monad[F[_]] {
    def unit[A](a: => A): F[A]
    def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]
}