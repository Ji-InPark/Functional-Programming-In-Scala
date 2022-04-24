import scala.annotation.tailrec

/**
 * 이 문제도 어렵다 애송이드라
 * 목록들의 목록을 하나의 목록으로 연결하는 함수를 작성하라.
 * 실행 시간은 반드시 모든 목록의 전체 길이에 선형으로 비례해야 한다.
 * 이미 정의한 함수들을 활용하도록 노력할 것
 */
object Main {
  def main(args : Array[String]): Unit = {
  }

  sealed trait List[+A]
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  object List {
    def foldRight[A,B](as: List[A], z: B)(f:(A,B) => B) : B = as match {
      case Nil => z
      case Cons(x,xs) => f(x, foldRight(xs,z)(f))
    }

    def foldLeft[A, B](as: List[A], z: B)(F: (B, A) => B): B = {
      @tailrec
      def fold(as: List[A],acc: B)(F:(B,A)=>B) : B = as match {
        case Nil => acc
        case Cons(x,xs) => fold(xs, F(acc,x))(F)
      }

      fold(as,z)(F)
    }

    def flatten[A](as : List[List[A]]) : List[A] = foldRight(as,Nil: List[A])((xs, y) => foldLeft(xs,y)(x2, y2) => Cons(x2,y2))
  }
}