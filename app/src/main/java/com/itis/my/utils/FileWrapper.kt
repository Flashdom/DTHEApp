package com.itis.my.utils

import com.google.firebase.storage.FileDownloadTask
import java.io.File

data class FileWrapper(val file: File, val task: FileDownloadTask)