### DynamicVisualTransformation
**DynamicVisualTransformation** is a lightweight Kotlin library for Jetpack Compose that allows you to create dynamic input masks and visually transform text in ```TextFields```. It supports custom templates, placeholders, and styling for unfilled characters, making user input more structured and user-friendly.
Features
Apply dynamic visual transformations to TextFields in Jetpack Compose.
Define custom templates using a template symbol.
Show placeholders for unfilled input with optional coloring.
Handles cursor offset mapping automatically.
Ideal for phone numbers, dates, credit cards, or any custom formatted input.

#### Example
<img src="https://github.com/user-attachments/assets/7f4cd04a-87e9-4344-bce8-cb196e693315" alt="DynamicVisualTransformation" style="width:50%; height:auto;">

#### Usage
##### PhoneNumber input example
``` kotlin
@Composable
fun PhoneInput(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    val phoneVisualTransformation = remember {
        DynamicVisualTransformation(
            template = "+7(%%%) %%%-%%-%%",
            templateSymbol = '%',
            placeholder = DynamicPlaceholder("+7(   )    -  -  ")
        )
    }

    TextField(
        value = text,
        modifier = modifier,
        onValueChange = { text = it },
        visualTransformation = phoneVisualTransformation
    )
}
```

# DynamicVisualTransformation

[![](https://jitpack.io/v/netimur/DynamicVisualTransformation.svg)](https://jitpack.io/#netimur/DynamicVisualTransformation)

## How to add the library

### Step 1. Add JitPack repository
Add the following in your `settings.gradle.kts` at the end of the `repositories` block:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

### Step 2. Add dependenct
dependencies {
    implementation("com.github.netimur:DynamicVisualTransformation:Tag")
}
