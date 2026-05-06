# NammaMistri — Construction Site Management App
### Project Documentation

---

## Table of Contents

- [NammaMistri — Construction Site Management App](#nammamistri--construction-site-management-app)
    - [Project Documentation](#project-documentation)
  - [Table of Contents](#table-of-contents)
  - [1. Project Overview](#1-project-overview)
  - [2. Introduction](#2-introduction)
  - [3. System Requirements](#3-system-requirements)
    - [3.1 Functional Requirements](#31-functional-requirements)
      - [Table 3.1: Functional Requirements Traceability Matrix](#table-31-functional-requirements-traceability-matrix)
    - [3.2 Non-Functional Requirements](#32-non-functional-requirements)
      - [Table 3.2: Non-Functional Requirements Validation](#table-32-non-functional-requirements-validation)
    - [3.3 Hardware and Software Requirements](#33-hardware-and-software-requirements)
      - [Table 3.3: Hardware and Software Requirements](#table-33-hardware-and-software-requirements)
  - [4. System Design](#4-system-design)
    - [4.1 Architecture Overview](#41-architecture-overview)
      - [Table 3.4: Architecture Component Summary](#table-34-architecture-component-summary)
    - [4.2 Database Schema](#42-database-schema)
      - [Table 3.5: Database Entity Summary](#table-35-database-entity-summary)
    - [4.3 Project Structure](#43-project-structure)
  - [5. Implementation](#5-implementation)
    - [5.1 Calculator Module](#51-calculator-module)
      - [Table 5.1: Calculator Module Details](#table-51-calculator-module-details)
    - [5.2 Labor Team Module](#52-labor-team-module)
      - [Table 5.2: Labor Module Features](#table-52-labor-module-features)
    - [5.3 Photo Management Module](#53-photo-management-module)
      - [Table 5.3: Photo Module Details](#table-53-photo-module-details)
    - [5.4 Material Rates Module](#54-material-rates-module)
      - [Table 5.4: Material Rates Module Details](#table-54-material-rates-module-details)
    - [5.5 UI/UX Design](#55-uiux-design)
      - [Table 5.5: UI Implementation Summary](#table-55-ui-implementation-summary)
  - [6. Results and Outcomes](#6-results-and-outcomes)
    - [Application Deliverables](#application-deliverables)
      - [Table 6.1: Project Quantitative Outcomes](#table-61-project-quantitative-outcomes)
    - [Technical Achievements](#technical-achievements)
  - [7. Challenges and Solutions](#7-challenges-and-solutions)
  - [8. Future Enhancements](#8-future-enhancements)
  - [9. Bibliography](#9-bibliography)

---

## 1. Project Overview

**App Name:** NammaMistri
**Platform:** Android (API 24+)
**Language:** Kotlin
**Architecture:** MVVM (Model-View-ViewModel)
**UI Framework:** Jetpack Compose with Material3

NammaMistri is a construction site management Android application designed to help contractors, site supervisors, and small construction business owners manage their day-to-day site operations. The app provides tools for material estimation, labor tracking, site photo documentation, and material rate management — all in one offline-first mobile application.

---

## 2. Introduction

Managing a construction site involves juggling multiple responsibilities simultaneously: tracking how many workers showed up, calculating how much material is needed, recording advance payments, and documenting site progress with photos. For small contractors and site supervisors in India, this is often done on paper or through fragmented tools, leading to errors, disputes, and inefficiencies.

NammaMistri addresses this problem by consolidating all these workflows into a single Android application that works entirely offline, stores all data locally on the device using a Room database, and provides an intuitive touch-first user interface built with Jetpack Compose.

The application is structured around four core modules, each accessible from a persistent tab navigation bar:

1. **Calculator** — Estimates bricks, cement bags, and sand required for a wall given its dimensions.
2. **Team** — Manages workers, records daily attendance, tracks advance payments, and calculates outstanding wages.
3. **Photos** — Captures and stores site progress photographs linked to the project.
4. **Rates** — Maintains a list of current market rates for construction materials.

The app was developed following modern Android development best practices including MVVM architecture, reactive UI with Kotlin Flow, Room for local persistence, Kotlin Coroutines for asynchronous operations, and Jetpack Compose for the UI layer.

---

## 3. System Requirements

### 3.1 Functional Requirements

- The app shall allow users to add, view, and remove construction workers with their daily wage.
- The app shall record daily labor entries including attendance (present/absent) and advance amounts.
- The app shall calculate and display real-time balance due for each worker (wages earned minus total advance).
- The app shall calculate material requirements (bricks, cement, sand) based on wall dimensions entered by the user.
- The app shall allow users to capture site photos using the device camera with runtime permission handling.
- The app shall store photos locally and display them in a gallery within the app.
- The app shall allow users to delete photos from the gallery.
- The app shall allow users to add and view material rates with name, unit, and price.
- The app shall persist all data locally across app restarts using a Room database.
- The app shall function fully offline without any network dependency.

#### Table 3.1: Functional Requirements Traceability Matrix

| Req. ID | Requirement Description | Priority | Status |
|---------|------------------------|----------|--------|
| FR-001 | Add/remove workers with daily wage | High | Implemented |
| FR-002 | Record daily attendance per worker | High | Implemented |
| FR-003 | Track advance payments per worker | High | Implemented |
| FR-004 | Real-time balance calculation (wages − advance) | High | Implemented |
| FR-005 | Material quantity calculator (bricks, cement, sand) | High | Implemented |
| FR-006 | Camera capture with FileProvider | Medium | Implemented |
| FR-007 | Site photo gallery with delete | Medium | Implemented |
| FR-008 | Material rates list (add/view) | Medium | Implemented |
| FR-009 | Offline-first local data persistence | High | Implemented |
| FR-010 | Runtime camera permission handling | High | Implemented |

---

### 3.2 Non-Functional Requirements

- **Performance:** UI should remain responsive at all times; database queries run on background threads via Coroutines.
- **Offline-First:** All features must work without internet connectivity.
- **Reactivity:** UI must update automatically when data changes in the database (via Kotlin Flow).
- **Security:** Camera photos are shared via FileProvider to avoid exposing private file paths.
- **Usability:** Single-activity architecture with tab navigation for quick access to all modules.
- **Maintainability:** Clear separation of concerns via MVVM pattern.

#### Table 3.2: Non-Functional Requirements Validation

| NFR ID | Requirement | Approach | Status |
|--------|-------------|----------|--------|
| NFR-001 | Responsive UI | DB ops on IO dispatcher; UI on Main dispatcher | Achieved |
| NFR-002 | Offline-first | Room local database, no network calls | Achieved |
| NFR-003 | Real-time UI updates | Kotlin Flow + collectAsState in Compose | Achieved |
| NFR-004 | Secure file sharing | FileProvider with authority declaration | Achieved |
| NFR-005 | Runtime permissions | ActivityResultContracts.RequestPermission | Achieved |
| NFR-006 | Clean architecture | MVVM with Repository pattern | Achieved |

---

### 3.3 Hardware and Software Requirements

#### Table 3.3: Hardware and Software Requirements

| Component | Specification |
|-----------|--------------|
| Processor | ARM-based mobile processor (Snapdragon / Dimensity) |
| RAM | 2 GB minimum (3 GB+ recommended) |
| Storage | 50 MB app storage + space for photos |
| OS | Android 7.0 (API 24) and above |
| Camera | Required for photo capture feature |
| Language | Kotlin 2.2.10 |
| UI Framework | Jetpack Compose (BOM 2024.09.00) + Material3 |
| Database | Room 2.8.4 |
| Build Tool | Gradle with Android Gradle Plugin 9.0.1 |
| Compiler | KSP (Kotlin Symbol Processing) 2.3.7 |
| IDE | Android Studio |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 36 |
| Compile SDK | API 36 |

---

## 4. System Design

### 4.1 Architecture Overview

NammaMistri follows the **MVVM (Model-View-ViewModel)** architectural pattern, which is the recommended architecture for Android apps. The architecture is divided into three layers:

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  Jetpack Compose Screens + Navigation   │
│  (CalculatorScreen, LaborScreen,        │
│   PhotoScreen, RatesScreen)             │
└──────────────────┬──────────────────────┘
                   │ observes State/Flow
┌──────────────────▼──────────────────────┐
│           ViewModel Layer               │
│  (CalculatorViewModel, LaborViewModel,  │
│   PhotoViewModel, RatesViewModel)       │
└──────────────────┬──────────────────────┘
                   │ calls suspend functions
┌──────────────────▼──────────────────────┐
│          Repository Layer               │
│       (NammaMistriRepository)           │
└──────────────────┬──────────────────────┘
                   │ delegates to DAOs
┌──────────────────▼──────────────────────┐
│            Data Layer                   │
│  Room Database (AppDatabase)            │
│  DAOs: Site, Worker, LaborEntry,        │
│        MaterialRate, Photo              │
└─────────────────────────────────────────┘
```

**Key architectural decisions:**

- **Single Activity:** One `MainActivity` hosts all screens via Compose navigation (TabRow).
- **Repository Pattern:** `NammaMistriRepository` is the single source of truth, abstracting all data access from ViewModels.
- **Kotlin Flow:** Room queries return `Flow<T>` so the UI reacts automatically to data changes without manual refresh.
- **Coroutines:** All database writes use `suspend` functions on `Dispatchers.IO`; the UI thread is never blocked.
- **ViewModelFactory:** Custom factory injects the shared `NammaMistriRepository` into each ViewModel.

#### Table 3.4: Architecture Component Summary

| Component | Technology | Role |
|-----------|-----------|------|
| UI Layer | Jetpack Compose + Material3 | Renders UI, collects state from ViewModels |
| ViewModel | AndroidX ViewModel + Kotlin Coroutines | Holds UI state, processes user actions |
| Repository | Plain Kotlin class | Abstracts data access, single source of truth |
| Local Database | Room 2.8.4 | SQLite ORM for persistent local storage |
| DAOs | Room DAOs | SQL queries as Kotlin functions |
| Image Loading | Coil 2.6.0 | Async image loading for photo thumbnails |
| File Sharing | AndroidX FileProvider | Secure URI sharing with camera app |

---

### 4.2 Database Schema

The Room database (`AppDatabase`) contains five entities:

#### Table 3.5: Database Entity Summary

| Entity | Table Name | Key Fields | Purpose |
|--------|-----------|------------|---------|
| Site | `sites` | id, name, location | Construction site info |
| Worker | `workers` | id, name, dailyWage, siteId | Worker records |
| LaborEntry | `labor_entries` | id, workerId, date, present, advance | Daily attendance + advance log |
| MaterialRate | `material_rates` | id, materialName, unit, rate | Market rate reference list |
| Photo | `photos` | id, uri, description, date, siteId | Site progress photos |

**Relationships:**
- A `Site` has many `Worker`s (one-to-many via `siteId`)
- A `Worker` has many `LaborEntry` records (one-to-many via `workerId`)
- A `Site` has many `Photo`s (one-to-many via `siteId`)

**Balance Calculation Logic:**
```
Balance Due = (Days Present × Daily Wage) − Total Advance Paid
```
Both `Days Present` and `Total Advance` are computed with reactive Room Flow queries that auto-update the UI on every new entry.

---

### 4.3 Project Structure

```
app/src/main/java/com/example/nammamistri/
├── MainActivity.kt                 # Single activity, tab navigation
├── data/
│   ├── AppDatabase.kt              # Room database definition
│   ├── Site.kt                     # Entity
│   ├── Worker.kt                   # Entity
│   ├── LaborEntry.kt               # Entity
│   ├── MaterialRate.kt             # Entity
│   ├── Photo.kt                    # Entity
│   └── dao/
│       ├── SiteDao.kt
│       ├── WorkerDao.kt
│       ├── LaborEntryDao.kt
│       ├── MaterialRateDao.kt
│       └── PhotoDao.kt
├── repository/
│   └── NammaMistriRepository.kt    # Single source of truth
├── viewmodel/
│   ├── CalculatorViewModel.kt
│   ├── LaborViewModel.kt
│   ├── PhotoViewModel.kt
│   └── RatesViewModel.kt
└── ui/
    ├── CalculatorScreen.kt
    ├── LaborScreen.kt
    ├── PhotoScreen.kt
    ├── RatesScreen.kt
    └── theme/
        ├── Color.kt                # Construction orange/amber palette
        ├── Theme.kt
        └── Type.kt
```

---

## 5. Implementation

### 5.1 Calculator Module

The Calculator module allows site supervisors to estimate material requirements for a brick wall by entering its dimensions.

**Input Parameters:**
- Length (metres)
- Width (metres)
- Height (metres)
- Wall Thickness (metres, default 0.23 m for standard brick wall)

**Calculation Formulas:**
```
Wall Volume    = Length × Height × Thickness
Bricks         = Volume × 420    (bricks/m³ including mortar)
Cement Bags    = Bricks ÷ 35     (1 bag per 35 bricks)
Sand (m³)      = Volume × 0.35
```

**UI Design:**
- Dimension inputs grouped in a card, arranged side-by-side (2 per row)
- Results displayed as individual visual cards with emoji icons after calculation
- Scrollable layout to accommodate results on small screens

#### Table 5.1: Calculator Module Details

| Item | Detail |
|------|--------|
| Input fields | 4 (Length, Width, Height, Thickness) |
| Output values | 3 (Bricks, Cement Bags, Sand m³) |
| Keyboard type | Decimal number input |
| Calculation trigger | "Calculate Materials" button |
| State management | Local `remember { mutableStateOf() }` |

---

### 5.2 Labor Team Module

The Labor Team module is the most feature-rich module, enabling full worker lifecycle management.

**Features:**
- Add workers with name and daily wage
- Record daily entries (attendance + advance amount) per worker
- Real-time display of days present, total advance paid, and balance due
- Color-coded balance: green (wages owed to worker), red (overpaid)
- Worker avatar showing initials
- Remove workers

**Real-time reactivity** is achieved by exposing Room aggregate queries as `Flow`:

```kotlin
// LaborEntryDao.kt
@Query("SELECT COALESCE(SUM(advance), 0.0) FROM labor_entries WHERE workerId = :workerId")
fun getTotalAdvanceFlow(workerId: Long): Flow<Double>

@Query("SELECT COUNT(*) FROM labor_entries WHERE workerId = :workerId AND present = 1")
fun getTotalDaysWorkedFlow(workerId: Long): Flow<Int>
```

These flows are collected in the Compose UI with `collectAsState()`, meaning the balance recalculates and re-renders immediately after each entry is saved — no tab switch or refresh needed.

#### Table 5.2: Labor Module Features

| Feature | Implementation |
|---------|---------------|
| Add worker | AlertDialog with name + wage fields |
| Add entry | AlertDialog with checkbox (present) + advance field |
| Balance calc | Reactive Flow query, recalculates on every DB change |
| Delete worker | Room `@Query DELETE` via ViewModel |
| Worker avatar | Initials extracted from name, drawn in colored circle |
| Empty state | Friendly message when no workers added |

---

### 5.3 Photo Management Module

The Photo module allows capturing and managing site progress photographs.

**Camera Integration Flow:**
```
User taps "+" FAB
       ↓
RequestPermission (CAMERA)
       ↓ granted
createImageUri() → FileProvider URI
       ↓
TakePicture contract → Camera app opens
       ↓ success
viewModel.addPhoto(uri, description)
       ↓
Room INSERT → Flow emits → LazyColumn updates
```

**FileProvider** is used to securely share the photo file URI with the system camera app, avoiding `FileUriExposedException`. Photos are stored in `getExternalFilesDir("Pictures")`.

**AndroidManifest.xml FileProvider declaration:**
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

**Delete photo:** Each photo card shows a red "Delete" TextButton. Tapping it calls `viewModel.deletePhoto(id)` which executes a Room `DELETE` query and the gallery updates reactively.

#### Table 5.3: Photo Module Details

| Item | Detail |
|------|--------|
| Camera contract | `ActivityResultContracts.TakePicture` |
| Permission | `Manifest.permission.CAMERA` (runtime) |
| Storage location | `getExternalFilesDir("Pictures")` |
| URI security | FileProvider (no raw file:// URIs) |
| Image display | Coil `AsyncImage` with `ContentScale.Crop` |
| Thumbnail size | 200dp height, full width |
| Delete | Room `@Query("DELETE FROM photos WHERE id = :photoId")` |

---

### 5.4 Material Rates Module

The Rates module provides a reference list of current market prices for construction materials.

**Features:**
- Add materials with name, unit (e.g., "bag", "sq ft", "tonne"), and rate (₹)
- View all rates in a scrollable list
- Rate value displayed in a highlighted chip/badge for quick scanning
- Empty state guidance when no rates added

#### Table 5.4: Material Rates Module Details

| Item | Detail |
|------|--------|
| Fields | Material Name, Unit, Rate (₹) |
| Storage | Room `material_rates` table |
| Display | Card with name + unit on left, rate badge on right |
| Add trigger | ExtendedFAB "+ Add Rate" |
| Keyboard | Decimal input for rate field |

---

### 5.5 UI/UX Design

The app uses **Jetpack Compose with Material3** and a custom construction-themed color palette.

**Color Palette:**

| Color Role | Value | Usage |
|------------|-------|-------|
| Primary | `#E65100` (Deep Orange) | Buttons, FAB, avatar background, tab indicator |
| Secondary | `#5D4037` (Brown) | Secondary actions |
| Background | `#FFF8F0` (Warm White) | App background — warm, construction feel |
| Balance Green | `#2E7D32` | Positive balance (wages owed to worker) |
| Balance Red | `#C62828` | Negative balance (overpaid) |

**Navigation:** Single `TabRow` with 4 tabs — Calculator, Team, Photos, Rates. Status bar insets handled via `windowInsetsPadding(WindowInsets.statusBars)` for edge-to-edge display.

**Design Patterns Used:**
- `ExtendedFloatingActionButton` with labels for discoverability
- `RoundedCornerShape(16.dp)` cards for modern feel
- Worker initials avatar instead of generic icons
- Stats displayed in horizontal `Row` chips (Days | Advance | Balance)
- Empty state messages with guidance text
- `HorizontalDivider` between header and stats rows

#### Table 5.5: UI Implementation Summary

| Screen | Key Components |
|--------|---------------|
| Calculator | Card with 2×2 input grid, result cards with emoji |
| Team | Avatar cards, stats chips, color-coded balance, ExtendedFAB |
| Photos | LazyColumn grid, AsyncImage thumbnails, Delete button |
| Rates | Rate badge chip, per-unit label, empty state |

---

## 6. Results and Outcomes

### Application Deliverables

A fully functional Android application was developed and deployed on Android devices. The app covers the complete workflow needed by a construction site supervisor for daily operations.

**Key Outcomes:**

- Developed a 4-module Android app using modern Jetpack Compose and MVVM architecture
- Implemented a fully reactive UI where data changes reflect instantly without manual refresh
- Integrated device camera with secure FileProvider URI sharing and runtime permission handling
- Applied Room database with 5 entities and complex reactive aggregate queries using Kotlin Flow
- Resolved multiple AGP 9.0 compatibility challenges (removed `kotlinOptions`, KSP2 compatibility with Room 2.8.4)
- Achieved a polished construction-themed UI with color-coded financial indicators

#### Table 6.1: Project Quantitative Outcomes

| Metric | Value |
|--------|-------|
| Modules implemented | 4 (Calculator, Team, Photos, Rates) |
| Database entities | 5 (Site, Worker, LaborEntry, MaterialRate, Photo) |
| Room DAOs | 5 |
| ViewModels | 4 |
| Kotlin source files | 18+ |
| Min Android version supported | Android 7.0 (API 24) |
| Architecture pattern | MVVM + Repository |
| UI framework | Jetpack Compose (Material3) |

---

### Technical Achievements

| Achievement | Detail |
|-------------|--------|
| Real-time balance | Flow-based aggregate queries update UI on every DB write |
| Camera integration | FileProvider + TakePicture contract + runtime permission |
| AGP 9.0 support | Migrated from `kotlinOptions` to `kotlin { jvmToolchain(17) }` |
| KSP2 compatibility | Resolved Room `unexpected jvm signature V` by upgrading to Room 2.8.4 |
| Edge-to-edge UI | Status bar insets handled via `windowInsetsPadding` |
| Thread safety | DB init on `Dispatchers.IO`, `setContent` on `Dispatchers.Main` |

---

## 7. Challenges and Solutions

| Challenge | Solution |
|-----------|----------|
| **KSP version not found** (`ksp = "2.2.10-1.0.26"`) | KSP uses standalone versioning since 2.3.0. Changed to `ksp = "2.3.7"` |
| **`kotlinOptions` unresolved in AGP 9.0** | AGP 9.0 removed `kotlinOptions` block. Replaced with `kotlin { jvmToolchain(17) }` |
| **Room `unexpected jvm signature V` crash with KSP2** | Room 2.6.x/2.7.0 incompatible with KSP2. Fixed by upgrading to Room 2.8.4 |
| **`setContent` crash — called on wrong thread** | `setContent` called from `Dispatchers.IO`. Fixed with `withContext(Dispatchers.Main)` wrapper |
| **Tabs not clickable (content overlapping)** | Scaffold padding not applied to content. Fixed with `Box(Modifier.padding(padding))` |
| **Content behind status bar** | Added `Modifier.windowInsetsPadding(WindowInsets.statusBars)` on `TabRow` |
| **Balance not updating without tab switch** | One-shot `LaunchedEffect` replaced with reactive `Flow` queries via `collectAsState()` |
| **Delete icon not visible** | `Icons.Default.Delete` rendering issue. Replaced with `TextButton { Text("Delete") }` |
| **Camera SecurityException** | Added `<uses-permission android:name="android.permission.CAMERA" />` + runtime request |
| **Photos not persisting** | Camera URI created before launch; stored in `pendingPhotoUri`; saved to Room on success callback |
| **App icon not updating** | `mipmap-anydpi-v26` XML pointed to old vector drawables. Updated to reference mipmap PNGs |
| **Duplicate functions after git reset** | `git reset --mixed` kept file changes, causing duplicate `RatesScreen`/`AddRateDialog`. Removed duplicates |

---

## 8. Future Enhancements

| Enhancement | Description |
|-------------|-------------|
| Multi-site support | Allow managing multiple construction sites from one app |
| PDF report export | Generate PDF payroll/attendance reports per worker per month |
| Daily material log | Track daily material consumption (cement bags, sand, bricks used) |
| Worker payment history | Detailed payment ledger per worker with date-wise entries |
| WhatsApp share | Share attendance/payment summaries via WhatsApp |
| Backup & restore | Export/import the Room database for backup |
| Biometric lock | App lock with fingerprint for privacy |
| Material cost estimate | Combine calculator results with rates to give total cost estimate |
| Photo labels | Allow custom labels/tags on site photos |
| Cloud sync | Optional Google Drive backup of data |

---

## 9. Bibliography

| # | Reference |
|---|-----------|
| [1] | Android Developers — Jetpack Compose. https://developer.android.com/compose |
| [2] | Android Developers — Room Persistence Library. https://developer.android.com/training/data-storage/room |
| [3] | Android Developers — ViewModel Overview. https://developer.android.com/topic/libraries/architecture/viewmodel |
| [4] | Android Developers — Kotlin Flow in Android. https://developer.android.com/kotlin/flow |
| [5] | Android Developers — FileProvider. https://developer.android.com/reference/androidx/core/content/FileProvider |
| [6] | Kotlin Documentation — Coroutines. https://kotlinlang.org/docs/coroutines-overview.html |
| [7] | Material Design 3 — Components. https://m3.material.io/components |
| [8] | Coil Image Loading Library. https://coil-kt.github.io/coil |
| [9] | KSP (Kotlin Symbol Processing). https://kotlinlang.org/docs/ksp-overview.html |
| [10] | Android Gradle Plugin 9.0 Release Notes. https://developer.android.com/build/releases/gradle-plugin |

---

*Document Version: 1.0*
*App Version: 1.0.0*
*Platform: Android (API 24–36)*
*Architecture: MVVM + Repository + Room + Jetpack Compose*
