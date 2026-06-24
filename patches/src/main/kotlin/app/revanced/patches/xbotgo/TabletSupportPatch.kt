package app.revanced.patches.xbotgo

import app.revanced.patcher.patch.resourcePatch
import org.w3c.dom.Element

private const val ANDROID_NS = "android:"
private const val NAME_ATTR = ANDROID_NS + "name"
private const val ORIENTATION_ATTR = ANDROID_NS + "screenOrientation"
private const val RESIZEABLE_ATTR = ANDROID_NS + "resizeableActivity"

/**
 * Tier 1 + Tier 2 tablet support for XbotGo (com.blink.blinkfocos).
 *
 *  - Tier 1: rewrites every activity locked to "portrait" so it becomes "user".
 *            "user" lets the Pixel Tablet rotate to landscape while still
 *            honoring the system rotation lock, instead of being letterboxed.
 *  - Tier 2: marks the whole app android:resizeableActivity="true" so it is
 *            treated as large-screen / multi-window friendly and stays out of
 *            the legacy compatibility-scaling path.
 *
 * Resource-only patch — it edits AndroidManifest.xml, so no bytecode
 * fingerprints and no extension (.rve) are needed; it is resilient across
 * app updates.
 *
 * Note: <supports-screens> is intentionally NOT added. At targetSdkVersion 35
 * every screen-size flag already defaults to true, so it would be redundant.
 */
@Suppress("unused")
val tabletSupportPatch = resourcePatch(
    name = "XbotGo tablet support",
    description = "Unlocks portrait-locked screens to user orientation and marks the app resizeable.",
    use = true,
) {
    compatibleWith("com.blink.blinkfocos")

    // If a specific screen looks wrong in landscape, add its fully-qualified
    // class name here to leave its orientation untouched.
    val orientationExclusions = setOf<String>(
        // e.g. "com.blink.blinkfocos.feature.camera.SomeCameraActivity",
    )

    execute {
        document("AndroidManifest.xml").use { document ->
            // Tier 2 — application-wide resizeable flag.
            val application = document.getElementsByTagName("application").item(0) as Element
            application.setAttribute(RESIZEABLE_ATTR, "true")

            // Tier 1 — convert portrait locks to "user".
            val activities = document.getElementsByTagName("activity")
            var converted = 0
            for (i in 0 until activities.length) {
                val activity = activities.item(i) as Element
                if (activity.getAttribute(NAME_ATTR) in orientationExclusions) continue

                if (activity.getAttribute(ORIENTATION_ATTR) == "portrait") {
                    activity.setAttribute(ORIENTATION_ATTR, "user")
                    converted++
                }
            }

            println("[XbotGo tablet support] unlocked $converted portrait activities to 'user'")
        }
    }
}
