/**
 * size와 maximum, depth, map의 유사성을 요약해서 일반화한 새 함수 fold를 작성하라.
 * 그런 다음 그 함수들을 새 fold를 이용해서 다시 구현해라.
 * 이 fold 함수와 List에 대한 왼쪽, 오른쪽 fold 사이의 유사성을 찾아낼 수 있는가?
 */

sealed trait Tree[+A]
case class Leaf[A](value : A) extends Tree[A]
case class Branch[A](left:Tree[A],right:Tree[A]) extends Tree[A]

def fold[A,B](root : Tree[A],z: B)(f:(A,B) => B)(g:(B,B) => B) : B = root match {
  case Leaf(x) => f(x,z)
  case Branch(l,r) => g(fold(l,z)(f)(g),fold(r,z)(f)(g))
}

def size[A](root : Tree[A]) : Int= fold(root,0)((_,y) => y+1)((x,y)=> x+y)

def depth[A](root: Tree[A]) : Int = fold(root,0)((_,y)=> y+1)((x,y) => 1 + (x max y))

def map[A,B](root:Tree[A])(f:A => B) : Tree[B] = fold(root,null: Tree[B])((x,_) => Leaf(f(x)))((x,y) => Branch(map(x)(f),map(y)(f)) )