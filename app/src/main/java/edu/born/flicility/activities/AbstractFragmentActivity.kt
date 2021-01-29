package edu.born.flicility.activities

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import edu.born.flicility.R
import edu.born.flicility.model.Photo

abstract class AbstractFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        val manager = supportFragmentManager
        // find exists fragment container
        var fragment = manager.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            // add fragment
            fragment = getFragment()
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    @LayoutRes
    protected fun getLayoutResId(): Int = R.layout.activity_fragment

    protected abstract fun getFragment(): Fragment
}