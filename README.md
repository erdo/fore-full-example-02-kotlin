# Full MVO app example using [fore](https://erdo.github.io/android-fore/) - (kotlin version)


[video](https://www.youtube.com/watch?v=t35CmCNYsyM) \|
[playstore listing](https://play.google.com/store/apps/details?id=foo.bar.example.fore.fullapp01) \| [source code](https://github.com/erdo/fore-full-example-02-kotlin)

A larger multi-page app written in Kotlin that demonstrates the use of the android fore library. The source includes details that are not present in the example apps bundled with the library, but would be necessary for a real app - one way of handling user messaging for example.

There is a lot going on in the UI of this app, the point is to demonstrate how you can support complex UIs whilst still maintaining a tight and robust code base underneath.

- Once you've logged in (with a fake email address and password, but the app still connects to an "authentication" server over the network), you are presented with three tabs.

- The first is a **fruit collector** where you click Go to retrieve fruit over a network connection, this ridiculous example lets us demonstrate how to make multiple network connections and handle adapter updates even when a user is: mashing the buttons; rotating the device; and three different network connections can return to update the list at the same time.

- The second tab is a **basket** such as is discussed in the [documentation](https://erdo.github.io/android-fore/03-databinding.html#shoom). The model driving the basket view is locally scoped (using a androidx.lifecycle.ViewModel). As such, rotating the screen won't destroy the model, but navigating away from it will.

- The third tab is a **todo** list which lets you add todo items and check them off as done.

- The **bottom navigation bar** in this app has badges added to it so that we can demonstrate how the observer pattern can be used to automatically update parts of the UI as appropriate. For example, try fetching some fruit, then change tabs, then rotate the device - fore Observers will ensure that the badge number remains consistent, even after the fruit network request has returned with new fruit.

- There is a permissions implementation you might want to steal, that pushes a lot of the boiler plate code out of the UI layer (using kotlin delegation)

- The DI is straight forwardly implemented with Koin

- The app uses a mix of techniques at the UI layer. eg: putting most of the view code in the Activity class, putting most of it in the Fragment class, and putting most of it in a custom View class, so you can compare the different ways of achieving the same thing. All the apps use the fore data binding technique
for very lightweight ui layer code anyway


> "fore Observers will ensure that the badge number remains consistent"


![image](https://raw.githubusercontent.com/erdo/fore-full-example-02-kotlin/master/screenshot_full_fore_02_phone_all_1000.png)


## Main Library Choices

Networking - Retrofit and OKHttp<br/>
DI - Koin<br/>
ViewModels - androidx.lifecycle.ViewModel<br/>
databinding - early.fore<br/>


## Dependency Injection

The app in the master branch is implemented using Koin DI. A version using Dagger 2 is in another branch. Previously there was a pure DI version that worked
perfectly well without any DI library (check the small app samples in the main fore repo for examples of that). More discussion on DI [here](https://erdo.github.io/android-fore/05-extras.html#dependency-injection-basics)

For examples of testing, please see the [sample apps](https://erdo.github.io/android-fore/#sample-apps) included with the fore library which are tested to within an inch of their lives.


## License


    Copyright 2017-2019 early.co

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
