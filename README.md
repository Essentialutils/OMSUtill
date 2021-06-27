[![](https://jitpack.io/v/sharafas-om/OMSUtill.svg)](https://github.com/sharafas-om/OMSUtill)

#### **Install**
    implementation 'com.github.sharafas-om:OMSUtill:{version}'

------------

[![](https://img.shields.io/badge/1-CircularProgress-red)](https://github.com/sharafas-om/OMSUtill)
```xml
<com.oms.utill.views.CircularProgress
	android:id="@+id/download.progress"
	android:layout_width="45dp"
	android:layout_height="45dp" />
```
- setProgressColor(int color)
- setProgressWidth(int width)
- setTextColor(int color)
- showProgressText(boolean show)

------------

[![](https://img.shields.io/badge/2-CustomStatusBar-red)](https://github.com/sharafas-om/OMSUtill)

- Call method you need after setContentView(), such as 
	`CustomStatusBar.setColor(MainActivity.this, mColor);`
- If you use this util in a page which containing a DrawerLayout, you need add `android:fitsSystemWindows="true"` inside root layout
- Set Light or Dark mode
	```java
	  CustomStatusBar.setLightMode(Activity activity)
	  CustomStatusBar.setDarkMode(Activity activity)
	```

------------

[![](https://img.shields.io/badge/3-LoaderView-red)](https://github.com/sharafas-om/OMSUtill)

```xml
<com.oms.utill.loading.LoaderView 
    style="@style/LoaderView.Large.ThreeBounce"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    app:Loader_Color="@color/colorPrimary" />
```
- style
	- @style/LoaderView
	- @style/LoaderView.Circle
	- @style/LoaderView.Large
	- @style/LoaderView.Small
	- @style/LoaderView.Small.DoubleBounce 
- ProgressBar
	```java
	ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
	Sprite doubleBounce = new DoubleBounce();
	progressBar.setIndeterminateDrawable(doubleBounce);
	```
	- Sprite Styles
		- RotatingPlane
		- DoubleBounce
		- Wave
		- WanderingCubes
		- Pulse
		- ChasingDots
		- ThreeBounce
		- Circle
		- CubeGrid
		- FadingCircle
		- FoldingCube
		- RotatingCircle

------------
