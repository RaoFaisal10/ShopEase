#!/bin/sh
# Local verification on a computer with the Android SDK installed and internet access.
set -eu
cd "$(dirname "$0")/.."
python3 scripts/verify_source.py
./gradlew --no-daemon --stacktrace assembleDebug
if command -v adb >/dev/null 2>&1 && adb devices | grep -qE '^([A-Za-z0-9.:_-]+)[[:space:]]+device$'; then
  adb install -r app/build/outputs/apk/debug/app-debug.apk
  adb shell am start -n pk.edu.nfciet.shopease/.SplashActivity
  echo 'APK installed and launched on the connected Android device.'
else
  echo 'APK built; no emulator/device connected for the runtime smoke check.'
fi
