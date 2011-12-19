class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
        // apply constraints here
      }
    }

    "/" (controller:"search", action:"search")
    "500"(view:'/error')
  }
}
