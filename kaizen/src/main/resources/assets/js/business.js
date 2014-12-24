$(document).ready(function () {
    $(document).on('click', "#find", function (e) {
    	alert("inside find");
    	var text = $('#review').val();
    	var businessName='Turf Paradise Race Course';
    	alert(text);
    	
    	$.ajax({
    	type:'POST',
		url:'/kaizen/publish',
		contentType : 'application/json',
		dataType:'json',
		data:{"name":businessName,"text":text},
		success : function(response) {
			 alert("You have successfully published the review " );
		}	
    	});
    	
    
    });
});
$("#subscriber").click(function() {
//	var name = $('#username').val();
//	
//	var username=$('#name').val();
	var username='Jan';
	var businessName='Turf Paradise Race Course';
	//	var userid=this.userid;
		var strData={"name":businessName,"username":username};
		alert(businessName);
		$.ajax({
			type:'POST',
			url:'/kaizen/subscribe?name=Turf Paradise Race Course&userid=r-t7IiTSD0QZdt8lOUCqeQ',
			contentType : 'application/json',
			dataType : 'json',
			data:JSON.stringify(strData),
			success : function(response) {
				 alert("You have successfully subscribed to the business " + name);
			}	
		});
	});

