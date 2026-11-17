# Interview & Client Questions (Q&A)

Here are 15 questions clients or technical interviewers may ask about this project, with confident answers.

---

### Q1: Why did you use `BigDecimal` instead of `Double` or `Float` for money math?
**Answer**: `Double` and `Float` use IEEE 754 binary floating-point representation, which cannot represent certain decimal numbers accurately (e.g. `0.1 + 0.2 = 0.30000000000000004`). In financial apps, precision is mandatory. `BigDecimal` provides exact decimal representation with explicit rounding modes (`RoundingMode.HALF_UP` / `CEILING`).

### Q2: Why did you choose Jetpack Compose over traditional XML Views?
**Answer**: Jetpack Compose is Android's modern declarative UI toolkit. It reduces boilerplate code, eliminates view binding errors, provides seamless state-driven UI recomposition, and accelerates UI iteration.

### Q3: How do you prevent recomposition performance issues in Compose?
**Answer**: By using immutable data classes for `UiState`, passing stable lambdas, leveraging `remember` and `derivedStateOf`, and keeping composable functions pure without side effects inside the body.

### Q4: What architecture pattern does this project follow?
**Answer**: It follows **MVVM (Model-View-ViewModel)** with **Unidirectional Data Flow (UDF)**. UI emits user events to ViewModel, ViewModel updates StateFlow, and UI observes StateFlow to re-render.

### Q5: How do you handle screen rotation without losing input data?
**Answer**: ViewModel survives configuration changes (like rotation) automatically. The UI state is held in ViewModel's `StateFlow`, so when `TipCalculatorScreen` re-subscribes upon rotation, it receives the exact active state.

### Q6: Why did you use DataStore Preferences instead of SharedPreferences?
**Answer**: `DataStore` is asynchronous, uses Kotlin Coroutines and Flow, handles thread safety on `Dispatchers.IO`, and eliminates runtime crashes caused by synchronous `SharedPreferences.apply()` or `.commit()`.

### Q7: Why did you use Room for calculation history?
**Answer**: Room provides an abstraction layer over SQLite with compile-time SQL query validation and native Kotlin Flow support, allowing the UI to automatically react whenever history entries are saved or deleted.

### Q8: What dependency injection framework is used and why?
**Answer**: Dagger Hilt. Hilt reduces Dagger boilerplate by providing predefined components (`SingletonComponent`, `ActivityComponent`) and standard annotations (`@HiltAndroidApp`, `@HiltViewModel`).

### Q9: How do you handle input sanitization for bill amounts?
**Answer**: The input text is filtered to allow digits and at most one decimal point (`.`), limits integer digits to 9, limits decimal places to 2, and strips invalid leading zeros.

### Q10: How does the app adapt to tablets and wide screen landscape?
**Answer**: `TipCalculatorScreen` checks `LocalConfiguration.current.orientation` and screen width (>=600dp). On wide screens, it switches from a single vertical column to an adaptive two-pane `Row` layout.

### Q11: How do you handle localization?
**Answer**: All user-facing strings are stored in `strings.xml`. Bangla translations are added in `res/values-bn/strings.xml`, enabling automatic language switching based on device locale.

### Q12: What accessibility features are implemented?
**Answer**: All icons and interactive controls include `contentDescription`, touch targets meet the minimum 48dp guideline, semantic roles (`RadioButton`, `Button`, `Switch`) are assigned, and TalkBack reads formatted state values.

### Q13: How did you implement the "Round Up Per Person" feature?
**Answer**: When enabled, the calculated `perPersonAmount` is rounded up to the nearest whole integer using `perPerson.setScale(0, RoundingMode.CEILING)`.

### Q14: How does the native share feature work?
**Answer**: It uses Android's native `Intent(Intent.ACTION_SEND)` with MIME type `"text/plain"`, wrapped in `Intent.createChooser()`.

### Q15: How are release builds optimized?
**Answer**: R8 minification and resource shrinking (`isMinifyEnabled = true`, `isShrinkResources = true`) remove unused code and resources, reducing APK size significantly.
