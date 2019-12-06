package com.example.booksappandroid.screens.books

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booksappandroid.R
import com.example.booksappandroid.core.TAG
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.chart_fragment.*


class ChartFragment : Fragment() {

    private var entries = arrayListOf<BarEntry>()
    private var labels = arrayListOf<String>()
    private var size = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey("size")) {
                val value = it.getInt("size")
                size = value
                for (i in 0 until value) {
                    val price = it.getFloat("book$i")
                    val title = it.getString("booktitle$i")
                    entries.add(BarEntry(i.toFloat(), price))
                    labels.add(title!!)
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.chart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupChart()
    }

    private fun setupChart() {

        val barDataSet = BarDataSet(entries, "Price")
        barDataSet.color = R.color.colorAccent
        barDataSet.valueTextSize = 12f
        val data = BarData(barDataSet)
        barChartView.data = data
        barChartView.animateXY(2000, 2000)
        barChartView.invalidate()

        barChartView.xAxis.valueFormatter = IndexAxisValueFormatter(labels) as ValueFormatter?
        barChartView.xAxis.setDrawGridLines(false)
        barChartView.xAxis.textSize=12f
        barChartView.xAxis.position=XAxis.XAxisPosition.BOTTOM
        barChartView.xAxis.labelCount=size

    }
}
