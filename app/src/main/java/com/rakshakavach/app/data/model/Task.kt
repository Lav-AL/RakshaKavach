package com.rakshakavach.app.data.model

data class Task(
    val id: Int,
    val name: String,
    val icon: String,
    val requiredGear: List<String>,
    val riskLevel: String, // LOW, MEDIUM, HIGH, EXTREME
    val description: String
)

object TaskRepository {
    val tasks = listOf(
        Task(
            id = 1,
            name = "Digging Trench",
            icon = "⛏️",
            requiredGear = listOf("Helmet", "Gloves", "Boots", "Safety Vest", "Eye Protection"),
            riskLevel = "HIGH",
            description = "Excavation work involving heavy machinery and deep trenches"
        ),
        Task(
            id = 2,
            name = "Welding Work",
            icon = "🔧",
            requiredGear = listOf("Welding Helmet", "Gloves", "Boots", "Welding Jacket", "Face Shield"),
            riskLevel = "EXTREME",
            description = "Metal joining operations with extreme heat and UV radiation"
        ),
        Task(
            id = 3,
            name = "Height Work",
            icon = "🏗️",
            requiredGear = listOf("Helmet", "Harness", "Boots", "Gloves", "Safety Net"),
            riskLevel = "EXTREME",
            description = "Working at elevated heights above 6 feet requiring fall protection"
        ),
        Task(
            id = 4,
            name = "Electrical Work",
            icon = "⚡",
            requiredGear = listOf("Insulated Gloves", "Safety Goggles", "Insulated Boots", "Helmet", "Arc Flash Suit"),
            riskLevel = "HIGH",
            description = "Electrical installations and maintenance work"
        ),
        Task(
            id = 5,
            name = "Chemical Handling",
            icon = "🧪",
            requiredGear = listOf("Chemical Gloves", "Face Shield", "Respirator", "Chemical Suit", "Boots"),
            riskLevel = "EXTREME",
            description = "Handling hazardous chemicals requiring full protective equipment"
        ),
        Task(
            id = 6,
            name = "Heavy Lifting",
            icon = "🏋️",
            requiredGear = listOf("Safety Belt", "Gloves", "Boots", "Knee Pads", "Back Support"),
            riskLevel = "MEDIUM",
            description = "Manual handling of heavy objects and machinery"
        ),
        Task(
            id = 7,
            name = "Concrete Pouring",
            icon = "🪣",
            requiredGear = listOf("Helmet", "Gloves", "Boots", "Safety Goggles", "Respirator"),
            riskLevel = "MEDIUM",
            description = "Concrete mixing and pouring operations"
        ),
        Task(
            id = 8,
            name = "Scaffolding",
            icon = "🔩",
            requiredGear = listOf("Helmet", "Harness", "Gloves", "Boots", "Visibility Vest"),
            riskLevel = "HIGH",
            description = "Assembly and use of scaffolding structures"
        ),
        Task(
            id = 9,
            name = "Machine Operation",
            icon = "⚙️",
            requiredGear = listOf("Helmet", "Ear Protection", "Safety Goggles", "Gloves", "Boots"),
            riskLevel = "HIGH",
            description = "Operating heavy industrial machinery and equipment"
        ),
        Task(
            id = 10,
            name = "Fire Safety Work",
            icon = "🔥",
            requiredGear = listOf("Fire Suit", "Fire Gloves", "Face Shield", "Breathing Apparatus", "Fire Boots"),
            riskLevel = "EXTREME",
            description = "Fire prevention and suppression operations"
        )
    )
}
