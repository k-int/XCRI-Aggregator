class UrlMappings {

  static mappings = {

  //   "/$controller/$action?/$id?"{
  //     constraints {
  //       // apply constraints here
  //     }
  //   }

    "/course/$id" (controller:"course", action:"index")
    
    "/search/count" (controller:"search", action:"count")

    "/" (controller:"search", action:"index")

    "500"(view:'/error')
  }
}
