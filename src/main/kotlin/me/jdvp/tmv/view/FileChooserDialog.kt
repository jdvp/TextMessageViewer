package me.jdvp.tmv.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun FileChooserDialog(
    parent: Frame? = null,
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    val resultFile = File(directory + file)
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