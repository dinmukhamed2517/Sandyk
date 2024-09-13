package kz.sdk.sandykprot.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.sdk.sandykprot.database.ClassEntity
import kz.sdk.sandykprot.database.GroupEntity
import kz.sdk.sandykprot.database.StudentEntity

abstract class BaseViewHolder<VB : ViewBinding, T>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bindView(item: T)
}

abstract class BaseStudentViewHolder<VB : ViewBinding>(public override val binding: VB) :
    BaseViewHolder<VB, StudentEntity>(binding)


abstract class BaseClassViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, ClassEntity>(binding)

abstract class BaseGroupViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, GroupEntity>(binding)


