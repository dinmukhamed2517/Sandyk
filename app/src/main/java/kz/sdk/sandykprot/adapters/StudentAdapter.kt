package kz.sdk.sandykprot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.base.BaseStudentViewHolder
import kz.sdk.sandykprot.database.StudentEntity
import kz.sdk.sandykprot.databinding.ItemStudentBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class StudentAdapter: ListAdapter<StudentEntity, BaseStudentViewHolder<*>>(StudentDiffUtils()) {


    private var onItemClickListener: ((StudentEntity) -> Unit)? = null
    class StudentDiffUtils : DiffUtil.ItemCallback<StudentEntity>() {
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseStudentViewHolder<*> {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseStudentViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(getItem(position))
            }
        }
    }

    fun setOnItemClickListener(listener: (StudentEntity) -> Unit) {
        onItemClickListener = listener
    }
    inner class StudentViewHolder(binding: ItemStudentBinding) : BaseStudentViewHolder<ItemStudentBinding>(binding) {

        override fun bindView(item: StudentEntity) {
            binding.apply {
                tvStudentNumber.text = item.studentIdInClass.toString()+"."
                tvStudentName.text = item.studentName
            }
        }
    }
}
