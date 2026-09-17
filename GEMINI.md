# Project Overview: Task Assistant Reminder (Android)

A minimal, reliable Android task reminder and enforcement assistant designed to wake the device and trigger exact background alarms even when closed or in Doze mode. Built natively in Kotlin/Java with a future plan to port core scheduling architecture to iOS.

---

## Architectural Constraints & Rules

1. **Keep the Project Footprint Minimal:**
   - Modify only the core 4 files unless explicitly asked otherwise:
     - `app/src/main/AndroidManifest.xml` (permissions & receiver declarations)
     - `app/src/main/java/**/MainActivity.kt` (UI interaction & scheduling triggers)
     - `app/src/main/java/**/ReminderReceiver.kt` (BroadcastReceiver for notification triggers)
     - `app/src/main/res/layout/activity_main.xml` (UI layout)
   - Do not generate boilerplate files, fragments, or complex multi-module scaffolding unless directed.

2. **OS Compatibility & Battery Optimization:**
   - Must support legacy Android releases (Android 7.0 / API 24+) while remaining backward-compatible with older patterns.
   - Use `AlarmManager.setExactAndAllowWhileIdle()` to ensure alarms break through Android Doze mode (`RTC_WAKEUP`).
   - For Android 8.0+ (API 26+), ensure notifications always specify a valid `NotificationChannel` with `IMPORTANCE_HIGH`.
   - Maintain `FLAG_IMMUTABLE` / `FLAG_UPDATE_CURRENT` safety on all `PendingIntent` declarations.

3. **Code Portability (Future iOS Migration):**
   - Keep business logic (scheduling intervals, task models) separate from Android-specific SDK calls (`Context`, `Intent`, `AlarmManager`).
   - Prefer simple Kotlin data classes for task data so they can translate 1:1 to Swift `struct` types later.

---

## Build & Test Commands

- **Build Debug APK:**
  ```bash
  ./gradlew assembleDebug


## Don't do anything mentioned it this just now till I tell you to do it.
