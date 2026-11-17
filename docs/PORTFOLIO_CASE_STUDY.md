# Portfolio Case Study — Neon Tip Calculator

## 📌 Executive Summary
**Neon Tip Calculator** is a modern Android application engineered to solve common pain points when splitting group bills and calculating tips. Built from the ground up using **Kotlin**, **Jetpack Compose**, **Material3**, and **MVVM Architecture**, the project showcases best-in-class Android development standards.

---

## 🎯 Problem Statement
Most utility tip calculator apps on the Play Store suffer from:
1. **Precision Anomalies**: Inaccurate calculations caused by IEEE 754 floating-point numbers (`Double`/`Float`).
2. **Poor Input Validation**: Crashing when users type multiple dots, empty values, or extremely large numbers.
3. **Lack of Customization**: Fixed tip chips without support for custom percentages, currencies, or history tracking.
4. **Outdated UIs**: Cluttered XML Views lacking dark theme optimization or adaptive tablet support.

---

## 💡 The Solution
A feature-rich, portfolio-grade application featuring:
- **Exact Money Math**: `BigDecimal` arithmetic ensuring exact two-decimal precision.
- **Multi-Currency & Custom Tip Slider**: Live currency switching (USD, EUR, GBP, BDT) and 0-50% custom tip slider.
- **Local Persistence**: Calculation history stored in a **Room Database** and user preferences saved via **DataStore**.
- **Modern UI/UX**: Dark neon aesthetic (`#00FF9C` & `#00D4FF`), custom animated radial canvas, haptic feedback, and adaptive two-pane layouts for tablets and landscape mode.

---

## 🛠️ Technical Decisions & Trade-Offs

| Component | Choice | Reason |
|---|---|---|
| **UI Toolkit** | Jetpack Compose (Material3) | Declarative, state-driven UI with automatic recomposition and reduced boilerplate. |
| **Architecture** | MVVM + StateFlow | Clean separation of UI and business logic with unidirectional data flow. |
| **Dependency Injection** | Dagger Hilt | Standardized Android DI with scope management (`@Singleton`, `@HiltViewModel`). |
| **Database** | Room Database | Compile-time query verification and native Kotlin Flow stream integration. |
| **Preferences** | DataStore Preferences | Safe asynchronous key-value storage powered by Coroutines. |

---

## 📊 Results & Key Takeaways
- **100% Pass Rate**: Unit tests covering ViewModel math logic, DataStore flow, and Room DAO operations pass successfully.
- **Optimized Release Build**: Configured R8 minification and resource shrinking (`isMinifyEnabled = true`), generating a compact production APK.
- **Full Accessibility & Localization**: Complies with Android 48dp touch target guidelines and includes complete English and Bangla translations.
