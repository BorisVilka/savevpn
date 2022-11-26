package org.safevpn.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.safevpn.main.MyService.MyBinder
import org.safevpn.main.databinding.FragmentLoadedBinding


class FragmentLoaded : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var myService: MyService? = null
    var sConn: ServiceConnection? = null
    lateinit var binding: FragmentLoadedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        requireActivity().startService(Intent(requireActivity(),MyService::class.java))
        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                myService = (binder as MyBinder).getService()
                Thread {
                   while(isVisible && !isDetached) {
                       requireActivity().runOnUiThread {
                           binding.textView3.setText("Время подключения: "+String.format("%d:%d",(myService!!.time/60),myService!!.time%60))
                       }
                       Thread.sleep(1000)
                   }
                }.start()
            }

            override fun onServiceDisconnected(name: ComponentName) {

            }
        }
        requireActivity().bindService(Intent(requireActivity(),MyService::class.java),
            sConn as ServiceConnection, 0);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoadedBinding.inflate(inflater,container,false)
        binding.button.setOnClickListener {
            requireActivity().stopService(Intent(requireActivity(),MyService::class.java))
            requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("loaded",false)
                .apply()
            val controller = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            controller.popBackStack()
            controller.navigate(R.id.fragmentStart)
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLoaded().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onResume() {
        super.onResume()
        Thread {
            while(isVisible && !isDetached) {
                requireActivity().runOnUiThread {
                    if(myService!=null) binding.textView3.setText("Время подключения: "+String.format("%d:%d",(myService!!.time/60),myService!!.time%60))
                }
                Thread.sleep(1000)
            }
        }.start()
    }
}