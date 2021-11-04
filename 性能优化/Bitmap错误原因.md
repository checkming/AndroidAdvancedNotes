# Bitmap错误的原因

如下图

![1](1.png)

LruCache 移除图片的时候回调 entryRemoved 方法，在这个方法中我们应该分情况处理：

1. 能够复用的时候（oldValue.isMutable() 就是判断能不能复用），我们就通过复用池来复用
2. 如果不能复用，我们就直接调用 recycler 回收

而上图这样处理，就是不管什么情况，都会去回收我们的bitmap ，这就导致我们去验证 Bitmap 使用复用池的时候，复用池使用的 Bitmap 内存被回收了的，所以报错。

所以上面应该改成下面这样：

```java
if (oldValue.isMutable()) {
    // < 3.0  bitmap 内存在 native
    // >= 3.0 在 java
    // >= 8.0 又变为 native
    // 如果从内存中移除，将其放入复用池
    reusablePool.add(new WeakReference<Bitmap>(oldValue, getReferenceQueue()));
} else {
    oldValue.recycle();
}
```

