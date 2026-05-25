package com.yonatankarp.openapi

import org.gradle.api.Named
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class ServerSpec
    @Inject
    constructor(private val specName: String) : Named {
        abstract val spec: Property<String>
        abstract val packageName: Property<String>
        abstract val modelPackageName: Property<String>
        abstract val templatesDir: Property<String>
        abstract val config: MapProperty<String, String>

        override fun getName(): String = specName
    }
