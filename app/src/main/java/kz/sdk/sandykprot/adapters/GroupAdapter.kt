package kz.sdk.sandykprot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import kz.sdk.sandykprot.base.BaseGroupViewHolder
import kz.sdk.sandykprot.database.GroupEntity
import kz.sdk.sandykprot.databinding.ItemGroupBinding

class GroupAdapter:ListAdapter<GroupEntity, BaseGroupViewHolder<*>>(GroupDiffUtils()) {

    class GroupDiffUtils:DiffUtil.ItemCallback<GroupEntity>(){
        override fun areItemsTheSame(oldItem: GroupEntity, newItem: GroupEntity): Boolean {
            return oldItem.groupName == newItem.groupName
        }

        override fun areContentsTheSame(oldItem: GroupEntity, newItem: GroupEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseGroupViewHolder<*> {
        return GroupViewHolder(ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseGroupViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class GroupViewHolder(binding: ItemGroupBinding):BaseGroupViewHolder<ItemGroupBinding>(binding){
        override fun bindView(item: GroupEntity) {
            binding.apply {
                tvGroupName.text = "${item.groupName}"
                recyclerViewStudents.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = StudentAdapter().apply {
                        submitList(item.students)
                    }
                }
            }
        }
    }
}