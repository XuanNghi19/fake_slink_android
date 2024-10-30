package com.example.fake_slink.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fake_slink.R
import com.example.fake_slink.model.response.StudentResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ItemStudentsInClassAdapter(
    private val context: Activity,
    private val list: List<StudentResponse>
) :ArrayAdapter<StudentResponse>(
    context,
    R.layout.item_student_list,
    list
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.item_student_list, null, true)
        val studentResponse = list[position]
        val avatar_image = rowView.findViewById<CircleImageView>(R.id.avatar_image)
        Picasso.get()
            .load(studentResponse?.avatarUrl)
            .into(avatar_image)
        val name = rowView.findViewById<TextView>(R.id.name)
        name.text = studentResponse.name

        val id_num = rowView.findViewById<TextView>(R.id.id_num)
        id_num.text = studentResponse.idNum
        return rowView
    }
}