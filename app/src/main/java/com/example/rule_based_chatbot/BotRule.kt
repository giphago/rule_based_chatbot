package com.example.rule_based_chatbot

class BotRule {
    fun getResponse(userInput: String): String {
        val normalizedInput = userInput.lowercase().trim()

        return when {
            // 인사 관련 룰
            normalizedInput.contains("안녕") || normalizedInput.contains("하이") -> "안녕하세요! 저는 룰 기반 챗봇입니다. 무엇을 도와드릴까요?"
            normalizedInput.contains("만나서 반가워") -> "저도 만나서 반갑습니다! 😊"

            // 질문 관련 룰
            normalizedInput.contains("이름") -> "제 이름은 챗봇입니다. 특별한 이름은 없어요."
            normalizedInput.contains("뭐해") || normalizedInput.contains("무슨 일") -> "사용자님과 대화할 준비가 되어있어요."
            normalizedInput.contains("시간") -> "죄송하지만, 저는 현재 시간을 알 수 없습니다."
            normalizedInput.contains("날씨") -> "저는 날씨 정보를 실시간으로 제공하지 못해요."

            // 감정/상태 관련 룰
            normalizedInput.contains("고마워") || normalizedInput.contains("감사") -> "천만에요! 언제든지 도와드릴 준비가 되어 있습니다."
            normalizedInput.contains("배고파") -> "맛있는 음식을 드시고 기운내세요! 🍕"
            normalizedInput.contains("졸려") -> "잠시 쉬면서 커피 한 잔 하는 건 어떠세요? ☕"

            // 종료 관련 룰
            normalizedInput.contains("종료") || normalizedInput.contains("그만") || normalizedInput.contains(
                "bye"
            ) -> "다음에 또 만나요! 좋은 하루 되세요."

            // 예외 처리
            else -> "이해하지 못했어요. 다시 한번 말씀해 주시겠어요?"
        }
    }
}