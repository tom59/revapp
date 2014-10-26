package com.hcomemea.rev.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hcomemea.rev.domain.BestHotelSearchRequest;
import com.hcomemea.rev.domain.DestinationHintRequest;
import com.hcomemea.rev.domain.HotelReviewSearchRequest;
import com.hcomemea.rev.domain.SemanticSearchRequest;
import com.hcomemea.rev.service.InternalAnalysisService;
import com.hcomemea.rev.service.ReviewSearchService;

@Controller
public class SemanticAnalysisController {

    @Autowired
    private InternalAnalysisService internalAnalysisService;
    
    @RequestMapping("/internalanalysis")
    public Model semanticSearch(Model model, @RequestParam(required=false) Integer page) {
    	model.addAttribute("analysisResult", internalAnalysisService.getAnalysedReviews(page != null? page : 1));
        return model;
    }
}
