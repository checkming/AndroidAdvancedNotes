# 方法、异常与类

[TOC]

## 方法

```dart
int add(int i,int j) {
  return i + j;
}
//也可以选择忽略类型(不推荐)
add( i, j) {
  return i + j;
}
//对于只有一个表达式的方法，可以选择使用缩写语法来定义：
add(i, j) => i + j;
//在箭头 (=>) 和分号 (;) 之间只能使用一个 表达式
```

### 一等方法对象

​	Dart 是一个真正的面向对象语言，方法也是对象并且具有一种 类型  `Function`。 这意味着，方法可以赋值给变量，也可以当做其他方法的参数。可以把方法当做参数调用另外一个方法

```dart
var list = [1,2,3];
//将 print 方法 作为参数传递给forEach
list.forEach(print);
//可以将方法赋值给一个变量 类型为Funcation
var p = print;
list.forEach(p);
```

​	在Java中如果需要能够通知调用者或者其他地方方法执行过程的各种情况，可能需要指定一个接口，比如View的onClickListener。而在Dart中，我们可以直接指定一个回调方法给调用的方法，由调用的方法在合适的时机执行这个回调。

```dart
void setListener(Function listener){
    listener("Success");
}
//或者
void setListener(void listener(String result)){
    listener("Success");
}

//两种方式，第一种调用者根本不确定 回调函数的返回值、参数是些什么
//第二中则需要写这么一大段 太麻烦了。

//第三种：类型定义 将返回值为voide，参数为一个String的方法定义为一个类型。
typedef  void Listener(String result)；
void setListener(Listener listener){
  listener("Success");
}

```



> 方法可以有两种类型的参数：必需的和可选的。 必需的参数需要在参数列表前面， 后面再定义可选参数。

### 可选命名参数

​	把方法的参数放到 `{}` 中就变成可选 命名参数

```dart
int add({int i, int j}) {
  if(i == null || j == null){
     return 0;
  }
  return i + j;
}
```

​	调用方法的时候，可以使用这种形式 `paramName: value` 来指定命名参数。例如：

```dart
//无必须参数
add()
//选择传递参数
add(i:1)
//位置无关
add(i:1, j:2)
add(j:1, i:2)
```

### 可选位置参数

​	把方法的参数放到 `[]` 中就变成可选 位置参数，传值时按照参数位置顺序传递

```dart
int add([int i, int j]) {
  if(i == null || j == null){
     return 0;
  }
  return i + j;
}
// 1赋值给i
add(1);
// 按照顺序赋值
add(1,2);
```

### 默认参数值

​	在定义方法的时候，可选参数可以使用 `=` 来定义可选参数的默认值。

```
int add([int i = 1, int j = 2]) => i + j;
int add({int i = 1, int j = 2}) => i + j;
```

### 匿名方法

​	没有名字的方法，称之为匿名方法，也可以称之为 lambda 或者 closure 闭包。匿名方法的声明为：

```dart
([Type] param1, …) { 
  codeBlock; 
}; 
```

​	如：

```dart
var list = ['apples', 'oranges', 'grapes', 'bananas', 'plums'];
list.forEach((i) {
  print(list[i]);
});
```



## 异常

​	和 Java 不同的是，所有的 Dart 异常是非检查异常。 方法不一定声明了他们所抛出的异常， 并且不要求你捕获任何异常。

​	     Dart 提供了 `Exception`和`Error` 类型， 以及一些子类型。你还 可以定义自己的异常类型。但是， Dart 代码可以 抛出任何非 null 对象为异常，不仅仅是实现了 `Exception` 或者` Error` 的对象。

```dart
throw new Exception('这是一个异常');
throw '这是一个异常';
throw 123;
```

​	     与Java不同之处在于捕获异常部分，Dart中捕获异常同样是使用`catch`语句，但是Dart中的`catch`无法指定异常类型。需要结合`on`来使用，基本语法如下：

```dart
try {
	throw 123;
} on int catch(e){
	//使用 on 指定捕获int类型的异常对象       
} catch(e,s){
    //函数 catch() 可以带有一个或者两个参数， 第一个参数为抛出的异常对象， 第二个为堆栈信息 ( StackTrace 对象)
    rethrow; //使用 `rethrow` 关键字可以 把捕获的异常给 重新抛出
} finally{
	
}
```





## 类

​	Dart 是一个面向对象编程语言。 每个对象都是一个类的实例，所有的类都继承于 `Object`。

```dart
//每个实例变量都会自动生成一个 getter 方法（隐含的）。 非final 实例变量还会自动生成一个 setter 方法。
class Point {
  num x;
  num y;
}
```

### 构造函数

​	由于把构造函数参数赋值给实例变量的场景太常见了， Dart 提供了一个语法糖来简化这个操作：

```dart
class Point {
  num x;
  num y;

  Point(this.x, this.y);
}
```

### 命名构造函数

​	Dart 并不支持构造函数的重载，而采用了命名构造函数为一个类实现多个构造函数：

```dart
class Point {
  num x;
  num y;
  Point(this.x, this.y);
  Point(this.y);///错误，不允许重载
  //命名构造函数
  Point.y(this.y) {
    x = 0;
  }
}

//使用
var p = Point.y(0);
```



### 初始化列表

​	在构造函数函数体执行之前会首先执行初始化列表，非常适合用来设置 final 变量的值。 

```dart
class Point {
  num x;
  num y;
  Point(this.x, this.y);
  //命名构造函数
  Point.y(this.y) {
    x = 0;
  }

  Point.fromMap(Map map)
      : x = map['x'], // : 和c++一样，初始化列表
        y = map['y'];
  
  Point.x(int i)
      : x = i, 
        y = 0;
}
```



### 重定向构造函数

​	有时候一个构造函数会调动类中的其他构造函数(在Java中就是 `this(...)`)。 一个重定向构造函数是没有代码的，在构造函数声明后，使用 冒号调用其他构造函数。

```dart
class Point {
  num x;
  num y;

  Point(this.x, this.y);
  Point.xy(int x,int y):this(x,y); ///调用上面的构造函数
}
```



### 常量构造函数

​	如果你的类提供一个状态不变的对象，你可以把这些对象 定义为编译时常量。要实现这个功能，需要定义一个 `const` 构造函数， 并且声明所有类的变量为 `final`。

```dart
class ImmutablePoint {
  final num x;
  final num y;
  //常量构造函数
  const ImmutablePoint(this.x, this.y);
}

void main(){
    //编译器常量
    var p1 = const ImmutablePoint(0,0);
    var p2 = const ImmutablePoint(0,0);
    print(p1 == p2); // true
}
```



### 工厂构造函数

​	  当实现一个使用` factory` 关键词修饰的构造函数时，这个构造函数不必创建类的新实例。例如，一个工厂构造函数 可能从缓存中获取一个实例并返回，或者 返回一个子类型的实例。（工厂构造函数无法访问 `this`）

```dart
class Logger {
  final String name;
  //从缓存获取对象
  static final Map _cache = {};
  //工厂构造函数，无法使用this变量
  factory Logger(String name) {
    if (_cache.containsKey(name)) {
      //工厂构造函数需要返回 Logger 实例对象
      return _cache[name];
    } else {
      final logger = Logger._internal(name);
      _cache[name] = logger;
      return logger;
    }
  }
  //以 _ 开头的函数、变量无法在库外使用
  Logger._internal(this.name);
}
```

​	借助工厂构造函数能够实现单例:

```dart
//使用工厂构造实现单例
class Manager {
  static Manager _instance;
  //和static是一样的, 区别是factory毕竟是构造函数，需要返回一个实例，而static是静态方法。
  factory Manager.getInstance() {
    if (_instance == null) {
      _instance =  new Manager._internal();
    }
    return _instance;
  }

//  static Manager getInstance() {
//    if (_instance == null) {
//      _instance = new Manager._internal();
//    }
//    return _instance;
//  }
  Manager._internal();
}

```



### Getters 和 Setters

​	Dart中每个实例变量都隐含的具有一个 getter， 如果变量不是 final 的则还有一个 setter。可以通过实现 getter 和 setter 来创建新的属性， 使用 `get` 和 `set` 关键字定义 getter 和 setter：

```dart
class Rect {
  num left;
  num top;
  num width;
  num height;

  Rect(this.left, this.top, this.width, this.height);

  //使用 get定义了一个 right 属性
  num get right             => left + width;
  set right(num value)  => left = value - width;
}

void main() {
  var rect = Rect(0, 0, 10, 10);
  print(rect.right); //10
  rect.right = 15;
  print(rect.left);  //5
}
```

> 需要注意的是，在get与set中使用自身会导致Stack Overflow

### 可覆写的操作符

​	把已经定义的、有一定功能的操作符进行重新定义。可以重新定义的操作符有：

| `<`  | `+`  | `|`  | `[]`  |
| ---- | ---- | ---- | ----- |
| `>`  | `/`  | `^`  | `[]=` |
| `<=` | `~/` | `&`  | `~`   |
| `>=` | `*`  | `<<` | `==`  |
| `–`  | `%`  | `>>` |       |

​	比如：List就重写了 `[]`。

```dart
class Point {
  int x;
  int y;
  //返回值 参数随你定义
  Point operator +(Point point) {
    return Point(x + point.x, y + point.y);
  }

  Point(this.x, this.y);
}

var p1 = Point(1, 1);
var p2 = p1 + Point(2, 2);
print(p2.x); ///3
print(p2.y); ///3
```



### 抽象类

​	使用 `abstract` 修饰符定义一个抽象类。抽象类中允许出现无方法体的方法

```dart
abstract class Parent {
  String name;
  void printName(); //抽象方法，不需要在方法前声明 abstract
}
```

​	抽象类不能被实例化，除非定义工厂方法并返回子类。

```dart
abstract class Parent {
  String name;
  //默认构造方法
  Parent(this.name);
  //工厂方法返回Child实例
  factory Parent.test(String name){
    return new Child(name);
  }
  void printName();
}
// extends 继承抽象类
class Child extends Parent{
  Child(String name) : super(name);

  @override
  void printName() {
    print(name);
  }
}

void main() {
  var p = Parent.test("Lance");
  print(p.runtimeType); //输出实际类型 Child
  p.printName();		
}

```

### 接口

​	与Java不同，Dart中没有`interface`关键字，**Dart中每个类都隐式的定义了一个包含所有实例成员的接口**， 并且这个类实现了这个接口。如果你想 创建类 A 来支持 类 B 的 方法，而不想继承 B 的实现， 则类 A 应该实现 B 的接口。

```dart
class Listener{
  void onComplete(){}
  void onFailure(){}
}

class MyListsner implements Listener{
  MyListsner(){

  }
  @override
  void onComplete() {
  }

  @override
  void onFailure() {
  }
}
```

与继承的区别在于：

1、单继承，多实现。

2、继承可以有选择的重写父类方法并且可以使用`super`，实现强制重新定义接口所有成员。



### 可调用的类

​	如果 Dart 类实现了 `call()` 函数则 可以当做方法来调用。

```dart
class Closure {
  call(String a, String b) => '$a $b!';
}

main() {
  var c = new Closure();
  var out = c("Hello","Dart");
  print(out);
}
```



### 混合mixins

​	Mixins 是一种在多类继承中重用 一个类代码的方法。它的基本形式如下：

```dart
//被mixin(混入)的类不能有构造函数
class A  {
  void a(){}
}
class B{
  void b(){}
}
class C with A,B{
  void c(){}
}
```

`with`后面跟着需要混入的类，被`mixin`(混入)的类不能有构造函数。现在的 `C`拥有了三个方法（a、b与c）。假设A与B 存在相同的方法，以最右侧的混入类为主，比如：

```dart
class A {
  String getMessage() => 'A';
}

class B {
  String getMessage() => 'B';
}
//
class AB with A, B {}

class BA  with B, A {}

void printMessage(obj) => print(obj.getMessage());

void main() {
  printMessage(AB()); //输出 B
  printMessage(BA()); //输出 A
}
```

继承与mixins是兼容的

```dart
class A {
  String getMessage() => 'A';
}

class B {
  String getMessage() => 'B';
}
class P{
  String getMessage() => 'P';
}
class AB extends P with A, B {}

class BA extends P with B, A {}
//可以简写成：
//class AB = P with A, B;
//class BA = P with B, A;
void printMessage(obj) => print(obj.getMessage());

void main() {
  printMessage(AB()); //输出 B
  printMessage(BA()); //输出 A
}
```

​	mixins弥补了接口和继承的不足，继承只能单继承，而接口无法复用实现，mixins却可以多混入并且能利用到混入类的具体实现：

```dart
abstract class Swimming{
    void swimming(){
        print("游泳");
    }
}

abstract class Jump{
    void jump(){
        print("跳跃");
    }
}

//只能单继承，如果需要Jump，只能以implements的形式
class Lance extends Swimming implements Jump{
 	//实现接口
    void jump(){
        print("跳跃");
    }
}

//但是实际上，我们经常不需要重新实现Jump方法，复用Jump所实现的jump方法就可以了
//这时使用混合能够更加方便
class Lance1 with Swimming, Jump {}
```



## 作业

​	设计一个表示分数的类`Fraction`。这个类用两个int类型的变量分别表示分子和分母。这个类的构造函数是：`Fraction(num a, num b)`构造一个x/y的分数。这个类要提供以下的功能：

- 将自己与另一个分数相加，产生一个新的Fraction的对象；
- 将自己和另一个分数相乘，产生一个新的Fraction的对象；
- 将自己以**分子/分母**的形式输出，如果分数是1/1，应该输出1；当分子大于分母时，不需要提出整数部分，即3/1是一个正确的输出；输出时需要为最简形式，如：2/4 应该是 1/2

