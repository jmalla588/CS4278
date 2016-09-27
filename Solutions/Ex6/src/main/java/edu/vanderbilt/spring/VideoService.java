package edu.vanderbilt.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoService {

	private VideoRepository videos_;
	
	@Autowired
	public VideoService(VideoRepository repo) {
		videos_ = repo;
	}
	
	@RequestMapping(value="/video/{id}", method=RequestMethod.GET)
	public Video getVideo(@PathVariable("id") Long videoId){
		System.out.println("id:"+videoId);
		return videos_.findOne(videoId);
	}
	
	@RequestMapping(value="/video", method=RequestMethod.GET)
	public Iterable<Video> findAll(){
		return videos_.findAll();
	}
	
	@RequestMapping(value="/video/{id}", method=RequestMethod.DELETE)
	public String deleteVideo(@PathVariable("id") Long videoId){
		try {
			videos_.delete(videoId);
		} catch (IllegalArgumentException exc){
			System.out.println("Video Not Found!");
			return "Video not found";
		}
		return "Successfully deleted video!";
	}
	
	
	@RequestMapping(value="/video", method=RequestMethod.POST)
	public Iterable<Video> addVideo(@RequestBody Video video){
		videos_.save(video);
		return videos_.findAll();
	}

	@RequestMapping(value="/video/{id}", method=RequestMethod.POST)
	public Video updateVideo(@PathVariable("id") Long videoId, @RequestBody Video video){
		video.setId(videoId);
		return videos_.save(video);
	}
	
	@RequestMapping(value="/test/foo", method=RequestMethod.GET)
	public String myTest(){
		System.out.println("footest");
		return "Test";
	}
	
}
