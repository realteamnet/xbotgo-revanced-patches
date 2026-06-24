# XbotGo tablet patches

![Build status](https://img.shields.io/github/actions/workflow/status/realteamnet/revanced-patches/release.yml)
![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)

A ReVanced patch bundle that adds basic tablet behavior to **XbotGo**
(`com.blink.blinkfocos`) — built from the
[ReVanced Patches template](https://github.com/ReVanced/revanced-patches-template).

## 🧩 What it does

`XbotGo tablet support` is a resource-only patch:

- **Tier 1** — rewrites every activity locked to `screenOrientation="portrait"`
  into `"user"`, so the app fills the landscape screen and rotates instead of
  running in a scaled portrait letterbox.
- **Tier 2** — adds `android:resizeableActivity="true"` to `<application>` so
  the app is treated as large-screen / multi-window friendly.

It does **not** create genuine tablet layouts — the UI is Jetpack Compose
compiled into bytecode, which a patch can't restructure. This removes the
letterbox/scaling problem; it does not redesign the screens.

## 🚀 Usage

Each push to `main` builds the bundle and publishes `patches-<version>.rvp` to
[Releases](https://github.com/realteamnet/revanced-patches/releases) via
semantic-release. Download the `.rvp` and apply it with the
[ReVanced CLI](https://github.com/ReVanced/revanced-cli) against a clean XbotGo
APK:

```sh
java -jar revanced-cli.jar patch \
  --patches patches-<version>.rvp \
  --out XbotGo-tablet.apk \
  XbotGo-original.apk
```

The output APK is resigned, so uninstall the original before sideloading.

## 🛠️ Building locally

```sh
# GITHUB_ACTOR / GITHUB_TOKEN (a PAT with read:packages) are needed to pull
# the ReVanced plugin from GitHub Packages.
./gradlew build
# bundle: patches/build/libs/patches-<version>.rvp
```

## 🔧 Tuning

- A screen looks wrong rotated? Add its fully-qualified class name to
  `orientationExclusions` in
  `patches/src/main/kotlin/app/revanced/patches/xbotgo/TabletSupportPatch.kt`.
- Want it to always rotate (ignore the rotation lock)? Change `"user"` to
  `"fullSensor"` in that patch.

## 📜 Licence

GPLv3 — see [LICENSE](LICENSE). This repository is a fork of ReVanced's
template; changes are tracked in source per the licence terms.
