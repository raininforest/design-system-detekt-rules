package com.github.raininforest.detekt.designsystem.config

import io.gitlab.arturbosch.detekt.api.Config

data class DesignSystemConfig(
    val allowedColorTokenPrefixes: List<String>,
    val allowedSpacingObject: String,
    val allowedTypographyObject: String,
    val allowedShapesObject: String,
) {
    companion object {
        fun from(config: Config): DesignSystemConfig = DesignSystemConfig(
            allowedColorTokenPrefixes = config.valueOrDefault(
                "allowedColorTokenPrefixes",
                listOf("MaterialTheme.colorScheme"),
            ),
            allowedSpacingObject = config.valueOrDefault("allowedSpacingObject", "Spacing"),
            allowedTypographyObject = config.valueOrDefault("allowedTypographyObject", "MaterialTheme.typography"),
            allowedShapesObject = config.valueOrDefault("allowedShapesObject", "MaterialTheme.shapes"),
        )
    }
}
