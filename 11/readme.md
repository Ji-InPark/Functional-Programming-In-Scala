# 11. 모나드

- 우리가 하고자 하는거 => 중복된 코드를 추출

## 1. Functor(함수자) : map 함수의 일반화

```scala
def map[A,B](ga: Gen[A])(f: A=>B): Gen[B]
def map[A,B](pa: Parser[A])(f: A=>B): Parser[B]
def map[A,B](oa: Option[A])(f: A=>B): Option[B] 
```
- 위 서명의 차이는 구체적인 자료형식(Gen, Parser, Option)뿐
- 'map을 구현하는 자료형식'이라는 개념을 추출하면 다음과같은 trait으로 표현할 수있다.

```scala
trait Fuctor[F[_]] {
  def map[A,B](fa: F[A])(f: A=> B): F[B]
}
```
- 이 함수들을 가지고 노는것만으로도 유용한 함수 발견가능
- ex> distribute
```scala
trait Fuctor[F[_]] {
  def map[A,B](fa: F[A])(f: A=> B): F[B]
  def distribute[A,B](fab:F[(A,B)]): (F[A],F[B]) = (map(fab)(_._1),map(fab)(_._2))
}
```
### 함수자의 법칙들

- functor와 같은 추상을 만들 때는 어떤 추상 메서드들이 필요한지 고민할 뿐 아니라 구현이 지켜야할 법칙들도 고민해야한다.
- 법칙이 중요한 이유 

  - 법칙은 인터페이스의 의미론, 해당 대수를 인스턴스들과는 독립적으로 추론할 수 있을정도의 새로운 수준으로 끌어올리는데 도움이 된다.(Monoid[A],Monoid[B]로 Monoid[(A,B)] 역시 결함법칙을 만족 => 해당 추론은 구체적인 타입과는 상관 X)
  - Functor같은 추상 인터페이스의 함수들로부터 조합기들을 파생할 때 법칙에 의존
- 법칙
  - map(x)( a => a ) == x  
    - 자료구조 x에대해 항등 함수를 mapping하는것 자체가 항등 함수 
    - 해당 법칙은 map(x)가 x의 **"구조를 보존해야한다"** 는 요구사항 반영
    - 법칙을 만족하기 위해선 이를테면 예외를 던지거나, list의 첫 요소를 제거하거나 Some을 none으로 바꾸는 등의 행동을 해서는 안된다.
    - 자료구조의 요소들만 수정해야 하며 구조 자체의 형태는 그대로 두어야한다.
  
```scala
def distribute[A,B](fab: F[(A,B)]) : (F[A],F[B])
def codistribute[A,B](e: Either[F[A],F[B]]): F[Either[A,B]]
```
- F가 함수자라는 사실 외에는 아는 게 없지만, 해당 법칙에 따라 반환된 값들이 그 인수들과 같은 형태임을 보장해준다.
- distribute에 쌍들의 목록(List)가 입력되었다면, distribute가 돌려주는 목록들의 쌍은 그 입력과 같은 길이일 것이며, 같은 순서로 나타난다.


## 2. Monad : flatMap, unit의 일반화

- Monad 특질.
- 최소한의 기본 집합.

```scala
trait Monad[F[_]] {
  def unit[A](a: => A): F[A]
  def flatMap[A,B](ma:F[A])(f:A => F[B]): F[B]
}
```

## 3. 모나드적 조합기

- 모든 모나드에 대해 기본 수단으로 구현할 수 있는 함수들.

```scala
trait Monad[F[_]] {
  def sequence[A](lma: List[F[A]]): F[List[A]] // 모나드를 바깥으로 보내는 함수
  def traverse[A,B](la:List[A])(f:A => F[B]): F[List[B]] //list -> monad of list 로 치환 
  def replicateM[A](n:Int, ma:F[A]): F[List[A]] // Gen, Parser 의 listOfN함수와 같은것
  def product[A,B](ma:F[A], mab:F[B]): F[(A,B)] = map2(ma,mb)((_,_)) // 곱 연산
  def filterM[A](ms:List[A])(f:A => F[Boolean]): F[List[A]]
}
```

- 이건 극히 일부래요. 더 궁금하면 13장에서 확인...

## 4. 모나드 법칙
- 함수자의 법칙들이 Monad에 대해서도 성립.

### 결합법칙
세 개의 모나드적 값을 하나로 조합한다고 할때 어떤 순서로 해야할까?

```scala
case class Order(item: Item,quantity: Int)
case class Item(name: String, price: Double)

val genOrder: Gen[Order] = for {
  name <- Gen.stringN(3) //길이 3 문자열
  price <- Gen.uniform.map(_ * 10) // 0,10균등분포 Double 난수
  quantity <- Gen.choose(1,100) // 1,100 난수 
} yield Order(Item(name,price), quantity)
```
- Item 생성에 대한 의존성을 낮추기 위해 Item생성기 분리

```scala
val genItem:Gen[Item] = for {
  name <- Gen.stringN(3)
  price <- Gen.uniform.map(_ * 10)
} yield Item(name,price)

val genOrder: Gen[Order] = for {
  item <- genItem
  quantity <- Gen.choose(1,100)
} yield Order(item, quantity)
```
- genOrder의 두 버전은 정확히 동일한 일을 해야 함.
- 그걸 확인하기위해서 map, flatmap호출로 전개
```scala
 Gen.nextString.flatMap(name =>
  Gen.nextDouble.flatMap(price =>
  Gen.nextInt.map(quantity => 
    Order(Item(name, price),quantity))))

Gen.nextString.flatMap(name =>
  Gen.nextDouble.map(price => 
    Item(name, price))).flatMap(item =>
        Gen.nextInt.map(quantity => 
        Order(item,quantity))
  )
```

- 정확한 구현은 다르지만, 정확히 같은일은 한다고 가정하는게 합당해 보인다. => 결합법칙때문
- 결합법칙의 flatMap 표현
```scala
x. flatMap(f).flatMap(g) == x.flatMap(a => f(a).flatMap(g))
```

### 결합법칙의 증명
- option에서의 증명
```scala
//x가 None 일때
None. flatMap(f).flatMap(g) == None.flatMap(a => f(a).flatMap(g))
//None.flatMap은 None이므로
None == None

//x가 Some(v)일때

x. flatMap(f).flatMap(g)        == x.flatMap(a => f(a).flatMap(g))
Some(v). flatMap(f).flatMap(g)  == Some(v).flatMap(a => f(a).flatMap(g))
f(v).flatMap(g)                 == (a => f(a).flatMap(g))(v)
f(v).flatMap(g)                 == f(v).flatMap(g)
```

- 클라이슬리 함성
  - F[A]같은 형식의 모나드적 값이 아니라 A => F[B]같은 형식의 모나드적 함수를 고려하면 이 법칙을 좀 더 명확하게 만들 수 있다.
  - 그런 함수를 클라이슬리 화살표라고 부른다.
  - 클라이슬리 화살표들은 합성이 가능하다.

```scala
def compose[A,B,C](f:A => F[B],g:B=>F[C]) : A => F[C]

//클라이슬리 합성을 통해 좀더 간결한 표현 가능
compose(compose(f,g),h) == compose(f,compose(g,h))
```
- compose, unit은 모나드의 또 다른 최소 집합, (연습문제 11.8

### 항등법칙
모노이드에서의 항등원(zero)처럼 모나드에서도 항등원이 존재. 그 항등원이 바로 unit함수

```scala
compose(f, unit) == f //왼쪽 항등법칙
compose(unit, f) == f //오른쪽 항등법칙
```

- 또 다른 (세번째) 모나드 최소 집합 map, unit, join 존재 (연습문제11.12) 

## 모나드란 무엇인가?
- 모나드는 추상적이고 순수 대수적인 인터페이스..
- 모나드 인스턴스들은 해당 집합중 하나는 구현해야함
  - unit, flatMap
  - unit, compose
  - unit, map, join
- 모나드는 결합법칙, 항등법칙을 만족해야함.

*모나드는 모나드적 조합기들의 최소 집합 중 하나를 결합법칙과 항등법칙을 만족하도록 구현한 것*

### 항등 모나드
```scala
case class Id[A](value: A)

for {
  a <- Id("Hello, ")
  b <- Id("monad!")
} yield a + b
```
- 항등 모나드에서의 flatMap은 단순한 변수 치환
- 모나드는 변수의 도입과 결속.. 그리고 변수 치환 수행을 위한 문맥을 제공!..이 무슨뜻이죠.

### State모나드
```scala
//type lambda를 사용한 표현
def stateMonad[S] = new Monad[({type f[x] = State[S,x]})#f] {
  def unit[A](a: => A): State[S,A] = State(s => (a,s))
  def flatMap[A,B](st:State[S,A])(f:A => State[S,B]): State[S,B] = st flatMap f
}
```
*flatMap호출의 연쇄에서, 모나드는 각 명령문의 경계(명령문 -> 명령문 사이)에서 어떤일이 일어나는지를 명시한다.*
- Id는 단순한 래핑만 해줌.
- State는 다음 상태를 넘겨줌.
- Option은 None을 통해 작업을 종료시켜줌.
- list는 해당 명령문이 여러결과를 돌려줄 수 있으며 그다음 명령문을 element수 만큼 실행.

*Monad 계약 자체는 **무슨일**이 일어나는지 명시해주지 않는다. 단지 결합벅칙과 항등법칙이 만족함을 명시해줄 뿐이다.*

