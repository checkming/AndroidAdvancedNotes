

# ProcessState

ActivityManager.java 

| ProcessState级别                       | 取值 | 解释                                                         |
| -------------------------------------- | ---- | ------------------------------------------------------------ |
| PROCESS_STATE_CACHED_EMPTY             | 16   | 进程处于cached状态，且为空进程                               |
| PROCESS_STATE_CACHED_ACTIVITY_CLIENT   | 15   | 进程处于cached状态，且为另一个cached进程(内含Activity)的client进程 |
| PROCESS_STATE_CACHED_ACTIVITY          | 14   | 进程处于cached状态，且内含Activity                           |
| PROCESS_STATE_LAST_ACTIVITY            | 13   | 后台进程，且拥有上一次显示的Activity                         |
| PROCESS_STATE_HOME                     | 12   | 后台进程，且拥有home Activity                                |
| PROCESS_STATE_RECEIVER                 | 11   | 后台进程，且正在运行receiver                                 |
| PROCESS_STATE_SERVICE                  | 10   | 后台进程，且正在运行service                                  |
| PROCESS_STATE_HEAVY_WEIGHT             | 9    | 后台进程，但无法执行restore，因此尽量避免kill该进程          |
| PROCESS_STATE_BACKUP                   | 8    | 后台进程，正在运行backup/restore操作                         |
| PROCESS_STATE_IMPORTANT_BACKGROUND     | 7    | 对用户很重要的进程，用户不可感知其存在                       |
| PROCESS_STATE_IMPORTANT_FOREGROUND     | 6    | 对用户很重要的进程，用户可感知其存在                         |
| PROCESS_STATE_TOP_SLEEPING             | 5    | 与PROCESS_STATE_TOP一样，但此时设备正处于休眠状态            |
| PROCESS_STATE_FOREGROUND_SERVICE       | 4    | 拥有一个前台Service                                          |
| PROCESS_STATE_BOUND_FOREGROUND_SERVICE | 3    | 拥有一个前台Service，且由系统绑定                            |
| PROCESS_STATE_TOP                      | 2    | 拥有当前用户可见的top Activity                               |
| PROCESS_STATE_PERSISTENT_UI            | 1    | persistent系统进程，并正在执行UI操作                         |
| PROCESS_STATE_PERSISTENT               | 0    | persistent系统进程                                           |
| PROCESS_STATE_NONEXISTENT              | -1   | 不存在的进程                                                 |

# ADJ 调度算法的核心方法

- `updateOomAdjLocked`：更新adj，当目标进程为空，或者被杀则返回false；否则返回true;
- `computeOomAdjLocked`：计算adj，返回计算后RawAdj值;
- `applyOomAdjLocked`：使用adj，当需要杀掉目标进程则返回false；否则返回true。



## updateOomAdjLocked

过程比较复杂，主要分为更新adj(满足条件则杀进程)和根据memFactor来调度执行TrimMemory操作； 

第一部分：更新adj(满足条件则杀进程) 

- 遍历mLruProcesses进程 
  - 当进程未分配adj的情况
    - 当进程procState=14或15，则设置`adj=curCachedAdj(初始化=9)`; 
      - 当curCachedAdj != nextCachedAdj，且stepCached大于cachedFactor时 则`curCachedAdj = nextCachedAdj`，（nextCachedAdj加2，nextCachedAdj上限为15）；
    - 否则，则设置`adj=curEmptyAdj(初始化=9)`; 
      - 当curEmptyAdj != nextEmptyAdj，且stepEmpty大于EmptyFactor时 则`curEmptyAdj = nextEmptyAdj`，（nextEmptyAdj加2，nextEmptyAdj上限为15）；
  - 根据当前进程procState状态来决策： 
    - 当curProcState=14或15，且cached进程超过上限(cachedProcessLimit=16)，则杀掉该进程
    - 当curProcState=16的前提下： 
      - 当空进程超过上限(TRIM_EMPTY_APPS=8)，且空闲时间超过30分钟，则杀掉该进程
      - 否则，当空进程超过上限(emptyProcessLimit=16)，则杀掉该进程
  - 没有services运行的孤立进程，则杀掉该进程；

第二部分：根据memFactor来调度执行TrimMemory操作； 

- 根据CachedAndEmpty个数来调整内存因子memFactor(值越大，级别越高)： 
  - 当CachedAndEmpty < 3，则memFactor=3；
  - 当CachedAndEmpty < 5，则memFactor=2；
  - 当CachedAndEmpty >=5，且numCached<=5,numEmpty<=8，则memFactor=1；
  - 当numCached>5 或numEmpty>8，则memFactor=0；
- 当内存因子不是普通0级别的情况下，根据memFactor来调整前台trim级别(fgTrimLevel): 
  - 当memFactor=3，则fgTrimLevel=TRIM_MEMORY_RUNNING_CRITICAL；
  - 当memFactor=2，则fgTrimLevel=TRIM_MEMORY_RUNNING_LOW；
  - 否则(其实就是memFactor=1)，则fgTrimLevel=TRIM_MEMORY_RUNNING_MODERATE 
  - 再遍历mLruProcesses队列进程： 
    - 当curProcState > 12且没有被am杀掉，则执行TrimMemory操作；
    - 否则，当curProcState = 9 且trimMemoryLevel<TRIM_MEMORY_BACKGROUND，则执行TrimMemory操作；
    - 否则，当curProcState > 7， 且pendingUiClean =true时 
      - 当trimMemoryLevel<TRIM_MEMORY_UI_HIDDEN，则执行TrimMemory操作；
      - 当trimMemoryLevel<fgTrimLevel，则执行TrimMemory操作；
- 当内存因子等于0的情况下,遍历mLruProcesses队列进程： 
  - 当curProcState >=7, 且pendingUiClean =true时, 
    - 当trimMemoryLevel< TRIM_MEMORY_UI_HIDDEN，则执行TrimMemory操作；

## computeOomAdjLock()

### Service情况

当adj>0 或 schedGroup为后台线程组 或procState>2时： 

- 当service已启动，则procState<=10； 
  - 当service在30分钟内活动过，则adj=5,cached=false;
- 获取service所绑定的connections 
  - 当client与当前app同一个进程，则continue;
  - 当client进程的ProcState >=ActivityManager.PROCESS_STATE_CACHED_ACTIVITY，则设置为空进程
  - 当进程存在显示的ui，则将当前进程的adj和ProcState值赋予给client进程
  - 当不存在显示的ui，且service上次活动时间距离现在超过30分钟，则只将当前进程的adj值赋予给client进程
  - 当前进程adj > client进程adj的情况 
    - 当service进程比较重要时，则设置adj >= -11
    - 当client进程adj<2,且当前进程adj>2时，则设置adj=2;
    - 当client进程adj>1时，则设置adj = clientAdj
    - 否则，设置adj <= 1；
    - 若client进程不是cache进程，则当前进程也设置为非cache进程
  - 当绑定的是前台进程的情况 
    - 当client进程状态为前台时，则设置mayBeTop=true，并设置client进程procState=16
    - 当client进程状态 < 2的前提下：若绑定前台service，则clientProcState=3；否则clientProcState=6
  - 当connections并没有绑定前台service时，则clientProcState >= 7
  - 保证当前进程procState不会比client进程的procState大
- 当进程adj >0，且activity可见 或者resumed 或 正在暂停，则设置adj = 0

## applyOomAdjLocked

- curRawAdj != setRawAdj

- 进程当前OOM的校准 != 进程最后的OOM校准(app.curAdj != app.setAdj)
  - 将adj值 发送给lmkd守护进程
- 最后设置的调度组 != 当前所需的调度组(app.setSchedGroup != app.curSchedGroup)
  - 等待被杀
    - 杀进程，并设置success = false
  - else
    - 设置进程组信息
    - 调整进程的swappiness值
-  最近的前台Activity != 正在运行的前台活动(app.repForegroundActivities != app.foregroundActivities)
- 最近的进程状态 != 当前的进程状态（app.repProcState != app.curProcState）
  - 设置进程状态
- 当setProcState = -1或者curProcState与setProcState值不同时
  - 计算pss下次时间
- else
  - 当前时间超过pss下次时间，则请求统计pss,并计算pss下次时间
- 进程跟踪器最后的状态 != 当前的状态

