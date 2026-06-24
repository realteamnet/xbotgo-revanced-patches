group = "app.revanced"

// No GPG key is configured in CI (simplified, secret-free release pipeline),
// so skip signing the published artifacts. The .rvp release asset itself is
// unaffected — only the Maven publication signature is dropped.
tasks.withType<org.gradle.plugins.signing.Sign>().configureEach {
    enabled = false
}

patches {
    about {
        name = "XbotGo tablet patches"
        description = "Tablet (orientation + resizeable) patches for XbotGo"
        source = "git@github.com:realteamnet/xbotgo-revanced-patches.git"
        author = "realteamnet"
        website = "https://github.com/realteamnet/xbotgo-revanced-patches"
        license = "GNU General Public License v3.0"
    }
}
