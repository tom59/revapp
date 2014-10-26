package com.hcomemea.rev.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hcomemea.rev.domain.ReviewAnalysisRequest;
import com.hcomemea.rev.domain.ReviewAnalysisResult;
import com.hcomemea.rev.domain.ReviewSearchRequest;
import com.hcomemea.rev.domain.ReviewSearchResult;
import com.hcomemea.rev.service.ReviewSearchService;

@Controller
public class AdminController {

    @Autowired
    private ReviewSearchService reviewSearchService;

    @RequestMapping("/search")
    public Model search(Model model, @ModelAttribute("searchRequest") ReviewSearchRequest searchRequest, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", "Errors during binding");
        } else {
            if (searchRequest != null && searchRequest.getKeywords() != null) {
                ReviewSearchResult result = reviewSearchService.search(searchRequest);
                model.addAttribute("searchResult", result);
            }
        }
        return model;
    }

    @RequestMapping("/analyse")
    public Model search(Model model, @ModelAttribute("analyseRequest") ReviewAnalysisRequest analysisRequest, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", "Errors during binding");
        } else {
            if (analysisRequest != null && analysisRequest.getResultsNumber() != null) {
                ReviewAnalysisResult result = reviewSearchService.analyse(analysisRequest);
                model.addAttribute("analyseResult", result);
            }
        }
        return model;
    }

}
