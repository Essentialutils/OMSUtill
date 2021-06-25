[![version tag](https://jitpack.io/v/sharafas-om/OMSUtill.svg)](https://github.com/sharafas-om/OMSUtill)
#### **Install**
    implementation 'com.github.sharafas-om:OMSUtill:{version tag}'
------------
 ### 1. CircularProgress
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

### 2.  CustomStatusBar
- Call method you need after setContentView(), such as
	`CustomStatusBar.setColor(MainActivity.this, mColor);`
- If you use this util in a page which containing a DrawerLayout, you need add `android:fitsSystemWindows="true"` inside root layout




