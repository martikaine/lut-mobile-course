# Learning diary

## Exercise 1

I started following along the tutorial but quickly realized that it was quite out of date. Since I wanted to learn with the most up-to-date tooling I decided to adapt the tutorial to Kotlin (now the default for new Android Studio projects) and Jetpack Compose for creating the UI. There was definitely a steeper initial learning curve and some additional Googling at the start, but I much preferred the Jetpack Compose way of creating the UI over the old way shown in the video. The main ideas of composition and state management reminded me quite a lot of React, which I'm already pretty well versed in.

In Jetpack Compose, instead of defining an XML layout and then hooking into the components in code with `findViewById()`, the components are defined as Kotlin functions with an `@Composable` annotation, which a compiler plugin transforms to Android UI elements. A composable function may define some component-level logic, and ends with a tree of composables that should be rendered upon calling the function. Components can store mutable state variables with the `remember` helper function (very reminiscent of React's `useState()`), and when a state variable updates, the subtree of components using that state variable refreshes. In this example application, the state holding the values of the two `NumberTextField`s had to be hoisted upwards into their first common ancestor component `Calculator`, so that they could be added together.

## Exercise 2

Again, I adapted the tutorial for Jetpack Compose. In Compose, navigation between activities (really just composables) is handled by creating a `NavHost` for defining routes and a `NavController` for navigating between them, which again reminded me a lot of something like `react-router` in the React ecosystem. I placed the two "activities" `MainButtons` and `SecondActivity` inside the NavHost as follows:

```
NavHost(navController = navController, startDestination = "main_buttons") {
    composable("main_buttons") {
        MainButtons(onSecondActivityClick = {
            navController.navigate("second_activity/$textForSecondActivity")
        })
    }
    composable("second_activity/{text}") { backStackEntry ->
        SecondActivity(text = backStackEntry.arguments?.getString("text"),
            onGoBackClick = { navController.navigate("main_buttons") })
    }
}
```

Arguments between activities can be passed via the route string, and retrieved via the `backStackEntry` argument in the `composable` function.

For launching the Google webpage, I found that `LocalUriHandler` simplifies the process quite a lot: a simple `uriHandler.openUri()` opens the browser without a need to check for activity success etc.. I also found a `rememberLauncherForActivityResult` function which could be used to create an activity launcher, which could be defined with a callback that handles the result from the activity, but in the case of opening a browser the results were not important.

## Exercise 3

Creating the list was very easy in Compose, and this was advertised as one of the big improvements over the old way in the Compose docs. Instead of using an Adapter as in the video, I used `LazyColumn` to create the list items. `LazyColumn` functions much like the `Column` layout composable, but renders items only when they are on the screen, allowing optimized rendering of large lists in a straightforward mannter. Each list item was simply a `Row` with a `Modifier.clickable()` applied, which allowed specifying a click handler that then navigated to another view using the `NavController` (as in the previous exercise).

I tried to scale the images down with the `Image` composable's builtin `contentScale`, but that resulted in out of memory errors as the full image was loaded before the scaling was applied. I ended up adapting the `BitmapFactory` code from the tutorial, using a different approach I found on StackOverflow to get the screen width for scaling.

## Final project

For the final project, I wanted to create a simple todo list application, which I thought to be simple enough to build but somewhat more complicated than the examples so far, requiring things such as a database. I found a tutorial from Android documentation at https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#0 and adapted it to my needs. The tutorial uses Room, which is a wrapper on top of the popular SQLite database built directly into Android Jetpack.

### Database with Room

First, to make Room work, some dependencies need to be added to the app's `build.gradle` file. Room works off of a Kotlin compiler plugin, which auto-generates the database-generating/reading/updating code based on class annotations found in the source. This caused me quite a bit of headaches, as there were multiple conflicting sources of information on setting it up, the most current way of doing things (KSP plugin) didn't seem to work for me no matter what, and Gradle's error messages were very cryptic. Thankfully a StackOverflow answer provided the right lines to add to the Gradle files but there was zero explanation added, so now it works and I'll try not to touch it, I guess. ¯\\\_(ツ)\_/¯

To read and write into a Room database, an Entity must be created by annotating a class with `@Entity`. The Entity defines the data structure of a database table. Then, a DAO (Data Access Object) is created, defining methods that read, modify and delete this table using the Entities. Finally, a class extending from `RoomDatabase` must be created to define access to the database, including all related Entities and DAOs. This class follows the Singleton pattern, ensuring that only one Database is instantiated throughout the application.

The tutorial also recommends creating a Repository, which can be used as an additional abstraction layer to decouple the application logic from the data source: one could create multiple repository implementations, of which one reads from a text file, another from the database, another from a JSON API, etc. without affecting the business logic of the app. Since this application only has one data source which is unlikely to change during the course of development, I decided to omit this layer.

Finally, an AppContainer class needs to be created as a dependency injection container, which instantiates the classes as the application is loaded, allowing the rest of the application to grab them from the container as needed.

### MVVM architecture

As recommended in the tutorial, I adopted a ViewModel pattern for holding UI state. A common pattern in many UI frameworks, ViewModels are particularly needed in Compose because they store UI state across configuration changes, such as changing the screen orientation. The pattern also neatly separates the view's business logic from the presentation logic.

In my app's home screen, the `HomeScreenViewModel` loads all `TodoItems` using the `TodoDao` as it is instantiated, and exposes them as a `Flow<List<TodoItem>>` to the view. ([`Flow`](https://kotlinlang.org/docs/flow.html) is a Kotlin feature that represents a stream of values loaded asynchronously, which allows the application to keep executing even if a query is slow to load.) Room automatically refreshes this Flow as the data changes, and the view tracks these changes with the `collectAsState()` function, automatically updating the view. In addition, this view model exposes the functionality to mark items as completed and delete them, as these actions are also available from the home screen.

The "create new item" screen has its own view model to keep concerns separated. This model uses the same database table and DAO as the other screen, but only exposes the functionality of inserting with the `createTodoItem()` method, as this is all the functionality needed on this screen. It also holds state from the TextFields within the view, as this way the text isn't lost if the device orientation changes.

A `ViewModelFactory` was required to instantiate all viewModels with their correct dependencies, allowing each view to request for them as needed. The tutorial's example code was a great help in getting it running.

### Animations

I wanted to add small animations to smoothen out state changes, and this was quite easy to do with Compose. In the home screen's `LazyColumn` list view, I wanted the completed todo items to swap to the bottom of the list. All I had to do was to sort the SQL query that loads the items with `ORDER BY`, and add the modifier `Modifier.animateItemPlacement()` to each direct descendant of the `LazyColumn`.

For smoothly desaturating the completed items, the `animateFloatAsState()` function was very helpful. When the value inside the function changes, it is updated to the UI state smoothly over a period of time, causing an animation to occur. For example:

```
val alpha by animateFloatAsState(if (toDoItem.isCompleted) 0.5f else 1f)

Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
            alpha = alpha
        )
    )
) { }
```

There was also a similar `animateColorAsChange()` function, but desaturation looked better to me.
