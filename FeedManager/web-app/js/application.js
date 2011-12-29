if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}


function resizeFrame()  
{ 
	var header_height = $('header').height();
	var nav_height = $('nav').height();
	var footer_height = $('footer').height();
	
    var content_height = $(window).height() - (nav_height + footer_height + header_height + 4);
    
    $("#content").css('minHeight', content_height < 400 ? 400 : content_height); 
} 

$(window).resize(function() 
{
	resizeFrame();
});

$(document).ready(function()
{	
	resizeFrame();
});
