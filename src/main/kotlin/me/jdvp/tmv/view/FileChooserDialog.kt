package me.jdvp.tmv.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import me.jdvp.tmv.model.EmbeddedBackupFile
import org.apache.commons.io.FilenameUtils
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun FileChooserDialog(
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(null as Frame?, "Choose a file", LOAD) {
            override fun setVisible(isVisible: Boolean) {
                super.setVisible(isVisible)
                if (isVisible) {
                    if (directory.isNullOrEmpty() || file.isNullOrEmpty()) {
                        return
                    }
                    val resultFile = File(directory, file)
                    if (resultFile.exists()) {
                        onCloseRequest(resultFile)
                    } else {
                        onCloseRequest(null)
                    }
                }
            }
        }.apply {
            file = "*.xml"
            setFilenameFilter { _, name -> name.endsWith(".xml") }
        }
    },
    dispose = FileDialog::dispose
)

@Composable
fun FileSaverDialog(
    embeddedBackupFile: EmbeddedBackupFile,
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(null as Frame?, "Save to disk", SAVE) {
            override fun setVisible(isVisible: Boolean) {
                super.setVisible(isVisible)
                if (isVisible) {
                    if (!directory.isNullOrEmpty() && !file.isNullOrEmpty()) {
                        val fileExtension = ".${FilenameUtils.getExtension(embeddedBackupFile.originalFileName)}"
                        if (!file.endsWith(fileExtension)) {
                            file += fileExtension
                        }
                        val resultFile = File(directory, file)
                        resultFile.writeBytes(embeddedBackupFile.bytes.toByteArray())
                        onCloseRequest(resultFile)
                    }
                }
            }
        }.apply {
            val fileExtension = FilenameUtils.getExtension(embeddedBackupFile.originalFileName)
            if (!fileExtension.isNullOrBlank()) {
                file = "*.$fileExtension"
                setFilenameFilter { _, name -> name.endsWith(".$fileExtension") }
            }

            file = embeddedBackupFile.originalFileName
        }
    },
    dispose = FileDialog::dispose
)