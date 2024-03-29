// definisanje globaljn ih promenljivih
var username;
var showAccountForm = false;
var showMessages = false;
var showMessageForm = false;
var allAccounts = false;
var createMessage = false;
var showOneMessage = false;
var messages = [];
var load = true;
var loadContacts = true;
var showFormAddContact = false;
var allContacts = false;
var counter = 1;
var fieldsContacts = [];
var fieldsMessages = [];
//var roles;

// funkcija za logovanje
function login() {
	
	// kreiramo JavaScript objekat sa podacima koje je korisnik uneo u input polja
	var user = {
		'username' : $('#email').val(),
		'password': $('#password').val()
	}
	
	// podatke na backend uvek saljemo kao JSON string
	var userJSON = JSON.stringify(user);
	
	$.ajax({
	    url : '/users/login',
	    type: 'POST',
	    data : userJSON,
	    contentType:"application/json; charset=utf-8",
	    dataType:"json",
	    success: function(data)
	    {
	    	// 1. sakrij poruku o gresci ako su pogresni kredencijali posto je korisnik uspesno ulogovan!
	    	$('#wrongCredentialsError').hide();
	    	username=data.username
	    	// 4. prikazemo novu formu
			showAnotherForm();
			/*var pos=roles.indexOf(" ");
			if(pos>0){
				var subs2=roles.substring(pos+1, roles.length-1);
			}
			var subs1=roles.substring(0, pos);
			console.log(subs2);
			console.log(subs1);
			if(subs1!="ROLE_ADMIN" && subs2!="ROLE_ADMIN"){
				$('#allUsersForm').remove();
			}*/
	    	timer();
	    },
	    error: function (error)
	    {
	    	// prikazi poruku o gresci ako su pogresni kredencijali
	    	$('#wrongCredentialsError').show();
	    }
	});
} 

function register() {
	
	// kreiramo JavaScript objekat sa podacima koje je korisnik uneo u input polja
	var user = {
		'username' : $('#usernameRegister').val(),
		'password': $('#passwordRegister').val(),
		'firstname': $('#firstnameRegister').val(),
		'lastname': $('#lastnameRegister').val()
	}
	
	// podatke na backend uvek saljemo kao JSON string
	var userJSON = JSON.stringify(user);

	if(user.username === "" || user.password === "" || user.firstname === "" || user.lastname === ""){
		alert("No field must be empty!")
	}else{
		$.ajax({
			url : '/users/signup',
			type: 'POST',
			data : userJSON,
			contentType:"application/json; charset=utf-8",
			dataType:"json",
			complete: function(data)
			{  	
				console.log(data);
				if(data.status == 201){
					alert("You have successfully registered!");
					$('#usernameRegister').val(""),
					$('#passwordRegister').val(""),
					$('#firstnameRegister').val(""),
					$('#lastnameRegister').val("")
					showLoginForm();
				}
				else if(data.status == 422){
					alert("User with username " +"'"+ $('#usernameRegister').val() +"'"+ " already exists!");
				}
			}
		});
	}
}

function signup() {
	
	// prikazujemo Register formu
	$('#registerForm').show();
	
	// sakrijemo Login formu
	$('#loginForm').hide();
	
	// sakrijemo drugu formu
	$('#otherForm').hide();
	
}

function logout() {
	
	
	$('#otherForm').hide();
	goToLogin();
	
}

function goToLogin() {
	// vrednosti u LocalStorage-u "prezive" reload stranice i zatvaranje Browser-a, pa ukoliko ima potrebe morate rucno obrisati token 
	// iz LocalStorage ili obrisati kes u browser-u
	
	localStorage.clear();
	location.reload();
}

function showLoginForm() {
	
	// prikazujemo Login formu
	$('#loginForm').show();
	$('#registerForm').hide();
}

function showAnotherForm() {
	$('#welcomeMessage').text('Hello ' + username + '!');
	
	// sakrijemo Login formu
	$('#loginForm').hide();
	
	// prikazemo drugu formu
	$('#otherForm').show();
}

function sendMessage() {

	var emailAddress=$('#emailReciever');
	var subject=$('#subjectMessage');
	var content=$('#contentMessage');
	var accountsSelect = $('#selectAccount');
	var year=new Date().getFullYear();
	var month=new Date().getMonth();
	var day=new Date().getDate();
	var h=new Date().getHours();
	var m=new Date().getMinutes();
	var s=new Date().getSeconds();

	var message = {
		'toReciver' : emailAddress.val(),
		'_cc' : emailAddress.val(),
		'_bcc' : emailAddress.val(),
		'fromSender' : accountsSelect.val(),
		'subject' : subject.val(),
		'content': content.val(),
		'dateTime': day+"-"+month+"-"+year+ " " +h+":"+m+":"+s,
	}

	// podatke na backend uvek saljemo kao JSON string
	var messageJSON = JSON.stringify(message);

	$.ajax({
	    url : '/messages',
		type: 'POST',
		data : messageJSON,
	    contentType:"application/json; charset=utf-8",
	    dataType:"json",
	   // headers: {'Authorization': 'Bearer ' + localStorage.getItem('jwt')}, // saljemo token u Authorization header-u gde ga serverska strana ocekuje
	    complete: function(data)
	    {
	    	console.log(data);
				if(data.status == 201){
					alert("You have successfully sent the message!");
					emailAddress.val("");
					subject.val("");
					content.val("");
				}
				else if(data.status == 422){
					alert("The message was not sent!");
				}
	    }
	});

}

function showCreateMessage() {
	var createMessage=$('#createMessage');
	showMessageForm=!showMessageForm;
	if(showMessageForm==false){
		createMessage.hide();
	}
	else if(showMessageForm==true){
		getAccountsForSelect();
		createMessage.show();
	}
}

function closeMessage(){
	var oneMessage=$('#oneMessage');
	oneMessage.hide();
	showMessagesTable();
}

function changeActive(username) {
	
	console.log("email: "+username);
	var checkboxId=$('[name=\''+username+'\']'); 
	
	console.log("checkboxId: "+checkboxId.is(':checked'));
	var checked=false;
	if(checkboxId.is(':checked')){
		console.log("CEKIRAN "+checkboxId.is(':checked'));
		checked=true;
	}else{
		console.log("ODCEKIRAN "+checkboxId.is(':checked'));
	}
	
	$.ajax({
	    url : '/accounts',
	    type: 'PUT',
	    data : JSON.stringify({'username':username, 'active':checked}),
	    contentType:"application/json; charset=utf-8",
	    dataType:"json",
	    complete: function(data)
			{  	
				console.log(data);
				if(data.status == 404){
					alert("An error occurred while changing the activity!");
				}
			}
	});
}

function showAddAccountForm(){
	var addAccountForm=$('#addAccountForm');
	showAccountForm=!showAccountForm;
	if(showAccountForm==false){
		addAccountForm.hide();
	}
	else if(showAccountForm==true){
		addAccountForm.show();
	}
}

function submitAccount() {
	
	emailAccountRegister = $('#emailAddressRegister').val().split("@")[0];
	domen = $('#emailAddressRegister').val().split("@")[1];

	var account = {
		'smtpAddress' : domen,
		'password': $('#passwordEmailRegister').val(),
		'username': emailAccountRegister,
		'lastname': $('#lastnameRegister').val(),
		'user' : username
	}
	
	// podatke na backend uvek saljemo kao JSON string
	var accountJSON = JSON.stringify(account);

	if(emailAccountRegister === "" || $('#passwordEmailRegister').val() === ""){
		alert("No field must be empty!")
	}else{
		$.ajax({
			url : '/accounts/'+username,
			type: 'POST',
			data : accountJSON,
			contentType:"application/json; charset=utf-8",
			dataType:"json",
			complete: function(data)
			{  	
				console.log(data);
				if(data.status == 201){
					alert("You have successfully added an account!");
					$('#emailAddressRegister').val(""),
					$('#passwordEmailRegister').val("")
				}
				else if(data.status == 422){
					alert("Account with e-mail address " +"'"+ $('#emailAddressRegister').val() +"'"+ " already exists!");
				}
			}
		});
	}
	
	
}

function getCertificate(email){

	console.log("email: "+email);
	
	$.ajax({
	    url : '/api/certificate/'+email,
	    type: 'GET',
	    contentType:"application/json; charset=utf-8",
	    dataType:"json",
	    headers: {'Authorization': 'Bearer ' + localStorage.getItem('jwt')}, // saljemo token u Authorization header-u gde ga serverska strana ocekuje
	    success: function(data)
	    {
			var file = new File([data], email+".cer");
			saveAs(file);
	    }
	});	

}

function getJks(email){

	console.log("email: "+email);
	
	$.ajax({
	    url : '/api/jks/'+email,
	    type: 'GET',
	    contentType:"application/json; charset=utf-8",
	    dataType:"json",
	    headers: {'Authorization': 'Bearer ' + localStorage.getItem('jwt')}, // saljemo token u Authorization header-u gde ga serverska strana ocekuje
	    success: function(data)
	    {
			console.log("Usepsno su stigli podaci!");
			console.log(data);
			if(data === true){
				alert("You have successfully downloaded the file!");
			}
		},
		error: function (error)
	    {
	    	console.log(error);
	    }
	});	

}

function download(filename, text) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}

function showMessagesTable(){
	var meessagesTable = $('#messagesTable');
	var oneMessage=$('#oneMessage');
	load = true;
	showMessages=!showMessages;
	if(showMessages){
		
		getMessages();
		timer();
		oneMessage.hide()
	}else{
		meessagesTable.hide();
		$('#searchMessages').hide();
	}
}

function getMessages() {
	var inputField = 'inputFieldMessage1';
	var inputOperation = 'inputOperationMessage1';
	var inputValue = 'inputValueMessage1';
	var field = {
					'id':1,
					'field':inputField,
					'value':inputValue,
					'operation':inputOperation
				}
	fieldsMessages.push(field);
	var loading = $('#loading');
	var meessagesTable = $('#messagesTable');
	var selectMessages = $('#selectMessages').val();
	if(load){
		loading.show();
	}
	load=true;
	$.ajax({
	    url : '/messages/'+selectMessages+"/"+username,
	    type: 'GET',
	    contentType:"application/json; charset=utf-8",
	    dataType:"json",
	    success: function(data)
	    {
			meessagesTable.find('tr:gt(1)').remove();
			loading.hide();
			meessagesTable.show();
			$('#searchMessages').show();			
			//console.log(data)
			messages = data;
			//console.log(messages.length)
			if(messages.length===0 && showMessages===true){
				showMessages=!showMessages;
				alert("You don't have any messages yet!");
				meessagesTable.hide();
			}else{
				for (it in messages) {
					meessagesTable.append(
						'<tr>' +  
							'<td>' + messages[it].fromSender + '</td>' + 
							'<td>' + messages[it].toReciver + '</td>' +
							'<td> <div class="btn btn-link" onClick="getMessageById(\''+messages[it].id+'\')">' + messages[it].subject + "</div></td>" +
							'<td>' + messages[it].content.substring(0,30) + "..." + '</td>' +
						'</tr>'
								)	
				}
			}

	    	console.log('Get Messages - Response:');
	    	//console.log(data);
	    	//console.log("===========================================================================");
	    	
	    }
	});
}

function getAllAccounts() {
	
	var accountsTable = $('#accountsTable');
	allAccounts=!allAccounts;
	if(allAccounts){
		$.ajax({
			url : 'accounts/'+username,
			type: 'GET',
			contentType:"application/json; charset=utf-8",
			dataType:"json",
			success: function(data)
			{
				accountsTable.find('tr:gt(1)').remove();
				accountsTable.show();
				var accounts = data;
				for (it in accounts) {
					var chacked="";
					if(accounts[it].active){
						checked='<input type="checkbox" onclick="changeActive(\''+ accounts[it].username +'\')" name="'+ accounts[it].username +'" class="select-row" checked>';
					}else{
						checked='<input type="checkbox" onclick="changeActive(\''+ accounts[it].username +'\')" name="'+ accounts[it].username +'" class="select-row">';
					}
					accountsTable.append(
						'<tr>' +  
							'<td>' + accounts[it].username +"@"+ accounts[it].smtpAddress + '</td>' + 
							'<td>' + checked + '</td>' +
						'</tr>'
								)	
					console.log('checked: '+checked);
				}
				
				console.log('Get All Accounts - Response:');
				console.log(data);
				console.log("===========================================================================");
				
				/*$('#getAllUsersError').hide();
				$('#getAllUsersSuccess').show();*/
				
			}
		});
	}
	else{
		accountsTable.hide();
	}
}

function timer(){
	load = false;
	
	if(showMessages){
		$('#oneMessage').hide();
		// Set the date we're counting down to
		var year = new Date().getFullYear();
		var month = new Date().getMonth();
		var day = new Date().getDate();
		var h= new Date().getHours();
		var m= new Date().getMinutes();
		var s= new Date().getSeconds()+20;
		var countDownDate = new Date(year,month,day,h,m,s).getTime();

		// Update the count down every 1 second
		var x = setInterval(function() {

		// Get today's date and time
		var now = new Date().getTime();
			
		// Find the distance between now and the count down date
		var distance = countDownDate - now;

		var seconds = Math.floor((distance % (1000 * 60)) / 1000);

		//console.log(seconds);
			
		// If the count down is over, write some text 
		if (distance < 0) {
			clearInterval(x);
			if(showMessages){
				getMessages();
			}
			timer();
		}
		}, 1000);
	}
}

function getAccountsForSelect(){
	var accountsSelect = $('#selectAccount');
	accountsSelect.empty();
	accountsSelect.append("<option value=''>Choose the sender's e-mail address</option>");
	$.ajax({
		url : 'accounts/'+username,
		type: 'GET',
		contentType:"application/json; charset=utf-8",
		dataType:"json",
		success: function(data){
			console.log(data);
			for (it in data){
				if(data[it].active){
					accountsSelect.append("<option value="+data[it].displayName+">"+data[it].displayName+"</option>")
				}
			}
		}
	});
}

function searchMessages(){
	showMessages=false;
	var loading = $('#loading');
	var filters = [];
	for (let index = 0; index < fieldsMessages.length; index++) {
		const element = fieldsMessages[index];
		if(!element.field.includes("Contacts")){
			var field = $('#'+element.field).val();
			var value = $('#'+element.value).val();
			var operation = $('#'+element.operation).val();
			console.log("\n\tField: "+field);
			if(field!=undefined && value!=undefined && operation!=undefined){
				var filter = {
					"user":username,
					"field":field,
					"value":value,
					"operation":operation
				}
				filters.push(filter);
			}
		}
	}
	console.log(JSON.stringify(filters))
	var data = JSON.stringify(filters);
	$.ajax({
        type: "POST",
        url: "messages/search",
        data: data,
        contentType: 'application/json',
        success: function (data) {
			loading.hide();
			messages = data;
        	$('#messagesTable').find('tr:gt(1)').remove();
            for(index = 0; index < messages.length; index++){
                var result = messages[index];
				//console.log(result);
				$('#messagesTable').append(
					"<tr>" +
						"<td>" + result.fromSender + "</td>" +
						"<td>" + result.toReciver + "</td>" +
						'<td> <div class="btn btn-link" onClick="getMessageById(\''+result.id+'\')">' + result.subject + "</div></td>" +
						"<td>" + result.content.substring(0, 30) + "..." + "</td>" +
					"</tr>"
					);
            }
            //console.log("SUCCESS : ", messages);
            //$("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').find('tr:gt(1)').remove();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            //$("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        }
    });
}

function showTableForMessages(){
	var meessagesTable = $('#messagesTable');
	meessagesTable.find('tr:gt(1)').remove();
	meessagesTable.show();
	for (it in messages) {
		meessagesTable.append(
			'<tr>' +  
				'<td>' + messages[it].fromSender + '</td>' + 
				'<td>' + messages[it].toReciver + '</td>' +
				'<td> <div class="btn btn-link" onClick="getMessageById(\''+messages[it].id+'\')">' + messages[it].subject + "</div></td>" +
				'<td>' + messages[it].content.substring(0,30) + "..." + '</td>' +
			'</tr>'
					)	
	}
}

function ElementForSort(element){
	showMessages=false;
	if(element === 'from'){
		//console.log("Sortiram po: "+element);
		messages.sort((a, b) => (a.fromSender > b.fromSender) ? 1 : -1);
		showTableForMessages();
	}else if(element === 'to'){
		//console.log("Sortiram po: "+element);
		messages.sort((a, b) => (a.toReciver > b.toReciver) ? 1 : -1);
		showTableForMessages();
	}else if(element === 'subject'){
		//console.log("Sortiram po: "+element);
		messages.sort((a, b) => (a.subject > b.subject) ? 1 : -1);
		showTableForMessages();
	}else if(element === 'content'){
		//console.log("Sortiram po: "+element);
		messages.sort((a, b) => (a.content > b.content) ? 1 : -1);
		showTableForMessages();
	}
}

function getMessageById(id){
	showMessages=false;
	$('#messagesTable').hide();
	var oneMessage=$('#oneMessage');
	var attachment=$('#attachment');
	var fromSender=$('#emailSenderOneMessage');
	var toReciver=$('#emailRecieverOneMessage');
	var subject=$('#subjectOneMessage');
	var content=$('#contentOneMessage');
	console.log("getMessageById \n Id: "+id);
	var data = JSON.stringify({
		"field":"id",
		"value":id
	});
	$.ajax({
        type: "POST",
        url: "messages/search-by-id",
        data: data,
        contentType: 'application/json',
        success: function (data) {
            for(index = 0; index < data.length; index++){
                var result = data[index];
				//console.log(result);
				fromSender.val(result.fromSender);
				toReciver.val(result.toReciver);
				subject.val(result.subject);
				content.val(result.content);
				if(result.path!=null){
					attachment.empty()
					var filename = result.path.substring(68,result.path.length);
					attachment.append('\nAttachment: <a href="\''+result.path+'\'" download="\''+result.path+'\'">'+filename+'</a>');
				}else{
					attachment.empty()
				}
            }
			oneMessage.show()
            //console.log("SUCCESS : ", messages);
            //$("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').find('tr:gt(1)').remove();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            //$("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        }
    });
}

function submitContact(){
	var firstName=$("#firstNameContact");
	var lastName=$("#lastNameContact");
	var displayName=$("#displayNameContact");
	var email=$("#emailContact");
	var note=$("#noteContact");
	var data = JSON.stringify({
		"firstName":firstName.val(),
		"lastName":lastName.val(),
		"displayName":displayName.val(),
		"email":email.val(),
		"note":note.val(),
		"user":username
	});
	$.ajax({
        type: "POST",
        url: "/contacts",
        data: data,
        contentType: 'application/json',
        complete: function(data)
	    {
	    	console.log(data);
				if(data.status == 201){
					alert("You have successfully create the contact!");
					firstName.val("");
					lastName.val("");
					displayName.val("");
					email.val("");
					note.val("");
				}
				else {
					alert("The contact was not create!");
				}
	    }
    });
}

function showCreateContact(){
	var createContact=$('#createContact');
	showFormAddContact=!showFormAddContact;
	if(showFormAddContact==false){
		createContact.hide();
	}
	else if(showFormAddContact==true){
		createContact.show();
	}
}

function getAllContacts(){
	var contactsTable = $('#contactsTable');
	var inputField = 'inputFieldContacts1';
	var inputOperation = 'inputOperationContacts1';
	var inputValue = 'inputValueContacts1';
	var field = {
					'id':1,
					'field':inputField,
					'value':inputValue,
					'operation':inputOperation
				}
	fieldsContacts.push(field);
	allContacts=!allContacts;
	if(allContacts){
		var loadingContacts = $('#loadingContacts');
		if(loadContacts){
			loadingContacts.show();
		}
		$.ajax({
			url : 'contacts/'+username,
			type: 'GET',
			contentType:"application/json; charset=utf-8",
			dataType:"json",
			success: function(data)
			{
				loadingContacts.hide();
				if(data.length==0){
					alert("There are no contacts yet!");
				}else{
					//console.log("Porsao sam u elese i data je: "+JSON.stringify(data));
					contactsTable.find('tr:gt(1)').remove();
					$('#searchContacts').show();
					contactsTable.show();
					var contacts = data;
					for (it in contacts) {
						contactsTable.append(
							'<tr>' +  
								'<td>' + contacts[it].firstName + '</td>' + 
								'<td>' + contacts[it].lastName + '</td>' +
								'<td>' + contacts[it].email + '</td>' +
							'</tr>'
									)	
					}
					
					//console.log('Get All Contacts - Response:');
					//console.log(data);
					//console.log("===========================================================================");
					
					/*$('#getAllUsersError').hide();
					$('#getAllUsersSuccess').show();*/
				}
				
			}
		});
	}
	else{
		contactsTable.hide();
		$('#searchContacts').hide();
	}
}

function searchContacts(){
	var filters = [];
	for (let index = 0; index < fieldsContacts.length; index++) {
		const element = fieldsContacts[index];
		if(!element.field.includes("Message")){
			var field = $('#'+element.field).val();
			var value = $('#'+element.value).val();
			var operation = $('#'+element.operation).val();
			console.log("\n\tField: "+field);
			if(field!=undefined && value!=undefined && operation!=undefined){
				var filter = {
					"user":username,
					"field":field,
					"value":value,
					"operation":operation
				}
				filters.push(filter);
			}
		}
	}
	console.log(JSON.stringify(filters))
	var loadingContacts = $('#loadingContacts');
	if(loadContacts){
		loadingContacts.show();
	}
	var data = JSON.stringify(filters);
	$.ajax({
        type: "POST",
        url: "contacts/search",
        data: data,
        contentType: 'application/json',
        success: function (data) {
			contacts = data;
			loadingContacts.hide();
        	$('#contactsTable').find('tr:gt(1)').remove();
            for(index = 0; index < contacts.length; index++){
                var result = contacts[index];
				console.log(result);
				$('#contactsTable').append(
					"<tr>" +
						"<td>" + result.firstName + "</td>" +
						"<td>" + result.lastName + "</td>" +
						"<td>" + result.email + "</td>" +
					"</tr>"
					);
            }
            //console.log("SUCCESS : ", messages);
            //$("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').find('tr:gt(1)').remove();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            //$("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        }
    });
}

// add row for Messages
function addRowMessages() {
	counter = counter + 1;
	var inputField = 'inputFieldMessage'+counter;
	var inputOperation = 'inputOperationMessage'+counter;
	var inputValue = 'inputValueMessage'+counter;
	var field = {
					'id':counter,
					'field':inputField,
					'value':inputValue,
					'operation':inputOperation
				}
	fieldsMessages.push(field);
	var html = '';
	html += '<div id="inputFormRowMessage">';
	html += '<input type="hidden" value="' + counter + '">';
	html += '<div class="input-group mb-3">';
	html += '<label style="padding-right: 5px;">Search by:';
	html += '<select class="form-control m-input" id="' + inputField + '">';
	html += '<option value="subject">Subject</option>';
	html += '<option value="content">Content</option>';
	html += '<option value="fromSender">Sender</option>';
	html += '<option value="toReciver">Reciver</option>';
	html += '<option value="attachment_content">Attachment</option>';
	html += '</select>';
	html += '</label>';
	html += '<label style="padding-right: 5px;">Operation:';
	html += '<select class="form-control m-input" id="' + inputOperation + '">';
	html += '<option value="and">AND</option>';
	html += '<option value="or">OR</option>';
	html += '</select>';
	html += '</label>';
	html += '<label style="padding-right: 5px;">Search value:';
	html += '<input id="' + inputValue + '" class="form-control m-input"/>';
	html += '</label>';
	html += '<label style="padding-top: 24px;">';
	html += '<div class="input-group-append">';
	html += '<button id="removeRowMessages" type="button" class="btn btn-danger">Remove</button>';
	html += '</div>';
	html += '</label>';
	html += '</div>';
	html += '</div>';

	$('#newRowMessages').append(html);
	console.log("Fields: "+JSON.stringify(fieldsMessages))
}

// add row for Contacts
function addRowContacts() {
	counter = counter + 1;
	var inputField = 'inputFieldContacts'+counter;
	var inputOperation = 'inputOperationContacts'+counter;
	var inputValue = 'inputValueContacts'+counter;
	var field = {
					'id':counter,
					'field':inputField,
					'value':inputValue,
					'operation':inputOperation
				}
	fieldsContacts.push(field);
	var html = '';
	html += '<div id="inputFormRowContacts">';
	html += '<input type="hidden" value="' + counter + '">';
	html += '<div class="input-group mb-3">';
	html += '<label style="padding-right: 5px;">Search by:';
	html += '<select class="form-control m-input" id="' + inputField + '">';
	html += '<option value="firstName">First name</option>';
	html += '<option value="lastName">Last name</option>';
	html += '<option value="note">Note</option>';
	html += '</select>';
	html += '</label>';
	html += '<label style="padding-right: 5px;">Operation:';
	html += '<select class="form-control m-input" id="' + inputOperation + '">';
	html += '<option value="and">AND</option>';
	html += '<option value="or">OR</option>';
	html += '</select>';
	html += '</label>';
	html += '<label style="padding-right: 5px;">Search value:';
	html += '<input id="' + inputValue + '" class="form-control m-input"/>';
	html += '</label>';
	html += '<label style="padding-top: 24px;">';
	html += '<div class="input-group-append">';
	html += '<button id="removeRowContacts" type="button" class="btn btn-danger">Remove</button>';
	html += '</div>';
	html += '</label>';
	html += '</div>';
	html += '</div>';

	$('#newRowContacts').append(html);
	//console.log("Fields: "+JSON.stringify(fieldsContacts))
}

// remove row from Contacts
$(document).on('click', '#removeRowContacts', function () {
	var id = $(this).closest('#inputFormRowContacts').children()[0].value;
	for (let index = 0; index < fieldsContacts.length; index++) {
		const element = fieldsContacts[index];
		if(id==element.id){
			fieldsContacts.splice(index, 1);
		}
	}
	console.log("Fields: "+JSON.stringify(fieldsContacts));
	$(this).closest('#inputFormRowContacts').remove();
});

// remove row from Messages
$(document).on('click', '#removeRowMessages', function () {
	var id = $(this).closest('#inputFormRowMessage').children()[0].value;
	for (let index = 0; index < fieldsMessages.length; index++) {
		const element = fieldsMessages[index];
		if(id==element.id){
			fieldsMessages.splice(index, 1);
		}
	}
	console.log("Fields: "+JSON.stringify(fieldsMessages));
	$(this).closest('#inputFormRowMessage').remove();
});