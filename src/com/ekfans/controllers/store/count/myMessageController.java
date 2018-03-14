package com.ekfans.controllers.store.count;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class myMessageController {
    @RequestMapping(value = "/store/count/myMessage")
    public String myMessage(HttpServletRequest request){
	try {
	    return "store/count/myMessage/myMessage";   
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return "error";
    }
    
    @RequestMapping(value = "/store/count/myMessage2")
    public String myMessage2(HttpServletRequest request){
	try {
	 return "store/count/myMessage/myMessage2";  
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return "error";
    }
    
    @RequestMapping(value = "/store/count/myMessage3")
    public String myMessage3(HttpServletRequest request){
	try {
	    return "/store/count/myMessage/myMessage3";
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return "error";
    }
}
