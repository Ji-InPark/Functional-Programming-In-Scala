import scala.annotation.tailrec

/**
 * 이 문제는 어렵다 애송이드라
 * foldLeft를 foldRight를 이용해서 구현할 수 있을까?
 * 그 반대 방향은 어떨까? foldLeft를 이용하면 foldRight를 꼬리 재귀적으로 구현할 수 있으므로 긴 목록에 대해서도 스택이 넘치지 않는다는 장점이 생긴다.
 */

object Main {
  def main(args : Array[String]): Unit = {
    println(foldLeft(Cons(1,Cons(2,Cons(3,Nil))),0)(_ + _))
  }

  sealed trait List[+A]
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]

//  def foldLeft[A, B](as: List[A], z: B)(F: (B, A) => B): B = {
//    @tailrec
//    def fold(as: List[A],acc: B)(F:(B,A)=>B) : B = as match {
//      case Nil => acc
//      case Cons(x,xs) => fold(xs, F(acc,x))(F)
//    }
//
//    fold(as,z)(F)
//  }

  def foldRight[A,B](as:List[A],z: B)(f: (A,B) => B) : B = as match {
    case Nil => z
    case Cons(x,xs) => f(x,foldRight(xs,z)(f))
  }

  def foldLeft[A,B](as:List[A],z: B)(f:(B,A)=>B) : B = as match {
    case Cons(x,xs) => foldRight(xs,f(x,z))
  }

  def foldRight[A,B](as:List[A], z: B)(f:(A,B)=>B) : B = as match {
    case Nil => {}
    case Cons(x,xs) => xs match {
      case Nil => foldLeft()
      case Cons(x1,xs2) => foldLeft(xs,)
    }
  }
}
