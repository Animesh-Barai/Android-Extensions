package com.tunjid.androidx.baseclasses

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tunjid.androidx.navigation.Navigator
import com.tunjid.androidx.uidrivers.InsetProvider
import com.tunjid.androidx.view.util.InsetFlags

abstract class AppBaseFragment(
        @LayoutRes contentLayoutId: Int = 0
) : Fragment(contentLayoutId),
        InsetProvider,
        com.tunjid.androidx.navigation.Navigator.TagProvider {

    override val insetFlags: InsetFlags = InsetFlags.ALL

    override val stableTag: String = javaClass.simpleName

}
