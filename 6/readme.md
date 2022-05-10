# 순수 함수적 상태

**목표**: 임의의 상태 있는 API를 순수 함수적으로 만드는 데 쓰이는 기본 패턴 익히기 (난수 발생 예시를 씹고 뜯고 맛보고 즐기고)

---

```scala
scala> val rng = new scala.util.Random

scala> rng.nextDouble
res1: Double = 0.9867076608154569

scala> rng.nextDouble
res2: Double = 0.8455696498024141
```

scala.util.Random 클래스는 부수 효과에 의존하는 전형적인 명력식 API를 제공한다.
뭔지 모르지만 method를 호출할 때마다 값이 다르니 내부 상태가 있고, 부수 효과로 상태가 변한다.

---

부수 효과로 제자리 변이하는 대신 새 상태를 반환하여 명시적으로 갱신하자

```scala
case class SimpleRNG(seed: Long) extends RNG {
  def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nexRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }
}

scala> val rng = SimpleRNG(42)
rng: SimpleRNG = SimpleRNG(42)

scala> val (n1, rng2) = rng.nextInt
n1: Int = 16159453
rng2: RNG = SimpleRNG(1059025964525)

scala> val (n2, rng3) = rng.nextInt
n2: Int = -1281479697
rng3: RNG = SimpleRNG(197491923327988)
```

위 예시는 반복해도 같은 결과가 나온다. ★po순수wer★

---

```scala
class Foo {
    private var s: FooState = ...
    def bar: Bar
    def baz: Int
}
```

위의 bar와 baz는 상태 s를 변이시킨다. 새 상태를 반환하는 패턴을 적용하면 아래와 같다.

```scala
trait Foo {
  def bar: (Bar, Foo)
  def baz: (Baz, Foo)
}
```

---

앞의 구현을 살펴보면 모든 함수가 어떤 type A에 대해 RNG => (A, RNG) type으로 공통 패턴을 보인다. 이런 종류의 함수를 `상태 동작` 또는 `상태 전이`라고 부른다. 고차함수 `조합기(combinater)`를 이용하여 반복을 줄이자.

**map** : 출력만 변환하는 단항 함수

**map2** : 두 상태 동작를 조합하는 이항 함수 (독립)

**sequence** : map2를 확장

**flatMap** : 두 상태 동작을 조합하는 이항(?커링) 함수 (종속)

---

**일반적 상태 동작 Type** : `type State[S, +A] = S => (A, S)`

---

비극: 앞 내용을 쌩까서 아직 for-comprehesion을 잘 알지 못 함

명령식 프로그래밍을 지원하는데 필요한 조합기는 단 두 개: 상태를 읽는 조합기 / 상태를 쓰는 조합기

소신발언: get, set 잘 모르겠다

get, set 조합기와 unit, map, map2, flatMap 조합기로 어떤 종류의 state machine 또는 stateful 프로그램을 순수 함수적 방식으로 구현 가능