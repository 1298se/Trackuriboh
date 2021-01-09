package tang.song.edu.yugiohcollectiontracker.data.types

enum class CardType(val value: String) {
    EFFECT_MONSTER("Effect Monster"),
    FLIP_EFFECT_MONSTER("Flip Effect Monster"),
    FLIP_EFFECT_TUNER_MONSTER("Flip Tuner Effect Monster"),
    GEMINI_MONSTER("Gemini Monster"),
    NORMAL_MONSTER("Normal Monster"),
    NORMAL_TUNER_MONSTER("Normal Tuner Monster"),
    PENDULUM_EFFECT_MONSTER("Pendulum Effect Monster"),
    PENDULUM_FLIP_EFFECT_MONSTER("Pendulum Flip Effect Monster"),
    PENDULUM_NORMAL_MONSTER("Pendulum Normal Monster"),
    PENDULUM_TUNER_EFFECT_MONSTER("Pendulum Tuner Effect Monster"),
    RITUAL_EFFECT_MONSTER("Ritual Effect Monster"),
    RITUAL_MONSTER("Ritual Monster"),
    SKILL_CARD("Skill Card"),
    SPELL_CARD("Spell Card"),
    SPIRIT_MONSTER("Spirit Monster"),
    TOKEN("Token"),
    TOON_MONSTER("Toon Monster"),
    TRAP_CARD("Trap Card"),
    TUNER_MONSTER("Tuner Monster"),
    UNION_EFFECT_MONSTER("Union Effect Monster"),
    UNKNOWN("Unknown"),
    FUSION_MONSTER("Fusion Monster"),
    LINK_MONSTER("Link Monster"),
    PENDULUM_EFFECT_FUSION_MONSTER("Pendulum Effect Fusion Monster"),
    SYNCHRO_MONSTER("Synchro Monster"),
    SYNCHRO_PENDULUM_EFFECT_MONSTER("Synchro Pendulum Effect Monster"),
    SYNCHRO_TUNER_MONSTER("Synchro Tuner Monster"),
    XYZ_MONSTER("XYZ Monster"),
    XYZ_PENDULUM_EFFECT_MONSTER("XYZ Pendulum Effect Monster");


    companion object {
        private val map = values().associateBy(CardType::value)
        fun fromString(cardType: String?) = map[cardType] ?: UNKNOWN
    }
}
