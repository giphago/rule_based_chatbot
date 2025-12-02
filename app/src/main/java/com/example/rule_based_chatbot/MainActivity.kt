package com.example.rule_based_chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.rule_based_chatbot.ui.theme.Rule_Based_ChatbotTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api

class MainActivity : ComponentActivity() {

    private val botRule = BotRule()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rule_Based_ChatbotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(
                        botRule = botRule,
                        coroutineScope = lifecycleScope
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(botRule: BotRule, coroutineScope: androidx.lifecycle.LifecycleCoroutineScope) {
    val messages = remember {
        mutableStateListOf(
            Message(text = "안녕하세요! 저는 룰 기반 챗봇입니다. 대화를 시작해보세요.", isUser = false)
        )
    }
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    val sendMessage: (String) -> Unit = { input ->
        if (input.isNotBlank()) {
            messages.add(Message(text = input, isUser = true))
            userInput = ""

            coroutineScope.launch {
                delay(700)
                val botResponseText = botRule.getResponse(input)
                messages.add(Message(text = botResponseText, isUser = false))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("룰 기반 챗봇") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MessageList(
                    messages = messages,
                    listState = listState,
                    modifier = Modifier.weight(1f)
                )

                ChatInput(
                    userInput = userInput,
                    onUserInputChanged = { userInput = it },
                    onSendMessage = { sendMessage(userInput) }
                )
            }
        }
    )
}

@Composable
fun MessageList(
    messages: SnapshotStateList<Message>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        items(messages) { message ->
            MessageBubble(message = message)
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ChatInput(
    userInput: String,
    onUserInputChanged: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = onUserInputChanged,
            label = { Text("메시지 입력") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSendMessage,
            enabled = userInput.isNotBlank(),
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "메시지 전송")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Rule_Based_ChatbotTheme {
        Column {
            MessageBubble(message = Message("사용자가 입력한 메시지입니다.", true))
            MessageBubble(message = Message("챗봇의 응답입니다.", false))
            Spacer(modifier = Modifier.height(10.dp))
            ChatInput(userInput = "테스트 입력", onUserInputChanged = {}, onSendMessage = {})
        }
    }
}