package com.example.newsfeedtestapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsfeedtestapp.data.model.Hit
import com.example.newsfeedtestapp.databinding.FragmentNewsFeedBinding
import com.example.newsfeedtestapp.ui.activity.WebViewActivity
import com.example.newsfeedtestapp.ui.adapter.NewsFeedAdapter
import com.example.newsfeedtestapp.ui.viewmodel.NewsFeedViewModel
import com.example.newsfeedtestapp.utils.toastLong
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFeedFragment : BaseFragment(), KoinComponent {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //DI
    private val newsFeedViewModel: NewsFeedViewModel by inject()

    //binding
    private var binding: FragmentNewsFeedBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Observe viewmodel
        observeViewModel()
        // Inflate the layout for this fragment
        binding = FragmentNewsFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        runViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun runViewModel() {
        newsFeedViewModel.getNewsFeedList()
    }

    private fun observeViewModel() = newsFeedViewModel.run {
        loadingState.observe(viewLifecycleOwner, Observer {

        })
        newsList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                //show toast? no news?
                requireActivity().toastLong("NO NEWS!!!")
            } else {
                requireActivity().toastLong("LOAD!")
                val rvNewsList = binding?.rvNewsList
                val adapter =
                    NewsFeedAdapter(requireActivity().applicationContext, it, onClick = onItemClick)
                rvNewsList?.adapter = adapter
                rvNewsList?.layoutManager = LinearLayoutManager(context)
            }
        })
        onError.observe(viewLifecycleOwner, Observer {
            //TODO: show toast with error!
            requireActivity().toastLong(it)
        })
    }

    private var onItemClick: (Hit) -> Unit = { hit ->
        val url = hit.url ?: hit.storyUrl
        val programDetailIntent = Intent(requireActivity(), WebViewActivity::class.java)
        programDetailIntent.putExtra("url", url)
        requireActivity().toastLong("CLICK! ${url}")
        startActivity(programDetailIntent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsFeedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsFeedFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}