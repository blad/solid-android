Sample Android Project | Android Studio + Otto, Dagger and ButterKnife [!["Build Status"](https://travis-ci.org/blad/android-studio-example-project.svg?branch=master)](https://travis-ci.org/blad/android-studio-example-project)
===================================================================================================================================================

This is a simple example project that makes use of:

- [Android Studio - Android Development Environment](https://developer.android.com/sdk/installing/studio.html)
- [Otto - Event Bus Library](http://square.github.io/otto/)
- [ButterKnife - View Injection Library](http://jakewharton.github.io/butterknife/)
- [Dagger - Dependency Injection Library](http://square.github.io/dagger/)
- [Travis CI - Continuos integration platform](https://travis-ci.org/)

## Android Studio & Gradle

This project was developed in Android Studio and all build configurations are
based on the Gradle Build System which is the newest standard for Android Projects.

### Setting up the project after cloning from a repository

From Android Studio simply choose to import and select the `build.gradle` in the
root directory of the repository. Android Studio will set everything else up
automatically.

### Summary of Dependencies

The dependencies are all declared in terms of **Group ID**, **Artifact ID**, and
**Version** number in `app/build.gradle`. The dependencies will then be downloaded
from [Maven Central](http://search.maven.org/) or [BinTray](https://bintray.com/).

The downloaded dependencies will be available automatically as part of the project.

Jar file dependencies can also be included. The configuration for this project
looks inside `app/libs` for `*.jar` files and includes them for all build types.


*Example Dependency Declarations:*
```
dependencies {
    compile 'com.squareup:otto:1.3.5' // Event Bus Library
    // Group ID: com.squareup
    // Artifact ID: otto
    // Version: 1.3.5, can also specify minimum version via plus symbol eg:1.3.+

    // Include Any jar dependencies
    compile fileTree(dir: 'libs', include: ['*.jar'])

    // Include only for Android Tests
    androidTestCompile 'com.jayway.android.robotium:robotium-solo:5.1'
    //... more dependencies
}
```


## Otto: Event Bus Library
[Otto](http://square.github.io/otto/) is an event bus library that aims to provide
a mechanism for components in an Android application to remain decoupled, while
still being able to communicate effectively.

### How Otto Works
Otto provides a `@Subscribe` annotation that allows instances to subscribe to a
posts of a particular type, which are specified by the function parameters type.

The following function will react to posts of type `CustomEventABC`
```Java
  //... inside some class
  @Subscribe public void onCustomEventOccurred(CustomEventABC event) {
    // ... do something magical
  }  
```

Where the class `CustomEventABC` is defined as:

```
class CustomEventABC {
  // ... Some Magical Custom Implementation, No Base Class Needed
}
```

And the method above annotated with `@Subscribe` is invoked after a call to:
```
  busInstance = BusFactory.getInstance(); // Roll Your Own Singleton to Share a Bus
  busInstance.post(new CustomEventABC());
```

This allows your components to communicate via a single instance of a `Bus`, or have
a set of components communicate via their own `Bus`'s.

**This reduces the need for a components to define interfaces to communicate,
bus will pass the posted objects to all subscribers expecting that object type.**

It is common to want to have any new subscribers receive the last broadcast object,
for that there is the `@Produce` annotation. It must simply return the type of object
which we want to produce for the new subscriber.

```
  //... in some class
  @Produce public CustomEventABC produceLastCustomEventABC() {
    // If this held a value, it would hold the same value as the last posted
    // message of this type.
    return new CustomEventABC();
  }
```

All Subscribers and Producers must register themselves via the `register` method,
and can unregister themselves via the `unregister` method.

### How Otto Works in This Sample Project
This project uses Otto to communicate between fragments, and uses the containing
activity as the "Producer" for any new subscribers.

The Container Activity and Fragments register themselves with Otto, and when a
button is clicked in the fragment we `post` an object we defined to the Bus.

## Butter Knife View Injection

[Butter Knife](http://jakewharton.github.io/butterknife/) is a view injector library
that generates boilerplate that is normally associated with assigning view references
to variables.

### How Butter Knife Works
The simplest way to use Butter Knife is to use the `@InjectView` annotation
before a variable that would normally store a reference to a view object, and then
call the `ButterKnife.inject` method to inject views into the class.

*Simple Example of Using View Injection; Simply use `ButterKnife.inject` where
`findViewById` would normally be used.*
```
class SimpleActivity extends Activity {
  @InjectView(R.id.title) TextView title;
  @InjectView(R.id.subtitle) TextView subtitle;
  @InjectView(R.id.footer) TextView footer;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    ButterKnife.inject(this);
    // Injected Views Ready for Use after This Point...
  }
}
```

Butter Knife does code generation at compile time, so there is no use of reflection
at RunTime.

More code examples can be found at: [jakewharton.github.io/butterknife/](http://jakewharton.github.io/butterknife/)

### How This Sample Project Uses Butter Knife

The use of ButterKnife in this sample project is very minimal, it is used to inject
views into Fragments, and although the gain is not apparent in this example,
large project could potentially benefit from it's use.

## Dagger Dependency Injector

### About Dependency Injection

### How This Sample Project Uses Dagger

## Continuous Integration

### TravisCI && CircleCI

### Jenkins CI
