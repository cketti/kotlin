/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.declarations.lazy

import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibility
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.DeclarationStubGenerator
import org.jetbrains.kotlin.ir.util.TypeTranslator
import org.jetbrains.kotlin.ir.util.withScope
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.propertyIfAccessor

@OptIn(ObsoleteDescriptorBasedAPI::class)
class IrLazyFunction(
    startOffset: Int,
    endOffset: Int,
    origin: IrDeclarationOrigin,
    override val symbol: IrSimpleFunctionSymbol,
    _initialDescriptor: FunctionDescriptor,
    name: Name,
    visibility: Visibility,
    override val modality: Modality,
    isInline: Boolean,
    isExternal: Boolean,
    override val isTailrec: Boolean,
    override val isSuspend: Boolean,
    isExpect: Boolean,
    override val isFakeOverride: Boolean,
    override val isOperator: Boolean,
    stubGenerator: DeclarationStubGenerator,
    typeTranslator: TypeTranslator
) :
    IrLazyFunctionBase(
        startOffset, endOffset, _initialDescriptor, origin, name, visibility, isInline, isExternal, isExpect, stubGenerator, typeTranslator
    ),
    IrSimpleFunction {

    override val wrappedDescriptor get() = symbol.wrappedDescriptor
    override val initialDescriptor get() = symbol.initialDescriptor

    override var typeParameters: List<IrTypeParameter> by lazyVar {
        typeTranslator.buildWithScope(this) {
            stubGenerator.symbolTable.withScope(_initialDescriptor) {
                val propertyIfAccessor = _initialDescriptor.propertyIfAccessor
                propertyIfAccessor.typeParameters.mapTo(arrayListOf()) { typeParameterDescriptor ->
                    if (_initialDescriptor != propertyIfAccessor) {
                        stubGenerator.generateOrGetScopedTypeParameterStub(typeParameterDescriptor).also { irTypeParameter ->
                            irTypeParameter.parent = this@IrLazyFunction
                        }
                    } else {
                        stubGenerator.generateOrGetTypeParameterStub(typeParameterDescriptor)
                    }
                }
            }
        }
    }


    override var overriddenSymbols: List<IrSimpleFunctionSymbol> by lazyVar {
        _initialDescriptor.overriddenDescriptors.mapTo(arrayListOf()) {
            stubGenerator.generateFunctionStub(it.original).symbol
        }
    }
    override var attributeOwnerId: IrAttributeContainer = this

    override var correspondingPropertySymbol: IrPropertySymbol? = null

    init {
        symbol.bind(this)
    }

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R =
        visitor.visitSimpleFunction(this, data)
}