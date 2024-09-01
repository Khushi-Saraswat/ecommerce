package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CommnServiceImpl implements CommonService {

	@Override
	public void removeSessionMessage() {
		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * (RequestContextHolder.getRequestAttributes()))
		 * .getRequest();
		 * HttpSession session = request.getSession();
		 * session.removeAttribute("succMsg");
		 * session.removeAttribute("errorMsg");
		 */
		// Obtain the current request attributes
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			HttpSession session = request.getSession();
			session.removeAttribute("succMsg");
			session.removeAttribute("errorMsg");
		} else {
			// Handle the case where RequestContextHolder.getRequestAttributes() is null
			// This might involve logging the issue or taking alternative action
			// For example:
			System.out.println("Request attributes are not available.");
		}
	}

}