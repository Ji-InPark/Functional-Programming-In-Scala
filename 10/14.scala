/**
 * Foldable[Option] 인스턴스를 작성하라
 *
 * 이 부분은 테스트가 없습니다...
 */

object OptionFoldable extends Foldable[Option] {
  override def foldMap[A, B](as: Option[A])(f: A => B)(mb: Monoid[B]): B = ???
  override def foldLeft[A, B](as: Option[A])(z: B)(f: (B, A) => B) = ???
  override def foldRight[A, B](as: Option[A])(z: B)(f: (A, B) => B) = ???
}


trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}