package com.pangpang.web3.controller;

import com.pangpang.web3.service.SpitterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by root on 16-10-2.
 */
@Controller
public class HomeController {
    public static final int DEFAULT_SPITTLES_PER_PAGE = 25;

    private SpitterService spitterService;

    @Inject
    public HomeController(SpitterService spitterService) {
        this.spitterService = spitterService;
    }

    @RequestMapping({"/","/home"})
    public String showHomePage(Map<String, Object> model){
        model.put("spittles", spitterService.getRecentSplittles(DEFAULT_SPITTLES_PER_PAGE));
        return "home";
    }

}
