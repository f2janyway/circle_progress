# How to
1. Add it in your root build.gradle at the end of repositories:
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Step 2. Add the dependency build.gradle(.app)
```gradle
	dependencies {
	        implementation 'com.github.f2janyway:custom_view:Tag'
	}
```
[![](https://jitpack.io/v/f2janyway/custom_view.svg)](https://jitpack.io/#f2janyway/custom_view)
## Index

  1. CircleProgress<br>
    [code](https://github.com/f2janyway/custom_view/blob/master/f2j_custom_view/src/main/java/com/box/f2j_custom_view/CircleProgress.kt)<br>
    [attr](https://github.com/f2janyway/custom_view/blob/master/f2j_custom_view/src/main/res/values/attrs.xml)<br>
    
 ![Alt Text](https://github.com/yegyu/android_portfolio/blob/master/gif/circle_progress.gif)
    
    
  2. SimpleFastScrollBar<br>
  (It can be used but not seekbar. Use RealVerticalSeekbar)
    [code](https://github.com/f2janyway/custom_view/blob/master/f2j_custom_view/src/main/java/com/box/f2j_custom_view/SimpleFastScrollBar.kt)<br>
    [attr](https://github.com/f2janyway/custom_view/blob/master/f2j_custom_view/src/main/res/values/attrs.xml)<br>
      - Don't use 'thumb_src' attr not implemented<br>
      
![Alt Text](https://github.com/yegyu/android_portfolio/blob/master/gif/vertical.gif)
    
    
  3. RealVerticalSeekbar<br>
  [code](https://github.com/f2janyway/custom_view/blob/master/f2j_custom_view/src/main/java/com/box/f2j_custom_view/RealVerticalSeekbar.kt)<br>
  [attr](https://github.com/f2janyway/custom_view/blob/master/f2j_custom_view/src/main/res/values/attrs.xml)<br>
  
  
 
![Alt Text](https://github.com/yegyu/android_portfolio/blob/master/gif/seekbar.gif)
