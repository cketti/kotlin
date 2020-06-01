/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.targets.js.ir

import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinTargetWithBinaries
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBinaryMode
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBinaryMode.DEVELOPMENT
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBinaryMode.PRODUCTION
import org.jetbrains.kotlin.gradle.targets.js.subtargets.KotlinJsSubTarget
import org.jetbrains.kotlin.gradle.utils.lowerCamelCaseName
import javax.inject.Inject


open class KotlinJsBinaryContainer
@Inject
constructor(
    val target: KotlinTargetWithBinaries<KotlinJsCompilation, KotlinJsBinaryContainer>,
    backingContainer: DomainObjectSet<JsBinary>
) : DomainObjectSet<JsBinary> by backingContainer {
    val project: Project
        get() = target.project

    private val binaryNames = mutableSetOf<String>()

    private val defaultCompilation: KotlinJsCompilation
        get() = target.compilations.getByName(KotlinCompilation.MAIN_COMPILATION_NAME)

    // For Groovy DSL
    @JvmOverloads
    fun executable(
        compilation: KotlinJsCompilation = defaultCompilation
    ) {
        if (target is KotlinJsIrTarget) {
            target.whenBrowserConfigured {
                (this as KotlinJsIrSubTarget).produceExecutable()
            }

            target.whenNodejsConfigured {
                (this as KotlinJsIrSubTarget).produceExecutable()
            }

            compilation.binaries.executableIrInternal(compilation)
        }

        if (target is KotlinJsTarget) {
            target.irTarget
                ?.let { throw IllegalStateException("Unfortunately you can't use `executable()` with 'both' compiler type") }

            target.whenBrowserConfigured {
                (this as KotlinJsSubTarget).produceExecutable()
            }

            target.whenNodejsConfigured {
                (this as KotlinJsSubTarget).produceExecutable()
            }

            compilation.binaries.executableLegacyInternal(compilation)
        }
    }

    internal fun executableIrInternal(compilation: KotlinJsCompilation) = createBinaries(
        compilation = compilation,
        jsBinaryType = KotlinJsBinaryType.EXECUTABLE,
        create = ::Executable
    )

    private fun executableLegacyInternal(compilation: KotlinJsCompilation) = createBinaries(
        compilation = compilation,
        jsBinaryType = KotlinJsBinaryType.EXECUTABLE,
        create = { compilation, name, type ->
            object : JsBinary {
                override val compilation: KotlinJsCompilation = compilation
                override val name: String = name
                override val mode: KotlinJsBinaryMode = type
            }
        }
    )

    internal fun getIrBinaries(
        mode: KotlinJsBinaryMode
    ): DomainObjectSet<JsIrBinary> =
        withType(JsIrBinary::class.java)
            .matching { it.mode == mode }

    private fun <T : JsBinary> createBinaries(
        compilation: KotlinJsCompilation,
        modes: Collection<KotlinJsBinaryMode> = listOf(PRODUCTION, DEVELOPMENT),
        jsBinaryType: KotlinJsBinaryType,
        create: (compilation: KotlinJsCompilation, name: String, mode: KotlinJsBinaryMode) -> T
    ) {
        modes.forEach {
            createBinary(
                compilation,
                it,
                jsBinaryType,
                create
            )
        }
    }

    private fun <T : JsBinary> createBinary(
        compilation: KotlinJsCompilation,
        mode: KotlinJsBinaryMode,
        jsBinaryType: KotlinJsBinaryType,
        create: (compilation: KotlinJsCompilation, name: String, mode: KotlinJsBinaryMode) -> T
    ) {
        val name = generateBinaryName(
            compilation,
            mode,
            jsBinaryType
        )

        if (name in binaryNames) {
            return
        }

        binaryNames.add(name)

        val binary = create(compilation, name, mode)
        add(binary)
        // Allow accessing binaries as properties of the container in Groovy DSL.
        if (this is ExtensionAware) {
            extensions.add(binary.name, binary)
        }
    }

    companion object {
        internal fun generateBinaryName(
            compilation: KotlinJsCompilation,
            mode: KotlinJsBinaryMode,
            jsBinaryType: KotlinJsBinaryType?
        ) =
            lowerCamelCaseName(
                compilation.name.let { if (it == KotlinCompilation.MAIN_COMPILATION_NAME) null else it },
                mode.name.toLowerCase(),
                jsBinaryType?.name?.toLowerCase()
            )
    }
}
