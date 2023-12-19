package com.example.navigation

import android.content.res.AssetManager
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.navigation.ui.theme.NavigationTheme
import com.example.navigation.ui.theme.ColorViewButton
import com.example.navigation.ui.theme.ColorViewDark
import com.example.navigation.ui.theme.ColorViewLine

import java.util.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.navigation.Core.Core
import com.example.navigation.ui.theme.ColorBack


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray/*MaterialTheme.colorScheme.background*/
                ) {
                    OnMap()
                }
            }
        }
    }
}

@Preview
@Composable
fun OnMap() {
    NavigationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = Color.Gray/*MaterialTheme.colorScheme.background*/
        ) {
            Map()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var webView: WebView? by remember { mutableStateOf(null) }

    val buildPath = remember { mutableStateOf(0) }

    val textStateOn = remember { mutableStateOf("") }
    val textStateIn = remember { mutableStateOf("") }
    Column(modifier = Modifier.background(Color.Gray)) {
        Column(
            modifier = Modifier
                .border(2.dp, ColorViewLine)
                .background(ColorBack)
        ) {
            Text(
                text = "Карта МИИТа",
                modifier
                    .align(CenterHorizontally)
                    .padding(12.dp)
            )
            Row(modifier = Modifier.align(CenterHorizontally)) {
                OutlinedTextField(
                    value = textStateOn.value,
                    onValueChange = { textStateOn.value = it },
                    label = { Text("Откуда") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors( textColor = Color.Black)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(4.dp),
                    value = textStateIn.value,
                    onValueChange = { textStateIn.value = it },
                    label = { Text("Куда") },
                    colors = TextFieldDefaults.outlinedTextFieldColors( textColor = Color.Black)
                )
            }

            Button(
                onClick = {
                    var path1 = Core.getPathRoute(context.assets,textStateOn.value.toInt(), textStateIn.value.toInt()) /*(textStateIn.value.toInt(), textStateOn.value.toInt())*/
                    webView?.evaluateJavascript("clearPaths();") {}
                    //var path1 = "781,766 779,789"
                   /* if (textStateOn.value == "0") path1 = "1353,1345,1336,1335,1353,1321,1364,1301,1371,1268,1350,1196,1155,1168,888,1134,769,1117,756,1185,729,1180"// От КПП до 6

                    if (textStateIn.value == "5") path1 = "1156,1139,1153,1169,986,1147,889,1134,889,1144,883,1176 "// от 1 до 5
                    if (textStateOn.value == "1" && textStateIn.value == "8") path1 = "1156,1139,1152,1169,889,1134,768,1116,744,1303,735,1298,723,1299,689,1299,657,1302,620,1313,586,1336,547,1361,489,1369,453,1375,427,1373,410,1331,402,1267,404,1218,405,1177,407,1128,405,1016,391,992,376,1006,376,1015"// от 1 до 8
                    if (textStateIn.value == "") path1 = "781,765,777,790,519,754,504,879,457,878,432,883,408,893,391,906,372,918,349,925,323,927,287,930,284,918,279,901,275,880,272,855,272,828,274,794,276,760"// от 1 до 6

                    // 0 6*/
                    // 1 5
                    // 1 8
                    val javascriptCode = "click1('$path1');"//
                    webView?.evaluateJavascript(javascriptCode) {}
                },
                colors = ButtonDefaults.buttonColors(ColorViewButton),
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .align(CenterHorizontally)
            ) {
                Text(
                    "Построить путь",
                    color = ColorViewDark,
                    fontSize = 16.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxSize()
        ) {

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        this.webViewClient = WebViewClient()
                        this.webChromeClient = WebChromeClient()
                        this.settings.javaScriptEnabled = true

                        this.settings.builtInZoomControls = true
                        this.settings.displayZoomControls = false

                        webView = this

                        val htmlPath = "file:///android_asset/nav.html"
                        loadUrl(htmlPath)

                    }
                },
                modifier = Modifier.fillMaxSize()
            )

        }
    }
}
