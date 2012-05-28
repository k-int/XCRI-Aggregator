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
	
	//common validation settings (governs where the error/validation messages are placed
	$.validator.setDefaults(
	{
        errorClass: "errormessage",
        onkeyup: false,
        errorClass: 'error',
        validClass: 'valid',
		errorPlacement: function(error, element)
        {
            // Set positioning based on the elements position in the form
            var elem = $(element),
               corners = ['right center', 'left center'],
               flipIt = elem.parents('span.right').length > 0;
 
            // Check we have a valid error message
            if(!error.is(':empty')) {
               // Apply the tooltip only if it isn't valid
               elem.filter(':not(.valid)').qtip({
                  overwrite: false,
                  content: error,
                  position: {
                     my: corners[ flipIt ? 0 : 1 ],
                     at: corners[ flipIt ? 1 : 0 ],
                     viewport: $(window)
                  },
                  show: {
                     event: false,
                     ready: true
                  },
                  hide: false,
                  style: {
                     classes: 'ui-tooltip-red' // Make it red... the classic error colour!
                  }
               })
 
               // If we have a tooltip on this element already, just update its content
               .qtip('option', 'content.text', error);
            }
 
            // If the error is empty, remove the qTip
            else { elem.qtip('destroy'); }
        },
        success: $.noop, // Odd workaround for errorPlacement not firing!		
	});

        $('*[title]:not(.complex-msg)').qtip(
           {
              position:
              {
                 my: 'top center',  // Position my top right...
                 at: 'bottom center', // at the top center of...
              },
              style:
              {
                 classes: 'ui-tooltip-rounded ui-tooltip-dark'
              },
              hide:
              {
                 fixed: true
              }
           });

});
