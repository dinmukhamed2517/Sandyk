package kz.sdk.sandykprot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import kz.sdk.sandykprot.base.BaseClassViewHolder
import kz.sdk.sandykprot.database.ClassEntity
import kz.sdk.sandykprot.databinding.ItemClassBinding
import kz.sdk.sandykprot.screens.ListFragmentDirections

class ClassAdapter(private val navController: NavController) : ListAdapter<ClassEntity, BaseClassViewHolder<*>>(ClassDiffUtils()) {

    class ClassDiffUtils : DiffUtil.ItemCallback<ClassEntity>() {
        override fun areItemsTheSame(oldItem: ClassEntity, newItem: ClassEntity): Boolean {
            return oldItem.className == newItem.className
        }

        override fun areContentsTheSame(oldItem: ClassEntity, newItem: ClassEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseClassViewHolder<*> {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseClassViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class ClassViewHolder(binding: ItemClassBinding) : BaseClassViewHolder<ItemClassBinding>(binding) {

        override fun bindView(item: ClassEntity) {
            binding.apply {
                tvClassName.text = "Название класса: ${item.className}"

                recyclerViewStudents.apply {
                    layoutManager = GridLayoutManager(context,2 )
                    adapter = StudentAdapter().apply {
                        submitList(item.students)
                    }
                }

                editButton.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToAddFragment(item.className)
                    navController.navigate(action)
                }

                deleteButton.setOnClickListener {
                    val action  = ListFragmentDirections.actionListFragmentToDeleteFragment(item.className)
                    navController.navigate(action)
                }
            }
        }
    }
}