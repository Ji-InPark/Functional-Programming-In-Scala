# 1. 함수형 프로그래밍이란 무엇인가?

## 함수형 프로그래밍

- 프로그램을 순수 함수로만 작성하는 것
- 함수형 프로그래밍은 프로그램을 작성하는 **방식**이지 프로그램의 **종류**를 제약하지 않는다
- 모듈성이 증가한다
- 테스트가 편리하다
- 재사용성, 병렬화, 일반화가 쉽다
- 분석이 쉽다
- 버그가 생길 여지가 적다
- 일반적으로 좋다고 여겨지는 것들을 극한까지 적용한 것
- 프로그램의 어딘가에는 부수효과가 존재하겠지만, 그것을 보이지 않도록 감추거나, 내 코드 밖으로 밀어내는 것

---

## 함수형 프로그래밍의 예시

###  부수 효과로 가득한 프로그램

``` scala
class Cafe {
  def buyCoffee(cc: CreditCard): Coffee = {
    val cup = new Coffee()
    cc.charge(cup.price)
    cup
  }
}
```

- Coffee객체를 돌려주는 작업 외에는 모두 부수효과
  - 카드회사에 대금을 청구하기
  - 대금 청구가 제대로 되지 않았으면 오류 던지기
- 부수효과가 있기 때문에 테스트가 어렵다
  - 커피를 실제로 한컵 만들어야 한다
  - 심지어 신용카드로 결제까지 해봐야한다
- 현재는 CreditCard에 결제 로직이 들어있는데, 사실 신용카드는 결제 정보를 알 필요가 없다

### 약간 개선하기

```scala
class Cafe {
  def buyCoffee(cc: CrreditCard, p: Payments): Coffee = {
    val cup = new Coffee()
    p.charge(cc, cup.price)
    cup
  }
}
```

- p.charge로 여전히 부수효과가 발생한다
- 하지만 Payments에 가짜 결제 수단을 넣으면, 실제 결제는 진행하지 않으면서 테스트할 수 있다
  - 테스트 코드 실행 이후 Payments가 적절히 mutate되었는지 검사한다
  - mock framework를 쓸 수 있지만, 너무 큰 일이다
- 커피를 여러잔 주문하려면 신용카드를 N번 긁어야하나? / 함수 재사용성이 부족하다

### 함수형 프로그래밍

```scala
class Cafe {
  def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
    val cup = new Coffee()
    (cup, Charge(cc, cup.price))
  }
  
  def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) = {
    val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffee(cc))
    val (coffees, charges) = purchases.unzip
    (coffees, charges.reduce((c1, c2) => c1.combine(c2)))
  }
}

case class Charge(cc: CreditCard, amount: Double) {
  def combine(other: Charge): Charge =
  	if (cc == other.cc)
  		Charge(cc, amount + other.amount)
  	else
  		throw new Exception("Can't combine charges to different cards") 
}

def coalesce(charges: List[Charge]): List[Charge] =
	charges.groupBy(_.cc).values.map(_.reduce(_ combine _)).toList
```

- 이제 buyCoffee 안에는 부수효과가 전혀 없다
- 청구서의 생성, 처리, 연동의 코드가 모두 분리되었다
- 여러잔의 커피도 한번의 결제로 처리할 수 있다
- Charges 객체 덕분에 같은 신용카드끼리 모아서 청구하는 등 복잡한 작업도 처리할 수 있다

---

## 순수 함수

- 입력 형식이 A이고 출력 형식이 B인 함수 f는 형식이 A인 모든 값 a를 형식이 B인 하나의 값 b에 연관시키되, b가 오직 a의 값에 의해서만 결정되는 계산
- 함수의 결과는 함수의 입력에 의해서만 결정
- 함수는 주어진 입력을 계산하여 결과를 내 놓는 것 말고는 어떠한 작업도 하지 않음
- 같은 값에 대해서 항상 같은 결과가 나옴

## 참조 투명성

- 프로그램 p에 대해 표현식 e의 모든 출현(occurence)을 e의 평가 결과로 치환해도 p의 의미에 아무 영향을 끼치지 않는다면 표현식 e는 참조에 투명(referential transparent)하다.
- 표현식 f(x)가 참조에 투명한 모든 x에 대해 참조에 투명하면, 함수 f는 순수(pure)하다.
- 등치 대 등치 치환을 통해 계산될 수 있으며, 등식적 추론을 가능하게 한다.
- 부수효과로 가득한 프로그램에서, ctrl + f 로 buyCoffee를 모두 new Coffee() 로 치환해버리면, 커피는 나오지만 대금은 청구되지 않는다.
- 참조 투명성이 없으면 프로그램의 행동에 대한 추론이 어려워진다.

## 참조 투명성 예시

### 참조 투명성 만족

```scala
val x = "Hello"
val y1 = x.reverse.toString // y1 = "olleH"
val y2 = x.reverse.toString // y2 = "olleH"
```

x.reverse를 그 결과로 치환해도 문제 없다.

```scala
val x = "Hello"
val y1 = "olleH".toString // y1 = "olleH"
val y2 = "olleH".toString // y2 = "olleH"
```

### 참조 투명성 만족하지 않음

```scala
val x = new StringBuilder("Hello")
val y = x.append(" world")
val y1 = y.toString // "Hello world"
val y2 = y.toString // "Hello world!!"
```

y1을 치환하면,

```scala
val x = new StringBuilder("Hello")
val y = x.append(" world")
val y1 = x.append(" world").toString // y1 = "Hello world world"
val y2 = x.append(" world").toString // y2 = "Hello world world world"
```

