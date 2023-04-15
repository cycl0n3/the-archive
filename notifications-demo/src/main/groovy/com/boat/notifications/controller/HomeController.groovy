package com.boat.notifications.controller

import org.springframework.ui.Model

import org.springframework.stereotype.Controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

import org.springframework.beans.factory.annotation.Value


@Controller
class HomeController {

  @GetMapping("/")
  String homePage(Model model) {
    model.addAttribute("x", 1)
    return "home"
  }
}
