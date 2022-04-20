package com.codewithharry.edvorasample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NearestAdapter(private val mContext: Context):
    RecyclerView.Adapter<NearestAdapter.RidesViewHolder>() {

    private var ridesList: List<Ride>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false)
        return RidesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RidesViewHolder, position: Int) {
        val ride: Ride? = ridesList?.get(position)

        val city: String? = ride?.city
        val state: String? = ride?.state
        val rideId: String = ride?.id.toString()
        val originStation: String = ride?.origin_station_code.toString()
        val date: String? = ride?.date
        val distance: String = ride?.distanceFromUser.toString()
        val mapUrl:String? = ride?.map_url

        holder.cityTextView.text = city
        holder.stateTextView.text = state
        holder.rideIdTextView.text = rideId
        holder.originStationTextView.text = originStation
        holder.dateTextView.text = date
        holder.distanceTextView.text = distance

        Glide.with(mContext).load(mapUrl).into(holder.mapImageView)
    }

    override fun getItemCount(): Int {
        if (ridesList != null) return ridesList!!.size
        else return 0
    }

    fun setRideList(rides: List<Ride>?) {
        ridesList = rides
        notifyDataSetChanged()
    }

    public inner class RidesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var cityTextView: TextView = itemView.findViewById(R.id.city_name)
        public var rideIdTextView: TextView = itemView.findViewById(R.id.ride_id)
        public var originStationTextView: TextView = itemView.findViewById(R.id.origin_station)
        public var dateTextView: TextView = itemView.findViewById(R.id.date)
        public var distanceTextView: TextView = itemView.findViewById(R.id.distance)
        public var stateTextView: TextView = itemView.findViewById(R.id.state_name)
        var mapImageView: ImageView = itemView.findViewById(R.id.map_image_view)
    }
}