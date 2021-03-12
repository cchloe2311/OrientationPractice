package com.example.orientationpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    // var n = 0
    private var countObject: Count = Count(0)
    private lateinit var retainerFragment: RetainerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag(RetainerFragment::class.java.simpleName) == null) {
            retainerFragment = RetainerFragment()

            supportFragmentManager.beginTransaction().add(retainerFragment, RetainerFragment::class.simpleName).commit()
            retainerFragment.setData(countObject)
        } else retainerFragment = supportFragmentManager.findFragmentByTag(RetainerFragment::class.java.simpleName) as RetainerFragment
    }

    fun count(v: View) {
        countObject.n++
        tv_count.text = (countObject.n).toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // outState.putInt("N", n)
        retainerFragment.setData(countObject)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
//        n = savedInstanceState.getInt("N")
//        tv_count.text = n.toString()

        countObject = retainerFragment.getData()
        tv_count.text = (countObject.n).toString()
    }
}