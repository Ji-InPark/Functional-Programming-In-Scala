/**
 * 트리의 노드, 즉 잎(leaf)과 가지(branch)의 개수를 세는 함수 size를 작성하라.
 */

sealed trait Tree[+A]
case class Leaf[A](value : A) extends Tree[A]
case class Branch[A](left:Tree[A],right:Tree[A]) extends Tree[A]

def size[A](root: Tree[A]): Int = root match {
  case Leaf(_) => 1
  case Branch(l,r) => 1 + size(l) + size(r)
}