# Dart基础

[TOC]

​	  学习一门新的语言，我们可以以自己现有的熟悉的语言来类比，比如我们非常熟悉`Java`，那么剩下的就是需要掌握与`Java`不同的`Dart`语法，剩下的就需要靠自己多写多看来慢慢熟悉。

​	国际惯例，使用Dart完成一个："Hello，World!"

```dart
void main() {
  	print('Hello, World!');
}
```

> 运行Dart 代码可以使用  `dart xxx.dart`，dart命令需要配置环境变量，将 “${FLUTTER_SDK}/bin/cache/dart-sdk/bin” 配置到变量 PATH 中即可。

## 变量

​	变量是一个引用,未初始化的变量值是null。

```dart
Object name1 = 'Lance';
var name2 = 'Lance';
dynamic name3 = 'Lance';
print('$name1 $name2 $name3');
//变量是一个引用，上面的name1、name2与name3的变量都引用了 一个内容为 “Lance” 的 String 对象。
```

​	可以用Object、var与dynamic声明的变量赋任何类型的值，但是背后的原理却是非常不同。

**1、Object：** 与Java一样Object是所有类的基类，Object声明的变量可以是任意类型。（在 Dart 中 甚至连 数字、方法和 `null` 都是对象，比如int。）

```
Object a = 1;
a = "a";
```

**2、var：** 声明的变量在赋值的那一刻，决定了它是什么类型。

```dart
//a已经确定为num，不能再赋值字符串，编译错误
var a = 1;
a = "a";
//正确 
var b;
b = 1;
b = "a";
```

**3、dynamic：** 不是在编译时候确定实际类型的, 而是在运行时。dynamic声明的变量行为与Object一样，使用一样，关键在于运行时原理不同。



> 没有初始化的变量自动获取一个默认值为 `null`（类型为数字的 变量如何没有初始化其值也是 null）。
>
> 在声明变量的时候，也可以选择加上具体 类型：int a = 1;
>
> 对于局部变量，按照Dart代码风格推荐，使用 `var` 而不是具体的类型来定义局部变量。

### final与const

​	如果不打算改变一个变量，可以使用final和const，它们可以替代任何类型，只能在声明时初始化，且不能改变。

> ```dart
> const a =  1;
> final  b =1;
> final int c = 1;
> const int d = 1;
> ```

​	final与const从使用上根本看不出区别，但是final是运行时常量，而const是编译器常量，它的值在编译期就可以确定，编译时常量能够让代码运行的更高效。

```dart
//正确，已经确定的值
const a = 1;
const b = a + 1;

//错误,final不能在编译时确定值，因此const也不能确定值
final a = 1;
const c = a + 1;
```

> 类的变量可以为 `final` 但是不能是 `const` 。如果 const 变量在类中，需要定义为`static const`静态常量
>
> ![const成员](图片/const成员.png)

## 内置的类型

​	与Java的八大内置基本数据类型不同，Dart 内置支持下面这些类型：

- numbers
- strings
- booleans
- lists (也被称之为 *arrays*)
- maps
- runes (用于在字符串中表示 Unicode 字符)
- symbols



### Numbers（数值）

`num`是数字类型的父类，有两个子类` int`和 `double`。



### Strings（字符串）

​	Dart 字符串是 UTF-16 编码的字符序列。 可以使用单引号或者双引号来创建字符串，单引号和双引号可以嵌套使用，否则需要使用`\`进行转义。字符串中也可以引用变量与表达式。

```dart
var name = 'lance';
//如果插入一个简单的标识符，而后面没有紧跟更多的字母数字文本，那么{}应该被省略。
var a = "my name is $name!";
var b = "my name is ${name.toUpperCase()}!";
```

​	  与Java一样可以使用 `+` 操作符来把拼接字符串，也可以把多个 字符串放到一起来实现同样的功能：

```dart
var a  = "my name is " "lance";
```

​	使用三个单引号或者双引号可以 创建多行字符串对象

```dart
var s1 = '''
You can create
multi-line strings like this one.
''';

var s2 = """This is also a
multi-line string.""";
```

​	提供一个 `r` 前缀可以创建一个 “原始 raw” 字符串

```dart
print(r"换行符：\n"); // 换行符：\n  r:不需要转义
print("换行符：\\n"); // 换行符：\n
```



### Booleans（布尔值）

​		Dart 有一个名字为 `bool` 的类型。 只有两个对象是布尔类型的：`true` 和 `false` 。这一点和Java没有太大的区别。



### Lists（列表）

​		几乎所有编程语言中最常见的集合可能是数组或有序对象组。在Dart中，数组就是`List`对象。对`List`进行遍历也和Java一样。

```dart
var list = [1, 2, 3];
//Lists 的下标索引从 0 开始，第一个元素的索引是 0. list.length - 1 是最后一个元素的索引
print(list[list.length-1]);
//修改元素
list[0] = 2;

//使用new（实际上new可以省去）
var list = new List(1);
list[0] = 2;

//在 list 字面量之前添加 const 关键字，可以 定义一个不变的 list 对象（编译时常量）
var list =  const [1,2,3];
i.add(2); ///错误，list不可变
```



### Maps（映射集合）

​	Map：键值对相关的对象。 键和值可以是任何类型的对象。每个 键 只出现一次， 而一个值则可以出现多次。

```dart
//直接声明，用{}表示，里面写key和value，每组键值对中间用逗号隔开
var companys = {'a': '阿里巴巴', 't': '腾讯', 'b': '百度'};
var companys2 = new Map();
companys2['a'] = '阿里巴巴';
companys2['t'] = '腾讯';
companys2['b'] = '百度';

//添加元素
companys['j'] = '京东';
//获取与修改元素
var c = companys['c']; ///没有对应的key返回null
companys['a'] = 'alibaba'; 
```

​	与List一样，在 map字面量之前添加 `const` 关键字，可以 定义一个 编译时常量 的 map

### Runes（用于在字符串中表示Unicode字符）

​	如果需要获得特殊字符的Unicode编码，或者需要将32位的Unicode编码转换为字符串，就可以借助Runes类。

​	Dart表达Unicode代码点的常用方法是\uXXXX，其中XXXX是4位十六进制值。要指定多于或少于4个十六进制数字，需要将值放在大括号中。

```dart
var clapping = '\u{1f44f}'; ///5个16进制 需要使用{}
print(clapping);//👏
//获得 16位代码单元
print(clapping.codeUnits); //[55357, 56399]
//获得unicode代码
print(clapping.runes.toList()); //[128079]

//fromCharCode 根据字符码创建字符串
print( String.fromCharCode(128079));
print( String.fromCharCodes(clapping.runes));
print( String.fromCharCodes([55357, 56399]));
print( String.fromCharCode(0x1f44f));

Runes input = new Runes(
  '\u2665  \u{1f605}  \u{1f60e}  \u{1f47b}  \u{1f596}  \u{1f44d}');
print(String.fromCharCodes(input));
```

> 实际上在Flutter开发中Runes与下一个Symbols可能永远也不会用到。

### Symbols

​	操作符标识符，可以看作C中的宏。表示编译时的一个常量

```dart
var i = #A; //常量

main() {
  print(i);
  switch(i){
    case #A:
      print("A");
      break;
    case #B:
      print("B");
      break;

  }
  var b = new Symbol("b");
  print(#b == b); ///true
}
```



## 操作符

常见的操作符就没什么可说的了，主要来看看Java所没有的。

### 类型判定操作符

`as`、 `is`、 和 `is!` 操作符是在运行时判定对象 类型的操作符

| 操作符 | 解释                           |
| ------ | ------------------------------ |
| `as`   | 类型转换                       |
| `is`   | 如果对象是指定的类型返回 True  |
| `is!`  | 如果对象是指定的类型返回 False |

 `as` 操作符把对象转换为特定的类型，但是如果无法完成转换则会抛出一个异常

`is` 和Java中的 `instanceof` 相同

### 赋值操作符

`=`、`+=`、`\=`、`*=`这些不必多说，还有一个 `??=` 操作符用来指定 值为 null 的变量的值

```dart
b ??= value; // 如果 b 是 null，则 value 赋值给 b；
             // 如果不是 null，则 b 的值保持不变
```

### 条件表达式

Dart 有两个特殊的操作符可以用来替代 [if-else](http://dart.goodev.org/guides/language/language-tour#if-and-else) 语句：

- `condition ? expr1 : expr2`

  如果 *condition* 是 true，执行 *expr1* (并返回执行的结果)； 否则执行 *expr2* 并返回其结果。

- `expr1 ?? expr2`

  如果 *expr1* 不为null，返回其值； 否则执行 *expr2* 并返回其结果。

### 级联操作符

​	级联操作符 (`..`) 可以在同一个对象上 连续调用多个函数以及访问成员变量。 使用级联操作符可以避免创建 临时变量， 并且写出来的代码看起来 更加流畅：

```dart
//StringBuffer write就是Java的append
var sb = new StringBuffer();
sb..write('foo')..write('bar');
```

### 安全操作符

​	Dart提供了 `?.`操作符。左边的操作对象 如果 为 null 则返回 null

```dart
String sb;
//空指针
print(sb.length);
print(sb?.length);
```



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





## 作业

使用Dart实现冒泡排序从小到大排列任意一个int数组。

```dart
void main(){
    var array = [23,45,678,9,32,2];
    //请实现冒泡排序
}
```

