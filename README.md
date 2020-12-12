
GreekMythologyRep
==============

A record of learning the knowledge points of all parts of Android.  And will be continuously updated.
<br>

Built in Librarys
==============
## 1. player-library
An Android player package library, supporting the following players：
- **Android MediaPlayer**
- **ExoPlayer2**
- **IJK**

Use sample：<br>
**`MediaPlayerFactory.newInstance(Context context, int mode, Object args)`** Default is ijk-player.

## 2. library-views
Includes custom views, encapsulation component and view related tool class used in daily development。

## 3. library-network
Base on RxJava2(RxAndroid) + retrofit2 + OkHttp3.

## 3. library-server
Android local servers,Include the following:
- NioServer
- NanoServer(TODO)

### Introduction to custom controls:

|Loading|Desc|
| ------ | --- |
|[WaveLoadingView][1]|水波加载效果View|
|[Win10LoaddingView][2]|仿win10效果的加载圈|

|Progress|Desc|
| ------ | --- |
|[CircleProgressView][3]|圆形进度条|
|[HorizontalProgressBarWithNumber][4]|横向带数字进度条|
|[RoundProgressBarWidthNumber][5]|圆角带数字进度条|
|[SpiralProgressView][6]|螺旋条纹进度条(仿mac)|

|Text|Desc|
| ------ | --- |
|[BorderTextView][7]|TextView with border.|


|List control|Desc|
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







