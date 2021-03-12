package com.example.orientationpractice

import android.os.Bundle
import androidx.fragment.app.Fragment

class RetainerFragment : Fragment() {

    lateinit var countObject: Count

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    fun setData(newCountObject: Count) {
        countObject = newCountObject
    }

    fun getData() = countObject
}