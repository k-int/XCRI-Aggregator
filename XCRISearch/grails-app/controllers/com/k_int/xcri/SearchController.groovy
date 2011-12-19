package com.k_int.xcri

class SearchController {

  def index() { }

  def search() {
    def result = [:]
    if ( params.q != null ) {
      render(view:'results',model:result) 
    }
  }
}
