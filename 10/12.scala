/**
 * Foldable[List]와 Foldable[IndexedSeq], Foldable[Stream]을 구현하라.
 * foldRight와 foldLeft, foldMap 모두 서로를 이용해서 구현할 수 있지만, 그것이 가장 효율적인 구현은 아닐 수 있음을 기억하기 바란다.
 *
 * 이 부분은 테스트가 없습니다...
 */

object ListFoldable extends Foldable[List] {
  override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B) = ???
  override def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B) = ???
  override def foldMap[A, B](as: List[A])(f: A => B)(mb: Monoid[B]): B = ???
}

object IndexedSeqFoldable extends Foldable[IndexedSeq] {
  override def foldRight[A, B](as: IndexedSeq[A])(z: B)(f: (A, B) => B) = ???
  override def foldLeft[A, B](as: IndexedSeq[A])(z: B)(f: (B, A) => B) = ???
  override def foldMap[A, B](as: IndexedSeq[A])(f: A => B)(mb: Monoid[B]): B = ???
}

object StreamFoldable extends Foldable[Stream] {
  override def foldRight[A, B](as: Stream[A])(z: B)(f: (A, B) => B) = ???
  override def foldLeft[A, B](as: Stream[A])(z: B)(f: (B, A) => B) = ???
}

trait Foldable[F[_]] {
  def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B =
    foldMap(as)(f.curried)(endoMonoid[B])(z)

  def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B =
    foldMap(as)(a => (b: B) => f(b, a))(dual(endoMonoid[B]))(z)

  def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B =
    foldRight(as)(mb.zero)((a, b) => mb.op(f(a), b))

  def concatenate[A](as: F[A])(m: Monoid[A]): A =
    foldLeft(as)(m.zero)(m.op)
}

def dual[A](m: Monoid[A]): Monoid[A] = new Monoid[A] {
  def op(x: A, y: A): A = m.op(y, x)
  val zero = m.zero
}

def endoMonoid[A]: Monoid[A => A] = new Monoid[A => A] {
  def op(f: A => A, g: A => A) = f compose g
  val zero = (a: A) => a
}

trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}