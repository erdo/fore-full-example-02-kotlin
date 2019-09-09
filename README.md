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


> "fore Observers will ensure that the badge number remains consistent"


![image](https://raw.githubusercontent.com/erdo/fore-full-example-02-kotlin/master/screenshot_full_fore_02_phone_all_1000.png)


## Main Library Choices

Networking - Retrofit and OKHttp
DI - Koin
DB - Room
ViewModels - androidx.lifecycle.ViewModel
databinding - early.fore


TODO...


## Dependency Injection

The app in the master branch is implemented using a pure DI solution. An identical app that uses Dagger 2 for dependency injection is in [this feature branch](https://github.com/erdo/fore-full-example-01-kotlin/tree/feature/dagger-version/app/src/main/java/foo/bar/example/fore/fullapp01).

The pure DI version is less boiler plate and (arguably) quite a lot easier to follow than the Dagger version, hence it's in master. I haven't added a bunch of tests to this app but I don't expect that to make any difference, both solutions are equally testable (as they both implement dependency injection).

For examples of testing, please see the [sample apps](https://erdo.github.io/android-fore/#sample-apps) included with the fore library which are tested to within an inch of their lives and also use pure DI. More discussion on DI [here](https://erdo.github.io/android-fore/05-extras.html#dependency-injection-basics).


### master branch using pure DI

http://cloc.sourceforge.net v 1.62  T=0.65 s (102.5 files/s, 6099.3 lines/s)

| Language  | files  | blank  | comment | code |
|:----------|-------:|-------:|--------:|-----:|
| Kotlin    | 42     | 633    | 381     | 1566 |
| XML       | 25     | 151    | 9       | 816  |
| SUM:      | 67     | 784    | 290     | 2382 |




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
