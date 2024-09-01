$(function(){

    var $userRegister= $("#userRegister");
    $userRegister.validate({
        rules:{
             name:{
                required:true,
                lettersonly:true
             },
             mobileNumber:{
                required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12
             },
             email:{
                required: true,
				space: true,
				email: true
             },
             address:{
                required:true,
                all:true
             },
             city:{
                requried:true,
                all:true
             },
             state:{
                required:true
             },
             pincode:{
                required:true,
                space:true,
                numericOnly:true
             },

             img:{
                required: true,
            },
            password: {
				required: true,
				space: true

			},

            cpassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			},

        },
        messages:{
            name:{
                required:'Name required',
                letteronly:'letters only'
            },
            email:{
                required: 'email name must be required',
				space: 'space not allowed',
				email: 'Invalid email'
            },
            mobileNumber:{
				required: 'mob no must be required',
				space: 'space not allowed',
				numericOnly: 'invalid mob no',
				minlength: 'min 10 digit',
				maxlength: 'max 12 digit'
			},
            password: {
				required: 'password must be required',
				space: 'space not allowed'

			},
            cpassword: {
				required: 'confirm password must be required',
				space: 'space not allowed',
				equalTo: 'password mismatch'

			},address: {
				required: 'address must be required',
				all: 'invalid'

			},

			city:{
				required: 'city must be required',
				space: 'space not allowed'

			},
			state: {
				required: 'state must be required',
				space: 'space not allowed'

			},
			pincode: {
				required: 'pincode must be required',
				space: 'space not allowed',
				numericOnly: 'invalid pincode'

			},
			img: {
				required: 'image required',
			}

        }
    })
   
	var $resetPassword=$("resetPassword");
	$resetPassword.validate({

        rules:{
			password: {
				required: true,
				space: true

			},
			cpassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			}
		},
		messages:{
		   password: {
				required: 'password must be required',
				space: 'space not allowed'

			},
			cpassword: {
				required: 'confirm password must be required',
				space: 'space not allowed',
				equalTo: 'password mismatch'

			}
		}	

     })
  
 
 
 
 })








jQuery.validator.addMethod('lettersonly', function(value, element) {
    return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
});
jQuery.validator.addMethod('space', function(value, element) {
    return /^[^-\s]+$/.test(value);
});

jQuery.validator.addMethod('all', function(value, element) {
    return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
});


jQuery.validator.addMethod('numericOnly', function(value, element) {
    return /^[0-9]+$/.test(value);
});