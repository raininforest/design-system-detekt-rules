package com.github.raininforest.detekt.designsystem

import com.github.raininforest.detekt.designsystem.rules.HardcodedColorRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class ComposeDesignSystemRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "ComposeDesignSystem"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            HardcodedColorRule(config),
        ),
    )
}
