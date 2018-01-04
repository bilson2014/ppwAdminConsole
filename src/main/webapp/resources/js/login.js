$().ready(function(){
	
	if(window.top!==window.self){window.top.location=window.location};
	
	$('#login-btn').on('click',login);
	
	$('body').keydown(function(event){
		if (event.keyCode == 13){
			
        	event.returnValue=false;
        	event.cancel = true;
        	login();
    	}
	});
});

function login(){
	$('#loginform').form('submit',{
	    url : getContextPath() + '/portal/doLogin',
	    onSubmit : function() {
	    	
	    	var ps = $('#password').val().trim();
			if(ps != null && ps != '' && ps != undefined){
				
				$('input[name="employeePassword"]').val(Encrypt(ps));
			}
			return $(this).form('validate');
		},
	    success:function(result){
	    	result = $.parseJSON(result);
	    	if (result.ret) {    		
	    		var port=document.location.port;
	    		if(document.location.protocol=="https:" || port=='7071'){
	    			port='8081';
	    			window.location.href="http://"+document.location.hostname+":"+port+ '/index';
	    		}else{
	    			window.location.href= getContextPath() + '/index';
	    		}
	    		
	    	}else{
	    		$.message('<div class="light-info"><div class="light-tip icon-tip"></div><div>'+result.message+'</div></div>');
	    	}
	    }
	});
}