$(document).ready( function () {

		$("#btn-bearbeiten").click(function(e)
	{
    	e.preventDefault();
    });
    
    	$("#btn-freigeben").click(function(e)
	{
    	e.preventDefault();
    });
    
    	$("#btn-loeschen").click(function(e)
	{
    	e.preventDefault();
    });
    
    	$("#btn-arch").click(function(e)
	{
    	e.preventDefault();
    });
    
    	$("#btn-deadline").click(function(e)
	{
    	e.preventDefault();
    });
    
      	$("#regi").click(function(e)
	{
    	e.preventDefault();
    });
    
    $("#reg").hide();
    $("#regi").click(function () {
    	$("#reg").show("slow");
    });
    
	$("#alert-bearbeiten").hide();
	$("#btn-bearbeiten").click (function () {
		$("#alert-bearbeiten").slideToggle("slow");
	});
	
	$("#alert-freigeben").hide();
	$("#btn-freigeben").click (function () {
		$("#alert-freigeben").slideToggle("slow");
		e.preventDefault();
	});
	
	$("#alert-arch").hide();
	$("#btn-arch").click (function () {
		$("#alert-arch").slideToggle("slow");
		e.preventDefault();
	});
	
	$("#alert-loeschen").hide();
	$("#btn-loeschen").click (function () {
		$("#alert-loeschen").slideToggle("slow");
		e.preventDefault();
	});
	
	$("#alert-deadline").hide();
	$("#btn-deadline").click (function () {
		$("#alert-deadline").slideToggle("slow");
		e.preventDefault();
	});
});