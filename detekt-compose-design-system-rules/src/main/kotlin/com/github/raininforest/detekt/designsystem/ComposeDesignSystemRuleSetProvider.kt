package com.github.raininforest.detekt.designsystem

import com.github.raininforest.detekt.designsystem.rules.HardcodedColorRule
import com.github.raininforest.detekt.designsystem.rules.HardcodedShapeRule
import com.github.raininforest.detekt.designsystem.rules.HardcodedTypographyRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class ComposeDesignSystemRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "ComposeDesignSystem"

    override fun instance(config: Config): RuleSet {
        val ruleSetConfig = config.subConfig(ruleSetId)
        return RuleSet(
            id = ruleSetId,
            rules = listOf(
                HardcodedColorRule(ruleSetConfig.subConfig("HardcodedColor")),
                HardcodedShapeRule(ruleSetConfig.subConfig("HardcodedShape")),
                HardcodedTypographyRule(ruleSetConfig.subConfig("HardcodedTypography")),
            ),
        )
    }
}
