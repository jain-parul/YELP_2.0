var userid = "";
$("#login").submit(function(event){
	//alert("Form Submitted");
	//window.location.reload();
	event.preventDefault();
	var $form = $( this );
	var userName = $form.find( "input[name='username']" ).val();
	userid = userName;
	var formdata = $form.serialize();
	$.ajax({
		  type: 'POST',
		  data:formdata,
		  url: '/kaizen/authenticate/',
		  success: function() {
			    window.location = '/kaizen/'+userName+'/home';
			  },
			error:function() {
				alert("Invalid Username or Password");
				/*resetFields();*/
			}
	});
});

$("#register").submit(function(event){
	
	//window.location.reload();
	event.preventDefault();
	var $form = $( this );
	var formdata = $form.serialize();
	$.ajax({
		  type: 'POST',
		  data:formdata,
		  url: '/kaizen/register/',
		  success: function() {
			  	alert("Registered successfully!")
			    window.location = '/kaizen/';
			  },
			error:function() {
				alert("Registration failed");
			}
	});
});

$("#searchBiz").submit(function(event){
	//window.location.reload();
	var pathArray=window.location.pathname.split('/');
	var user=pathArray[2];
	event.preventDefault();
	
	var $form = $( this );
	var formdata = $form.serialize();
	$.ajax({
		  type: 'POST',
		  data:formdata,
		  url: '/kaizen/'+user+'/home/search',
		  success: function() {
			  	window.location = '/kaizen/'+user+'/home';
			  	
			  },
			error:function() {
				alert("Search failed");
			}
	});
});

$("#saveReview").submit(function(event){
	//window.location.reload();
	var pathArray=window.location.pathname.split('/');
	var user=pathArray[2];
	var business_id = pathArray[4];
	event.preventDefault();
	var $form = $( this );
	var formdata = $form.serialize();
	$.ajax({
		  type: 'POST',
		  data:formdata,
		  url: '/kaizen/'+user+'/review/'+business_id,
		  success: function() {
			  	alert("Review saved successfully!!");
			  	window.location = '/kaizen/'+user+'/home';
			  },
			error:function() {
				alert("Failed to submit review.");
			}
	});
});
		
/*function resetFields(){
	$("#login").find( "input[name='username']" ).val()= '';
	document.getElementById('password').value = '';
}*/