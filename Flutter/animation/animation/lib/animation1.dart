import 'package:flutter/material.dart';

void main() => runApp(Animation1());

class Animation1 extends StatefulWidget {
  @override
  _Animation1State createState() => _Animation1State();
}

class _Animation1State extends State<Animation1>
    with SingleTickerProviderStateMixin {
  AnimationController animationController;

  MyValueTween tween;
  Animation<MyValue> valueAnimation;

  @override
  Widget build(BuildContext context) {
    MyValue value = valueAnimation.value;
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text("动画"),
        ),
        body: Container(
          width: value.value,
          height: value.value,
          color: Colors.red,
          child: Text("11111"),
        ),
      ),
    );
  }

  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  void initState() {
    animationController = AnimationController(
        duration: Duration(seconds: 3),
        vsync: this);

    animationController.addListener(() {
      setState(() {

      });
      print(animationController.value);
    });

    tween = MyValueTween(begin: MyValue(10),end: MyValue(11));
    valueAnimation = tween.animate(animationController);

    //正向启动动画
    animationController.reverse();
  }
}


class MyValue{
  double value;

  MyValue(this.value);

}
class MyValueTween extends Tween<MyValue>{


  MyValueTween({ MyValue begin, MyValue end }) : super(begin: begin, end: end);

  @override
  MyValue lerp(double t) {
    return MyValue(begin.value+t*1000);
  }
}
