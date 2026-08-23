package app.revanced.patches.samsungkeyboard.misc.nononeui

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

private val storeRequestStrings = listOf("&deviceId=", "&abiType=", "&oneUiVersion=")

internal object StoreDownloadRequestFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(
        "Landroid/content/Context;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Z",
        "Z",
        "Z",
    ),
    strings = storeRequestStrings,
)

internal object StoreUpdateCheckRequestFingerprint : Fingerprint(
    returnType = "[I",
    parameters = listOf("Landroid/content/Context;", "I", "Ljava/lang/String;"),
    strings = storeRequestStrings,
)

internal object ShowSoftInputFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("I", "Landroid/content/Context;"),
    strings = listOf("showSoftInputInner flags="),
)

internal object ClipBoardHandlerConstructorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf("Landroid/content/Context;", "L", "L", "L", "L"),
    returnType = "V",
    strings = listOf("semclipboard"),
)

internal object ClipboardKnoxCheckFingerprint : Fingerprint(
    strings = listOf("isClipboardAllowedAsUser", "kbdContext"),
)
