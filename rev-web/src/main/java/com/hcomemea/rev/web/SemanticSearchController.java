package com.hcomemea.rev.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hcomemea.rev.domain.BestHotelSearchRequest;
import com.hcomemea.rev.domain.DestinationHintRequest;
import com.hcomemea.rev.domain.HotelReviewSearchRequest;
import com.hcomemea.rev.domain.SemanticSearchRequest;
import com.hcomemea.rev.service.ReviewSearchService;

@Controller
public class SemanticSearchController {

    @Autowired
    private ReviewSearchService reviewSearchService;
    
    @RequestMapping("/semanticsearch")
    public Model semanticSearch(Model model, @ModelAttribute("searchRequest") SemanticSearchRequest searchRequest, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", "Errors during binding");
        } else {
            if (searchRequest != null) {
                model.addAttribute("searchResult", reviewSearchService.semanticSearch(searchRequest));
            }
        }
        return model;
    }

    @RequestMapping("/findreviews")
    public Model findReviews(Model model, @ModelAttribute("searchRequest") HotelReviewSearchRequest searchRequest, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", "Errors during binding");
        } else {
            if (searchRequest != null && searchRequest.getHotelId() != null) {
                model.addAttribute("searchResult", reviewSearchService.findHotelReviews(searchRequest));
            }
        }
        return model;
    }

    @RequestMapping("/findbesthotels")
    public Model findBestHotels(Model model, @ModelAttribute("searchRequest") BestHotelSearchRequest searchRequest, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", "Errors during binding");
        } else {
            if (searchRequest != null && searchRequest.getDestinationId() != null) {
                model.addAttribute("searchResult", reviewSearchService.findBestHotelReviewSearchResult(searchRequest));
            }
        }
        return model;
    }

    @RequestMapping("/finddestinationhints")
    public Model findDestinationHints(Model model, @ModelAttribute("searchRequest") DestinationHintRequest searchRequest, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("errors", "Errors during binding");
        } else {
            if (searchRequest != null && searchRequest.getDestinationId() != null) {
                model.addAttribute("searchResult", reviewSearchService.findDestinationHint(searchRequest));
            }
        }
        return model;
    }

}
