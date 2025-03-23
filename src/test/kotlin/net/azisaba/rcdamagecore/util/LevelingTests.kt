package net.azisaba.rcdamagecore.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class LevelingTests :
    FunSpec({
        withData(
            Pair(1, 1000),
            Pair(2, 2000),
            Pair(60, 60000),
        ) { p ->
            LevelCalculator.getLevelExp(
                p.first,
                80,
            ) shouldBe p.second
        }
    })
