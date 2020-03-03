package tang.song.edu.yugiohcollectiontracker.data.models

enum class ExtraDeckMonsterTypes(val value: List<String>) {
    SYNCHRO_MONSTER(
        listOf(
            BaseMonsterTypes.SYNCHRO_MONSTER.value,
            BaseMonsterTypes.SYNCHRO_PENDULUM_EFFECT_MONSTER.value,
            BaseMonsterTypes.SYNCHRO_TUNER_MONSTER.value
        )
    ),
    FUSION_MONSTER(
        listOf(
            BaseMonsterTypes.FUSION_MONSTER.value,
            BaseMonsterTypes.PENDULUM_EFFECT_FUSION_MONSTER.value
        )
    ),
    XYZ_MONSTER(
        listOf(
            BaseMonsterTypes.XYZ_MONSTER.value,
            BaseMonsterTypes.XYZ_PENDULUM_EFFECT_MONSTER.value
        )
    ),
    LINK_MONSTER(
        listOf(
            BaseMonsterTypes.LINK_MONSTER.value
        )
    )
}
