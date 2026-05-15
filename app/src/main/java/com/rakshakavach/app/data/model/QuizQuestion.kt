package com.rakshakavach.app.data.model

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val taskCategory: String = "GENERAL"
)

object QuizRepository {
    val questions = listOf(
        QuizQuestion(
            id = 1,
            question = "What is the FIRST thing you should do before starting any construction task?",
            options = listOf("Start working immediately", "Inspect your PPE equipment", "Check your phone", "Have breakfast"),
            correctAnswerIndex = 1,
            explanation = "Always inspect your PPE before starting any task to ensure it's in proper working condition.",
            taskCategory = "GENERAL"
        ),
        QuizQuestion(
            id = 2,
            question = "At what height must a safety harness be worn?",
            options = listOf("Above 10 feet", "Above 4 feet", "Above 6 feet", "Above 15 feet"),
            correctAnswerIndex = 2,
            explanation = "A safety harness must be worn when working at heights above 6 feet (1.8 meters).",
            taskCategory = "HEIGHT"
        ),
        QuizQuestion(
            id = 3,
            question = "Which color code indicates the HIGHEST risk level?",
            options = listOf("Yellow", "Orange", "Red", "Purple"),
            correctAnswerIndex = 2,
            explanation = "Red indicates the highest risk level requiring immediate action and full PPE compliance.",
            taskCategory = "GENERAL"
        ),
        QuizQuestion(
            id = 4,
            question = "How often should safety gear be inspected?",
            options = listOf("Once a month", "Before every use", "Once a week", "After an accident"),
            correctAnswerIndex = 1,
            explanation = "Safety gear must be inspected before every use to identify any damage or wear.",
            taskCategory = "GENERAL"
        ),
        QuizQuestion(
            id = 5,
            question = "What does PPE stand for?",
            options = listOf("Personal Protection Equipment", "Public Protection Equipment", "Primary Protective Element", "Professional Prevention Equipment"),
            correctAnswerIndex = 0,
            explanation = "PPE stands for Personal Protective Equipment - gear designed to protect workers from hazards.",
            taskCategory = "GENERAL"
        ),
        QuizQuestion(
            id = 6,
            question = "What should you do if you notice a safety hazard?",
            options = listOf("Ignore it and continue", "Report it immediately", "Work around it", "Wait for someone else to fix it"),
            correctAnswerIndex = 1,
            explanation = "Any safety hazard must be reported immediately to prevent accidents and protect all workers.",
            taskCategory = "GENERAL"
        ),
        QuizQuestion(
            id = 7,
            question = "What type of gloves are required for electrical work?",
            options = listOf("Leather gloves", "Cotton gloves", "Insulated rubber gloves", "Any gloves"),
            correctAnswerIndex = 2,
            explanation = "Insulated rubber gloves are required for electrical work to prevent electric shock.",
            taskCategory = "ELECTRICAL"
        ),
        QuizQuestion(
            id = 8,
            question = "What is a 'Near Miss' in workplace safety?",
            options = listOf("A minor injury", "An incident that almost caused injury but didn't", "A colleague's accident", "Equipment failure"),
            correctAnswerIndex = 1,
            explanation = "A 'Near Miss' is an unplanned event that didn't result in injury but had the potential to do so.",
            taskCategory = "GENERAL"
        ),
        QuizQuestion(
            id = 9,
            question = "How should chemical spills be handled?",
            options = listOf("Clean it with bare hands", "Use proper PPE and follow spill procedures", "Ignore small spills", "Use water only"),
            correctAnswerIndex = 1,
            explanation = "Chemical spills must be handled with proper PPE and following established spill procedures.",
            taskCategory = "CHEMICAL"
        ),
        QuizQuestion(
            id = 10,
            question = "What should you do before operating heavy machinery?",
            options = listOf("Just start the machine", "Check operator manual and conduct safety inspection", "Ask a colleague", "Nothing special needed"),
            correctAnswerIndex = 1,
            explanation = "Always check the operator manual and conduct a pre-operation safety inspection before using heavy machinery.",
            taskCategory = "MACHINE"
        )
    )

    fun getRandomQuestions(count: Int = 5): List<QuizQuestion> {
        return questions.shuffled().take(count)
    }
}
