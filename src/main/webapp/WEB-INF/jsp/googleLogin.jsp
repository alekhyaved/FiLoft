<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="main.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Google SignIn</title>
    <link href="/resources/css/index.css" rel="stylesheet" type="text/css">  
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://apis.google.com/js/platform.js?onload=init" async defer></script>
  <meta name="google-signin-client_id" content="347401215478-vsr9o1hn49lpmt49vsa6nj3s4rrgu3g7.apps.googleusercontent.com">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="main.js"></script>
</head>
<body>

   <div class="g-signin2" style="margin-left: 500px;
	margin-top: 200px;" data-onsuccess="onSignIn">  
   </div>
  <div class="googledata hide" >
  Welcome <p id="name"></p>
    <img id="profilepic" width="100" height="100"> 
  </div>
  <div class="googleform hide" >
 <form action="/loginWithGoogle"  method="post">
   <input type="hidden" id="emailid" name="emailid">
   <input type="hidden" id="googleusername" name="googleusername">
 <button type="submit" class="dashboard">Go to dashboard</button>	
    </form>
    </div>
    <div class="logoutbtn hide" style="display: none">
      <button onclick="googleLogout()">Sign Out</button> </div>
      </body>    
      </html>
      
  