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




