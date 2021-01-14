/*
 * 
 * Copyright 2014 Jules White
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
 * 
 */
package org.magnum.dataup;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.magnum.dataup.model.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class MyVideoController {

	private static final AtomicLong currentId = new AtomicLong(0L);
	private Map<Long, Video> videos = new HashMap<Long, Video>();
	
	// Added new videos with unique ID for each other.
	public Video save(Video entity) {
		checkAndSetId(entity);
		entity.setDataUrl(getDataUrl(entity.getId()));
		videos.put(entity.getId(), entity);
		return entity;
	}
	
	// prevent if the app save videos with ID = 0. 
	private void checkAndSetId(Video entity) {
		if(entity.getId() == 0) {
			entity.setId(currentId.incrementAndGet());
		}
	}
	
	// return the complete URL for saved video in instruction such (http://localhost:8080/video/1/data)
	private String getDataUrl(long videoId) {
		String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
		return url;
	}
	
	// Return the base URl from local server (e.g http://localhost:8080)
	private String getUrlBaseForLocalServer() {
		HttpServletRequest request = ( (ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String base = "http://"+request.getServerName()
						+ (( request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
		return base;
	}
	
	// Controller for Get all videos saved in app.
  	@RequestMapping(value = "/video", method = RequestMethod.GET)
  	public @ResponseBody Collection<Video> getVideoList(){
		return videos.values();
  	}
  	
  	
}
