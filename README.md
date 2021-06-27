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

[![](https://img.shields.io/badge/4-NotificationUtill-red)](https://github.com/sharafas-om/OMSUtill)

- You may need to add the default notification channel to your app/res/values/strings.xml file
```xml
<resources>
    <string translatable="false" name="oms_notification_channel_id">MyDefaultChannelID</string>
    <string translatable="false" name="oms_notification_channel_name">MyDefaultChannelName</string>
    <string translatable="false" name="oms_notification_channel_description">MyDefaultChannelDescription</string>
</resources>
```

- Methods

| Method | Description |
|------------------------------------|--------------------------|
| **NotificationUtill.build(context)** | Create a NotificationUtill object |
| **setId(id)** | Set the identifier for the notification |
| **setTitle(title)** | Set the notification title |
| **setContent(content)** | Set the notification content |
| **setImportance(importance)** | Handle the importance with NotificationUtill Importance |
| **setLargeIcon(largeIcon)** | Set the large icon from a drawable or URL resource |
| **largeCircularIcon()** | Make large icon circular |
| **setSmallIcon(smallIcon)** | Set the small icon from a drawable resource |
| **setPicture(picture)** | Set a picture from a drawable or URL resource |
| **setChannelId(id)** | Set the notification channel id |
| **setChannelName(name)** | Set the notification channel name |
| **setChannelDescription(description)** | Set the notification channel description |
| **setAutoCancel(autoCancel)** | Set the auto-cancel value |
| **setAction(intent)** | Set the action for when user clicks the notification  |
| **enableVibration(vibration)** | Enable or disable the vibration |
| **setVibrationPattern(vibrationPattern)** | Set the vibration pattern |
| **getNotificationBuilder()** | Return the native NotificationCompat.Builder object |
| **show()** | Show the notification |
| **cancel(context, id)** | Cancel the specified notification by id |
| **cancelAll(context)** | Cancel all notifications |

------------

[![](https://img.shields.io/badge/5-NotificationUtill-red)](https://github.com/sharafas-om/OMSUtill)


```java
AnimationUtils.with(Techniques.Tada)
    .duration(700)
    .repeat(5)
    .playOn(findViewById(R.id.edit_area));
```
- Attension
`Flash`, `Pulse`, `RubberBand`, `Shake`, `Swing`, `Wobble`, `Bounce`, `Tada`, `StandUp`, `Wave`

- Special
`Hinge`, `RollIn`, `RollOut`,`Landing`,`TakingOff`,`DropOut`

- Bounce
`BounceIn`, `BounceInDown`, `BounceInLeft`, `BounceInRight`, `BounceInUp`

- Fade
`FadeIn`, `FadeInUp`, `FadeInDown`, `FadeInLeft`, `FadeInRight`
`FadeOut`, `FadeOutDown`, `FadeOutLeft`, `FadeOutRight`, `FadeOutUp`

- Flip
`FlipInX`, `FlipOutX`, `FlipOutY`

- Rotate
`RotateIn`, `RotateInDownLeft`, `RotateInDownRight`, `RotateInUpLeft`, `RotateInUpRight`
`RotateOut`, `RotateOutDownLeft`, `RotateOutDownRight`, `RotateOutUpLeft`, `RotateOutUpRight`

- Slide
`SlideInLeft`, `SlideInRight`, `SlideInUp`, `SlideInDown`
`SlideOutLeft`, `SlideOutRight`, `SlideOutUp`, `SlideOutDown`

- Zoom
`ZoomIn`, `ZoomInDown`, `ZoomInLeft`, `ZoomInRight`, `ZoomInUp`
`ZoomOut`, `ZoomOutDown`, `ZoomOutLeft`, `ZoomOutRight`, `ZoomOutUp`

------------


