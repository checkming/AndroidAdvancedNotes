 命令用法

**命令行**

```
python systrace.py [options] [category1] [category2] ... [categoryN]
```

####  options

其中options可取值：

| options                                        | 解释                                                |
| :--------------------------------------------- | :-------------------------------------------------- |
| -o `<FILE`>                                    | 输出的目标文件                                      |
| -t N, –time=N                                  | 执行时间，默认5s                                    |
| -b N, –buf-size=N                              | buffer大小（单位kB),用于限制trace总大小，默认无上限 |
| -k `<KFUNCS`>，–ktrace=`<KFUNCS`>              | 追踪kernel函数，用逗号分隔                          |
| -a `<APP_NAME`>,–app=`<APP_NAME`>              | 追踪应用包名，用逗号分隔                            |
| –from-file=`<FROM_FILE`>                       | 从文件中创建互动的systrace                          |
| -e `<DEVICE_SERIAL`>,–serial=`<DEVICE_SERIAL`> | 指定设备                                            |
| -l, –list-categories                           | 列举可用的tags                                      |

#### category

category可取值：

| category   | 解释                           |
| :--------- | :----------------------------- |
| gfx        | Graphics                       |
| input      | Input                          |
| view       | View System                    |
| webview    | WebView                        |
| wm         | Window Manager                 |
| am         | Activity Manager               |
| sm         | Sync Manager                   |
| audio      | Audio                          |
| video      | Video                          |
| camera     | Camera                         |
| hal        | Hardware Modules               |
| app        | Application                    |
| res        | Resource Loading               |
| dalvik     | Dalvik VM                      |
| rs         | RenderScript                   |
| bionic     | Bionic C Library               |
| power      | Power Management               |
| sched      | CPU Scheduling                 |
| irq        | IRQ Events                     |
| freq       | CPU Frequency                  |
| idle       | CPU Idle                       |
| disk       | Disk I/O                       |
| mmc        | eMMC commands                  |
| load       | CPU Load                       |
| sync       | Synchronization                |
| workq      | Kernel Workqueues              |
| memreclaim | Kernel Memory Reclaim          |
| regulators | Voltage and Current Regulators |

####  示例

例如，在`systrace.py`所在目录下执行指令：

```
python systrace.py -b 32768 -t 5 -o mytrace.html wm gfx input view sched freq
./systrace.py -b 32768 -t 5 -o mytrace.html wm gfx input view sched freq //等价
```

又例如，输出全部的trace信息

```
python systrace.py -b 32768 -t 5 -o mytrace.html gfx input view webview wm am sm audio video camera hal app res dalvik rs bionic power sched irq freq idle disk mmc load sync workq memreclaim regulators
```

**注：**收集trace，需要提前安装python，并且一定要注意必须是python 2.x，而不是能3.x，否则可能会出现问题。另外，buffer大小不可过大，否则会出现oom异常。



### 图形化

横坐标是以时间为单位，纵坐标是以进程-线程的方式来划分，同一进程的线程为一组放在一起，可收缩/展开，如下图：

![](.\mytrace.png)

#### Frames

产生的html格式的trace文件必须使用Google Chrome打开，才能正确地解析并已图标形式展现。上图中红色圈起来的，都是可以点击操作的地方，最上方是搜索栏，往下处是Alerts按钮，再往下是鼠标操作模式。

在每个app进程，都有一个Frames行，正常情况以绿色的圆点表示。当圆点颜色为黄色或者红色时，意味着这一帧超过16.6ms（即发现丢帧），这时需要通过放大那一帧进一步分析问题。对于Android 5.0(API level 21)或者更高的设备，该问题主要聚焦在UI Thread和Render Thread这两个线程当中。对于更早的版本，则所有工作在UI Thread。

![](.\mytrace1.png)

#### Alerts

Systrace能自动分析trace中的事件，并能自动高亮性能问题作为一个Alerts，建议调试人员下一步该怎么做。

比如对于丢帧是，点击黄色或红色的Frames圆点便会有相关的提示信息；另外，在systrace的最右上方，有一个Alerts tab可以展开，这里记录着所有的的警告提示信息。

**注：**本文讲到最新版的systrace，其中sdk 23，chrome版本49.0，部分功能在老版本systrace并没有。

## 快捷操作

### 导航操作

| 导航操作 | 作用                   |
| :------- | :--------------------- |
| w        | 放大，[+shift]速度更快 |
| s        | 缩小，[+shift]速度更快 |
| a        | 左移，[+shift]速度更快 |
| d        | 右移，[+shift]速度更快 |

### 快捷操作

| 常用操作 | 作用                                        |
| :------- | :------------------------------------------ |
| f        | **放大**当前选定区域                        |
| m        | **标记**当前选定区域                        |
| v        | 高亮**VSync**                               |
| g        | 切换是否显示**60hz**的网格线                |
| 0        | 恢复trace到**初始态**，这里是数字0而非字母o |

| 一般操作 | 作用                                |
| :------- | :---------------------------------- |
| h        | 切换是否显示详情                    |
| /        | 搜索关键字                          |
| enter    | 显示搜索结果，可通过← →定位搜索结果 |
| `        | 显示/隐藏脚本控制台                 |
| ?        | 显示帮助功能                        |

对于脚本控制台，除了能当做记事本的功能，目前还不清楚有啥功能，或许还在开发中。

### 模式切换

1. Select mode: **双击已选定区**能将所有相同的块高亮选中；（对应数字1）
2. Pan mode: 拖动平移视图（对应数字2）
3. Zoom mode:通过上/下拖动鼠标来实现放大/缩小功能；（对应数字3）
4. Timing mode:拖动来创建或移除时间窗口线。（对应数字4）

可通过按数字1~4，用于切换鼠标模式； 另外，按住alt键，再滚动鼠标滚轮能实现放大/缩小功能。