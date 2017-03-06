#un-material-tab library
A customizable alternative of TabLayout from Support Design library which provides almost the same functionality.

<img src="https://github.com/creageek/un-material-tabs/blob/master/resources/sample.png" width="360">

##How to add?
I. In your `build.gradle` file add the following dependency:
```
dependencies {
    compile 'com.ruslankishai:unmaterialtabs:0.1a'
}
```
II. Declare `RoundTabLayout` inside your `layout.xml` file:
```xml
<com.ruslankishai.unmaterialtab.tabs.RoundTabLayout
            android:id="@+id/round_tabs"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/colorPrimary"
            app:accent="@color/colorAccent" />
```
III. Declare `RoundTabLayout` with `ViewPager` in your Java class:
```java
ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

//set adapter to your ViewPager
ViewPager.setAdapter(new TabPagerAdapter(getFragmentManager()));

RoundTabLayout tabLayout = (RoundTabLayout) findViewById(R.id.roundTabLayout);
tabLayout.setupWithViewPager(viewPager);
```
IV. Override you `getPageTitle` method in your ViewPager’s adapter to return tab title.
```java
@Override
public CharSequence getPageTitle(int position) {  
     switch (position) {
          case 0:
               return "First item";
          ...
     }
}
```
##How to customize?
I. In your `layout.xml` file you can set a few attributes to `RoundedTabLayout`:
* `android:background` 
    * This attribute works just like usual `background` attribute (reference to a color).
* `app:accent`
    * This attribute allows you to set accent color for tab selection, stroke and text (reference to a color).
* `app:cornerRadius`
    * This attribute sets the tab corner radius. Possible values are:
        * `circle` (default)
        * `rounded`
        * `rectangle`
        * value in px from `0` to `50`
        
II. In your `class.java` you can set some values to customize specific tab:
* Use `RoundTab#setIcon` method to change icon. You can get `RoundTab` object via `RoundTabLayout#getTab` method which accept tab index as parameter. To enable tab icon, you should also use `RoundTab#setHasIcon`.
```java
…
//after initializing RoundTabLayout and ViewPager
RoundTab tab = tabLayout.getTab(0);
Drawable icon = getResources().getDrawable(R.drawable.globe);
tab.setIcon(icon);
//enable icon in current tab
tab.setHasIcon(true);

//repeat this code for another tabs
…
```
* To disable tab stroke use `RoundTab#setHasStroke` method.
```java
…
//after initializing RoundTabLayout and ViewPager
RoundTab tab = tabLayout.getTab(0);
tab.setHasStroke(true);
…
```
* To change corners radius use `RoundTab#setCornerRadius` which accept values from `0` to `50` as a parameter.
```java
…
//after initializing RoundTabLayout and ViewPager
RoundTab tab = tabLayout.getTab(0);
tab.setCornerRadius(35);
…
```
* To change tab text use `RoundTab#setText` which accept `String` as a parameter.
```java
…
//after initializing RoundTabLayout and ViewPager
RoundTab tab = tabLayout.getTab(0);
tab.setText(“Usage example”);
…
```
##Where is un-material-tab demo app?
The `app` works just as an example of usage with different options. Will be uploaded to Play Store sooner.

<img src="https://github.com/creageek/un-material-tabs/blob/master/resources/circle.gif" width="360">

<img src="https://github.com/creageek/un-material-tabs/blob/master/resources/rounded.gif" width="360">

<img src="https://github.com/creageek/un-material-tabs/blob/master/resources/custom_corners.gif" width="360">

<img src="https://github.com/creageek/un-material-tabs/blob/master/resources/custom_tab.gif" width="360"_tab>

##What about contributions?
This is my first public repo and first library so I’m trying to keep this code as much clean and well-commented as I can.
Feel free to contribute :)

##License
```
Copyright (C) 2015 Said Tahsin Dane

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

