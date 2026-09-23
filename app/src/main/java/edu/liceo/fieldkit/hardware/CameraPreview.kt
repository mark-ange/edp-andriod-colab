package edu.liceo.fieldkit.hardware

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.awaitCancellation

@Composable
fun CameraPreview(
    capture: ImageCapture,
    modifier: Modifier = Modifier,
    onCameraReady: (Camera) -> Unit = {} // used by the bonus
) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    var request by remember { mutableStateOf<SurfaceRequest?>(null) }
    LaunchedEffect(owner) {
        val provider = ProcessCameraProvider.awaitInstance(context)
        // TODO 7a: val preview = a Preview whose surface provider stores the request
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider { req -> request = req }
        }
        // TODO 7b: provider.unbindAll(), then bind preview + capture to the owner
        // with the back camera, and keep the result in val camera
        provider.unbindAll()
        val camera = provider.bindToLifecycle(
            owner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            capture
        )
        // TODO 7c: onCameraReady(camera)
        onCameraReady(camera)
        try {
            awaitCancellation()
        } finally {
            provider.unbindAll()
        } // GIVEN
    }
    request?.let {
        CameraXViewfinder(surfaceRequest = it, modifier = modifier) // GIVEN
    }
}
