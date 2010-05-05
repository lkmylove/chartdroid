/**
 * Copyright (C) 2009 Karl Ostmo
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.achartengine.activity;

import com.googlecode.chartdroid.core.ColumnSchema;

import org.achartengine.consumer.DataCollector;
import org.achartengine.consumer.DoubleDatumExtractor;
import org.achartengine.consumer.DataCollector.SeriesMetaData;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.util.MathHelper.MinMax;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



abstract public class XYSpatialChartActivity extends XYChartActivity {


	public static class RenderingAxesContainer extends AxesContainer {
		XYMultipleSeriesRenderer renderer;
		List<String> axis_labels;
	}
	
	
	RenderingAxesContainer getAxesSets(Uri intent_data) {
		

		RenderingAxesContainer axes_container = new RenderingAxesContainer();
		
		List<? extends List<? extends List<? extends Number>>> sorted_series_list = DataCollector.getGenericSortedSeriesData(
				intent_data,
				getContentResolver(),
				new DoubleDatumExtractor());

		assert( sorted_series_list.size() >= 1 );


		if (sorted_series_list.size() == 1) {
			// Let the Y-axis carry the only data.
			axes_container.x_axis_series = new ArrayList<List<Number>>();
			axes_container.y_axis_series = (List<List<Number>>) sorted_series_list.get( 0 );

		} else {
			axes_container.x_axis_series = (List<List<Number>>) sorted_series_list.get( ColumnSchema.X_AXIS_INDEX );
			axes_container.y_axis_series = (List<List<Number>>) sorted_series_list.get( ColumnSchema.Y_AXIS_INDEX );    
		}


		assert (axes_container.x_axis_series.size() == axes_container.y_axis_series.size()
				|| axes_container.x_axis_series.size() == 1
				|| axes_container.x_axis_series.size() == 0);

		List<SeriesMetaData> series_meta_data = DataCollector.getSeriesMetaData( getIntent(), getContentResolver() );
		axes_container.titles = new String[series_meta_data.size()];
		for (int i=0; i<series_meta_data.size(); i++)
			axes_container.titles[i] = series_meta_data.get(i).title;
		
		
		assert (axes_container.titles.length == axes_container.y_axis_series.size());


		assert (axes_container.titles.length == axes_container.y_axis_series.get(0).size());


		// If there is no x-axis data, just fill it in by numbering the y-elements.
		List<Number> prototypical_x_values; 
		if (axes_container.x_axis_series.size() == 0) {
			for (int i=0; i < axes_container.y_axis_series.size(); i++) {
				prototypical_x_values = new ArrayList<Number>();
				axes_container.x_axis_series.add( prototypical_x_values );
				for (int j=0; j < axes_container.y_axis_series.get(i).size(); j++)
					prototypical_x_values.add(j);
			}
		}


		// Replicate the X-axis data for each series if necessary
		if (axes_container.x_axis_series.size() == 1) {
			Log.i(TAG, "Replicating x-axis series...");
			prototypical_x_values = axes_container.x_axis_series.get(0);
			Log.d(TAG, "Size of prototypical x-set: " + prototypical_x_values.size());
			while (axes_container.x_axis_series.size() < axes_container.titles.length)
				axes_container.x_axis_series.add( prototypical_x_values );
		}



		axes_container.axis_labels = DataCollector.getAxisTitles(getIntent(), getContentResolver());





		axes_container.renderer = org.achartengine.ChartGenHelper.buildRenderer(series_meta_data);
		assignAxesExtents(axes_container.renderer, axes_container.x_axis_series, axes_container.y_axis_series);
		
		
		return axes_container;
	}
	

	// ========================================================================
	@Override
	MinMax getYAxisLimits(List<List<Number>> multi_series) {
		return getAxisLimits(multi_series);
	}

	// ========================================================================
	@Override
	MinMax getXAxisLimits(List<List<Number>> multi_series) {
		return getAxisLimits(multi_series);
	}
}