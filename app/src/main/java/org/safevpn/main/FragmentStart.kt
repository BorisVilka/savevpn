package org.safevpn.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.safevpn.main.databinding.FragmentStartBinding
import java.util.*


class FragmentStart : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("loaded",false)) {
            requireActivity().runOnUiThread {
                val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                controller.popBackStack()
                controller.navigate(R.id.fragmentLoaded)
            }
        }
        binding = FragmentStartBinding.inflate(inflater,container,false)
        binding.imageView.setOnClickListener {
            binding.imageView.visibility = View.INVISIBLE
            binding.textView.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.card.visibility = View.VISIBLE
            binding.animationView3.playAnimation()
            Thread {
                while(binding.progressBar.progress<binding.progressBar.max) {
                    requireActivity().runOnUiThread {
                        binding.progressBar.progress+=5;
                    }
                    Thread.sleep(400)
                }
                requireActivity().runOnUiThread {
                    requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("loaded",true)
                        .apply()
                    Toast.makeText(requireContext(),"Готово!",Toast.LENGTH_LONG).show()
                    val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    controller.popBackStack()
                    controller.navigate(R.id.fragmentLoaded)
                }
            }.start()
        }
        return binding.root
    }

    private lateinit var binding: FragmentStartBinding

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentStart().apply {
                arguments = Bundle().apply {

                }
            }
    }
}