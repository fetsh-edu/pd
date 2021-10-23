package klaster

import androidx.recyclerview.widget.RecyclerView

/**
 * Use [get] to build adapters.
 *
 * Use [withViewHolder] to build adapters using your own implementation of [ViewHolder].
 */
object Klaster {

    /**
     * Returns new [KlasterBuilder].
     */
    fun get(): KlasterBuilder = KlasterBuilder()

    /**
     * Returns new [KlasterBuilderWithViewHolder].
     *
     * It is intended for the cases when you need a custom [ViewHolder].
     */
    fun <VH : RecyclerView.ViewHolder> withViewHolder(): KlasterBuilderWithViewHolder<VH> = KlasterBuilderWithViewHolder()

}

class KlasterException(override val message: String) : RuntimeException()