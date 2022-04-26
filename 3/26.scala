/**
 * Tree[Int]에서 가장 큰 요소를 돌려주는 함수 maximum을 작성하라.
 * (스칼라에서 두 정수 x와 y의 최댓값은 x.maxZ(y)나 x max y로 계산할 수 있다.)
 */
sealed trait Tree[+A]
case class Leaf[A](value : A) extends Tree[A]
case class Branch[A](left:Tree[A],right:Tree[A]) extends Tree[A]

def maximum(root : Tree[Int]) : Int = root match {
  case Leaf(x) => x
  case Branch(l,r) => maximum(l) max maximum(r)
}