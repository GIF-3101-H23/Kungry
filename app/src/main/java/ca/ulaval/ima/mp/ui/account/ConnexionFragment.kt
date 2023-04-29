package ca.ulaval.ima.mp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.databinding.FragmentConnexionBinding

import ca.ulaval.ima.mp.MainActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import ca.ulaval.ima.mp.ConnexionHolder

import ca.ulaval.ima.mp.utilities.AccountLogin
import ca.ulaval.ima.mp.utilities.InteractionAPI
import ca.ulaval.ima.mp.utilities.getInstanceAPI

class ConnexionFragment : Fragment() {

    private var _binding: FragmentConnexionBinding?=null

    lateinit var mTextMotDePasse: EditText
    lateinit var mTextEmail: EditText
    lateinit var mBtnConnexion: Button
    lateinit var mTextIncription: TextView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle("Connexion")
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConnexionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mTextEmail = binding.inputCourriel
        mTextIncription = binding.inscrire
        mBtnConnexion = binding.btnConnecter
        mTextMotDePasse = binding.inputMotDePasse



        mBtnConnexion.setOnClickListener {
            if( mTextEmail.text.toString() != "" && mTextMotDePasse.text.toString() != "" ){

                val login = AccountLogin(mTextEmail.text.toString(),mTextMotDePasse.text.toString())

                val api = getInstanceAPI()
                api.loginAccount(login)

                if (!api.accountDoesntExist && api.connected) {
                    val fragment = CompteFragment()
                    loadFragment(fragment)
                }
                else if (api.accountDoesntExist) {
                    Toast.makeText(context, "Le compte n'existe pas", Toast.LENGTH_SHORT).show()
                    api.accountDoesntExist = false
                }
                else if (!api.connected) {
                    Toast.makeText(context, "Problème de connexion", Toast.LENGTH_SHORT).show()
                }

            }
        }

        mTextIncription.setOnClickListener{
            loadFragment(CreationCompte())
        }

        return root
    }

    fun loadFragment(fragment : Fragment) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()

        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}