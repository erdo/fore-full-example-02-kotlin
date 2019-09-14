package foo.bar.example.fore.fullapp02.ui.common.uiutils

import android.view.View
import foo.bar.example.fore.fullapp02.App
import org.koin.android.ext.android.inject
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * Copyright © 2019 early.co. All rights reserved.
 */

inline fun <reified T : Any> View.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy { App.inst.inject<T>(qualifier, parameters) }
