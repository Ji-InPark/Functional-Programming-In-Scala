# 10. 모노이드

## 모노이드란 무엇인가?

- 한 문장으로 표현하자면 **결합법칙과** **항등법칙이** 적용되는 모든 대수를 **모노이드**라고한다.
- 그래서 **결합법칙**과 **항등법칙**이 무엇이냐?

### 결합법칙 

문자열 결합 대수를 생각해보자.

"foo" + "bar" + "jin"의 결과는 "foobarjin"이다.

이 때 괄호로 "foo" + ("bar" + "jin") 와 같이 묶든 ("foo" + "bar") + "jin" 처럼 묶든 항상 같은 결과가 나온다.

이러한 법칙을 **결합법칙**이라 한다.

### 항등법칙

위 예제처럼 문자열을 연결하는 연산은 **항등원**이 빈 문자열이다.

즉 (s + "") 나 ("" + s) 의 결과는 항상 s다.

정수 덧셈에서 항등원은 0이고, 정수 곱셈에서 항등원은 1이다.

이러한 법칙을 **항등법칙**이라 한다.

---

위 **결합법칙**과 **항등법칙**을 합쳐서 **모노이드 법칙**이라고 부른다.

하나의 모노이드는 다음과 같은 요소들로 구성된다.

- 어떤 형식 A
  - ex) Int
- A 형식의 값 두 개를 받아서 하나의 값을 산축하는 결합적 이항 연산 op.
- 임의의 x: A, y: A, z: A에 대해 op(op(x, y) z) == op(x, op(y, z))가 성립한다.
  - ex) x = 1, y = 2, z =3, op = + 일 때 ((x + y) + z) == (x + (y + z))가 성립한다.
- 그 연산의 항등원인 값 zero: A, 임의의 x: A에 대해 op(x, zero) == x이고 op(zero, x) == x 이다.
  - ex) zero = 0, x = 1, op = + 일 때 (x + zero) == x이고 (zero + x) == x 이다.

이를 스칼라 코드로 보면 다음과 같다.

```scala
trait Monoid[A]{
  def op(a1: A, a2: A): A
  def zero: A
}

val stringMonoid = new Monoid[String]{
  def op(a1: String, a2: String) = a1 + a2
  val zero = ""
}

def listMonoid[A] = new Monoid[List[A]]{
  def op(a1: List[A], a2: List[A]) = a1 ++ a2
  val zero = Nil
}
```

---

## 모노이드를 이용한 목록 접기 (fold)

- 모노이드는 목록과 밀접한 관계가 있다고 한다.
- List에 대한 foldLeft와 foldRight의 서명을 보면, 파라매터 형식들에 눈에 띄는게 있을 것이다.

```scala
def foldRight[B](z: B)(f: (A, B) => B) : B
def foldLeft[B](z: B)(f: (B, A) => B) : B
```

- 이 때 A와 B가 같은 형식이면 어떨까?

```scala
def foldRight[A](z: A)(f: (A, A) => A) : A
def foldLeft[A](z: A)(f: (A, A) => A) : A
```

- 이렇게 보니 모노이드 구성요소들과 형식이 딱 들어맞는다!
- 즉 모노이드의 op와 zero만 넘겨주면 문자열 연결과 같은 일이 가능하다!

```scala
val words = List("Hic", "Est", "Index")
val s = words.foldRight(StringMonoid.zero)(stringMonoid.op) //"HicEstIndex"
val t = words.foldLeft(StringMonoid.zero)(stringMonoid.op)  //"HicEstIndex"
```

- 이 때 foldLeft와 foldRigth 중 어떤 것을 사용해야 하는지 고민할 수도 있을 것이다.
- 하지만 모노이드를 사용할 때는 항상 둘 다 같은 결과를 낸다.
- 이유는 결합법칙과 항등법칙이 성립하기 때문이다!

```scala
words.foldLeft("")(_ + _) == (("" + "Hic") + "Est") + "Index"
words.foldRight("")(_ + _) == "Hic" + ("Est" + ("Index" + ""))
```

- 이를 킹반화해서 모노이드로 목록을 접는 일반적 함수 concatenate를 만들 수도 있다.

```scala
def concatenate[A](as: List[A], m: Monoid[A]): A =
  as.foldLeft(m.zero)(m.op)
```

- 그런데 목록의 원소 형식이 Monoid 인스턴스와는 부합하지 않을 수도 있다.
- 그럴 때에는 map을 이용해서 형식을 맞춰주면 된다.
```scala
def foldMap[A, B](as: List[A], m: Monoid[B])(f: A => B): B
```

---
