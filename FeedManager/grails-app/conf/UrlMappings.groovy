class UrlMappings {

  static mappings = {

                
    "/$controller/$action?/$id?"{
      constraints {
        // apply constraints here
      }
    }

    "/feed/$id/$action?"(controller:'feed')

    "/"(controller:'frontpage', action:"index")

    "500"(view:'/error')
  }
}
