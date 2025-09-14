package com.example.pdftest

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment
import androidx.pdf.viewer.fragment.PdfViewerFragment
import com.example.pdftest.ui.theme.PDFTestTheme

class MainActivity : FragmentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PDFTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PdfScreen()
                }
            }
        }
    }
}

@Composable
fun PdfScreen() {
    val context = LocalContext.current
    val pdfUri = remember { mutableStateOf<Uri?>("android.resource://${context.packageName}/${R.raw.sample}".toUri()) }
    val openPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        pdfUri.value = uri
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { openPdfLauncher.launch(arrayOf("application/pdf")) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Select PDF")
        }

        pdfUri.value?.let {
            PdfViewerComposable(
                uri = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } ?: run {
            // Placeholder UI when no PDF selected
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No PDF selected")
            }
        }
    }
}

@Composable
fun PdfViewerComposable(
    uri: Uri,
    modifier: Modifier = Modifier
) {
    AndroidFragment<PdfViewerFragment>(
        arguments = bundleOf("documentUri" to uri),
        modifier = modifier
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PDFTestTheme {
        Greeting("Android")
    }
}