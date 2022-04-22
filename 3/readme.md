# 함수적 자료구조

## 함수적 자료구조의 정의

- 오직 순수 함수로만으로 조작되는 자료구조이다
- 즉 정의에 의해 불변이(immutable)이다

- 이 때 여분의 복사가 많이 일어나지 않을까하는 걱정이 생기게되기 마련이다

```scala
package fpinscala.datastructures

sealed trait List[+A] // 형식 A에 대해 매개변수화된 List 자료 형식
// 일반적으로 자료 형식을 도입할 때는 trait(특질) 키워드를 사용함
// trait는 하나의 추상 인터페이스로 필요시 일부 메소드의 구현을 담을 수 있음 <- 여기서는 List 인터페이스를 정의
// sealed를 붙이는 이유는 이 trait의 모든 구현이 반드시 이 파일 안에 선언되어 있어야 함을 뜻함

case object Nil extends List[Nothing] // 빈 목록을 나타내는 List 자료 생성자
case class Cons[+A](head: A, tail: List[A]) extends List[A] // 비지 않은 목록을 나타내는 또 다른 자료 생성자 
                                                            // tail은 Nil일 수도 있고 다른 Cons일 수도 있다
// 위 case로 시작하는 두 줄은 List의 두 가지 구현, 즉 두 가지 자료 생성자이다
// 위에서는 List가 취할 수 있는 두 가지 형태를 나타내는데
// 1. Nil로 표기되는 빈 List
// 2. Cons로 표기되는 비어있지 않은 List - 첫 요소 head와 나머지 요소들을 담은 Nil일 수 있는 List tail로 구성된다.

val ex1: List[Double] = Nil
val ex2: List[Int] = Cons(1, Nil)
val ex3: List[String] = Cons("a", Cons("B", Nil))
// 위와 같이 자료 형식도 다형적으로 만들 수 있다.

object List { // List 동반(Companion) 객체, 목록의 생성과 조작을 위한 함수들을 담는다
  def sum(ints: List[Int]): Int = ints match { // 패턴 부합을 이용해서 목록의 정수들을 합하는 함수
    case Nil => 0 // 빈 목록의 합은 0
    case Cons(x, xs) => x + sum(xs) // x로 시작하는 목록의 합은 x 더하기 목록 나머지 부분의 합
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
}
```

아래는 스칼라 언어 개념 설명이다
```scala
// 스칼라의 타입 시스템은 클래스 계층 관계도 처리해줘야 한다
// 공변성(covariant)	        C[T’]는 C[T]의 하위 클래스이다	[+T]
// 반공변성(contravariant)	C[T]는 C[T’]의 하위 클래스이다	[-T]
// 불변성(invariant)	        C[T]와 C[T’]는 아무 관계가 없다	[T]

// Dog가 Animal의 하위 형식이라면
// List[Dog]도 List[Animal]의 하위 형식으로 간주된다는 것

/* 위 개념중 반공변성에 대한 예제*/
class Animal { val sound = "rustle" }
class Bird extends Animal { override val sound = "call" }
class Chicken extends Bird { override val sound = "cluck" }

val getTweet: (Bird => String) = // TODO 

f: (Bird=>String)=>String     // 이 때 Duck을 넘기는 것은 괜찮음
g: (Chicken=>String)=>String  // 이 때 Duck을 넘기면 문제가 생겨벌임
h: (Animal=>String)=>String   // 그래서 Animal을 인자로 받는 함수를 넘겨버리면 Duck을 넘기는 것은 괜찮음

// 사실 왜 필요한지는 몰?루
```

- 각 자료 생성자는 sum이나 product 같은 함수들에서처럼 패턴 부합(pattern matching)에 사용할 수 있는 패턴도 도입한다

## 패턴 부합 (Pattern Matching)

- 위 object List에 속한 함수 sum과 product와 같은 함수들을 List의 동반 객체(Companion object)라고 부르기도 한다

```scala
def sum(ints: List[Int]): Int = ints match { // 패턴 부합을 이용해서 목록의 정수들을 합하는 함수
  case Nil => 0 // 빈 목록의 합은 0
  case Cons(x, xs) => x + sum(xs) // x로 시작하는 목록의 합은 x 더하기 목록 나머지 부분의 합
}

def product(ds: List[Double]): Double = ds match {
  case Nil => 1.0
  case Cons(0.0, _) => 0.0
  case Cons(x, xs) => x * product(xs)
}
```

- 위 함수들은 모두 재귀적인 정의이다
- List 같은 재귀적인 자료 형식을 다루는 함수들을 작성할 때는 이처럼 재귀적인 정의를 사용하는게 국룰이다

- 패턴 부합은 그 구조의 부분 표현식을 추출하는 복잡한 switch문과 비슷하게 동작한다
- 패턴 부합 구문은 ds 같은 표현식으로 시작해서 다음 math 키워드가 오고 그다음 일련의 경우들이 감싸진 형태이다

아래 예시를 더 보면서 익혀보자
```scala
List(1, 2, 3) match { case _ => 42} // 결과는 42다 
// 스칼라에서 무시해도 되는 변수를 나타낼 때는 _를 사용하는 것이 국룰이다

List(1, 2, 3) match { case Cons(h, _) => h} // 결과는 1이다

List(1, 2, 3) match { case Cons(_, t) => t} // 결과는 List(2, 3)이다

List(1, 2, 3) match { case Nil => 42} // 결과는 실행시점 MatchError 오류이다
// MatchError는 부합 표현식 중 대상 부합하는 것이 하나도 없음을 뜻한다
```

- 만약 부합하는 경우의 수가 많다고 했을 때 어떤 것을 리턴할지 의문이 생길 것이다
- 스칼라에서는 처음으로 부합한 문을 리턴한다

아래는 스칼라 언어 개념 설명이다
```scala
  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

// A*의 뜻은 A형식의 인수를 0개 이상 받을 수 있음을 뜻한다
// 스칼라에서는 저런 가변 인수 apply 함수를 동반 객체에 집어넣는 관례가 흔히 쓰인다고 한다

// 그런 생성 함수의 이름을 aplly로 해서 동반 객체에 집어넣으면
// List(1, 2, 3, 4) 처럼 임의의 개수의 인수들을 쉼표로 구분한 구문으로 함수를 호출할 수 있다
```

## 함수적 자료구조의 자료 공유

- 자료가 불변이라면 List에 요소를 추가하거나 요소를 제거하는 함수는 어떻게 작성해야할까?
- 답은 간단하다 걍 새로 만든다
- 사실 재사용해도 돼서 폰생성임 ㅋㅋㄹㅃㅃ

```scala
List(1, 2, 3, 4)

// 여기서 tail만 떼어다가 주면 앞에 없애는거 <- 실질적인 제거는 일어나지 않음
// 여기서 head 떼어다가 다른 원소로 바꿔 주면 원소 바꾸는거
// 사실상 Linked List에서 값을 바꾸고 싶으면 노드를 새로 만들어서 새로 갈아끼는 느낌인듯?
```

- 이러한 함수적 자료구조는 영속적이라고 한다
- 이는 자료구조에 연산이 가해져도 기존의 참조들이 결코 변하지 않음을 뜻한다

### 자료 공유의 효율성

- 자료 공유를 이용하면 연산을 좀 더 효율적으로 수행할 수 있는 경우가 많다

```scala
def append[A](al: List[A], a2: List[A]): List[A] =
  a1 match {
    case Nil => a2
    case Cons(h, t) => Cons(h, append(t, a2))
  }
```

- 위 함수는 첫 목록이 다 소진될 때까지만 값들을 복사한다
- 즉 실행시간과 메모리는 오직 al의 길이에만 의존한다
- 만약 이 함수를 배열 2개를 이횽해서 구현한다면 두 배열의 모든 요소를 결과 배열에 복사해야 했을 것이다

- 순수 함수적 자료구조를 작성할 때는 자료 공유를 현명하게 사용하는 방법을 찾아내는 것이 중요하다 

### 고차 함수를 위한 형식 추론 개선

- dropWhile 같은 고차 함수에는 흔히 익명 함수를 넘겨준다

```scala
def dropWhile[A](l: List[A], f: A => Boolean): List[A]

val xs: List[Int] = List(1, 2, 3, 4, 5)
val ex1 = dropWhile(xs, (x: Int) => x < 4)
```

- 인수 f에 익명함수를 지정해서 호출하기 위해서는 그 익명 함수의 인수 형식을 명시해야한다
- 이렇게 x의 형식이 Int임을 명시적으로 표기해야 한다는 것은 다소 번거롭다

```scala
def dropWhile[A](as: List[A])(f: A => Boolean): List[A] =
  as match {
    case Cons(h, t) if f(h) => dropWhile(t)(f)
    case _ => as
  }
  
val xs: List[Int] = List(1, 2, 3, 4, 5)
val ex1 = dropWhile(xs)(x => x < 4)
```

- 이 버전의 dropWhile을 호출하는 구문은 dropWhile(xs)(f) 형태이다  
- 즉 인수들을 이런 식으로 묶는 것은 스칼라의 형식 추론을 돕기 위한 것이다

## 목록에 대한 재귀와 고차 함수로의 일반화

- sum과 product의 구현을 다시 살펴보자
- product의 구현은 0.0의 점검을 위한 '평가 단축(short-circuiting)' 논리를 포함하지 않도록 조금 단순화했다

```scala
def sum(ints: List[Int]): Int = ints match {
  case Nil => 0
  case Cons(x, xs) => x + sum(xs)
}

def product(ds: List[Double]): Double = ds match {
  case Nil => 1.0
  case Cons(x, xs) => x *  product(xs)
}
```

- 두 정의가 아주 비슷하다는 점에 주목하자
- 타입을 제외한 유일한 차이는 빈 목록일 때의 반환값, 그리고 결과를 결합하는데 쓰이는 연산뿐이다

- 이렇게 중복되는 부분을 발견했다면 부분 표현식들을 추출해서 일반화하는 것이 항상 가능하다

```scala
def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // 스칼라가 형식 추론을 할 수 있게 개별적 인수그룹에 넣어줌
  as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

def sum2(ns: List[Int]) =
  foldRight(ns, 0)((x,y) => x + y)

def product2(ns: List[Double]) =
  foldRight(ns, 1.0)(_ * _)       // _ * _은 (x, y) => x * y를 좀 더 간결하게 표기한 것이다.
```

- foldRight는 하나의 요소 형식에만 특화되지 않았다
- 그리고 일반화 과정에서 이 함수가 돌려주는 값이 목록의 요소와 같은 형식일 필요가 없다는 점도 알게되었다
- foldRight가 하는 일을 아래와 같이 설명할 수 있다: Nil과 Cons를 z와 f로 치환한다

```scala
Cons(1, Cons(2, Nil))
f   (1, f   (2, z))
```

- foldRight가 하나의 값으로 축약되려면 반드시 목록의 끝까지 순회해야 함을 주목하기 바란다

### 단순 구성요소들로 목록 함수를 조립할 때의 효율성 손실

- List의 한 가지 문제는, 어떤 연산이나 알고리즘을 아주 범용적인 함수들로 표현이 가능하다
- 하지만 그 결과로 만들어진 구현이 항상 효율적이지는 않다는 점이다

## 트리

- List는 소위 대수적 자료 형식이라고 부르는 것의 한 예일뿐이다
- 대수적 자료 형식이라는 이름에 걸맞게 대수학의 용어들이 쓰임을 주목하기 바란다

- 대수적 자료 형식을 다른 자료구조의 정의에 사용할 수 있다
- 간단한 이진 트리를 정의해보자

```scala
sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](Left: Tree[A], right: Tree[A]) extends Tree[A]
```
