class UrlMappings {

  static mappings = {

  //   "/$controller/$action?/$id?"{
  //     constraints {
  //       // apply constraints here
  //     }
  //   }

    "/course/$id" (controller:"course", action:"index")
    
    "/search/count" (controller:"search", action:"count")
    "/search/autocomplete" (controller:"search", action:"autocomplete")

    "/" (controller:"search", action:"index")

    "500"(view:'/error')
  }
}
