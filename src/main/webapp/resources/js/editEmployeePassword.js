$().ready(function(){
	$('#editUserPwdForm').form({
		url : getContextPath() + '/portal/editEmpwd',
		onSubmit : function() {
			progressLoad();
			
			// 加密
			var ps = $('input[name="oldPwd"]').val().trim();
			if(ps != null && ps != '' && ps != undefined){
				
				$('input[name="oldPwd"]').val(Encrypt(ps));
			}
			
			var nps = $('input[name="pwd"]').val().trim();
			if(nps != null && nps != '' && nps != undefined){
				
				$('input[name="pwd"]').val(Encrypt(nps));
			}
			
			var isValid = $(this).form('validate');
			if (!isValid) {
				progressClose();
			}
			return isValid;
		},
		success : function(result) {
			progressClose();
		}
	});
});