# 예외를 이용하지 않은 오류 처리

## 예외의 장단점

- 예외는 참조 투명하지 않다. (try-catch 라는 context에 영향을 받는다) 문맥에 의존적이다.

```scala
def failingFn(i: Int): Int = {
  val y: Int = throw new Exception("fail!")
  try {
    val x = 42 + 5
    x + y
  } catch {
    case e: Exception => 43
  }
}

def failingFn2(i: Int): Int = {
  try {
    val x = 42 + 5
    x + ((throw new Exception("fail!")): Int)
  } catch {
    case e: Exception => 43
  }
}
```

- 함수의 형식만 보면 무슨 오류를 던질지 모른다. 형식에 안전하지 않다.

```scala
def throwSomething(int: Int): Int =
  if (int == 0) throw new Exception()
  else int

val someFunction: (Int) => Int = throwSomething
```

- 하지만 오류 처리 코드를 통합할 수 있다는 장점을 준다.

## 예외의 가능한 대안들

예시에 사용할 평균 함수이다.
리스트에 값이 없다면 당연히 결과를 낼 수 없다.
일부 입력에 대해서는 정의되지 않으므로 부분 함수(partial function)이다.

```scala
def mean(xs: Seq[Double]): Double =
  if (xs.isEmpty)
    throw new ArithmeticException("mean of empty list!")
  else xs.sum / xs.length
```

아래는 몇가지 대안이다.

- 가짜 값을 돌려주기
  ```scala
  def mean(xs: Seq[Double]): Double =
    if (xs.isEmpty) Double.NaN
    else xs.sum / xs.length
  ```
  오류를 돌려주는 대신, 적당한 가짜 값을 돌려준다. 여기에서는 Double.NaN을 돌려줄 수도 있고, 다른 자료형식이라면 null이 될
  수도 있을 것이다.
    - 오류가 조용히 전파될 수 있다
    - 사용자가 항상 제대로 된 결과를 받았는지 if-else로 확인해야한다
    - 일반적으로 적용이 불가능하다. 최대 값을 찾는 max의 경우 가짜 값으로 뭘 줘야할까?
    - 사용자가 함수를 쓰기 위한 특별한 규칙을 따라야한다
- 처리를 못하는 경우 돌려줄 값을 전달하기

  ```scala
  def mean(xs: Seq[Double], onEmpty: Double): Double =
    if (xs.isEmpty) onEmpty
    else xs.sum / xs.length
  ```
    - 함수를 호출하는 사람이 결과가 정의되지 않을 때 어떻게 처리되는지를 알아야한다
    - 무조건 같은 타입의 값 하나를 돌려줘야한다

## Option 자료 형식

```scala
sealed trait Option[+A]

case class Some[+A](get: A) extends Option[A]

case object None extends Option[Nothing]
```

- 함수가 답을 내지 못할 수 있다는 명시적인 형식을 선언해서 사용하면 된다.
- 값이 정의될 경우 Some[A]
- 값이 정의되지 않을 때는 None
- mean을 다시 구현해보자면,
  ```scala
  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)
  ```
  이제 mean은 완전 함수이다.
- Option은 스칼라 표준 라이브러리에서도 많이 사용된다
    - Map(Dictionary)에서 값을 가져올 경우 반환값이 Option
    - 리스트에서 처음이나 마지막 요소를 가져오는 함수도 반환값이 Option
- List와 비슷하게 아래의 함수들을 지원한다
    - map, flatMap \
      결과가 있다면, 해당 결과를 가공함. 오류 처리를 나중의 코드에게 미루는 수단
    - filter \
      성공적인 값을 확인하고 실패처리
    - orElse / getOrElse \
      결과가 없다면 다른 결과를 사용
- 그러면 모든 함수가 Option을 받고 Option을 리턴하게 짜야하나? \
  -> 일반 함수도 Option을 지원하도록 lift 할 수 있다.
  ```scala
  def lift[A, B](f: A => B): Option[A] => Option[B] = _ map f
  ```
  위의 함수를 확장하면 인수가 몇개든 Option을 지원하도록 승급시킬 수 있다

## Either 자료 형식

```scala
sealed trait Either[+E, +A]

case class Left[+E](value: E) extends Either[E, Nothing]

case class Right[+A](value: A) extends Either[Nothing, A]
```

- 위의 Option는 뭔가 부족하다 \
  실패했다는건 None으로 표현가능하지만, 왜 실패했는지는 모른다
- 성공과 실패 모두 값을 가지고, Left값과 Right값 중 하나만 가질 수 있다
- Left는 실패, Right는 성공
- mean을 다시 구현해보자면,

  ```scala
  def mean(xs: Seq[Double]): Either[String, Double] =
    if (xs.isEmpty)
      Left("mean of empty list!")
    else
      Right(xs.sum / xs.length)
  ```

## for comprehension

함수형 프로그래밍을 하면 map, flatMap 등을 엄청 많이 쓴다.
그래서 scala에서는 for-comprehension이라는 문법으로 쉽게 사용할 수 있도록 해준다

```scala
def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
  for {
    aa <- a
    bb <- b
  } yield f(aa, bb)
```

위의 코드는 아래의 코드로 변환된다

```scala
def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
  a flatMap (aa =>
    b map (bb =>
      f(aa, bb)
      )
    )
```

for문 안의 <-는 모두 flatMap으로, 마지막 <- 는 map으로 변환되고, yield 뒤의 표현식은 마지막 map에 들어가는 함수이다.
