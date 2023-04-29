package ca.ulaval.ima.mp.ui.account

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ca.ulaval.ima.mp.ConnexionHolder
import ca.ulaval.ima.mp.R

import ca.ulaval.ima.mp.utilities.CreateAccountCreate
import ca.ulaval.ima.mp.utilities.InteractionAPI
import ca.ulaval.ima.mp.utilities.getInstanceAPI
import ca.ulaval.ima.mp.databinding.FragmentCreationCompteBinding


/**
 * A simple [Fragment] subclass.
 * Use the [CreationCompte.newInstance] factory method to
 * create an instance of this fragment.
 */

class CreationCompte : Fragment() {

    private var _binding: FragmentCreationCompteBinding? = null

    lateinit var mTextNom: EditText
    lateinit var mTextEmail: EditText
    lateinit var mTextPrenom: EditText
    lateinit var mTextMotPass: EditText
    lateinit var mBtnIncription:Button
    lateinit var mTextConnexion: TextView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle("Nouveau compte")
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentCreationCompteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mTextNom = binding.NomInscrip
        mTextPrenom = binding.PrenomInscrip
        mTextEmail = binding.courrielInscrip
        mTextMotPass = binding.motDePasseIns
        mBtnIncription = binding.btnInscrip
        mTextConnexion = binding.clickConnextion



        mBtnIncription.setOnClickListener{

            if(mTextPrenom.text.toString() != ""
                && mTextEmail.text.toString() != ""
                && mTextMotPass.text.toString() != ""
                && mTextNom.text.toString() != "")
            {
                val createAccountId = CreateAccountCreate(
                    mTextPrenom.text.toString(),
                    mTextNom.text.toString(),
                    mTextEmail.text.toString(),
                    mTextMotPass.text.toString())

                getInstanceAPI().createAccount(createAccountId)

                if (!getInstanceAPI().accountAlreadyExists && getInstanceAPI().connected) {
                    val fragment = CompteFragment()
                    loadFragment(fragment)

                }
                else if (getInstanceAPI().accountAlreadyExists) {
                    Toast.makeText(context,"Le compte existe déjà", Toast.LENGTH_SHORT).show()
                    getInstanceAPI().accountAlreadyExists = false
                }
                else if (!getInstanceAPI().connected) {
                    Toast.makeText(context, " Problème de réseau", Toast.LENGTH_SHORT).show()
                }



            }
            else {
                Toast.makeText(context, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show()
            }

        }
        mTextConnexion.setOnClickListener {
            loadFragment(ConnexionFragment())
        }


        return root
    }

    fun loadFragment(fragment: Fragment) {

        val transaction = parentFragmentManager.beginTransaction()

        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}