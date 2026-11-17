# Architecture Guide — Neon Tip Calculator

Welcome! This document explains how the Neon Tip Calculator app is structured in plain, simple English.

---

## 🏛️ Why MVVM Architecture?

**MVVM** stands for **Model-View-ViewModel**. It separates the app into three distinct layers so that the UI (View) remains clean and focused solely on drawing shapes and text, while the ViewModel handles business logic and math calculations.

### Benefits of MVVM:
1. **Unidirectional Data Flow (UDF)**: Data flows in a single circle:
   `User Action` -> `ViewModel Event` -> `Stateflow Update` -> `UI Recomposition`.
2. **Testability**: ViewModel and math calculations can be tested with Unit Tests without needing a phone emulator.
3. **Configuration Survival**: Screen rotation doesn't reset user input because the ViewModel survives configuration changes.

---

## 📂 Directory & File Breakdown

### 1. `ui/screens/tipcalculator/`
- **`TipCalculatorUiState.kt`**: An immutable data class holding every piece of data shown on screen (`billInput`, `selectedCurrency`, `totalAmount`, `amountPerPerson`, etc.).
- **`TipCalculatorViewModel.kt`**: The brain of the screen. Calculates exact amounts using `BigDecimal`, updates DataStore preferences, and saves history into Room.
- **`TipCalculatorScreen.kt`**: The Compose screen rendering controls based on `TipCalculatorUiState`. Automatically adapts between single-pane portrait and two-pane landscape layouts.

### 2. `data/`
- **`data/currency/CurrencyOption.kt`**: Enum defining USD ($), EUR (€), GBP (£), and BDT (৳).
- **`data/db/`**:
  - `TipHistoryEntity.kt`: Room database table columns for calculation history.
  - `TipHistoryDao.kt`: Database queries (insert, delete, query all).
  - `AppDatabase.kt`: Room database builder instance.
- **`data/preferences/UserPreferencesRepository.kt`**: Uses Android Preferences DataStore to persist currency, last tip %, and round-up toggle asynchronously.

### 3. `di/`
- **`DatabaseModule.kt`**: Hilt module providing singletons for Room database & DAO.
- **`PreferencesModule.kt`**: Hilt module binding `PreferencesRepository` interface.

---

## 🔄 Data Flow Walkthrough (When User Taps 20% Tip)

1. **User Action**: Taps the `20%` TipChip on screen.
2. **Event Trigger**: `TipChip` invokes `onClick` -> `onTipSelected(TipOption.TWENTY)`.
3. **ViewModel Processing**:
   - `TipCalculatorViewModel` receives `TipOption.TWENTY`.
   - Calls `BigDecimal` calculation helper to calculate new total and per-person amounts.
   - Asynchronously updates DataStore with last chosen tip percentage.
   - Emits a new updated copy of `TipCalculatorUiState` via `_uiState.update { ... }`.
4. **UI Recomposition**: `TipCalculatorScreen` collects `uiState` StateFlow and automatically re-renders the result card with spring bouncy progress ring animation!

---

## 🇧🇩 Banglish Summary (সংক্ষিপ্ত সারসংক্ষেপ)

> MVVM holo emon ekta pattern jekhane UI shudhu dekhanor kaj kore, r ViewModel shob hisab-nikash kore. Jetpack Compose-e amra StateFlow bebohar kori. UI-te user kichu click korle ViewModel-e event jay, ViewModel BigDecimal diye accurate bill + tip calculate kore notun State dey, r Compose UI auto update hoye jay!
