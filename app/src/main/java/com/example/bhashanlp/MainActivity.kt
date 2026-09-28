package com.example.bhashanlp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale("hi", "IN")
            }
        }

        setContent {
            BhasaNLPApp(
                speakText = { text ->
                    tts.speak(
                        text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "BHASANLP_TTS"
                    )
                }
            )
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhasaNLPApp(
    speakText: (String) -> Unit
) {

    var currentScreen by remember {
        mutableStateOf("home")
    }

    MaterialTheme {

        Scaffold(
            topBar = {

                if (currentScreen != "home") {

                    TopAppBar(
                        title = {
                            Text(
                                when (currentScreen) {
                                    "translation" -> "Voice Translation"
                                    "curriculum" -> "Curriculum Generator"
                                    "worksheet" -> "Bilingual Worksheet"
                                    else -> "BhasaNLP"
                                }
                            )
                        },

                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    currentScreen = "home"
                                }
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            },

            bottomBar = {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentScreen == "home",
                        onClick = {
                            currentScreen = "home"
                        },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text("Home")
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == "translation",
                        onClick = {
                            currentScreen = "translation"
                        },
                        icon = {
                            Icon(
                                Icons.Default.Translate,
                                contentDescription = "Translate"
                            )
                        },
                        label = {
                            Text("Translate")
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == "curriculum",
                        onClick = {
                            currentScreen = "curriculum"
                        },
                        icon = {
                            Icon(
                                Icons.Default.Book,
                                contentDescription = "Lessons"
                            )
                        },
                        label = {
                            Text("Lessons")
                        }
                    )
                }
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                when (currentScreen) {

                    "home" -> HomeScreen(
                        onTranslation = {
                            currentScreen = "translation"
                        },
                        onCurriculum = {
                            currentScreen = "curriculum"
                        },
                        onWorksheet = {
                            currentScreen = "worksheet"
                        }
                    )

                    "translation" -> TranslationScreen(
                        speakText = speakText
                    )

                    "curriculum" -> CurriculumScreen(
                        onWorksheet = {
                            currentScreen = "worksheet"
                        },
                        speakText = speakText
                    )

                    "worksheet" -> WorksheetScreen(
                        speakText = speakText
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------------
   HOME SCREEN
--------------------------------------------------------- */

@Composable
fun HomeScreen(
    onTranslation: () -> Unit,
    onCurriculum: () -> Unit,
    onWorksheet: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "BhasaNLP",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "AI-Powered Mother Tongue Learning",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "●",
                        color = Color(0xFF2E7D32),
                        fontSize = 25.sp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {

                        Text(
                            text = "Offline Mode",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Learning resources available offline",
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Teacher Tools",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {

            FeatureCard(
                icon = "🎤",
                title = "Voice Translation",
                description = "Convert Hindi speech into Santhali learning content",
                onClick = onTranslation
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                icon = "📚",
                title = "Curriculum Generator",
                description = "Generate simple lessons and activities",
                onClick = onCurriculum
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                icon = "📝",
                title = "Bilingual Worksheet",
                description = "View Hindi and Santhali learning worksheets",
                onClick = onWorksheet
            )
        }

        item {

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "How BhasaNLP Works",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Teacher speaks Hindi"
                    )

                    Text(text = "↓")

                    Text(
                        text = "Speech Recognition"
                    )

                    Text(text = "↓")

                    Text(
                        text = "Hindi → Santhali Translation"
                    )

                    Text(text = "↓")

                    Text(
                        text = "Santhali Text + Voice"
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------------
   FEATURE CARD
--------------------------------------------------------- */

@Composable
fun FeatureCard(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = icon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

/* ---------------------------------------------------------
   TRANSLATION SCREEN
--------------------------------------------------------- */

@Composable
fun TranslationScreen(
    speakText: (String) -> Unit
) {

    var hindiText by remember {
        mutableStateOf("गिनती एक से दस तक सीखो")
    }

    var santhaliText by remember {
        mutableStateOf("Mitar 1 khon 10 tala sikha")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            Text(
                text = "Hindi → Santhali",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Voice Translation Demo",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "Teacher Input",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = hindiText,
                        fontSize = 19.sp
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {
                            // Prototype:
                            // Replace this later with Android SpeechRecognizer
                            hindiText =
                                "गिनती एक से दस तक सीखो"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Start Voice Input")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Translation",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "Santhali",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = santhaliText,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {
                            speakText(santhaliText)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text("Play Voice")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    santhaliText =
                        "Mitar 1 khon 10 tala sikha"
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    Icons.Default.Translate,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text("Translate")
            }
        }
    }
}

/* ---------------------------------------------------------
   CURRICULUM SCREEN
--------------------------------------------------------- */

@Composable
fun CurriculumScreen(
    onWorksheet: () -> Unit,
    speakText: (String) -> Unit
) {

    var selectedClass by remember {
        mutableStateOf("Class 2")
    }

    var selectedSubject by remember {
        mutableStateOf("Mathematics")
    }

    var selectedTopic by remember {
        mutableStateOf("Numbers 1–10")
    }

    var generated by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            Text(
                text = "Create Lesson",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            SelectionCard(
                title = "Class",
                value = selectedClass,
                options = listOf(
                    "Class 1",
                    "Class 2",
                    "Class 3"
                ),
                onSelected = {
                    selectedClass = it
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SelectionCard(
                title = "Subject",
                value = selectedSubject,
                options = listOf(
                    "Mathematics",
                    "EVS",
                    "Language"
                ),
                onSelected = {
                    selectedSubject = it
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SelectionCard(
                title = "Topic",
                value = selectedTopic,
                options = listOf(
                    "Numbers 1–10",
                    "Animals",
                    "Colours"
                ),
                onSelected = {
                    selectedTopic = it
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    generated = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Generate Lesson")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (generated) {

                LessonResult(
                    speakText = speakText,
                    onWorksheet = onWorksheet
                )
            }
        }
    }
}
/* ---------------------------------------------------------
   SELECTION CARD
--------------------------------------------------------- */

@Composable
fun SelectionCard(
    title: String,
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box {

                OutlinedButton(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = value,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    options.forEach { option ->

                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {

                                onSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ---------------------------------------------------------
   LESSON RESULT
--------------------------------------------------------- */

@Composable
fun LessonResult(
    speakText: (String) -> Unit,
    onWorksheet: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Generated Lesson",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "🎯 Learning Outcome",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Student will recognize and count numbers from 1 to 10."
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "📖 Lesson",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Learn numbers from 1 to 10 using objects and counting activities."
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "🗣 Santhali Support",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Mitar 1 khon 10 tala sikha."
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "🎮 Activity",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ask students to count objects around them from 1 to 10."
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {
                        speakText(
                            "Learn numbers from one to ten"
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {

                    Text("🔊 Listen")
                }

                Button(
                    onClick = onWorksheet,
                    modifier = Modifier.weight(1f)
                ) {

                    Text("Worksheet")
                }
            }
        }
    }
}

/* ---------------------------------------------------------
   WORKSHEET SCREEN
--------------------------------------------------------- */

@Composable
fun WorksheetScreen(
    speakText: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {

            Text(
                text = "Bilingual Worksheet",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Class 2 • Mathematics • Numbers 1–10",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            WorksheetQuestion(
                question =
                    "Q1. Count the objects and select the answer.",
                santhali =
                    "Q1. Chitra ko ginti karo aur sahi jawab chuno.",
                options = "2     3     4"
            )

            Spacer(modifier = Modifier.height(15.dp))

            WorksheetQuestion(
                question =
                    "Q2. What comes after 5?",
                santhali =
                    "Q2. 5 reya kana?",
                options = "4     6     7"
            )

            Spacer(modifier = Modifier.height(15.dp))

            WorksheetQuestion(
                question =
                    "Q3. Count: ● ● ● ●",
                santhali =
                    "Q3. Ginti karo: ● ● ● ●",
                options = "3     4     5"
            )

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    speakText(
                        "Count the objects and select the answer"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Read Worksheet Aloud")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    Icons.Default.Print,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Save Worksheet")
            }
        }
    }
}

/* ---------------------------------------------------------
   WORKSHEET QUESTION
--------------------------------------------------------- */

@Composable
fun WorksheetQuestion(
    question: String,
    santhali: String,
    options: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = question,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = santhali,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = options,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}