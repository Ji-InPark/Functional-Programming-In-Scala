# 5장 - 엄격성과 나태성

```scala
List(1,2,3,4).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
```

- 모든 함수는 immutable
- 각 메소드 호출은 새로운 List를 생성하며, 이는 다음 메소드 호출의 입력으로만 쓰인 후 즉시 폐기된다.

### 이 챕터의 목적

- 이상의 과정에서 발생하는 임시적인 자료구조 instance 생성을 피한다.
- 이를 위해 이상의 과정을 하나의 패스로 융합(fusion)하는 방법을 알아본다.
- 단, 루프가 아닌 고차 함수(map, filter)를 이용할 수 있도록 한다.
- 서술과 평가의 관심사 분리를 통해, 서술을 여러 문맥에서 재사용할 수 있음을 알아본다.
- 나아가서는, *비엄격성*이 함수적 프로그램의 효율성과 모듈성을 개선하는 근본적인 기법임을 확인한다.



## 5.1 엄격한 함수와 엄격하지 않은 함수

### 비엄격성

함수가 엄격하지 않다.

- 그 함수가 하나 이상의 인수들을 평가하지 않을 수도 있다.
- 조금 더 풀면, 함수 호출의 argument에 주어진 expression이 evalutate되지 않을 수 있다는 의미
- (이 논의는 컴파일러가 expression을 단축하는 최적화를 다루는 것이 아니다.)

예: Logic operators &&와 ||는 **엄격하지 않다.**

- Evaluation short-circuiting에 의해 우변이 평가되지 않을 수 있다.

```scala
false && { println("!!"); true} // Nothing is printed

true || { println("!!"); false} // Nothing is printed
```

예: Scala의 if 제어 구조는 **엄격하지 않다.**

```scala
val result = if (true) 1 else { println("!!"); -1 }
```

대부분의 프로그래밍 언어는 엄격한 함수만 지원한다.

- Scala에서도 특별하게 지정하지 않는 한, 엄격한 함수가 기본이다.
- 지금까지 본 모든 함수 정의는 모두 엄격하다.

### Thunk(성크)

표현식이 평가되지 않은 형태

- 나중에 그 표현식을 평가해서 결과를 내도록 **강제**할 수 있다.

### Thunk - 명시적으로 비엄격성을 지정하기

```scala
def if2[A](cond: Boolean, onTrue: () => A, onFalse: () => A): A =
    if (cond) onTrue() else onFalse()

if2(
    a < 22,
    () => println("TRUE"), // thunk
    () => println("FALSE"), // thunk
)
```

- 이때 if2의 호출 부분에서 성크를 명시적으로 생성해야 한다.

### Thunk - **Scala의 구문을 활용하기**

```scala
def if2[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
    if (cond) onTrue else onFalse

if2(a < 22, 3, sys.error("FAIL")) // Not evaluated
```

- 호출 부분을 보면, Scala가 성크 안의 expression을 알아서 감싸준다.
- 함수 내부에서도 참조할 때 평소처럼 참조하면 된다.
- 인수의 평가 결과는 기본적으로 캐싱되지 않는다. 즉, 참조할 때마다 매번 호출된다.

### Thunk - 평과 결과를 캐싱하기

```scala
lazy val onT = onTrue
```

- 해당 변수가 처음 참조될 때까지, 우변의 평가 결과를 지연한다.
- 평가 결과를 캐싱한다.

참고: 비엄격 함수의 인수는 값(by value)으로 전달되는 것이 아니라 이름(by name)으로 전달된다.



## 5.2 확장 예제: 게으른 목록

게으른 목록(lazy list) 또는 스트림(stream)

### Memoization

- [01.scala](./01.scala)
- 예시에서, smart constructor로 한 번 감싸서 캐싱을 강제하는 기법
- 이후에 처음 참조될 때 평가된 값으로 캐싱된다.



## 5.3 프로그램 서술과 평가의 분리

### **관심사의 분리(separation of concerns)**

함수형 프로그래밍의 주된 주제 중 하나

예: 계산의 서술(description)과 실제 실행의 분리

- 일급 함수는 계산의 서술을 parameter로 받는다.
- Option은 오류가 발생했다는 사실만을 담고, 오류에 대해 무엇을 수행해야 하는가는 분리된 관심사.
- **Stream은 계산을 구축하되 계산의 실제 실행은 요소가 필요할 때까지 미룰 수 있다.**

일반화하면, 나태성을 통해서 표현식의 서술을 평가로부터 분리할 수 있다.

- **필요한 것보다 '더 큰' 표현식을 서술하되, 실제 평가는 그 표현식의 일부에 대해서만 할 수 있는 능력이 생긴다!**
- `foldRight` 예시에서, 더 이상 재귀를 할 필요가 없다면 재귀가 조기 종료된다. (엄격한 버전에서는 불가능하다.)

### 점진적(incremental) 구현

Stream은 전체 결과를 생성하지 않는다.

- 결과 Stream의 element가 실제로 참조될 때 그 element를 생성한다.
- **따라서 중간 결과를 완전히 인스턴스화하지 않아도 method chaining이 가능해진다.**

메모리 관련 내용은 4장 참조



## 무한 스트림과 공재귀

### 무한 Stream

Stream은 전체 결과를 생성하지 않는다.

- `ones`는 무한하지만, 실제로 요구된 만큼만 산출해낸다.
- 단, 무한 재귀 또는 stack OF를 주의해야 한다.

### Corecursive a.k.a Guarded Recursion, Cotermination

공재귀: 자료를 생산하는 재귀

- Primarily of interest in functional programming
- Dual operation to recursion
- A kind of contrast with recursion
- Ref: https://en.wikipedia.org/wiki/Corecursion

| 재귀 | 공재귀 |
|---|---|
| 자료를 소비한다. | 자료를 생산한다. |
| 점점 작은 입력으로 재귀하다 종료된다. | 생산성을 유지하는 한 종료될 필요가 없다. |

`unfold`

- fold(목록을 하나의 값으로 "접는" 연산)의 dual
- 목록을 Stream으로서 생산한다.
- f가 종료되기 전까지 생산성을 유지한다.


