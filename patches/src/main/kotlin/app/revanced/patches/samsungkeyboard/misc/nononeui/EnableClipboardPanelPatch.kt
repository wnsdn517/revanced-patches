package app.revanced.patches.samsungkeyboard.misc.nononeui

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.revanced.patches.samsungkeyboard.shared.Constants.COMPATIBILITY_SAMSUNG_KEYBOARD
import app.revanced.util.indexOfFirstInstructionOrThrow
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
        val method = ClipBoardHandlerConstructorFingerprint.method
        val returnIndex = method.indexOfFirstInstructionOrThrow {
            opcode == Opcode.RETURN_VOID
        }

        method.addInstructions(
            returnIndex,
            """
                const/4 v0, 0x1
                iput-boolean v0, p0, $CLIP_BOARD_HANDLER_TYPE->isServiceEnable:Z
            """.trimIndent(),
        )
    }
}
