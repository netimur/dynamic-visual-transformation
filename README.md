### DynamicVisualTransformation
A lightweight Kotlin library for Jetpack Compose that applies dynamic input masks and visual text transformations to `TextField`s. It supports custom templates, placeholders, and styling for unfilled characters, making user input more structured and user‑friendly.

- Works great for phone numbers, dates, credit cards, postal codes, and other formatted inputs
- Customizable template symbols and placeholders
- Automatic cursor/offset mapping so caret position feels natural while typing
- Simple, Compose‑first API

---

### Demo
<img src="https://github.com/user-attachments/assets/7f4cd04a-87e9-4344-bce8-cb196e693315" alt="DynamicVisualTransformation" style="width:30%; height:auto;">

---

### Installation
[![](https://jitpack.io/v/netimur/DynamicVisualTransformation.svg)](https://jitpack.io/#netimur/DynamicVisualTransformation)

#### 1) Add JitPack repository
Add the following to your `settings.gradle.kts` inside the `dependencyResolutionManagement` block:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

#### 2) Add the dependency
Replace `Tag` with the latest version from the badge above.

```kotlin
dependencies {
    implementation("com.github.netimur:dynamic-visual-transformation:Tag")
}
```

---

### Quick start
```kotlin
@Composable
fun PhoneInput(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    val phoneVisualTransformation = remember {
        DynamicVisualTransformation(
            template = "+7(%%%) %%%-%%-%%",
            templateSymbol = '%',
            placeholder = DynamicPlaceholder(
                text = "+7(   )    -  -  "
            )
        )
    }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier,
        visualTransformation = phoneVisualTransformation
    )
}
```

What it does:
- Uses `template` to define where user characters go (`%` marks input positions)
- Displays a `placeholder` for remaining positions
- Automatically keeps the cursor in the expected spot while typing or deleting

---

### Concepts
- `template`: A mask string that mixes static characters with input slots.
  - Example: `"#### #### #### ####"` with `templateSymbol = '#'` for credit cards.
- `templateSymbol`: The character in `template` that represents an input slot.
- `placeholder`: Optional visual hint for unfilled input (can be plain text or styled).

---

### More examples

#### Credit card
```kotlin
val cardMask = remember {
    DynamicVisualTransformation(
        template = "#### #### #### ####",
        templateSymbol = '#',
        placeholder = DynamicPlaceholder("____ ____ ____ ____")
    )
}
```

#### Date (MM/YY)
```kotlin
val dateMask = remember {
    DynamicVisualTransformation(
        template = "%%/%%",
        templateSymbol = '%',
        placeholder = DynamicPlaceholder("MM/YY")
    )
}
```

#### Custom placeholder styling (optional)
If `DynamicPlaceholder` supports styling (e.g., color or span style), you can provide it. Example shape:
```kotlin
val styled = remember {
    DynamicVisualTransformation(
        template = "+1 (%%%) %%%-%%%%",
        templateSymbol = '%',
        placeholder = DynamicPlaceholder(
            text = "+1 (   )    -    ",
            // e.g., color = Color.Gray, style = SpanStyle(...)
        )
    )
}
```

---

### Tips and best practices
- Keep the number of `templateSymbol` occurrences equal to the maximum expected input length.
- Validate raw input separately (e.g., strip non‑digits if needed) before submission.
- Use `remember` to avoid recreating the transformation on every recomposition.
- Test deletion and mid‑string edits—offset mapping is handled automatically, but your own `onValueChange` logic should remain simple.

---

### API overview (simplified)
- `DynamicVisualTransformation(
    template: String,
    templateSymbol: Char,
    placeholder: DynamicPlaceholder? = null
)`
- `DynamicPlaceholder(
    text: String /* plus optional styling parameters if available */
)`

Result can be passed to `TextField` or `OutlinedTextField` via `visualTransformation`.

---

### Compatibility
- Language: Kotlin
- UI: Jetpack Compose (`TextField` / `OutlinedTextField`)

---

### License
This library is distributed under the terms of its repository license. See the project’s `LICENSE` file for details.
