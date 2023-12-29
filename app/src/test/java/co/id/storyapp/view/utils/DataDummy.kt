package co.id.storyapp.view.utils

import co.id.storyapp.data.pref.ListStoryItem
import co.id.storyapp.data.pref.UserModel

object DataDummy {
    fun generateDummyUserModel(): List<ListStoryItem> {
        val userList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val user = ListStoryItem(
                "Photo ke $i",
                "AZiwjsiadw9321jkawd",
                "name ke $i",
                "deskripsi",
                0.1,
                "id user",
                0.1
            )
            userList.add(user)
        }
        return userList
    }
}