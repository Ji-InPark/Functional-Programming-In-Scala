 # 속성 기반 검사
 
 속성 기간 검사 라이브러리는 프로그램 행동 방식의 서술과 그런 행동 방식을 검사하는 test case의 생성을 분리하기 위함
 프로그래머는 행동 방식의 서술과 그런 행동 방식을 명시하고 test case에 대한 고수준 제약을 제시하는데만 집중하면 프레임워크가 test case를 자동으로 생성하고, 테스트 해준다.
 
 ### 속성 기반 검사란?
 
 val intList = Gen.listOf(Gen.choose(0,100))
 val prop = 
    forAll(intList)(ns => ns.reverse.reverse == ns) &&
    forAll(intList)(ns => ns.headOption == ns.reverse.lastOption)
var failingProp = forAll(intList)(ns => ns.reverse == ns)

prop.check
failingProp.check
를 실행 시켜서 결과값을 확인
Gen.choose(0,100)은 0~100사이의 난수로 채워진 여러 가지 길이의 목록을 산출한다.

여러 유용한 기능
-> 검례 최소화 : 가장 작은 목록에서 발생하는 오류를 찾아내서 디버깅에 도움을 줌
-> 전수 검례 생성 : 검사해야되는 목록이 작다면 모두 검사 가능 => 증명된거라 볼수 있음

이번 장의 목적은 위와 같은 ScalaCheck 라이브러리를 만들어보는 것

### 자료 형식과 함수 선택

위 코드의 Gen.choose와 Gen.listOf, forAll의 매개변수와 리턴값에 대한 형식을 추론해본다.
Prop은 Gen과 술어를 묶은 결과를 나타내기 위한 새로운 형식

trait Prop { def &&(p: Prop): Prop } 우선 만들어봄(확실하니깐)

Prop형식을 위한 함수는 세가지
1. 속성을 생성하는 forAll
2. 속성 합성을 위한 &&
3. 속성 점검을 위한 check

trait Prop{
  def check: Unit
  def &&(p:Prop): Prop = ???
}

check에 부수효과 존재, &&에서는 check를 모두 실행 -> 이러면 각자의 성공 실패라 표현 어렵다. (실패한 구현)
어떻게 구현할까? 8.3 연습문제

check에 그냥 true false만 표현하기에는 실패시 예시 / 성공 숫자 등을 알려줘야 돼서 부-족
그럼 이제 Either를 도입해보자

trait Prop {
  def check: Either[(FailedCase, SuccessCount), SuccessCount]
}

이제 ForAll을 살펴보기 위해 Gen을 먼저 파악해보자
Gen에 대해서 아는 것 : 난수 생성
Gen.choose 구현 : 연습문제 8.4, 8.5

우리는 어떤 연산이 기본수단이고 그로부터 파생된 연산들이 어떤것인지 이해하고, 기본수단들의 집합을 구하는데 관심이 있다.
즉, 함수를 가지고 놀면서 조합기들을 만들고 집합을 정련해 나간다 -> 라이브러리화?
*놀이의 중요성

이제는 생성된 값에 의존하는 생성기를 만들어보자
이런 생성방식을 지원하려면 한 생성기가 다른 생성기에 의존하게 만드는 flatMap이 필요하다 (연습문제 8.6 ~ 8.8)

Gen의 표현에 관해 많은 것을 알아냈으니 이제 다시 Prop로 넘어간다.

trait Prop {
  def check: Either[(FailedCase, SuccessCount), SuccessCount]
}

이 Prop은 그냥 비엄격 Either일뿐 통과했다는 결론을 내리는데 충분하지 않다. 몇개가 통과해야되는지에 대한 정보가 아직 없다.

type TestCases = Int
type Result = Either[(FailedCase, SuccessCount), SucessCount]
case class Prop(run: TestCases => Result)

현재 Either의 Right의 경우에 어떠한 정보도 필요하지 않다. 따라서 Option으로 변경한다
다만 Option의 None의 경우 실패의 부재를 의미하므로 의미가 모호하다 따라서 새로운 자료형식을 만든다.

sealed trait Result {
  def isFalasified: Boolean
}
case object Passed extends Result {
  def isFalsified = false
}
case class Falsified(failure: FailedCase, successes: SuccessCount) extends Result {
  def isFalsified = ture
}

다만 이것으로도 forAll을 구현할수 없다.
Prop.run에는 시도할 test case 갯수 이외에도 생성하는데 필요한 모든정보가 필요 하기 때문
따라서 RNG를 Prop에 넣어주어야 함

case class Prop(run: (TestCases,RNG) => Result)
이를 이용해서 목록 8.3(p170)에 나온 forAll / randomStream / buildMsg 등을 구현할 수 있다

def forAll[A] (as: Gen[A])(f:A => Boolean): Prop = Prop {
  (n,rng)

(try catch에 if else만 있어도 뭔가 함수형스럽지 않아서 반가운 이 기분)

### 검례 최소화
크게 두가지 방법으로 나뉨
1. 수축 - 오류가 나오면 해당 오류를 점점 줄여가면서 검사를 반복
2. 크기별 생성 - 애초에 크기와 복잡도를 점차 늘려가면서 test case 생성

우리는 크기별 생성을 시도해볼 것이다.
주어진 최대 크기까지의 test case를 생성하는 방식 (목록 8.4 / p.173)

### 라이브러리의 사용과 사용성 개선

실제로 만든 라이브러리를 사용해서 테스트를 해보고 사용성을 개선하자
Max를 테스트 해보면 Prop에 run을 직접 실행하는 것이 번거롭다.
따라서 Prop 값들을 실행하고 그 결과를 유용한 형식으로 콘솔에 출력해주는 보조함수를 도입해보자
목록8.5/p.174 에서 run에 기본 인수 기능을 활용해서 조금더 편하게 라이브러리를 사용할 수 있게 한다.

다음으로 7장에서 보았던 병렬 계산에 대한 테스트를 진행해보자
map(unit(1))(_ + 1) == unit(2)
이를 표현하는건 가능하지만 깔끔하지는 못하다

val ES: ExecutorService = Executors.newCachedThreadPool
val p1 = Prop.forAll(Gen.unit(Par.unit(1)))(i => 
  Par.map(i)(_ + 1)(ES).get == Par.unit(2)(ES).get)
  
테스트 케이스를 만들었지만 다소 장황하고 지저분하고 의도가 정확히 드러나지 않는다.

def check(p: => Boolean): Prop = {
  lazy val result = p
  forAll(unit(())) (_ => result)
}

그러나 해당 테스트케이스는 100회 반복할 필요가 없으므로 forAll을 쓰지 않아도 좋고 출력도 바꿔줄 필요가 있다.
따라서 Proved라는 속성을 도입하고 check가 생성한 속성이 Passed대신 Proved를 돌려주게 하면 된다. (목록 8.6/p.177)

다시 Par.map(Par.unit(1))(_ + 1) == Par.unit(2) 문제로 돌아가서 새 check를 이용하면

val p2 = Prop.check {
  val p = Par.map(Par.unit(1))(_ + 1)
  val p2 = Par.unit(2)
  p.(ES).get == p2(ES).get
}
이제 상당히 명확하다. 하지만 p.(ES).get == p2(ES).get 요놈이 눈에 거슬린다. Par의 내부 구현 세부사항이 들어났기 때문
그럼 map2를 이용해 상등비교를 Par로 승급 시켜서 더 깔끔하게 만들 수 있다.

def equal[A](p: Par[A], p2: Par[A]): Par[Boolean] =
  Par.map2(p,p2)(_ == _)
  
val p3 = check {
  equal(
    Par.map(Par.unit(1))(_ + 1),
    Par.unit(2)
  )(ES).get
}

더 나아가서 Par의 실행을 개별함수 forAllPar로 옮기고 => 새조합기를 도입하고 => '**' 을 커스텀 추출기를 이용하는 패턴으로 바꾸면 코드가 훨씬 깔끔해진다.

### 고차 함수의 검사와 향후 개선 방향

현재의 라이브러리는 고차함수를 검사하기 위한 적합한 수단을 제공하지 않음.
예를들어 List와 Stream에 정의된 takeWhile함수를 점검하려면? 연습문제 8.18
한가지 방법으로 특정 인수만 조사할수도 있지만 이 접근방식으로는 충분하지 않다.
이는 그냥 자신의 입력을 무시하는 상수 함수를 생성할 뿐이다.

### 생성기의 법칙들
Gen 형식에 대해 구현한 함수중에는 Par나 List, Stream, Option에 대해 정의한 다른 함수들과 상당히 비슷한 부분이 많다.
이들은 단지 서명만 비슷한 것이 아니라 실제로 각자의 영역에 관해 서로 비슷한 의미를 가지고 있다.
3부에서는 이런 패턴들을 배우게 될것!

