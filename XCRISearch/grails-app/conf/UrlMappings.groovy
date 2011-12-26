class UrlMappings {

  // static mappings = {
  //   "/$controller/$action?/$id?"{
  //     constraints {
  //       // apply constraints here
  //     }
  //   }

    "/" (controller:"search", action:"index")
    "/course/$id" (controller:"course", action:"index")

    "500"(view:'/error')
  }
}
