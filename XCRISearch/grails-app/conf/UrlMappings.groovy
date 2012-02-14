class UrlMappings {

  static mappings = {

  //   "/$controller/$action?/$id?"{
  //     constraints {
  //       // apply constraints here
  //     }
  //   }


    "/course/$id" (controller:"course", action:"index")

    "/" (controller:"search", action:"index")

    "500"(view:'/error')
  }
}
