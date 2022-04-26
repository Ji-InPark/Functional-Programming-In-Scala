/**
 * 트리의 뿌리(root)에서 임의의 잎으로의 가장 긴 경로의 길이를 돌려주는 함수 depth를 작성하라
 */

sealed trait Tree[+A]
case class Leaf[A](value : A) extends Tree[A]
case class Branch[A](left:Tree[A],right:Tree[A]) extends Tree[A]

def depth(root : Tree[Int]) : Int = root match {
  case Leaf(x) => 1
  case Branch(l,r) => 1  + (depth(l) max depth(r))
}