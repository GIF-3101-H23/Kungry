package ca.ulaval.ima.mp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ca.ulaval.ima.mp.ConnexionHolder
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.utilities.Account
import ca.ulaval.ima.mp.utilities.getInstanceAPI
import ca.ulaval.ima.mp.databinding.FragmentMonCompteBinding


class CompteFragment: Fragment(){

    private var _binding: FragmentMonCompteBinding? = null

    lateinit var account : Account

    lateinit var mTextNom: TextView
    lateinit var mTextEmail: TextView
    lateinit var mTextNbrEval: TextView
    lateinit var mBtnDeco: Button

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle("Account")
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMonCompteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        account = getInstanceAPI().getMe()!!

        initComponents(root)

        return root
    }

    fun initComponents(v : View) {
        mTextNom = v.findViewById(R.id.nom_compte)
        mTextEmail = v.findViewById(R.id.email_compte)
        mTextNbrEval = v.findViewById(R.id.evalnbr_compte)
        mBtnDeco = v.findViewById(R.id.decoBtn)

        mTextNom.text = "${account.first_name} ${account.last_name}"
        mTextEmail.text = account.email
        mTextNbrEval.text = account.total_review_count.toString()

        mBtnDeco.setOnClickListener {
            getInstanceAPI().disconnect()
            loadFragment(ConnexionFragment())
        }
    }


    fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()

        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}