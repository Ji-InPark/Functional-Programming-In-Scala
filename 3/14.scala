import scala.annotation.tailrec

/**
 * append를 foldLeft나 foldRight를 이용해서 구현하라
 */

object Main {
  def main(args : Array[String]): Unit = {
    val list = Cons(1,Cons(2,Cons(3,Nil)))
    println(List.append(list,4).toString)
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

    def append[A](as : List[A], z : A) : List[A] = foldRight(as, Cons(z,Nil))((x,y) =>
        Cons(x,y)
      )

  }
}
