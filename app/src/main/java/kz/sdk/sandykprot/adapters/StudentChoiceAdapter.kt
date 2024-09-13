package kz.sdk.sandykprot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kz.sdk.sandykprot.R
import kz.sdk.sandykprot.base.BaseStudentViewHolder
import kz.sdk.sandykprot.database.StudentEntity
import kz.sdk.sandykprot.databinding.ItemChooseStudentBinding

class StudentChoiceAdapter : ListAdapter<StudentEntity, StudentChoiceAdapter.StudentViewHolder>(StudentDiffUtils()) {

    private var selectedStudent: StudentEntity? = null
    private var onItemClickListener: ((StudentEntity) -> Unit)? = null
    var studentName:String? =  null

    class StudentDiffUtils : DiffUtil.ItemCallback<StudentEntity>() {
        override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemChooseStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.bindView(student)

        holder.itemView.setOnClickListener {
            selectedStudent = student
            notifyDataSetChanged()
            onItemClickListener?.invoke(student)
        }

        if (student == selectedStudent) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.choice_bg))
            holder.binding.checkboxDelete.visibility = View.VISIBLE
            holder.binding.checkboxDelete.isChecked = true
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.secondary_bg))
            holder.binding.checkboxDelete.visibility = View.GONE
            holder.binding.checkboxDelete.isChecked = false
        }
    }

    fun setOnItemClickListener(listener: (StudentEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun getSelectedStudent(): StudentEntity? = selectedStudent

    fun deleteSelectedStudent() {
        selectedStudent?.let { student ->
            if (currentList.contains(student)) {
                val updatedList = currentList.toMutableList().apply {
                    remove(student)
                }
                selectedStudent = null
                submitList(updatedList)
            }
        }
    }

    inner class StudentViewHolder( binding: ItemChooseStudentBinding) : BaseStudentViewHolder<ItemChooseStudentBinding>(binding) {

        override fun bindView(item: StudentEntity) {
            binding.tvStudentName.text = item.studentName

            binding.checkboxDelete.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedStudent = item
                    studentName = item.studentName
                } else {
                    if (selectedStudent == item) {
                        selectedStudent = null
                    }
                }
            }

            binding.checkboxDelete.isChecked = (item == selectedStudent)
        }
    }
}
