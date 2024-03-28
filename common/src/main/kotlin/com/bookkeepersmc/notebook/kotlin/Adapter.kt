/*
 * Copyright (c) 2023, 2024 BookkeepersMC under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.bookkeepersmc.notebook.kotlin

import net.fabricmc.loader.api.LanguageAdapter
import net.fabricmc.loader.api.LanguageAdapterException
import net.fabricmc.loader.api.ModContainer
import java.lang.invoke.MethodHandleProxies
import java.lang.invoke.MethodHandles
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

/**
 * Like <p>fabric-language-kotlin</p>,
 * but for forge, neoforge, fabric, and common!*/
open class Adapter : LanguageAdapter {
    override fun <T : Any> create(mod: ModContainer, value: String, type: Class<T>): T {
        val split = value.split("::").dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitSize = split.size
        if (splitSize >= 3) {
            throw LanguageAdapterException("Invalid format: $value")
        }

        val c: Class<Any> = try {
            Class.forName(split[0]) as Class<Any>
        } catch (e: ClassNotFoundException) {
            throw LanguageAdapterException(e)
        }
        val k = c.kotlin

        when (split.size) {
            1 -> {
                return if (type.isAssignableFrom(c)) {
                    @Suppress("UNCHECKED_CAST")
                    k.objectInstance as? T
                        ?: try {
                            k.createInstance() as T
                        } catch (e: Exception) {
                            throw LanguageAdapterException(e)
                        }
                } else {
                    throw LanguageAdapterException("Class " + c.name + " cannot be cast to " + type.name)
                }
            }
            2 -> {
                val instance = k.objectInstance ?: run {
                    return LanguageAdapter.getDefault().create(mod, value, type)
                }

                val methodList = instance::class.memberFunctions.filter { m ->
                    m.name == split[1]
                }

                k.declaredMemberProperties.find {
                    it.name == split[1]
                }?.let { field ->
                    try {
                        val fType = field.returnType

                        if (methodList.isNotEmpty()) {
                            throw LanguageAdapterException("Ambiguous $value refers to both the field and method!")
                        }

                        if (!type.kotlin.isSuperclassOf(fType.jvmErasure)) {
                            throw LanguageAdapterException("Field " + value + " cannot be cast to " + type.name + "!")
                        }

                        return field.get(instance) as T
                    } catch (e: NoSuchFieldException) {
                        // ignore
                    } catch (e: IllegalAccessException) {
                        throw LanguageAdapterException("Field $value cannot be accessed!", e)
                    }
                }

                if (!type.isInterface) {
                    throw LanguageAdapterException("Cannot proxy method " + value + " to non-interface " + type.name)
                }

                if (methodList.isEmpty()) {
                    throw LanguageAdapterException("Could not find $value!")
                } else if (methodList.size >= 2) {
                    throw LanguageAdapterException("Found multiple method entries of name $value!")
                }

                val handle = try {
                    MethodHandles.lookup()
                        .unreflect(methodList[0].javaMethod)
                        .bindTo(instance)
                } catch (ex: Exception) {
                    throw LanguageAdapterException("Failed to create method handle for $value!", ex)
                }

                try {
                    return MethodHandleProxies.asInterfaceInstance(type, handle)
                } catch (ex: Exception) {
                    throw LanguageAdapterException(ex)
                }
            }
            else -> throw LanguageAdapterException("Invalid handle format: $value")
        }
    }
}