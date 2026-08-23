package app.revanced.patches.samsungkeyboard.misc.nononeui

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstructionOrThrow
import app.revanced.patches.samsungkeyboard.shared.Constants.COMPATIBILITY_SAMSUNG_KEYBOARD
import com.android.tools.smali.dexlib2.Opcode

private const val CLIP_BOARD_HANDLER_TYPE =
    "Lcom/samsung/android/honeyboard/icecone/clipboard/stub/ClipBoardHandler;"

@Suppress("unused")
val enableClipboardPanelPatch = bytecodePatch(
    name = "Enable clipboard panel on non-Samsung ROMs",
    description = "Restores the copy/paste clipboard panel when the One UI clipboard " +
        "service is unavailable, such as on a non-Samsung custom ROM.",
) {
    compatibleWith(COMPATIBILITY_SAMSUNG_KEYBOARD)
    dependsOn(enableNonOneUiPatch)

    execute {
        // Force isServiceEnable = true in constructor
        val constructor = ClipBoardHandlerConstructorFingerprint.method
        val returnIndex = constructor.indexOfFirstInstructionOrThrow { opcode == Opcode.RETURN_VOID }
        constructor.addInstructions(
            returnIndex,
            """
                const/4 v0, 0x1
                iput-boolean v0, p0, $CLIP_BOARD_HANDLER_TYPE->isServiceEnable:Z
            """.trimIndent(),
        )

        // Force isDisable() and other checks to return false
        val clazz = ClipBoardHandlerConstructorFingerprint.classDef
        listOf("isDisable", "isDisableByDeviceSystem", "isDisableByPackage", "isDisableByWebView").forEach { methodName ->
            clazz.methods.firstOrNull { it.name == methodName }?.replaceInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """.trimIndent()
            )
        }

        // Bypass Knox clipboard check
        ClipboardKnoxCheckFingerprint.method.replaceInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """.trimIndent()
        )
    }
}
