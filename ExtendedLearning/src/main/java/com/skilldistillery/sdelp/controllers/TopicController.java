package com.skilldistillery.sdelp.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.skilldistillery.sdelp.data.ContentDAO;
import com.skilldistillery.sdelp.data.ContentDAOJpaImpl;
import com.skilldistillery.sdelp.data.LogDAO;
import com.skilldistillery.sdelp.data.LogDAOJpaImpl;
import com.skilldistillery.sdelp.data.ResourceDAO;
import com.skilldistillery.sdelp.data.ResourceDAOJpaImpl;
import com.skilldistillery.sdelp.data.TopicCommentDAO;
import com.skilldistillery.sdelp.data.TopicCommentDAOJpaImpl;
import com.skilldistillery.sdelp.data.TopicDAO;
import com.skilldistillery.sdelp.data.TopicDAOJpaImpl;
import com.skilldistillery.sdelp.data.UserProfileDAO;
import com.skilldistillery.sdelp.data.UserProfileDAOJpaImpl;
import com.skilldistillery.sdelp.entities.Content;
import com.skilldistillery.sdelp.entities.Log;
import com.skilldistillery.sdelp.entities.Profile;
import com.skilldistillery.sdelp.entities.Resource;
import com.skilldistillery.sdelp.entities.Topic;
import com.skilldistillery.sdelp.entities.TopicComment;

@Controller
public class TopicController {

	@Autowired
	TopicDAO topicdao = new TopicDAOJpaImpl();

	@Autowired
	ContentDAO contentdao = new ContentDAOJpaImpl();

	@Autowired
	ResourceDAO resourcedao = new ResourceDAOJpaImpl();
	
	@Autowired
	TopicCommentDAO commentdao = new TopicCommentDAOJpaImpl();
	
	@Autowired
	UserProfileDAO userdao;
	
	@Autowired
	LogDAO logdao = new LogDAOJpaImpl();

	@RequestMapping(path = "showSingleTopic.do", method = RequestMethod.GET)
	public String showTopicList(@RequestParam("topicId") Integer cid, Model model, HttpSession session) {
		Topic topic = topicdao.getTopicById(cid);
		model.addAttribute("topic", topic);
		if (topic != null) {
			try {
				Profile profile = (Profile) session.getAttribute("profile");
				if (profile == null) {
					return "topic_page";
				}
				Log log = new Log();
				log.setTopic(topic);
				log.setUser(profile.getUser());
				log.setTimeStamp(LocalDateTime.now());
				logdao.writeLog(log);
				List<TopicComment> comments = commentdao.getAllCommentsForTopic(topic);
				model.addAttribute("comments", comments);
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
		return "topic_page";
	}

	@RequestMapping(path = "showAllTopics.do")
	public String showAllTopics(Model model) {
		List<Topic> topics = topicdao.getAllTopics();
		model.addAttribute("topics", topics);
		return "content_index";
	}

	@RequestMapping(path = "showTopicsBySearch.do", method = RequestMethod.POST)
	public String showTopicsBySearch(@RequestParam("keyword") String keyword, Model model) {
		List<Topic> topics = topicdao.findTopicsBySearchTerm(keyword);
//		model.addAttribute("keyword", keyword);
		model.addAttribute("topics", topics);
		return "content_index";
	}

	@RequestMapping(path = "showNewTopic.do")
	public String showNewTopic() {
		return "newTopic";
	}

	@RequestMapping(path = "attemptCreateTopic.do", method = RequestMethod.POST)
	public String attemptCreateTopic(Model model, @RequestParam String title, @RequestParam String content,
			@RequestParam String resourceTitle, @RequestParam String resourceUrl) {
		Topic addTopic = new Topic();
		addTopic.setTitle(title);
		topicdao.addTopic(addTopic);
		Content addContent = new Content();
		addContent.setContent(content);
		addContent.setTopic(addTopic);
		contentdao.addContent(addContent);
		Resource addResource = new Resource();
		addResource.setTitle(resourceTitle);
		addResource.setResourceUrl(resourceUrl);
		addResource.setTopic(addTopic);
		resourcedao.addResource(addResource);

		Topic newTopic = topicdao.getTopicById(addTopic.getId());
		model.addAttribute("topic", newTopic);
		
		return "redirect:showSingleTopic.do?topicId=" + newTopic.getId();
	}

	@RequestMapping(path = "showUpdateTopic.do")
	public String updateTopic(Model model, @RequestParam Integer cid) {
		Topic topic = topicdao.getTopicById(cid);
		model.addAttribute("topic", topic);
		return "update_topic";
	}

	@RequestMapping(path = "attemptUpdateTopic.do", method = RequestMethod.POST)
	public String attemptUpdateTopic(Model model, @RequestParam Integer tid, @RequestParam String title,
			@RequestParam String content, @RequestParam String resourceTitle, @RequestParam String resourceUrl) {
		Topic updatingTopic = topicdao.getTopicById(tid);
		Content updatingFirstContent = updatingTopic.getContents().get(0);
		Resource updatingFirstResource = updatingTopic.getResources().get(0);

		updatingTopic.setTitle(title);
		topicdao.updateTopic(updatingTopic);
		updatingFirstContent.setContent(content);
		contentdao.updateContent(updatingFirstContent);
		updatingFirstResource.setTitle(resourceTitle);
		updatingFirstResource.setResourceUrl(resourceUrl);
		resourcedao.updateResource(updatingFirstResource);

		Topic updatedTopic = topicdao.getTopicById(tid);
		model.addAttribute("topic", updatedTopic);	
		return "content_index";
	}
	
	@RequestMapping(path = "addComment.do", method = RequestMethod.POST)
	public String addComment(Model model, 
			@RequestParam("title") String title, 
			@RequestParam("comment") String content, 
			@RequestParam("topicId") Integer topicId, 
			HttpSession session){
		int userId = ((Profile) session.getAttribute("profile")).getUser().getId();
		Topic topic = topicdao.getTopicById(topicId);
		TopicComment comment = new TopicComment();
		comment.setContent(content);
		comment.setTitle(title);
		comment.setUser(userdao.getUserById(userId));
		comment.setTopic(topicdao.getTopicById(topicId));
		comment.setActive(true);
		
		comment = commentdao.addTopicComment(comment);
//		model.addAttribute("comment", comment);
		model.addAttribute("topic", topic);
		return "topic_page";
	}
	
	@RequestMapping(path="hideComment.do")
	public String hideComment(Model model,
			@RequestParam("cid") Integer cid) {
		TopicComment tc = commentdao.getTopicCommentById(cid);
		tc.setActive(false);
		commentdao.updateTopicComment(tc);
		Topic topic = tc.getTopic();
		model.addAttribute("topic", topic);
		return "topic_page";
	}
//	public String updateComments(Model model) {
//		
//	}
	
}
