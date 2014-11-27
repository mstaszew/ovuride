var page = require('webpage').create();

var fillLoginInfo = function(){
  	var frm = document.getElementById("LoginForm");
    frm.elements["USER"].value = 'ANNBALDERAMA';
    frm.elements["PASSWORD"].value = 'bellx2573';
    frm.submit();
}

page.onLoadFinished = function(){
	if(page.title == "Log in to MyBell"){
		page.evaluate(fillLoginInfo);
		return;
	}
	else {
	    setTimeout(function() {
		  console.log(page.content);	
          // window.page = page;
		  // var myFunction = function(myWindow) {console.log(myWindow.page.content);};
		  // 	      page.evaluate(myFunction(window));
		  // page.render('./screens/logged-in.png');
	      phantom.exit();
	    }, 60000);
	}
}

page.open('https://mybell.bell.ca/Login');
