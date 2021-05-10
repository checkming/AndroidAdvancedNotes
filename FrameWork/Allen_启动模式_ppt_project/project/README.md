# 这是一个帮助你理解和记忆 android startActivity 中 launch mode 和 flags 作用的 demo 工程
---

![screenshot](https://raw.githubusercontent.com/walfud/TaskDemo/master/doc/screenshot.png)

上图中, 我们可以看到:
* 当前 app 中有 4 个 task
* 当前的 activity(id: 5) 位于 id=297 的 task 中


## xml 中 launchMode 共有四种:
---

* standard (ActivityInfo.LAUNCH_MULTIPLE)
* singleTop (ActivityInfo.LAUNCH_SINGLE_TOP)
* singleTask (ActivityInfo.LAUNCH_SINGLE_TASK)
* singleInstance (ActivityInfo.LAUNCH_SINGLE_INSTANCE)

standard 和 singleTop 是一类. 它们都可以被创建多个 instance. 这两者的区别是:
* standard 无论何时接到 Intent, 都会在调用者的 task 中 push 一个新的 activity 放在尾部
* singleTop 当调用者当前 task 的顶部是 Intent 要启动的 activity 时, 则当前顶部的 activity 回收到 onNewIntent 并且不会启动新的 activity. 否则, 也会在调用者的 task 中 push 一个新的 activity 放在尾部

singleTask 和 singleInstance 是一类. 它们在系统中只能存在一个 instance. 这两者的区别是:
* singleTask 根据 affinity 决定在哪个 task 中启动. 如果存在该 affinity 所指定的 task, 则在该 task 中启动, 否则会新建一个该 affinity 的 task, 并在其中启动.
* singleInstance 被启动的 activity 只能单独存在于一个独立的 task 中. 如果从该 task 再次启动其它 activity, 则新启动的 activity 会被放到其它 task 中. 其它规则同 singleTask

以下是我总结的神图:

1. 从 `standard` activity 中启动其它类型的 activity, 总结如图:

![start activity from `Standard`](https://raw.githubusercontent.com/walfud/TaskDemo/master/doc/standard.jpg)

## 代码中常用的有如下 flag:
---

FLAG_ACTIVITY_NEW_TASK
---

* 如果 task 中没有该 Activity, 则新建一个 task 并启动 Activity
* 否则, 拥有该 Activity 的 task 会被带到前台.
* 如果调用者想要从 Activity 获取 result, 请不要使用此标记

通常 launcher 会使用该标记启动新 Activity 


FLAG_ACTIVITY_MULTIPLE_TASK
---

* 只能和 FLAG_ACTIVITY_NEW_TASK 或者 FLAG_ACTIVITY_NEW_DOCUMENT. 单独使用或和其他标记组合则无效
* 使上述两个标记跳过 task 查找过程, 从而无条件创建 Activity


FLAG_ACTIVITY_CLEAR_TASK
---

* 只能和 FLAG_ACTIVITY_NEW_TASK 组合使用
* 如果已经有 task 运行该 Activity, 则清除该 task 以及其中所有 Activity, 然后在一个新的 task 中创建 Activity


FLAG_ACTIVITY_TASK_ON_HOME
---

* 只能和 FLAG_ACTIVITY_NEW_TASK 组合使用
* 该 Activity 会添加到 home 所在的 task 中. 这会导致从该 Activity 返回会直接返回 home


FLAG_ACTIVITY_SINGLE_TOP
---

* 如果在 history stack 的 top, 则不再创建新的


FLAG_ACTIVITY_CLEAR_TOP
---

* 判断当前栈
* 清空所有上层 Activity
* * 指定 FLAG_ACTIVITY_SINGLE_TOP -> onNewIntent
  * 否则 -> 销毁旧 Activity 并创建新的


FLAG_ACTIVITY_NO_HISTORY
---

* 不在 history stack 中显示
* 当切换走的时候 (navigate 或者 home), 则 finish
* 无法接受 onActivityResult


FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
---

* 不在 history stack 中显示


FLAG_ACTIVITY_REORDER_TO_FRONT
---

* 如果 task: A B C D, 那么启动 B 后: A C D B
* 如果同时设置了 FLAG_ACTIVITY_CLEAR_TOP, 则此标记无效

  
FLAG_ACTIVITY_PREVIOUS_IS_TOP
---

* 当前 Activity 不作为 top. 也就是说, top 依然是上一个 Activity
 
通常用作很快就结束的 Activity 或者是有从属关系的 Activity 之用


## 其它
---

* xml 中无法指定 FLAG_ACTIVITY_CLEAR_TOP, 而 flag 中无法指定 singleInstance

* 脱离 affinity 讨论 singleTask 和 singleInstance 是不科学的

* [Tasks and Back Stack](http://developer.android.com/guide/components/tasks-and-back-stack.html) 中说:
> The system creates a new task and instantiates the activity at the root of the new task...

 这是不严谨的. 因为同一个应用中如果不指定 'taskAffinity' 的话, 那么 'singleTask' 会在默认的 task (即 MAIN/LAUNCHER 所在的 task) 中启动.

 [official doc](http://developer.android.com/guide/topics/manifest/activity-element.html) 中还说: 
> Since activities with "singleTask" or "singleInstance" launch modes can only be at the root of a task...

  显然, 这也是错误的. 当你从 MAIN/LAUNCHER 的 activity 中启动一个没指定任何 affinity 的 'singleTask' activity 时, 这个新启动的 activity 会在当前 task 中被创建(见上描述), 并且是在 MAIN/LAUNCHER 的上面. 因此, 'singleTask' 的 activity 未必一定是 the root activity of a task.


## Ref
---

[Tasks and Back Stack](http://developer.android.com/guide/components/tasks-and-back-stack.html)

[official doc](http://developer.android.com/guide/topics/manifest/activity-element.html)

[解开Android应用程序组件Activity的"singleTask"之谜](http://blog.csdn.net/luoshengyang/article/details/6714543)

[深入讲解Android中Activity launchMode](http://droidyue.com/blog/2015/08/16/dive-into-android-activity-launchmode/)

[android深入解析Activity的launchMode启动模式，Intent Flag，taskAffinity](http://blog.csdn.net/self_study/article/details/48055011)

## Contribute
---

此工程还需要很多努力才能变得更加完善, 比如: 目前 startActivity/back 后需要等待 2s 后才能对 task 进行绘制, 因为要等待 activity 的 onDestroy 完成; 再或者, 目前界面上 task 都是一个 id 标识, 不那么直观, 可以增加一些颜色, 比如 standard 用绿色, singleTop 用黄色等等(参考 [Activities-LaunchMode-demo](https://github.com/gnorsilva/Activities-LaunchMode-demo)). 希望你也能为它添砖加瓦, 帮助更多的人.

Any question, feel free to contact me.