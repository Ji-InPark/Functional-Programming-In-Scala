# 7장 - 순수함수적 병렬성

목표: 

- 순수 함수적 라이브러리의 설계 문제를 배우는 것이 중요하다.
- 서술과 실행의 관심사를 분리하는 법

이번 챕터에서 병렬처리는 단순히 도구에 불과

실제로는 함수형 라이브러리를 설계하는 방식을 순차적으로 접근하는데 주안을 맞춘다.

## 7.1.2 자료형식과 함수의 선택

unit: 주어진 인수를 개별적인 스레드(논리적 스레드)에서 즉시 평가한다.
get: 결과 값을 추출한다.

sum 함수의 문제점?

```scala
def sum(ints: IndexedSeq[Int]): Int = 
  if (ints.size <= 1)
    ints headOption getOrElse 0
  else {
    val (l, r) = int.splitAt(ints.length/2)
    val sumL: Par[Int] = Par.unit(sum(l))
    val sumR: Par[Int] = Par.unit(sum(r))
    Par.get(sumL) + Par.get(sumR)
  }
```

만약 위의 sumL과 sumR이 지연되지 않고 즉시 병렬적으로 실행된다면? get을 호출하는 시점에서 완료되지 않은 계산값을 가져올 수 있다.
=> 참조 투명성이 깨진다. 즉, 매번 호출시 같은(순수한) 값 반환을 보장하지 않음.

```scala
Par.get(Par.unit(sum(l))) + Par.get(Par.unit(sum(r)))
```

위와 같이 바꾸면? get은 평가가 완료될 떄(unit의 평가가 끝날때)까지 기다려야 한다.
=> 병렬로 실행되지 않는다. 순차 실행과 다를게 없음.
=> 이는 get에 한정적인 부수 효과(Side Effect)가 존재함을 의미한다.
=> 따라서 즉시 실행(Eager Evaluation)되지 않고 지연 실행(Lazy Evaluation)이 되도록 바꿔야 한다.

```scala
def sum(ints: IndexedSeq[Int]): Par[Int] =
  if (ints.size <= 1)
    Par.unit(ints.headOption getOrElse 0)
  else {
    val (l, r) = ints.splitAt(ints.length/2)
    Par.map2(sum(l), sum(r))(_ + _)
  }
```

map2는 두 인수를 병렬적으로 실행한다. 따라서 map2를 도입해서 바꾸는 것이 필요하다. 

## 7.1.3 명시적 분기

그러나 병렬로 항상 평가하는 건 적은 수의 인수를 계산하는 경우에는 오버헤드가 존재한다.

```scala
Par.map2(Par.unit(l), Par.unit(l))(_ + _)
```

 위 예제는 굳이 병렬이 아니더라도 빠르게 계산됨이 *자명*하다. 따라서 병렬 실행에 대한 분기 처리가 필요하다.
 이를 위해 ```fork``` 함수를 도입한다.

```scala
def sum(ints: IndexedSeq[Int]): Par[Int] =
  if (ints.size <= 1)
    Par.unit(ints.headOption getOrElse 0)
  else {
    val (l, r) = ints.splitAt(ints.length/2)
    Par.map2(fork(sum(l)), fork(sum(r)))(_ + _)
  }
```

그러나 아직 fork를 즉시 실행(Eager Evaluation) 할 것인지 지연 실행(Lazy Evaluation) 할 것인지를 결정하지는 않았다.
=> 평가의 책임을 어디에 둘 것인가? fork? get?

1. fork 에 평가 책임을 두고 즉시 평가하는 경우
 fork가 즉시 평가한다고 가정해보자. 그러면 fork는 스레드를 생성하는 방법, 어떤 작업을 스레드풀에 넘기는 방법 등을 알고 있어야 한다.
 이는 스레드풀이 전역적 자원으로 접근 가능해야함을 의미한다. 그런데 과연 그것이 설게상 적절한가? 따라서 *get에 평가 책임을 두는 것이 적절*해보인다.

2. 지연 평가하는 경우
 1번에서 언급한 문제는 고려대상이 아니게 된다. 이 경우 get의 이름을 run으로 바꾸어 생각하는 것이 가능.

 => 관심사의 분리 가능
   
```
def run[A](a: Par[A]): A
```

이제 기존의 get 함수는 run 함수로 이름이 변경되었고 이름에 걸맞게 병렬성을 구현해야한다.


## 7.2 표현의 선택

각 함수에 대해 우리는 다음과 같이 의미들을 부여한다.

```scala
def unit[A](a: A): Par[A] // 상수 값을 병렬 계산으로 승격
def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C):Par[C] // 두 병렬 계산의 결과들을 이항 함수로 조합
def fork[A](a: => Par[A]): Par[A] //주어진 인수가 동시적으로 평가될 계산임을 표시한다. 그 평가는 run에 강제되어야 실제로 실행된다.
def lazyUnit[A](a: => A): Par[A] = fork(unit(a)) // 평가되지 않은 인수를 Par로 감싸고, 그것을 병렬 평가 대상으로 표시한다.
def run[A](a: Par[A]): A // 계산을 실제로 실행하여 Par로 부터 값을 추출한다.
```

비동기를 구현하기 위해 Java의 표준 라이브러리에 존재하는 ExecutorService를 사용한다.

```scala
def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)
```

run은 Future를 반환

## 7.3 API의 정련
 - 표현에 대한 착안이 API 설계의 개선에 도움이 된다.
 - API의 특성이 표현의 선택에 힌트를 준다.

 지금까지 작성한 API는 정제되지 않은 형태이다. 따라서 이와 같은 형태의 API를 개선한다. 
 

 ```scala
 object Par:
  extension [A](pa: Par[A]) def run(s: ExecutorService): Future[A] = pa(s)

  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  private case class UnitFuture[A](get: A) extends Future[A]:
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false

  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] = 
    (es: ExecutorService) => {
      val af = a(es)
      val bf = b(es)
      UnitFuture(f(af.get, bf.get))
    }

 ```

여기서 위의 Future 인터페이스는 순수 함수적이지 않다. 비록 Future의 메소드들이 부수 효과에 의존하지만 Par API 자체는 순수하다.

Future의 내부 동작은 run 호출 후 ExcutorService를 받아야만 들어난다.(언젠가는 부수효과가 들어난다.)

하지만 Par API가 순수하므로 사실상 부수효과가 아니다. 어차피 사용자는 순수한 인터페이스로 프로그램을 작성할 수 있으므로.


### sortPar의 일반화

List[Int]를 병렬로 게산한다. 근데 이를 다시 정렬하는 함수 sortPar를 가정

Par에 run을 적용하고, 결과 목록을 정렬한 뒤, 정렬된 목록을 unit을 이용해서 다시 Par로 돌려주는 방법이 있다. 그러나 run 호출을 지양.

따라서 map2를 사용해 정렬한다.

```scala
def sortPar(parList: Par[List[Int]]): Par[List[Int]] =
  map2(parList, unit(()))((a, _) => a.sorted)
```

그런데 여기서 더 일반화가 가능하다. map을 다음과 같이 정의한다.

```scala
def map[A, B](pa: Par[A])(f: A => B): Par[B] =
  map2(pa, unit(()))((a, _) => f(a))
```

그러면 이제 sortPar를 map으로 표현가능하다.

```scala
def sortPar(parList: Par[List[Int]]): Par[List[Int]] =
  map(parList)(_.sorted)
```

이로써 간결해졌으며, 명확하다.

### parMap

2개 만이 아니라 N개에 대해서도 적용가능한 함수 parMap을 만들어보자.

```scala
def parMap[A, B](ps: List[A])(f: A=>B): Par[List[B]] = fork {
  val fbs = ps.map(asyncF(f))
  sequence(fbs)
}
```

계산들의 결과를 취합하는 sequence 함수가 있으면 parMap을 위와 같이 구현할 수 있다.

## 7.4 API의 대수

API를 하나의 대수(algebra), 일단의 법칙(law) 또는 참이라고 가정하는 속성(property)들을 가진 추상적인 연산 집합으로 간주하고, 그 대수에 정의된 규칙에 따라 그냥 형식적으로 기호를 조작하면서 문제를 풀어나간다.

### 7.4.1 map의 법칙

```scala
map(unit(1))(_ + 1) == unit(2)
```

공식을 함수 f로 일반화 가능

```scala
map(unit(x))(f) == unit(f(x))
```

```scala
map(unit(x))(f) == unit(f(x)) // 초기법칙
map(unit(x))(id) == unit(id(x)) // f를 항등함수로 치환
map(unit(x))(id) == unit(x) // 단순화
map(y)(id) == y // 양변에 unit(x)를 y로 치환
```

### 7.4.2 fork의 법칙

```scala
fork(x) == x
```

### 7.4.3 법칙 깨기: 미묘한 버그 하나

fork를 고정된 크기의 스레드풀로 사용하는 경우 교착이 발생하게된다.

```scala
def fork[A](a: => Par[A]): Par[A] =
  es => es.submit(new Callable[A] {
    def call = a(es).get
  })
```

 1. Callable을 먼저 제출.
 2. 그 Callable 안에서 또 다른 Callable을 ExecutorService에 제출한다. 
 3. 마지막으로 제출한 결과가 나오기 전까지는 실행이 차단.
 4. 이 때 스레드풀 크기가 1 이라면? 유일한 스레드가 제출되고 사용가능한 스레드가 남아 있지 않게되어 서로 기다리게 된다.

이러한 반례를 발견하면? 다음 두가지 선택지가 존재.

1. 법칙이 성립하도록 구현을 고친다.
2. 법칙이 성립하는 조건들을 좀 더 명시적으로 밝히도록 법칙을 정련한다.

```scala
def fork[A](fa: => Par[A]): Par[A] =
  es => fa(es)
```

 위와 같은 방법으로

 그러나 이는 고정 크기 스레드 풀 방식이 아니다. 

### 7.4.4 행위자를 이용한 완전 비차단 Par 구현

고정 크기 스레드 풀에서도 여전히 잘 작동하며, 전혀 차단되지 않는(Fully Non-Blocking: 완전 비차단) 방식으로 구현해본다.

본질적인 문제: Futre에서 get을 호출하지 않으면 값을 꺼낼 수 없다. 그런데 문제는 get은 차단 방식이다.

*비차단: 현재의 스레드를 차단하지 않는 방식.

#### 기본 착안
 꺼내기(get) 대신 적당한 때에 호출되는 *콜백* (또는 계속 함수라고도 함) 을 등록할 수 있는 Future를 만든다.


```scala
def run[A](s: ExecutorService)(a: Par[A]): A = {
  val ref = new AtomicReference[A] // 스레드 안전한 변수, 원자적 읽기/쓰기 지원
  val latch = new CountDownLatch(1) // counDown 메서드가 일정 횟수만큼 호출될 때까지 스레드를 대기
  
  a(s){ a => ref.set(a); latch.countDown } // p로부터 값을 받으면 latch의 값을 감소 
  latch.await // 결과를 받기전까지는 스레드가 대기한다.
  ref.get // 결과를 받은 이후 값을 return 한다.
}

def unit[A](a: A) : Par[A] =
  es => new Future[A] {
    def apply(cb: (A) => Unit): Unit = cb(a) // 그냥 전달한다
  }

def fork[A](a: => Par[A]): Par[A] =
  es => new Future[A] {
    def apply(k: (A) => Unit): Unit = eval(es)(a(es)(k))
  }

def eval(es: ExecutorService)(r: => Unit):Unit =
 es.submit(new Callable[Unit] { def call = r}) // fork에 대한 보조함수
```

- latch가 풀리기 전까지는 run을 호출하는 스레드는 차단된다.
- unit 함수의 경우 그냥 콜백을 전달
- fork는 받은 a(콜백)에 대해 다른 스레드에 비동기적으로 호출한다.


map2를 구현하기 위해서는 간략한 행위자가 필요하다. map2는 두 인수를 병렬적으로 실행한다.

그러나 그 과정에서 경쟁 조건(race condition)이 발생할 수 있다. -> 왜 발생?

행위자는 메시지를 받은 경우에만 스레드를 점유한다. 행위자는 여러 메시지를 받아도 오직 한 번에 하나만 처리한다.

### 행위자를 이용한 map2 구현


## 7.5 조합기를 일반적인 형태로 정련

함수적 설계는 반복적인 과정이다.

API 명세 정의 -> prototype 구현 -> 복잡하고 현실적인 시나리오 적용 -> 새로운 조합기 필요

조합기를 가장 일반적인 형태로 정련할 필요가 있음. 그러나 새 조합기가 필요한것이 아니라 좀더 일반적인 조합기의 특별한 경우가 필요했던 것이다.


```scala

```


## 7.6 요약

참조

http://dogfeet.github.io/articles/2012/by-example-continuation-passing-style-in-javascript.html
https://knight76.tistory.com/entry/scala-%EB%B3%91%EB%A0%AC-%EC%BD%9C%EB%A0%89%EC%85%98-par-collection