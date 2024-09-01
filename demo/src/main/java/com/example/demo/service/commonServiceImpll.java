package com.example.demo.service;

import org.springframework.stereotype.Component;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import jakarta.servlet.http.HttpSession;


@Component
public class commonServiceImpll implements CommonService {
       @Override
	public void removeSessionMessage() {
	       try{
			System.out.println("removing message form session ");
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
			 session.removeAttribute("succMsg");
			 session.removeAttribute("errorMsg");
		
			}catch(Exception e){
				e.printStackTrace();
			}
             
	}
}



