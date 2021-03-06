/**
 * Copyright 2014 SAP AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spotter.ext.detection.stifle;

import java.util.ArrayDeque;
import java.util.Deque;

public class StifleQuery {
	private String query;
	private final Deque<Integer> occurrences = new ArrayDeque<>();
	
	public StifleQuery(String query) {
		setQuery(query);
	}
	
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	/**
	 * @return the occurrences
	 */
	public Deque<Integer> getOccurrences() {
		return occurrences;
	} 
	
	/**
	 * Increase the occurence by one.
	 * 
	 * @return the occurrences
	 */
	public void increaseOccurence() {
		int counter = occurrences.pollFirst();
		counter++;
		occurrences.offerFirst(counter);
	} 
}
