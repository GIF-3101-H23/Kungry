package ca.ulaval.ima.mp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.fragment.app.FragmentTransaction

import ca.ulaval.ima.mp.utilities.getInstanceAPI

import ca.ulaval.ima.mp.ui.account.ConnexionFragment
import ca.ulaval.ima.mp.ui.account.CompteFragment

class ConnexionHolder: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)

        val view = inflater.inflate(R.layout.fragment_holder, container, false)

        val fragment_connected  = CompteFragment()
        val fragment_noconnected  = ConnexionFragment()
        val connected = getInstanceAPI().connected

        if (connected == true )
        {
            loadFragment(fragment_connected)

        }
        else {

            loadFragment(fragment_noconnected)

        }

        return view
    }

    fun loadFragment(fragment: Fragment) {

        val transaction = childFragmentManager.beginTransaction()

        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}