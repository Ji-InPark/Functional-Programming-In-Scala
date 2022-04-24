/**
 * size와 maximum, depth, map의 유사성을 요약해서 일반화한 새 함수 fold를 작성하라.
 * 그런 다음 그 함수들을 새 fold를 이용해서 다시 구현해라.
 * 이 fold 함수와 List에 대한 왼쪽, 오른쪽 fold 사이의 유사성을 찾아낼 수 있는가?
 */


sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](Left: Tree[A], right: Tree[A]) extends Tree[A]

def fold[A, B](t: Tree[A])(f: A => B)(g: (B, B) => B):B =
  t match {
    case Leaf(x) => f(x)
    case Branch(l, r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }

def depth(t: Tree[Int]): Int =
  fold(t)(_ => 1)((l, r) => 1 + l.max(r))

def maximum(t: Tree[Int]): Int =
  fold(t)(_ => _)((l, r) => l.max(r))

def map[A, B](t: Tree[A])(f: A => B): Tree[B] =
  fold(t)(x => Leaf(f(x)))((l, r) => Branch(l, r))

// 아니 이건 맞는거 같은데 왜 오류 계속 뜨지 ㅡㅡ
// 음... 형식 추론 때문인가