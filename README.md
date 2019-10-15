
GreekMythologyRep
==============

A record of learning the knowledge points of all parts of Android.  And will be continuously updated.
<br>
记录自己Android各方面学习的app，会持续不停的更新。

内置Modules
==============
因个人使用的原因，所以库并没有单独抽离出来，而是包含在此项目中，方便修改。
## 1. player-library
一款Android播放器封装库，支持以下播放器：
- **内置MediaPlayer**
- **ExoPlayer2**
- **ijk**

实例化方式：<br>
**`MediaPlayerFactory.newInstance(Context context, int mode, Object args)`** 默认创建ijk播放器

## 2. views-library
包括了日常开发中使用的自定义view/封装组件以及view相关工具类。

### 自定义组件简介 (按效果):

|加载效果|介绍|
| ------ | --- |
|[WaveLoadingView][1]|水波加载效果View|
|[Win10LoaddingView][2]|仿win10效果的加载圈|

|进度条效果|介绍|
| ------ | --- |
|[CircleProgressView][3]|圆形进度条|
|[HorizontalProgressBarWithNumber][4]|横向带数字进度条|
|[RoundProgressBarWidthNumber][5]|圆角带数字进度条|
|[SpiralProgressView][6]|螺旋条纹进度条(仿mac)|

|文字|介绍|
| ------ | --- |
|[BorderTextView][7]|带border的TextView|


|列表控件|介绍|
| ------ | --- |
|[SliderDeleteListView][8]|可滑动删除的List|



[1]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/loading/WaveLoadingView.java
[2]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/loading/Win10LoaddingView.java
[3]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/progress/CircleProgressView.java
[4]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/progress/HorizontalProgressBarWithNumber.java
[5]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/progress/RoundProgressBarWidthNumber.java
[6]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/progress/SpiralProgressView.java
[7]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/text/BorderTextView.java

[8]:https://github.com/kivensolo/GreekMythologyRep/blob/master/views-library/src/main/java/com/module/views/touch/SliderDeleteListView.java







