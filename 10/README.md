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

## 결합법칙과 병렬성

- 모노이드 연산이 결합적이라는 것은 목록 같은 자료구조를 접을 때 그 방향을 선택할 수 있음을 의미한다.
- 그런데 모노이드를 이용해서 목록을 축약할 때 **균형 접기**(balanced fold)를 사용할 수도 있다.

```scala
// foldLeft
op(a, op(b, op(c, d)))

// foldRight
op(op(op(a, b), c), d)

// balancedFold
op(op(a, b), op(c, d))
```

- 균형 접기를 사용하면 병렬 처리가 가능함을 볼 수 있다.
- 또한 op의 비용이 인수 크기에 비례하는 경우 트리 구조의 균형이 좋을수록 전체 계산의 효율성이 높아진다.
- 한 예로 다음 표현식의 실행시점 성능을 보자.

```scala
List("lorem", "ipsum", "dolor", "sit").foldLeft("")(_ + _)
```

- 단계마다 전체 String을 임시로 할당했다가 폐기하고, 그 다음단계에서는 더 큰 String을 할당하게 된다.
- 다음은 위 연산의 평가를 추적한 것이다

```scala
List("lorem", "ipsum", "dolor", "sit").foldLeft("")(_ + _)
List("ipsum", "dolor", "sit").foldLeft("lorem")(_ + _)
List("dolor", "sit").foldLeft("loremipsum")(_ + _)
List("sit").foldLeft("loremipsumdolor")(_ + _)
List().foldLeft("loremipsumdolorsit")(_ + _)
"loremipsumdolorsit"
```

- 임시 String들이 생성되었다가 즉시 폐기됨을 주목하기 바란다.
- 좀 더 효율적인 전략이라면 순차열을 반으로 나누어서 결합할 것이다.
- **균형 접기**가 바로 그런 방식이다.

---

## 예제: 병렬 파싱

- 상당히 간단한 파싱 문제인 String의 단어 개수를 세는 파싱을 생각해보자.
- 그저 한문자씩 훑으면서 공백을 찾고 공백이 아닌 연속되는 문자열의 개수를 세면된다.
- 그러나 짧은 문자열이 아니라 메모리에도 다 못올라가는 크기의 String이라면 어떻게 해야할까?
- 메모리에 담을 수 있을 만큼의 크기로 나누어서 병렬로 조사하면 좋을 것이다.

- 예제로 드는 문자열을 거대한 파일인양 사용해보자
```scala
"lorem ipsum dolor sit amet"
```

- 문자열을 무작정 반으로 나누면 단어가 중간에서 나뉠 위험이 있다.
```scala
"lorem ipsum do"
"lor sit amet"
```

- 이 두 문자열의 단어 수를 센 후 결합한다면 "dolor"가 두 번 계산된다.
- 즉 이러한 경우를 처리할 수 있는, 지금까지 찾은 완전한 단어를 기억할 수 있는 자료구조가 필요하다.
- 단어 개수의 부분 결과를 다음과 같은 대수적 자료 형식으로 표현할 수 있다.
```scala
sealed trait WC
case class Stub(chars: String) extends WC
case class Part(lStub: String, words: Int, rStub: String) extends WC
```

- Stub은 가장 단순한 경우로, 아직 완전한 단어를 하나도 보지 못한 상태에 해당한다.
- Part는 지금까지 조사한 완전한 단어들의 개수를 words에 유지한다.
- lStub은 그 단어들의 왼쪽에서 발견한 부분 단어 개수를, rStub은 오른쪽 부분 단어 개수를 담는다.

---

## 접을 수 있는 자료구조

- 제 3장에서 구현한 자료구조 List와 Tree는 둘 다 접을 수 있다.
- 5장에서는 List와 아주 비슷하게 접을 수 있는 게으른 자료구조 Stream을 작성했다.
- 그리고 이번 장에서는 IndexedSeq를 접는 함수를 작성했다.

- 이런 자료구조들에 담긴 자료를 처리해야하는 코드를 작성할 때 해당 자료구조의 형태나 지연여부 등은 크게 신경 쓰지 않는 경우가 많다.
```scala
ints.foldRight(0)(_ + _)
```

- 이 코드를 작성할 때 ints의 구체적인 형식은 신경 쓸 필요가 없다.
- 그저 foldRight 메서드가 있는 형식이기만 하면 된다.
- 이러한 공통점은 다음과 같은 trait로 명시할 수 있다.
```scala
trait Foldable[F[_]]{
  def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B
  def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B
  def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B
  def concatenate[A](as: F[A])(m: Monoid[A]): A =
    foldLeft(as)(m.zero)(m.op)
}
```

- 이 특질은 형식 생성자 F에 대한 추상화를 통해 나온 것이다.
- F[_]라는 표기에서 밑줄은 F가 형식이 아니라 하나의 형식 인수를 받는 형식 생성자임을 나타낸다
- 이를 **고차 형식 생성자** 또는 **상위 종류 형식**이라고 부른다.

---

## 모노이드 합성

- Monoid 추상 자체는 그리 대단하지 않으면, 일반화된 foldMap과 결합한다 해도 약간 더 흥미로워질 뿐이다.
- 모노이드의 진정한 위력은 **합성 능력**에서 비롯된다.
- 예를 들어 형식 A와 B가 모노이드면 튜플 형식 (A, B) 역시 모노이드임을 뜻한다.

---

## 좀 더 복잡한 모노이드 합성

- 자료구조에 담긴 요소들의 형식들이 모노이드를 형성한다면 그 자료구조 자체도 흥미로운 모노이드를 형성할 때가 있다.
- 예를 들어 key-value Map이 있을 때, key 형식이 모노이드면 그런 Map들을 병합하기 위한 모노이드가 존재한다.

```scala
def mapMergeMonoid[K, V](V: Monoid[V]): Monoid[Map[K, V]] =
  new Monoid[Map[K, V]]{
    def zero = Map[K, V]()
    def op(a: Map[K, V], b: Map[K, V]) =
      (a.keySet ++ b.keySet).foldLeft(zero){
        (acc, k) => acc.updated(k, V.op(a.getOrElse(k, V.zero),
                                        b.getOrElse(k, V.zero)))
      }
  }
```

- 이 간단한(?) 조합기를 이용하면 좀 더 복잡한 모노이드를 상당히 수월하게 조립할 수 있다.
```scala
val M: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAddition))
```

- 이에 의해 추가적인 프로그래밍 없이도 모노이드를 이용해서 중첩된 표현식들을 조합할 수 있다.