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
