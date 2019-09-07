package com.tunjid.androidbootstrap.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.tunjid.androidbootstrap.GlobalUiController
import com.tunjid.androidbootstrap.PlaceHolder
import com.tunjid.androidbootstrap.R
import com.tunjid.androidbootstrap.UiState
import com.tunjid.androidbootstrap.activityGlobalUiController
import com.tunjid.androidbootstrap.adapters.DoggoAdapter.ImageListAdapterListener
import com.tunjid.androidbootstrap.adapters.InputAdapter
import com.tunjid.androidbootstrap.baseclasses.AppBaseFragment
import com.tunjid.androidbootstrap.model.Doggo
import com.tunjid.androidbootstrap.recyclerview.ListManagerBuilder
import com.tunjid.androidbootstrap.view.util.InsetFlags
import com.tunjid.androidbootstrap.viewholders.DoggoViewHolder
import com.tunjid.androidbootstrap.viewholders.InputViewHolder

class AdoptDoggoFragment : AppBaseFragment(), GlobalUiController, ImageListAdapterListener {

    override var uiState: UiState by activityGlobalUiController()

    private lateinit var doggo: Doggo

    override val insetFlags: InsetFlags = InsetFlags.NO_TOP

    override fun getStableTag(): String {
        return super.getStableTag() + "-" + arguments!!.getParcelable<Parcelable>(ARG_DOGGO)!!.hashCode()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doggo = arguments!!.getParcelable(ARG_DOGGO)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        uiState = uiState.copy(
                showsToolbar = false,
                toolBarMenu = 0,
                fabText = getString(R.string.adopt),
                fabIcon = R.drawable.ic_hug_24dp,
                showsFab = true,
                fabExtended = !restoredFromBackStack(),
                navBarColor = ContextCompat.getColor(requireContext(), R.color.white_75),
                fabClickListener = View.OnClickListener {
                    showSnackbar { snackBar -> snackBar.setText(getString(R.string.adopted_doggo, doggo.name)) }
                }
        )

        val root = inflater.inflate(R.layout.fragment_adopt_doggo, container, false)

        ListManagerBuilder<InputViewHolder, PlaceHolder.State>()
                .withRecyclerView(root.findViewById(R.id.model_list))
                .withLinearLayoutManager()
                .withAdapter(InputAdapter(listOf(*resources.getStringArray(R.array.adoption_items))))
                .build()

        val viewHolder = DoggoViewHolder(root, this)
        viewHolder.bind(doggo)

        viewHolder.thumbnail.tint(R.color.black_50) { color, imageView -> this.setColorFilter(color, imageView) }
        viewHolder.fullSize?.let { viewHolder.fullSize.tint(R.color.black_50) { color, imageView -> this.setColorFilter(color, imageView) } }

        return root
    }

    private fun setColorFilter(color: Int, imageView: ImageView) = imageView.setColorFilter(color)

    private fun <T : View> T.tint(@ColorRes colorRes: Int, biConsumer: (Int, T) -> Unit) {
        val endColor = ContextCompat.getColor(requireContext(), colorRes)
        val startColor = Color.TRANSPARENT

        val animator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
        animator.duration = BACKGROUND_TINT_DURATION.toLong()
        animator.addUpdateListener { animation ->
            (animation.animatedValue as? Int)?.let { biConsumer.invoke(it, this) }
        }
        animator.start()
    }

    private fun prepareSharedElementTransition() {
        val baseSharedTransition = baseSharedTransition()

        sharedElementEnterTransition = baseSharedTransition
        sharedElementReturnTransition = baseSharedTransition
    }

    companion object {
        internal const val ARG_DOGGO = "doggo"

        fun newInstance(doggo: Doggo): AdoptDoggoFragment = AdoptDoggoFragment().apply {
            arguments = bundleOf(ARG_DOGGO to doggo)
            prepareSharedElementTransition()
        }
    }

}
