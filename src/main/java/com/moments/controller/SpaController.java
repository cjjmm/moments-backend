package com.moments.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SPA 路由转发控制器
 * 处理前端管理后台的所有路由，将它们转发到 index.html
 */
@Controller
public class SpaController {

  /**
   * 处理管理后台的所有路由请求
   * 将 /admin 及其子路由转发到 index.html，由 Vue Router 处理
   */
  @GetMapping(value = { "/admin", "/admin/login", "/admin/dashboard", "/admin/dashboard/**" })
  public String forwardAdmin() {
    return "forward:/admin/index.html";
  }
}
