# 스칼라로 함수형 프로그래밍 시작하기

스칼라 언어 자체의 설명은 제외했습니다. 알아서 공부하세요 ㅎㅎ;;

## 고차 함수

- 함수도 값으로 취급할 수 있다
- 함수를 값으로서 주고 받는 함수는 고차함수이다

```scala
def formatResult(name: String, n: Int, f: Int => Int) = // 함수 f를 값으로 받음
	"The %s of %d is %d.".format(name, n, f(n)) // 받은 함수를 실행

formatResult("abs", -10, abs) // 함수 자체를 값처럼 넘겨줌
formatResult("factorial", 5, factorial)
```

- 고차 함수에서 함수 인수는 f, g, h과 같이 짧은 이름을 사용함
- 함수를 아주 일반적으로 만들어버리면, 각 함수가 수행하는 일을 알지 못하기 때문에 짧은 이름을 써도 문제 없음

## 함수적으로 루프 작성하기

- for, while 등등은 함수적이지 못하다
- 필연적으로 mutable한 상태를 가지게 된다
- 함수적으로 루프를 작성하려면 재귀호출을 사용하면 된다

```scala
def factorial(n: Int) = {
  @tailrec // 꼬리재귀 최적화를 강제한다
  def go(n: Int, acc: Int): Int = // 스칼라에서는 함수의 본문에 함수를 정의할 수 있음
  	if (n <= 0) acc
  	else go(n-1, n*acc)
  go(n, 1)
}
```

- 재귀호출이 좋긴 하지만 성능은 눈물이 난다
- 그렇기 때문에 꼬리 재귀 최적화를 통해서 성능을 개선하고 stack overflow를 피한다
- 함수의 가장 마지막 절이 자기 재귀로 끝나면, 컴파일러가 재귀 호출을 loop로 변환한다

## 다형적 함수: 형식에 대한 추상

- 우리가 일반적으로 짜는 함수는 대부분 단형적 함수(monomorphic function)
  특정 타입에 대해서만 동작함

  ```scala
  def findFirst(arr: Array[String], key: String): Int = {
    @tailrec
    def loop(n: Int): Int =
    	if (n >= arr.length) -1
    	else if (arr(n) == key) n
    	else loop(n + 1)
    
    loop(0)
  }
  ```

  

- 하지만 다형적 함수(polymorphic function)는 다양한 타입에 대해서 동작함
  형식에 대한 추상화(abstracting over type)을 통해 일반적 함수(generic function)을 만들 수 있음

  ```scala
  def findFirst[A](arr: Array[A], p: A => Boolean): Int = {
    @tailrec
    def loop(n: Int): Int = 
    	if (n >= arr.length) -1
    	else if (p(arr(n))) n
    	else loop(n + 1)
    
    loop(0)
  }
  ```

- [A]로 형식 매개변수 (type parameter)를 받는다
- A는 형식 변수 (type variable)로 함수 안에서 사용할 수 있다
- 검색 조건도 같은 요소를 찾는 연산에서 p를 만족하는 요소를 찾도록 일반화 되었다
- 다형적 함수로 함수를 일반화하면 가능한 구현이 줄어든다

## 형식에서 도출된 구현

- 아주 일반화된 함수는 함수 시그니쳐만 보고 구현할 수 있을 정도로 구현의 범위가 제한됨

- 부분 적용은 함수의 일부 파라미터만 미리 넣어두는 것
  ```scala
  def partial[A,B,C](a: A, f: (A,B) => C): B => C
  ```

  위의 함수를 구현하려고 하면, 가장 먼저 함수 시그니쳐를 보고 B => C를 반환 해야한다고 생각을 함
  ```scala
  def partial[A,B,C](a: A, f: (A,B) => C): B => C =
  	(b: B) => ???
  ```

  이제 C를 반환해야하니까, f를 무지성 호출하자
  ```scala
  def partial[A,B,C](a: A, f: (A,B) => C): B => C =
  	(b: B) => f(a, b)
  ```

  함수 시그니쳐만 보고 구현 완성

- 커링은 함수의 인자를 하나씩 전달할 수 있도록 쪼개는 것
- 함수 합성은 한 함수의 출력을 다른 함수의 입력으로 연결하는 것

