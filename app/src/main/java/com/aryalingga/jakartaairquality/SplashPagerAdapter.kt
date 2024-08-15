package com.aryalingga.jakartaairquality

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aryalingga.jakartaairquality.databinding.SplashPageBinding

class SplashPagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<SplashPagerAdapter.SplashViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplashViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SplashPageBinding.inflate(inflater, parent, false)
        return SplashViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SplashViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class SplashViewHolder(private val binding: SplashPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.imageView.setImageResource(imageRes)
        }
    }
}
