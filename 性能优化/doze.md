### 初识Doze

  在Android 6.0版本中google提供了两个策略：doze和app standby，能有效的减少电量的消耗，提升待机时间。设备没有连接到电源，设备进入Doze模式时，系统将通过延迟最近用户没有使用的应用程序的后台CPU运作及网络活动，让应用程序处于App Standby状态，以此来减少电池消耗。 
  Doze功能借助动作检测来确定用户有多长时间没用手机，然后就进入“打盹”模式，从而延长续航时间。而一旦用户重新开始使用手机，Doze模式就会取消恢复到正常状态。

> 谷歌称，配备Doze的Nexus9将比普通Nexus续航能力提高一倍。德国科技博客ComputerBase分别用一部安装了Android M开发者预览版和一部安装了Android 5.1.1的Nexus 5来做对比。两部手机都安装了同样的应用，电池充满。同时连入相同的无线网络，使用相同的设置，都不装SIM卡，蓝牙，NFC，Android Beam以及LED等全都处于关闭状态。 结果对比是在待机8小时后，安装了Android 5.1.1的Nexus 5消耗了4%的电量，而Android M版则仅消耗了1.5%;在24小时后，Android 5.1.1版的Nexus 5消耗了12%的电量，Android M版则仅消耗了4.5%；48小时后，Android 5.1.1版的Nexus 5消耗了24%的电量，而Android M版则仅消耗了9%。总的来看，在Doze开启下，Nexus 5在Android M下则能待机533小时，在Android 5.1.1下可待机200小时。

### 2. 理解Doze

  1、设备进入Doze睡眠模式时机： 
    –用户不操作设备一段时间 （通过动作监测来判断） 
    –屏幕关闭 
    –设备未连接电源充电 
  2、Doze模式下应用程序有什么变化： 
    –系统试图通过限制应用程序访问网络和CPU密集型8服务节省电池 
    –防止应用程序访问网络，推延应用程序的工作，同步，和标准的警报 
    –系统定期提供一个短暂的时间让应用程序完成延迟的工作活动，在这个时间片里，系统将提供维持性窗口应用程序访问网络，运行在等待的同步，工作，和报警等活动 
  Doze模式的五种状态，分别如下： 
    –ACTIVE：手机设备处于激活活动状态 
    –INACTIVE：屏幕关闭进入非活动状态 
    –IDLE_PENDING：每隔30分钟让App进入等待空闲预备状态 
    –IDLE：空闲状态 
    –IDLE_MAINTENANCE：处理挂起任务 
  下面一张图将介绍这几种状态之间的切换关系 
![](.\doze_state_change.png)
  google官方文档说明，在灭屏后至少60分钟才会进入到doze模式。在进入到doze模式后，应用程序的活动和网络链接都会被挂起，每个一段时间会进入一次Maintenance阶段（持续30秒），让应用能处理挂起的任务。 
![](.\doze.png)
  进入Maintenance的时间间隔会随着doze模式的深入越来越长。 
  3、如何退出doze模式 
    –用户唤醒装置移动，打开屏幕 
    –连接电源 
  4、doze模式有哪些限制 
    –网络连接会被禁止 
    –Wake Lock会被屏蔽 
    –AlarmManager定时任务延迟到下一个maintenance window进行处理，除非使用AlarmManager提供的方法：setAndAllowWhileIdle()或者setExactAndAllowWhileIdle() 
    –系统将不扫描热点WIFI 
    –同步工作将被禁止 
    –不允许JobScheduler进行任务调度 
  5、如何在开发时适配doze模式 
    –Doze影响到AlarmManager闹钟和定时器管理活动，在Android6.0引入了两个新方法：setAndAllowWhileIdle() 和setExactAndAllowWhileIdle()，调用两个方法可以在Doze模式下让系统响应定时任务 
    –Doze模式下限制了网络的连接，如果应用程序依赖于实时信息，那么这个将影响App的体验。那么你需要使用Google Cloud Messaging (GCM)谷歌云消息 
  6、测试Doze和App Standby模式的方法（Adb命令）

  **测试Doze模式**

- 1.首先确保你的硬件或虚拟设备是Android6.0或更高版本系统；

- 2.连接设备到开发机上并安装你的app；

- 3.运行app并让其运行活动；

- 4.关闭设备的屏幕；

- 5.运行以下adb命令使系统进入Doze模式：

  ```
    $ adb shell dumpsys battery unplug      //（断开电源）
    $ adb shell dumpsys deviceidle step     //（获取状态）
  ```

- 6.观察你的app表现行为是否有需优化改进的地方。

  **测试App Standby模式**

  步骤1-3同测试Doze模式

- 4.运行以下adb命令迫使系统进入App Standby模式：

  ```
    $ adb shell dumpsys battery unplug
    $ adb shell am set-inactive <packageName> true
  ```

- 5.模拟唤醒你的应用程序使用以下命令：

  ```
    $ adb shell am set-inactive <packageName> false
    $ adb shell am get-inactive <packageName>
  ```

- 6.观察你的App，确保应用程序恢复正常从待机模式过程中，App的通知及其背部活动能达到预期结果。

### 3.理解app standby策略

  当用户不触摸使用应用程序一段时间时，该应用程序处于App Standby状态，系统将把该App标志为空闲状态。除非触发以下任意条件，应用程序将退出App Standby状态：

- 1.用户主动启动该App;
- 2.该App当前有一个前台进程（或包含一个活动的前台服务，或被另一个activity或前台service使用）;
- 3.App生成一个用户所能在锁屏或通知托盘看到的Notification,而当用户设备插入电源时，系统将会释放App的待机状态，允许他们自由的连接网络及其执行未完成的工作和同步。如果设备空闲很长一段时间，系统将允许空闲App一天一次访问网络。

  **Doze和App Standby的区别：** 
  Doze模式需要屏幕关闭（通常晚上睡觉或长时间屏幕关闭才会进入），而App Standby不需要屏幕关闭，App进入后台一段时间也会受到连接网络等限制。