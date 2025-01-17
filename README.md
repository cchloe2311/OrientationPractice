# OrientationPractice
안드로이드 화면전환 관련 연습 레포지토리 입니다 ! 🤓

screen orientation, keyboard availability, and language 이런 configure이 런타임에 변경이 발생하면 안드로이드는 액티비티나 프래그먼트를 destroy하고 recreate함(Android does this so that application can reload its resources based on the new configuration)

-> 따로 제어하지 않는다면 input이 지워지거나 다시 통신을 하는 일이 발생함

그렇다면, 어떻게 대처해야할까?

-> 안드로이드는 콜백을 제공함. callbacks to save your application state before destroying either Activity or Fragment.

orientation이 변경되었을 때 이 callback 안에서는 다음 옵션들을 선택할 수 있음
1. Lock screen orientation
2. Prevent Activity to recreated
3. Save basic state
4. Save complex objects

## Lock screen orientation
menifest에서 해당 액티비티에 screenOrientation 값을 설정해줌
## Prevent Activity to recreated
recreate 되는걸 막아 뷰 내 데이터들을 그대로 유지하는 방법임

-> setting the android:configChanges flag on your Activity in AndroidManifest.xml

이렇게 하면 lifecycle 내 onConfigurationChanged(Configuration) 콜백을 호출함

따라서, orientation에 따라 다른 layout을 보여주고 싶다면 여기서 바꿔야함

근데 android:configChanges attribute 이걸 사용하는건 추천하지 않음..!!! => 왜일까? (https://stackoverflow.com/questions/37787042/why-using-androidconfigchanges-is-a-bad-practice)
## Save basic state
Primitive data such as String, Boolean, Integers or Parcelable objects in a Bundle during the orientation change and read the same data when Activity recreated.

-> save, restore 데이터는 라이프사이클 내 onSaveInstanceState() and onRestoreInstanceState() 이 콜백에서 진행됨

onSaveInstanceState() 콜백에서 key-value 형태로 값들을 번들에 저장하셈

onRestoreInstanceState() 콜백에서 번들 내 값들을 가져오셈
## Save complex objects
Manage complex Object inside a Retained Fragment

원래 프래그먼트는 부모 액티비티와 함께 destroy, recreate 됨

근데 setRetainInstance(true) 이걸 프래그먼트 내에서 호출하면 destroy, recreate 되는걸 막고 retain the current instance of the fragment when the activity is recreated.

이렇게 하려면 다음과 같이 하면됨 !!
1. Extend the Fragment class and declare references to your stateful objects.
2. Call setRetainInstance(boolean) when the fragment is created.
3. Add the fragment to your activity.
4. Use FragmentManager to retrieve the fragment when the activity is restarted.

+) In order to proactively remove the retained worker fragment when you no longer need it, you may check for isFinishing() in onPause() in the activity.

+) 이 프래그먼트는 유지되기 때문에, 액티비티에 대한 참조를 하지 않는게 좋음. configure가 변경되는 경우 기존 context는 없어지고 새로운 context가 생성되는데 프래그먼트 내에서 뷰에 대한 참조를 유지하면 old context가 GC되지 않음 !!! 만약 참조해야한다면 cleaning up 코드까지 꼭 짜줘야 

+) 디바이스 orientation이 AsyncTask가 동작중이 바뀐다면?

IllegalArgumentException이 발생함. 뷰가 윈도우에 안붙고 leak이 일어남

sol1) use IntentService along with BroadCastReceiver to deliver result.

sol2) run the AsyncTask inside worker Fragment. As explained above using fragments is the cleanest way to handle configuration changes because Fragment has the ability to retain their instances simply by calling setRetainInstance(true) in one of its callback methods.

참조

https://medium.com/android-news/handling-orientation-changes-in-android-7072958c442a
https://developer.android.com/guide/topics/resources/runtime-changes.html

+)
https://www.geeksforgeeks.org/screen-orientations-in-android-with-examples/
이런 방식으로 어떤 화면에 접근했을 때 그 화면 orientation을 설정할 수 있음

+) configChanges를 신중히 사용해야하는 이유

주의: 구성 변경을 직접 처리하면 대체 리소스를 사용하는 것이 훨씬 더 까다로워질 수 있습니다. 시스템이 개발자 대신 자동으로 이를 적용해주지 않기 때문입니다. 이 기법은 구성 변경으로 인한 재시작을 반드시 피해야만 하는 경우 최후의 수단으로서만 고려해야 하며 대부분 애플리케이션에는 권장하지 않습니다.

For example, one of these reasons is when your app is in the background and the OS decides to kill it (with your Activity, of course) to reclaim memory.

When you return to your app, the OS will try to recreate your Activity as you left it, but will fail to do so, because you decided not to bother with it, just used android:configChanges in your Manifest.

If you make sure your app can recover properly from a restart, android:configChanges might not be necessary at all. Because of this, the need to use android:configChanges might indicate some flaw in your app, that may worth to take a look at.
