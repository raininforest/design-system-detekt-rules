package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Assert.assertEquals
import org.junit.Test

class HardcodedShapeRuleTest {

    private val rule = HardcodedShapeRule(Config.empty)

    @Test
    fun `reports RoundedCornerShape with dp argument`() {
        val code = """
            import androidx.compose.foundation.shape.RoundedCornerShape
            val shape = RoundedCornerShape(8.dp)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports CutCornerShape with dp argument`() {
        val code = """
            import androidx.compose.foundation.shape.CutCornerShape
            val shape = CutCornerShape(4.dp)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports RoundedCornerShape with percent integer argument`() {
        val code = """
            import androidx.compose.foundation.shape.RoundedCornerShape
            val shape = RoundedCornerShape(50)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `does not report MaterialTheme shapes token`() {
        val code = """
            val shape = MaterialTheme.shapes.medium
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `does not report custom shape token when allowedTokens is set`() {
        val customRule = HardcodedShapeRule(TestConfig("allowedTokens" to "AppShapes"))
        val code = """
            val shape = AppShapes.medium
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `reports RoundedCornerShape even when custom allowedTokens is set`() {
        val customRule = HardcodedShapeRule(TestConfig("allowedTokens" to "AppShapes"))
        val code = """
            import androidx.compose.foundation.shape.RoundedCornerShape
            val shape = RoundedCornerShape(8.dp)
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(1, findings.size)
    }
}
